package shop.yesaladin.shop.tag.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;
import shop.yesaladin.shop.tag.domain.model.ProductTag;
import shop.yesaladin.shop.tag.domain.model.Tag;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("local-test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaProductTagRepositoryTest {

    private final String ISBN = "0000000000001";
    private final String URL = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaProductTagRepository repository;
    private Product product;

    @BeforeEach
    void setUp() {
        SubscribeProduct subscribeProduct = DummySubscribeProduct.dummy();
        File thumbnailFile = DummyFile.dummy(URL + "/image.png");
        File ebookFile = DummyFile.dummy(URL + "/ebook.pdf");
        TotalDiscountRate totalDiscountRate = DummyTotalDiscountRate.dummy();

        entityManager.persist(subscribeProduct);
        entityManager.persist(thumbnailFile);
        entityManager.persist(ebookFile);
        entityManager.persist(totalDiscountRate);

        product = DummyProduct.dummy(ISBN, subscribeProduct, thumbnailFile, ebookFile, totalDiscountRate);

        entityManager.persist(product);
    }

    @Test
    @DisplayName("태그 관계 저장")
    void save() {
        // given
        String name = "행복한";

        Tag tag = Tag.builder().name(name).build();
        entityManager.persist(tag);

        ProductTag productTag = ProductTag.create(product, tag);

        // when
        ProductTag savedProductTag = repository.save(productTag);

        // then
        assertThat(savedProductTag).isNotNull();
        assertThat(savedProductTag.getProduct().getIsbn()).isEqualTo(ISBN);
        assertThat(savedProductTag.getTag().getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("상품으로 태그 관계 삭제")
    void deleteByProduct() {
        // given
        String name = "행복한";

        Tag tag = Tag.builder().name(name).build();
        entityManager.persist(tag);

        ProductTag productTag = ProductTag.create(product, tag);
        entityManager.persist(productTag);

        // when
        repository.deleteByProduct(product);

        // then
        ProductTag foundProductTag = entityManager.find(ProductTag.class, productTag.getPk());
        assertThat(foundProductTag).isNull();
    }

    @Test
    @DisplayName("상품으로 태그 관계 조회")
    void findByProduct() {
        // given
        String name1 = "행복한";
        String name2 = "슬픈";

        Tag tag1 = Tag.builder().name(name1).build();
        Tag tag2 = Tag.builder().name(name2).build();
        entityManager.persist(tag1);
        entityManager.persist(tag2);

        ProductTag productTag1 = ProductTag.create(product, tag1);
        ProductTag productTag2 = ProductTag.create(product, tag2);
        entityManager.persist(productTag1);
        entityManager.persist(productTag2);

        // when
        List<ProductTag> foundProductTags = repository.findByProduct(product);

        // then
        assertThat(foundProductTags).hasSize(2);
        assertThat(foundProductTags.get(0).getTag().getName()).isEqualTo(name1);
        assertThat(foundProductTags.get(1).getTag().getName()).isEqualTo(name2);
        assertThat(foundProductTags.get(0).getProduct()).isEqualTo(product);
        assertThat(foundProductTags.get(0).getProduct().getIsbn()).isEqualTo(ISBN);
        assertThat(foundProductTags.get(0).getProduct()).isEqualTo(foundProductTags.get(1).getProduct());
    }

    @Test
    @DisplayName("상품으로 태그 관계 존재 여부 확인_존재 O")
    void existsByProduct_true() {
        // given
        String name = "행복한";

        Tag tag = Tag.builder().name(name).build();
        entityManager.persist(tag);

        ProductTag productTag = ProductTag.create(product, tag);
        entityManager.persist(productTag);

        // when
        boolean isExists = repository.existsByProduct(product);

        // then
        assertThat(isExists).isTrue();
    }

    @Test
    @DisplayName("상품으로 태그 관계 여부 확인_존재 X")
    void existsByProduct_false() {
        // when
        boolean isExists = repository.existsByProduct(product);

        // then
        assertThat(isExists).isFalse();
    }
}
