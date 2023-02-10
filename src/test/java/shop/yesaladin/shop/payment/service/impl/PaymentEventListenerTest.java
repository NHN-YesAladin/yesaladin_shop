package shop.yesaladin.shop.payment.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.apache.kafka.common.protocol.types.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.service.inter.QueryOrderService;
import shop.yesaladin.shop.payment.dto.PaymentCompleteSimpleResponseDto;
import shop.yesaladin.shop.payment.dto.PaymentRequestDto;
import shop.yesaladin.shop.payment.service.inter.CommandPaymentService;


@Disabled("실제 토스 api 테스트")
@SpringBootTest
@Transactional
class PaymentEventListenerTest {

    @Autowired
    private CommandPaymentService commandPaymentService;

    @Autowired
    private QueryOrderService queryOrderService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void handleCancelPayment() {
        // given
        String orderNumber = "NM-03280478012347";
        Order order = queryOrderService.getOrderByNumber(orderNumber);
        PaymentRequestDto requestDto = new PaymentRequestDto(
                "d9ojO5qEvKma60RZblrqJ6e7Le2wjZ8wzYWBn14MXAPGg7pD",
                order.getOrderNumber(),
                order.getTotalAmount()
        );

        // when
        PaymentCompleteSimpleResponseDto responseDto = commandPaymentService.confirmTossRequest(
                requestDto);
    }
}
