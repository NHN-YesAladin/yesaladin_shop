package shop.yesaladin.shop.delivery.service.event;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.config.GatewayProperties;
import shop.yesaladin.shop.delivery.dto.DeliveryEventDto;
import shop.yesaladin.shop.delivery.dto.TransportResponseDto;
import shop.yesaladin.shop.order.domain.model.OrderStatusCode;
import shop.yesaladin.shop.order.service.inter.CommandOrderStatusChangeLogService;
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
public class DeliveryEventListener {
    private final RestTemplate restTemplate;
    private final GatewayProperties gatewayProperties;
    private final CommandOrderStatusChangeLogService commandOrderStatusChangeLogService;

    /**
     * 배송 생성을 위해 통신하는 메서드
     *  CommandPaymentServiceImpl.confirmTossRequest()에서 event publish
     *
     * @param eventDto 주문 id가 들어있는 dto
     */
    @EventListener
    public void handleRegisterDeliveryStatus(DeliveryEventDto eventDto) {

        Long orderId = eventDto.getOrderId();
        log.info("DeliveryEventListener - handleRegisterDeliveryStatus : {}", orderId);

        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(
                        gatewayProperties.getDeliveryUrl() + "/api/delivery/" + orderId)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<ResponseDto<TransportResponseDto>> exchange = restTemplate.exchange(
                uriComponents.toUri(),
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<>() {
                }
        );
        ResponseDto<TransportResponseDto> responseDto = Objects.requireNonNull(exchange.getBody());
        if (!responseDto.isSuccess()) {
            throw new ClientException(ErrorCode.BAD_REQUEST, "배송 서버의 응답이 올바르지 않습니다.");
        }
        commandOrderStatusChangeLogService.appendOrderStatusChangeLogByOrderId(
                LocalDateTime.now().plusSeconds(3),
                orderId,
                OrderStatusCode.DELIVERY
        );
        log.info("handleCancelPayment success");
    }
}
