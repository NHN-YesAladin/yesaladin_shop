package shop.yesaladin.shop.payment.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.delivery.dto.DeliveryEventDto;
import shop.yesaladin.shop.order.domain.model.NonMemberOrder;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.domain.model.OrderStatusCode;
import shop.yesaladin.shop.order.dto.OrderPaymentResponseDto;
import shop.yesaladin.shop.order.service.inter.CommandOrderStatusChangeLogService;
import shop.yesaladin.shop.order.service.inter.QueryOrderService;
import shop.yesaladin.shop.payment.domain.model.Payment;
import shop.yesaladin.shop.payment.domain.model.PaymentCancel;
import shop.yesaladin.shop.payment.domain.model.PaymentCode;
import shop.yesaladin.shop.payment.domain.repository.CommandPaymentRepository;
import shop.yesaladin.shop.payment.domain.repository.QueryPaymentRepository;
import shop.yesaladin.shop.payment.dto.PaymentCancelDto;
import shop.yesaladin.shop.payment.dto.PaymentCompleteSimpleResponseDto;
import shop.yesaladin.shop.payment.dto.PaymentEventDto;
import shop.yesaladin.shop.payment.dto.PaymentRequestDto;
import shop.yesaladin.shop.payment.exception.PaymentFailException;
import shop.yesaladin.shop.payment.service.inter.CommandPaymentService;

