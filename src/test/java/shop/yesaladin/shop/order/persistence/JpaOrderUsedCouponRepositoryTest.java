package shop.yesaladin.shop.order.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.yesaladin.shop.order.domain.dummy.CouponIssuance;
import shop.yesaladin.shop.order.domain.model.Order;
import shop.yesaladin.shop.order.domain.model.OrderUsedCoupon;
import shop.yesaladin.shop.order.domain.model.OrderUsedCoupon.Pk;
import shop.yesaladin.shop.order.persistence.dummy.DummyCouponIssuance;
import shop.yesaladin.shop.order.persistence.dummy.DummyOrder;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaOrderUsedCouponRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JpaOrderUsedCouponRepository orderUsedCouponRepository;

    private Order order;
    private CouponIssuance couponIssuance;

    private OrderUsedCoupon orderUsedCoupon;


    @BeforeEach
    void setUp() {
        order = DummyOrder.nonMemberOrder();
        couponIssuance = DummyCouponIssuance.couponIssuance;

        entityManager.persist(order);
        entityManager.persist(couponIssuance);

        orderUsedCoupon = OrderUsedCoupon.create(order, couponIssuance);
    }

    @Test
    void save() {
        //when
        OrderUsedCoupon savedOrderUsedCoupon = orderUsedCouponRepository.save(orderUsedCoupon);

        //then
        assertThat(savedOrderUsedCoupon.getOrder()).isEqualTo(order);
        assertThat(savedOrderUsedCoupon.getCouponIssuance()).isEqualTo(couponIssuance);
    }

    @Test
    void findById() {
        //given
        entityManager.persist(orderUsedCoupon);
        Pk pk = orderUsedCoupon.getPk();

        //when
        Optional<OrderUsedCoupon> foundOrderUsedCoupon = orderUsedCouponRepository.findById(pk);

        //then
        assertThat(foundOrderUsedCoupon).isPresent();
        assertThat(foundOrderUsedCoupon.get().getPk()).isEqualTo(pk);
        assertThat(foundOrderUsedCoupon.get().getOrder()).isEqualTo(order);
        assertThat(foundOrderUsedCoupon.get().getCouponIssuance()).isEqualTo(couponIssuance);
    }
}