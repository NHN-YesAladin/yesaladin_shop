package shop.yesaladin.shop.producttag.persistence;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.producttag.domain.model.ProductTag;
import shop.yesaladin.shop.producttag.domain.model.ProductTag.Pk;
import shop.yesaladin.shop.producttag.domain.model.Tag;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaProductTagRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JpaProductTagRepository jpaProductTagRepository;

    private ProductTag productTag;

    @BeforeEach
    void setUp() {
        Product product = DummyProduct.dummy("00001-...");
        Tag tag = Tag.builder().name("눈물나는").build();

        entityManager.persist(product);
        entityManager.persist(tag);

        productTag = ProductTag.builder()
                .pk(Pk.builder().productId(product.getId()).tagId(tag.getId()).build())
                .product(product)
                .tag(tag)
                .build();
    }

    @Test
    void save() {
        // when
        ProductTag savedProductTag = jpaProductTagRepository.save(productTag);

        // then
        assertThat(savedProductTag).isNotNull();
        assertThat(savedProductTag.getProduct().getISBN()).isEqualTo("00001-...");
        assertThat(savedProductTag.getTag().getName()).isEqualTo("눈물나는");
    }
}
