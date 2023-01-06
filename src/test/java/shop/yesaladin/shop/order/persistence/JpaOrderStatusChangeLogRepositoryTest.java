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
import shop.yesaladin.shop.order.domain.model.NonMemberOrder;
import shop.yesaladin.shop.order.domain.model.OrderStatusChangeLog;
import shop.yesaladin.shop.order.domain.model.OrderStatusChangeLog.Pk;
import shop.yesaladin.shop.order.persistence.dummy.DummyOrder;
import shop.yesaladin.shop.order.persistence.dummy.DummyOrderStatusChangeLog;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaOrderStatusChangeLogRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JpaOrderStatusChangeLogRepository orderStatusChangeLogRepository;

    private NonMemberOrder order;
    private OrderStatusChangeLog orderStatusChangeLog;

    @BeforeEach
    void setUp() {
        Long orderId = 1L;
        order = DummyOrder.nonMemberOrder();
        orderStatusChangeLog = DummyOrderStatusChangeLog.orderStatusChangeLog(orderId, order);
    }

    @Test
    void save() {
        //given
        entityManager.persist(order);

        //when
        OrderStatusChangeLog savedOrderStatusChangeLog = orderStatusChangeLogRepository.save(
                orderStatusChangeLog);

        //then
        assertThat(savedOrderStatusChangeLog).isNotNull();
    }

    @Test
    void findById() {
        //given
        entityManager.persist(order);

        Pk pk = orderStatusChangeLogRepository.save(orderStatusChangeLog).getPk();

        //when
        Optional<OrderStatusChangeLog> foundOrderStatusChangeLog = orderStatusChangeLogRepository.findById(
                pk);

        //then
        assertThat(foundOrderStatusChangeLog).isPresent();
        assertThat(foundOrderStatusChangeLog.get().getPk()).isEqualTo(pk);
    }
}