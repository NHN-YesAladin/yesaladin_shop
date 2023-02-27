package shop.yesaladin.shop.payment.service.event;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import shop.yesaladin.shop.payment.dto.PaymentCancelDto;
import shop.yesaladin.shop.payment.dto.PaymentEventDto;
import shop.yesaladin.shop.payment.exception.PaymentFailException;

/**
 * 결제 진행 중 rollback이 되는 상황에 다른 서비스들에 영향을 주지 않기 위해 사용
 *
 * @author 배수한
 * @since 1.0
 */

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentEventListener {

    private static final String TOSS_SECRET_KEY = "test_sk_MGjLJoQ1aVZPoLzaRvg8w6KYe2RN:";
    private final RestTemplate restTemplate;

    /**
     * 결제 service가 rollback 될 경우 동작하는 메서드, 결제 취소 api를 통해 결제가 취소 됨. (DB저장 안함)
     *
     * @param eventDto
     */
    @EventListener
    public void handleCancelPayment(PaymentEventDto eventDto) {
        String paymentKey = eventDto.getPaymentKey();
        log.warn("TransactionalEventListener : {}", paymentKey);

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("https").host("api.tosspayments.com")
                .path("/v1/payments/{paymentKey}/cancel").buildAndExpand(paymentKey);

        String base64SecretKey = Base64.getEncoder().encodeToString(TOSS_SECRET_KEY.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(base64SecretKey);

        HttpEntity<PaymentCancelDto> entity = new HttpEntity<>(
                new PaymentCancelDto("결제 중 이상 감지"),
                headers
        );

        try {
            restTemplate.exchange(
                    uriComponents.toUri(),
                    HttpMethod.POST,
                    entity,
                    JsonNode.class
            );
        } catch (RestClientException e) {
            log.error("{}", e.getMessage());
            throw new PaymentFailException(e.getMessage(), "CANCEL ERROR");
        }
        log.info("handleCancelPayment success");
    }
}
