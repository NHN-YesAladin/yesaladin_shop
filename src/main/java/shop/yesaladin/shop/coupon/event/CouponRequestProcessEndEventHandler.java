package shop.yesaladin.shop.coupon.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.coupon.message.CouponResultDto;
import shop.yesaladin.shop.config.GatewayProperties;

/**
 * CouponRequestProcessEndEvent가 발생하면 socket 서버로 메시지를 전달합니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class CouponRequestProcessEndEventHandler implements
        ApplicationListener<CouponRequestProcessEndEvent> {

    private final RestTemplate restTemplate;
    private final GatewayProperties gatewayProperties;


    @Override
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 3000))
    public void onApplicationEvent(CouponRequestProcessEndEvent event) {
        String requestUrl = UriComponentsBuilder.fromHttpUrl(gatewayProperties.getSocketUrl())
                .pathSegment("v1", "coupon-messages")
                .toUriString();

        RequestEntity<CouponResultDto> body = RequestEntity.post(requestUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(event.getResultMessage());

        restTemplate.exchange(body, new ParameterizedTypeReference<ResponseDto<Void>>() {
        });
    }

    @Recover
    public void recoverSendMessageFailToSocketServer(
            Exception e,
            CouponRequestProcessEndEvent event
    ) {
        CouponResultDto message = event.getResultMessage();
        log.error(
                "Send message fail to socket server. request id : {}. request success : {}. request message : {}. \nstack trace -> ",
                message.getRequestId(),
                message.isSuccess(),
                message.getMessage(),
                e
        );
    }
}
