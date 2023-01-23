package shop.yesaladin.shop.product.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.dummy.DummySubscribeProduct;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaSubscribeProductRepositoryTest {

    private final String ISSN = "0000-XXXX";

    @PersistenceContext
    private EntityManager entityManager;

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

    @Test
    void findByISSN() {
        // given
        entityManager.persist(subscribeProduct);

        // when
        Optional<SubscribeProduct> foundSubscribeProduct = jpaSubscribeProductRepository.findByISSN(
                ISSN);

        // then
        assertThat(foundSubscribeProduct).isPresent();
        assertThat(foundSubscribeProduct.get().getISSN()).isEqualTo(ISSN);
    }
}
