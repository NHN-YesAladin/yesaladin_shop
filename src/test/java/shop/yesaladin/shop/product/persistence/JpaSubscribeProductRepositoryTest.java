package shop.yesaladin.shop.product.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.dummy.DummySubscribeProduct;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaSubscribeProductRepositoryTest {

    private final String ISSN = "0000XXXX";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaSubscribeProductRepository repository;

    private SubscribeProduct subscribeProduct;

    @BeforeEach
    void setUp() {
        subscribeProduct = DummySubscribeProduct.dummy();
    }

    @Test
    @DisplayName("구독상품 저장")
    void save() {
        // when
        SubscribeProduct savedSubscribeProduct = repository.save(subscribeProduct);

        // then
        assertThat(savedSubscribeProduct).isNotNull();
        assertThat(savedSubscribeProduct.getISSN()).isEqualTo(ISSN);
    }

    @Test
    @DisplayName("ID로 구독상품 조회")
    void findById() {
        // given
        SubscribeProduct savedSubscribeProduct = entityManager.persist(subscribeProduct);

        // when
        Optional<SubscribeProduct> foundSubscribeProduct = repository.findById(savedSubscribeProduct.getId());

        // then
        assertThat(foundSubscribeProduct).isPresent();
        assertThat(foundSubscribeProduct.get().getISSN()).isEqualTo(ISSN);
    }

    @Test
    @DisplayName("ISSN으로 구독상품 조회")
    void findByISSN() {
        // given
        entityManager.persist(subscribeProduct);

        // when
        Optional<SubscribeProduct> foundSubscribeProduct = repository.findByISSN(ISSN);

        // then
        assertThat(foundSubscribeProduct).isPresent();
        assertThat(foundSubscribeProduct.get().getISSN()).isEqualTo(ISSN);
    }
}
