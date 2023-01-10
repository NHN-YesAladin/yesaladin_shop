package shop.yesaladin.shop.tag.persistence;

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
import shop.yesaladin.shop.tag.domain.model.ProductTag;
import shop.yesaladin.shop.tag.domain.model.Tag;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaProductTagRepositoryTest {

    private final String ISBN = "00001-...";
    private final String TAG_NAME = "눈물나는";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JpaProductTagRepository jpaProductTagRepository;

    private ProductTag productTag;

    @BeforeEach
    void setUp() {
        Product product = DummyProduct.dummy(ISBN);
        Tag tag = Tag.builder().name(TAG_NAME).build();

        entityManager.persist(product);
        entityManager.persist(tag);

        productTag = ProductTag.create(product, tag);
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
