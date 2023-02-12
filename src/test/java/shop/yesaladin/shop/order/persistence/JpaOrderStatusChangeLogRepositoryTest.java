package shop.yesaladin.shop.order.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
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
import shop.yesaladin.shop.order.domain.model.OrderStatusCode;
import shop.yesaladin.shop.order.persistence.dummy.DummyOrder;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaOrderStatusChangeLogRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JpaOrderStatusChangeLogRepository orderStatusChangeLogRepository;

    private NonMemberOrder order;
    private LocalDateTime changeDateTime = LocalDateTime.now();
    private OrderStatusCode orderStatusCode = OrderStatusCode.ORDER;

    private OrderStatusChangeLog orderStatusChangeLog;

    @BeforeEach
    void setUp() {
        order = DummyOrder.nonMemberOrder();

        entityManager.persist(order);

        orderStatusChangeLog = OrderStatusChangeLog.create(
                order,
                changeDateTime,
                orderStatusCode
        );
    }

    @Test
    void save() {
        //when
        OrderStatusChangeLog savedOrderStatusChangeLog = orderStatusChangeLogRepository.save(
                orderStatusChangeLog);

        //then
        assertThat(savedOrderStatusChangeLog.getOrder()).isEqualTo(order);
        assertThat(savedOrderStatusChangeLog.getOrderStatusCode()).isEqualTo(orderStatusCode);
    }

    @Test
    void findById() {
        //given
        entityManager.persist(orderStatusChangeLog);
        Pk pk = orderStatusChangeLog.getPk();

        //when
        Optional<OrderStatusChangeLog> foundOrderStatusChangeLog = orderStatusChangeLogRepository.findById(
                pk);

        //then
        assertThat(foundOrderStatusChangeLog).isPresent();
        assertThat(foundOrderStatusChangeLog.get().getPk()).isEqualTo(pk);
        assertThat(foundOrderStatusChangeLog.get().getOrder()).isEqualTo(order);
        assertThat(foundOrderStatusChangeLog.get().getOrderStatusCode()).isEqualTo(orderStatusCode);
    }

    @Test
    void findByOrderId() {
        //given
        entityManager.persist(orderStatusChangeLog);
        Long id = orderStatusChangeLog.getOrder().getId();

        OrderStatusChangeLog changeLog1 = OrderStatusChangeLog.create(
                order,
                changeDateTime.plusMinutes(2),
                OrderStatusCode.DEPOSIT
        );
        entityManager.persist(changeLog1);

        OrderStatusChangeLog changeLog2 = OrderStatusChangeLog.create(
                order,
                changeDateTime.plusMinutes(3),
                OrderStatusCode.READY
        );
        entityManager.persist(changeLog2);

        //when
        List<OrderStatusChangeLog> foundOrderStatusChangeLogs = orderStatusChangeLogRepository.findAllByOrder_IdOrderByOrderStatusCodeDesc(
                id);

        //then
        assertThat(foundOrderStatusChangeLogs.get(0).getOrder()).isEqualTo(order);
        assertThat(foundOrderStatusChangeLogs.get(0).getOrderStatusCode()).isEqualTo(changeLog2.getOrderStatusCode());
    }
}
