package shop.yesaladin.shop.product.persistence;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
}
