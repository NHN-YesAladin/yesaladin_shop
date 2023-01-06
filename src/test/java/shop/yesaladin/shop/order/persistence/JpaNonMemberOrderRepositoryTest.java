package shop.yesaladin.shop.order.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
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
    private JpaOrderRepository<NonMemberOrder> nonMemberOrderRepository;

    private final String orderNumber = "1234-5678";
    private final LocalDateTime orderDateTime = LocalDateTime.now();
    private final LocalDate expectedTransportDate = LocalDate.now();
    private final boolean isHidden = false;
    private final long usedPoint = 0;
    private final int shippingFee = 0;
    private final int wrappingFee = 0;
    private final OrderCode orderCode = OrderCode.NON_MEMBER_ORDER;
    private final String address = "Gwang-ju";
    private final String name = "yerin";
    private final String phoneNumber = "010-1234-1234";

    private final NonMemberOrder nonMemberOrder = createNonMemberOrder();

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
        assertThat(savedOrder.getName()).isEqualTo(name);
        assertThat(savedOrder.getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @Test
    void findById() {
        //given
        entityManager.persist(nonMemberOrder);
        Long id = nonMemberOrder.getId();

        //when
        Optional<NonMemberOrder> foundOrder = nonMemberOrderRepository.findById(id);

        //then
        assertThat(foundOrder).isPresent();
        assertThat(foundOrder.get().getId()).isEqualTo(id);
        assertThat(foundOrder.get().getOrderNumber()).isEqualTo(orderNumber);
        assertThat(foundOrder.get().getExpectedTransportDate()).isEqualTo(expectedTransportDate);
        assertThat(foundOrder.get().isHidden()).isEqualTo(isHidden);
        assertThat(foundOrder.get().getUsedPoint()).isEqualTo(usedPoint);
        assertThat(foundOrder.get().getShippingFee()).isEqualTo(shippingFee);
        assertThat(foundOrder.get().getWrappingFee()).isEqualTo(wrappingFee);
        assertThat(foundOrder.get().getOrderCode()).isEqualTo(orderCode);
        assertThat(foundOrder.get().getAddress()).isEqualTo(address);
        assertThat(foundOrder.get().getName()).isEqualTo(name);
        assertThat(foundOrder.get().getPhoneNumber()).isEqualTo(phoneNumber);
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
                .orderCode(orderCode)
                .address(address)
                .name(name)
                .phoneNumber(phoneNumber)
                .build();
    }
}