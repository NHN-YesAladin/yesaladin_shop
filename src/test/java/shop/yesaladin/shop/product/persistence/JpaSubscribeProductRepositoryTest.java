package shop.yesaladin.shop.product.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.dummy.DummySubscribeProduct;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaSubscribeProductRepositoryTest {

    private final String ISSN = "0000-XXXX";

    @Autowired
    private JpaSubscribeProductRepository jpaSubscribeProductRepository;

    private SubscribeProduct subscribeProduct;

    @BeforeEach
    void setUp() {
        subscribeProduct = DummySubscribeProduct.dummy();
    }

    @Test
    void save() {
        // when
        SubscribeProduct savedSubscribeProduct = jpaSubscribeProductRepository.save(subscribeProduct);

        // then
        assertThat(savedSubscribeProduct).isNotNull();
        assertThat(savedSubscribeProduct.getISSN()).isEqualTo(ISSN);
    }
}
