package shop.yesaladin.shop.order.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.yesaladin.shop.order.domain.model.NonMemberOrder;
import shop.yesaladin.shop.order.persistence.dummy.DummyOrder;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaNonMemberOrderRepositoryTest {

    @Autowired
    private JpaOrderRepository<NonMemberOrder> nonMemberOrderRepository;
    private NonMemberOrder order;

    @BeforeEach
    void setUp() {
        order = DummyOrder.nonMemberOrder();
    }

    @Test
    void save() {
        //given

        //when
        NonMemberOrder savedOrder = nonMemberOrderRepository.save(order);

        //then
        assertThat(savedOrder).isNotNull();
    }

    @Test
    void findById() {
        //given
        Long id = nonMemberOrderRepository.save(order).getId();

        //when
        Optional<NonMemberOrder> foundOrder = nonMemberOrderRepository.findById(id);

        //then
        assertThat(foundOrder).isPresent();
        assertThat(foundOrder.get().getId()).isEqualTo(id);
    }
}