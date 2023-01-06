package shop.yesaladin.shop.product.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.RelatedProduct;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummyRelatedProduct;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaRelatedProductRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JpaRelatedProductRepository jpaRelatedProductRepository;

    private RelatedProduct relatedProduct;

    private Product product1;
    private Product product2;


    @BeforeEach
    void setUp() {
        product1 = DummyProduct.dummy("0001-...");
        product2 = DummyProduct.dummy("0002-...");

        entityManager.persist(product1);
        entityManager.persist(product2);

        relatedProduct = DummyRelatedProduct.dummy(product1, product2);
    }

    @Test
    void save() {
        // when
        RelatedProduct savedRelatedProduct = jpaRelatedProductRepository.save(relatedProduct);

        // then
        assertThat(savedRelatedProduct).isNotNull();
    }
}
