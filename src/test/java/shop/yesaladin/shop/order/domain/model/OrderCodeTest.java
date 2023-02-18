package shop.yesaladin.shop.order.domain.model;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class OrderCodeTest {

    @Test
    void findByType() {
        //given
        String type = "MEMBER_ORDER";

        //when
        Optional<OrderCode> orderCode = OrderCode.findByType(type);

        //then
        assertThat(orderCode).isPresent();
        assertThat(orderCode.get().getCode()).isEqualTo(2);
        assertThat(orderCode.get().getOrderClass()).isEqualTo(MemberOrder.class);
    }
}
