package shop.yesaladin.shop.product.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaTotalDiscountRateRepositoryTest {

    private final int ID = 1;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaTotalDiscountRateRepository repository;

    private TotalDiscountRate totalDiscountRate;

    @BeforeEach
    void setUp() {
        totalDiscountRate = DummyTotalDiscountRate.dummy();
    }

    @Test
    @DisplayName("전체 할인율 저장")
    void save() {
        // when
        TotalDiscountRate savedTotalDiscountRate = repository.save(totalDiscountRate);

        // then
        assertThat(savedTotalDiscountRate).isNotNull();
        assertThat(savedTotalDiscountRate.getId()).isEqualTo(ID);
        assertThat(savedTotalDiscountRate.getDiscountRate()).isEqualTo(10);
    }

    @Test
    @DisplayName("전체 할인율 ID(고정값 1)로 조회")
    void findById() {
        // given
        entityManager.persist(totalDiscountRate);

        // when
        Optional<TotalDiscountRate> foundTotalDiscountRate = repository.findById(ID);

        // then
        assertThat(foundTotalDiscountRate).isPresent();
        assertThat(foundTotalDiscountRate.get().getDiscountRate()).isEqualTo(10);
    }
}
