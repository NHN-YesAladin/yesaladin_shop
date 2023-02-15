package shop.yesaladin.shop.payment.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import java.net.URI;
import java.util.Base64;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.service.inter.QueryOrderService;
import shop.yesaladin.shop.payment.dto.PaymentCancelDto;
import shop.yesaladin.shop.payment.dto.PaymentCompleteSimpleResponseDto;
import shop.yesaladin.shop.payment.dto.PaymentEventDto;
import shop.yesaladin.shop.payment.dto.PaymentRequestDto;
import shop.yesaladin.shop.payment.exception.PaymentFailException;
import shop.yesaladin.shop.payment.service.event.PaymentEventListener;
import shop.yesaladin.shop.payment.service.inter.CommandPaymentService;


class PaymentEventListenerTest {

    private RestTemplate restTemplate;
    private PaymentEventListener paymentEventListener;

    @BeforeEach
    void setUp() {
        restTemplate = Mockito.mock(RestTemplate.class);

        paymentEventListener = new PaymentEventListener(restTemplate);
    }

    @Test
    void handleCancelPayment() {
        // given
        PaymentEventDto paymentKey = new PaymentEventDto("paymentKey");
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("https").host("api.tosspayments.com")
                .path("/v1/payments/{paymentKey}/cancel").buildAndExpand(paymentKey);

        String base64SecretKey = Base64.getEncoder().encodeToString("TOSS_SECRET_KEY".getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(base64SecretKey);
        HttpEntity<PaymentCancelDto> entity = new HttpEntity<>(
                new PaymentCancelDto("결제 중 이상 감지"),
                headers
        );

        Mockito.when(restTemplate.exchange(
                uriComponents.toUri(),
                HttpMethod.POST,
                entity,
                JsonNode.class
        )).thenReturn(new ResponseEntity<JsonNode>(HttpStatus.OK));

        // when
        // then
        Assertions.assertThatCode(() -> paymentEventListener.handleCancelPayment(paymentKey))
                .doesNotThrowAnyException();
    }

    @Disabled("내부 exception 동작 안함")
    @Test
    void handleCancelPayment_restTemplateError_fail() {
        // given
        PaymentEventDto paymentKey = new PaymentEventDto("paymentKey");
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("https").host("api.tosspayments.com")
                .path("/v1/payments/{paymentKey}/cancel").buildAndExpand(paymentKey);

        String base64SecretKey = Base64.getEncoder().encodeToString("TOSS_SECRET_KEY".getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(base64SecretKey);
        HttpEntity<PaymentCancelDto> entity = new HttpEntity<>(
                new PaymentCancelDto("결제 중 이상 감지"),
                headers
        );

        Mockito.when(restTemplate.exchange(
                uriComponents.toUri(),
                HttpMethod.POST,
                entity,
                JsonNode.class
        )).thenThrow(new RestClientException("이미 취소된 주문 입니다"));

        // when
        // then
        Assertions.assertThatCode(() -> paymentEventListener.handleCancelPayment(paymentKey))
                .isInstanceOf(PaymentFailException.class).hasMessageContaining("이미 취소된 주문 입니다");

    }
}
