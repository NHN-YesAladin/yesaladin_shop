package shop.yesaladin.shop.payment.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import shop.yesaladin.shop.order.domain.model.Order;
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
    //TODO gitActions 에서 관리할 정보인지 검토 필요
    private static final String TOSS_SECRET_KEY = "test_sk_MGjLJoQ1aVZPoLzaRvg8w6KYe2RN:";
    private final RestTemplate restTemplate;
    private final CommandPaymentRepository commandPaymentRepository;
    private final QueryOrderService queryOrderService;

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public PaymentCompleteSimpleResponseDto confirmTossRequest(PaymentRequestDto requestDto) {
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
        log.info("{}", responseFromToss);

        //database에 저장
        Order order = queryOrderService.getOrderByNumber(responseFromToss.get("orderId").asText());
        Payment payment = commandPaymentRepository.save(Payment.toEntity(responseFromToss, order));

        return PaymentCompleteSimpleResponseDto.fromEntity(payment);
    }

}
