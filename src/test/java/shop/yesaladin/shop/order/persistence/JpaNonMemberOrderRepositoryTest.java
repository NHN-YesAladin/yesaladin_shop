package shop.yesaladin.shop.order.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.yesaladin.shop.order.domain.model.NonMemberOrder;
import shop.yesaladin.shop.order.domain.model.OrderCode;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaNonMemberOrderRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JpaOrderCommandRepository<NonMemberOrder> nonMemberOrderRepository;

    private String orderNumber = "1234-5678";
    private LocalDateTime orderDateTime = LocalDateTime.now();
    private LocalDate expectedTransportDate = LocalDate.now();
    private boolean isHidden = false;
    private long usedPoint = 0;
    private int shippingFee = 0;
    private int wrappingFee = 0;
    private long totalAmount = 10000L;
    private OrderCode orderCode = OrderCode.NON_MEMBER_ORDER;
    private String address = "Gwang-ju";
    private String name = "yerin";
    private String phoneNumber = "01012341234";
    private String recipientName = "김몽대";
    private String recipientPhoneNumber = "01012341234";

    private NonMemberOrder nonMemberOrder = createNonMemberOrder();

    @Test
    void save() {
        //when
        NonMemberOrder savedOrder = nonMemberOrderRepository.save(nonMemberOrder);

        //then
        assertThat(savedOrder.getOrderNumber()).isEqualTo(orderNumber);
        assertThat(savedOrder.getExpectedTransportDate()).isEqualTo(expectedTransportDate);
        assertThat(savedOrder.isHidden()).isEqualTo(isHidden);
        assertThat(savedOrder.getUsedPoint()).isEqualTo(usedPoint);
        assertThat(savedOrder.getShippingFee()).isEqualTo(shippingFee);
        assertThat(savedOrder.getWrappingFee()).isEqualTo(wrappingFee);
        assertThat(savedOrder.getOrderCode()).isEqualTo(orderCode);
        assertThat(savedOrder.getAddress()).isEqualTo(address);
        assertThat(savedOrder.getNonMemberName()).isEqualTo(name);
        assertThat(savedOrder.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(savedOrder.getTotalAmount()).isEqualTo(totalAmount);
    }

    NonMemberOrder createNonMemberOrder() {
        return NonMemberOrder.builder()
                .orderNumber(orderNumber)
                .orderDateTime(orderDateTime)
                .expectedTransportDate(expectedTransportDate)
                .isHidden(isHidden)
                .usedPoint(usedPoint)
                .shippingFee(shippingFee)
                .wrappingFee(wrappingFee)
                .totalAmount(totalAmount)
                .recipientName(recipientName)
                .recipientPhoneNumber(recipientPhoneNumber)
                .orderCode(orderCode)
                .address(address)
                .name("비회원 주문")
                .nonMemberName(name)
                .phoneNumber(phoneNumber)
                .recipientName("수령인 이름")
                .recipientPhoneNumber("수령인 폰번호")
                .build();
    }
}