/**
 * ?????? ??????, ????????????, ??????????????? ??????,??????,?????? ??? ??? ?????? ????????? ?????? ????????? ?????????
 *
 * @author ?????????
 * @since 1.0
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class CommandPaymentServiceImpl implements CommandPaymentService {

    private static final String TOSS_SECRET_KEY = "test_sk_MGjLJoQ1aVZPoLzaRvg8w6KYe2RN:";
    private final RestTemplate restTemplate;
    private final CommandPaymentRepository commandPaymentRepository;
    private final QueryPaymentRepository queryPaymentRepository;
    private final QueryOrderService queryOrderService;
    private final CommandOrderStatusChangeLogService commandOrderStatusChangeLogService;
    private final ApplicationEventPublisher applicationEventPublisher;

    private HttpHeaders getHttpHeaders() {
        String base64SecretKey = Base64.getEncoder().encodeToString(TOSS_SECRET_KEY.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(base64SecretKey);
        return headers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentCompleteSimpleResponseDto confirmTossRequest(PaymentRequestDto requestDto) {
        Order order = null;
        PaymentCompleteSimpleResponseDto responseDto;
        try {
            // ?????? ?????????????????? ?????? ??????????????? ?????? ?????? ????????? ??? ????????? ???????????? ????????????
            order = queryOrderService.getOrderByNumber(requestDto.getOrderId());

            JsonNode responseFromToss = getResponseFromToss(requestDto);

            // ?????? ?????? insert
            Payment payment = commandPaymentRepository.save(Payment.toEntity(responseFromToss, order));

            if (payment.getMethod().equals(PaymentCode.EASY_PAY)) {
                responseDto = PaymentCompleteSimpleResponseDto.fromEntityByEasyPay(payment);
            } else {
                responseDto = PaymentCompleteSimpleResponseDto.fromEntityByCard(payment);
            }

            // ?????? ?????? ?????? ?????? - ?????? ?????? ??????
            commandOrderStatusChangeLogService.appendOrderStatusChangeLog(
                    LocalDateTime.now(),
                    order,
                    OrderStatusCode.DEPOSIT
            );

            // ?????? ??????
            applicationEventPublisher.publishEvent(new DeliveryEventDto(order.getId()));

            // ?????? ?????? ?????? ?????? - ?????? ?????? ??????
            commandOrderStatusChangeLogService.appendOrderStatusChangeLog(
                    LocalDateTime.now().plusSeconds(1L),
                    order,
                    OrderStatusCode.READY
            );
        } catch (Exception e) {
            applicationEventPublisher.publishEvent(new PaymentEventDto(requestDto.getPaymentKey()));
            throw new PaymentFailException(e.getMessage(), "ERROR");
        }

        return getPaymentResponseDto(order, responseDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelPayment(String paymentKey, String cancelReason) {
        //TODO ????????? - ???????????? ?????? ??????
        Payment payment = queryPaymentRepository.findById(paymentKey, null)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.PAYMENT_NOT_FOUND, ErrorCode.PAYMENT_NOT_FOUND.getDisplayName()));

        // ?????? ?????? ?????? ?????? - ?????? (?????? findByOrderNumber ?????????)

        // Payment?????? status CANCELED ??? ??????

        // ?????? ?????? is_canceled true??? ??????

        // ?????? ?????? ??????
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("https").host("api.tosspayments.com")
                .path("/v1/payments/{paymentKey}/cancel").buildAndExpand(paymentKey);

        HttpHeaders headers = getHttpHeaders();

        HttpEntity<PaymentCancelDto> entity = new HttpEntity<>(
                new PaymentCancelDto(cancelReason),
                headers
        );

        ResponseEntity<JsonNode> exchange = null;
        try {
            exchange = restTemplate.exchange(
                    uriComponents.toUri(),
                    HttpMethod.POST,
                    entity,
                    JsonNode.class
            );
        } catch (RestClientException e) {
            log.error("{}", e.getMessage());
            throw new PaymentFailException(e.getMessage(), "CANCEL ERROR");
        }
        JsonNode responseFromToss = Objects.requireNonNull(exchange.getBody());
        payment.setPaymentCancel(PaymentCancel.toEntity(responseFromToss));
    }

    /**
     * ?????? ????????? ?????? ????????? ??????, ???????????? ???????????? ?????????
     *
     * <p>
     * ????????? : ????????? ?????? ??????????????? ?????? ?????? ????????? ??????
     * </p>
     * <p>
     * ?????? & ?????? : ?????? id??? ?????? ?????? ????????? ???????????? ?????? ??????
     * </p>
     *
     * @param order       ????????? ???????????? ?????? ?????????
     * @param responseDto ?????? ??????
     * @return ?????? ?????? (??????,????????? ??????)
     */
    private PaymentCompleteSimpleResponseDto getPaymentResponseDto(
            Order order,
            PaymentCompleteSimpleResponseDto responseDto
    ) {
        if (order.getOrderCode().equals(OrderCode.NON_MEMBER_ORDER)) {
            NonMemberOrder nonMemberOrder = (NonMemberOrder) order;
            responseDto.setUserInfo(
                    nonMemberOrder.getNonMemberName(),
                    nonMemberOrder.getAddress(),
                    nonMemberOrder.getRecipientName(),
                    nonMemberOrder.getRecipientPhoneNumber()
            );
            return responseDto;
        }
        OrderPaymentResponseDto nameAndAddress = queryOrderService.getPaymentDtoByMemberOrderId(
                order.getId());
        responseDto.setUserInfo(
                nameAndAddress.getOrdererName(),
                nameAndAddress.getAddress(),
                order.getRecipientName(),
                order.getRecipientPhoneNumber()
        );
        return responseDto;
    }

    /**
     * ????????? restTemplate??? ?????? ?????? ?????? ????????? ?????? ?????? ?????????
     *
     * @param requestDto ?????? ????????? ???????????? dto
     * @return ???????????? ????????? ???????????? JsonNode ???????????? ??????????????????
     */
    private JsonNode getResponseFromToss(PaymentRequestDto requestDto) {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(
                "https://api.tosspayments.com/v1/payments/confirm").build();

        HttpHeaders headers = getHttpHeaders();

        HttpEntity<PaymentRequestDto> entity = new HttpEntity<>(requestDto, headers);

        ResponseEntity<JsonNode> exchange = null;
        try {
            exchange = restTemplate.exchange(
                    uriComponents.toUri(),
                    HttpMethod.POST,
                    entity,
                    JsonNode.class
            );
        } catch (RestClientException e) {
            log.error("{}", e);
            throw new PaymentFailException(e.getMessage(), "ERROR");
        }

        JsonNode responseFromToss = Objects.requireNonNull(exchange.getBody());

        if (exchange.getStatusCode() != HttpStatus.OK) {
            throw new PaymentFailException(
                    responseFromToss.get("message").asText(),
                    responseFromToss.get("code").asText()
            );
        }

        return responseFromToss;
    }

}
