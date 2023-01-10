package shop.yesaladin.shop.product.persistence;

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
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaTotalDiscountRateRepositoryTest {

    @Autowired
    private JpaTotalDiscountRateRepository jpaTotalDiscountRateRepository;

    private TotalDiscountRate totalDiscountRate;

    @BeforeEach
    void setUp() {
        totalDiscountRate = DummyTotalDiscountRate.dummy();
    }

    @Test
    void save() {
        // when
        TotalDiscountRate savedTotalDiscountRate = jpaTotalDiscountRateRepository.save(totalDiscountRate);

        // then
        assertThat(savedTotalDiscountRate).isNotNull();
        assertThat(savedTotalDiscountRate.getDiscountRate()).isEqualTo(10);
    }

    @Test
    void findById() {
        // given
        TotalDiscountRate savedTotalDiscountRate = jpaTotalDiscountRateRepository.save(totalDiscountRate);

        // when
        Optional<TotalDiscountRate> foundTotalDiscountRate = jpaTotalDiscountRateRepository.findById(savedTotalDiscountRate.getId());

        // then
        assertThat(foundTotalDiscountRate).isPresent();
        assertThat(foundTotalDiscountRate.get().getDiscountRate()).isEqualTo(10);
    }
}
