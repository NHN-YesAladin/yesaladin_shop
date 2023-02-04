package shop.yesaladin.shop.payment.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Base64;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import shop.yesaladin.shop.order.domain.model.NonMemberOrder;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.dto.OrderPaymentResponseDto;
import shop.yesaladin.shop.order.service.inter.QueryOrderService;
import shop.yesaladin.shop.payment.domain.model.Payment;
import shop.yesaladin.shop.payment.domain.repository.CommandPaymentRepository;
import shop.yesaladin.shop.payment.dto.PaymentCompleteSimpleResponseDto;
import shop.yesaladin.shop.payment.dto.PaymentRequestDto;
import shop.yesaladin.shop.payment.exception.PaymentFailException;
import shop.yesaladin.shop.payment.service.inter.CommandPaymentService;

/**
 * 결제 정보, 카드정보, 취소정보를 생성,수정,삭제 할 수 있는 기능을 가진 서비스 구현체
 *
 * @author 배수한
 * @since 1.0
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class CommandPaymentServiceImpl implements CommandPaymentService {

    private static final String TOSS_SECRET_KEY = "test_sk_MGjLJoQ1aVZPoLzaRvg8w6KYe2RN:";
    private final RestTemplate restTemplate;
    private final CommandPaymentRepository commandPaymentRepository;
    private final QueryOrderService queryOrderService;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public PaymentCompleteSimpleResponseDto confirmTossRequest(PaymentRequestDto requestDto) {
        // 이미 입력되어있지 않은 주문이라면 결제 승인 처리를 할 필요가 없으므로 예외처리
        Order order = queryOrderService.getOrderByNumber(requestDto.getOrderId());

        JsonNode responseFromToss = getResponseFromToss(requestDto);
        log.info("{}", responseFromToss);

        //database에 저장
        Payment payment = commandPaymentRepository.save(Payment.toEntity(responseFromToss, order));
        PaymentCompleteSimpleResponseDto responseDto = PaymentCompleteSimpleResponseDto.fromEntity(
                payment);
        return getPaymentResponseDto(order, responseDto);
    }

    /**
     * 주문 코드에 맞춰 주문자 이름, 주소지를 셋팅하는 메서드
     *
     * <p>
     *     비회원 : 비회원 주문 엔티티에서 바로 필요 정보를 획득
     * </p>
     * <p>
     *     회원 & 구독 : 주문 id를 통해 회원 주문을 조회하여 정보 획득
     * </p>
     * @param order 추상화 되어있는 주문 엔티티
     * @param responseDto 결제 정보
     * @return 결제 정보 (주소,주문자 포함)
     */
    private PaymentCompleteSimpleResponseDto getPaymentResponseDto(
            Order order,
            PaymentCompleteSimpleResponseDto responseDto
    ) {
        if (order.getOrderCode().equals(OrderCode.NON_MEMBER_ORDER)) {
            NonMemberOrder nonMemberOrder = (NonMemberOrder) order;
            responseDto.setOrdererNameAndAddress(
                    nonMemberOrder.getNonMemberName(),
                    nonMemberOrder.getAddress()
            );
            return responseDto;
        }
        OrderPaymentResponseDto nameAndAddress = queryOrderService.getPaymentDtoByMemberOrderId(
                order.getId());
        responseDto.setOrdererNameAndAddress(
                nameAndAddress.getOrdererName(),
                nameAndAddress.getAddress()
        );
        return responseDto;
    }

    /**
     * 토스에 restTemplate을 통해 결제 승인 통신을 하기 위한 메서드
     *
     * @param requestDto 결제 정보가 담겨있는 dto
     * @return 토스에서 전송한 정보들이 JsonNode 타입으로 저장되어있음
     */
    private JsonNode getResponseFromToss(PaymentRequestDto requestDto) {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(
                "https://api.tosspayments.com/v1/payments/confirm").build();

        String base64SecretKey = Base64.getEncoder().encodeToString(TOSS_SECRET_KEY.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(base64SecretKey);

        HttpEntity<PaymentRequestDto> entity = new HttpEntity<>(requestDto, headers);

        ResponseEntity<JsonNode> exchange = restTemplate.exchange(
                uriComponents.toUri(),
                HttpMethod.POST,
                entity,
                JsonNode.class
        );

        if (exchange.getStatusCode() != HttpStatus.OK) {
            throw new PaymentFailException(exchange.getBody());
        }

        JsonNode responseFromToss = exchange.getBody();
        if (Objects.isNull(responseFromToss)) {
            throw new PaymentFailException("Body is empty");
        }
        return responseFromToss;
    }

}
