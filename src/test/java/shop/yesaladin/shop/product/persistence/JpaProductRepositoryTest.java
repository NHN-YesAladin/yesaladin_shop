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
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummyProductTypeCode;
import shop.yesaladin.shop.product.dummy.DummyPublisher;
import shop.yesaladin.shop.product.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaProductRepositoryTest {

    private final String ISBN = "00000-000XX-XXX-XXX";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JpaProductRepository jpaProductRepository;

    private Product product;

    @BeforeEach
    void setUp() {
//        entityManager.persist(DummySubscribeProduct.dummy());
//        entityManager.persist(DummyPublisher.dummy());
//        entityManager.persist(DummyFile.dummy(".png"));
//        entityManager.persist(DummyFile.dummy(".pdf"));
//        entityManager.persist(DummyTotalDiscountRate.dummy());

        product = DummyProduct.dummy(ISBN);
    }

    @Test
    void save() {
        // when
        Product savedProduct = jpaProductRepository.save(product);

        // then
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getISBN()).isEqualTo(ISBN);
    }

    @Test
    void findById() {
        // given
        entityManager.persist(product);

        // when
        Optional<Product> foundProduct = jpaProductRepository.findById(product.getId());

        // then
        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getISBN()).isEqualTo(ISBN);
    }

}
