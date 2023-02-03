package shop.yesaladin.shop.category.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.model.ProductCategory;
import shop.yesaladin.shop.category.domain.model.ProductCategory.Pk;
import shop.yesaladin.shop.category.dummy.CategoryDummy;
import shop.yesaladin.shop.category.dummy.ProductCategoryDummy;
import shop.yesaladin.shop.category.exception.ProductCategoryNotFoundException;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;
import shop.yesaladin.shop.publish.domain.model.Publisher;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaProductCategoryRepositoryTest {

    private final String ISBN = "000000000000";
    private final String url = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type";

    ProductCategory productCategory;
    SubscribeProduct subscribeProduct;
    Publisher publisher;
    File thumbnailFile;
    File ebookFile;
    TotalDiscountRate totalDiscountRate;
    Product product;
    Category category;
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private JpaProductCategoryRepository repository;
    private String name = "국내도서";

    @BeforeEach
    void setUp() {
        subscribeProduct = DummySubscribeProduct.dummy();
        thumbnailFile = DummyFile.dummy(url + "/image.png");
        ebookFile = DummyFile.dummy(url + "/ebook.pdf");
        totalDiscountRate = DummyTotalDiscountRate.dummy();

        entityManager.persist(subscribeProduct);
        entityManager.persist(thumbnailFile);
        entityManager.persist(ebookFile);
        entityManager.persist(totalDiscountRate);

        product = DummyProduct.dummy(
                ISBN + 9,
                subscribeProduct,
                thumbnailFile,
                ebookFile,
                totalDiscountRate
        );
        category = CategoryDummy.dummyParent();

        entityManager.persist(product);
        entityManager.persist(category);

        productCategory = ProductCategoryDummy.dummy(category, product);

    }

    @Test
    void save() throws Exception {
        // when
        ProductCategory savedProductCategory = repository.save(productCategory);

        // then
        assertThat(savedProductCategory.getPk()
                .getCategoryId()).isEqualTo(productCategory.getCategory().getId());
        assertThat(savedProductCategory.getPk()
                .getProductId()).isEqualTo(productCategory.getProduct().getId());
    }

    @Test
    void deletedByPk() throws Exception {
        // given
        entityManager.persist(productCategory);
        Pk pk = new Pk(
                productCategory.getCategory().getId(),
                productCategory.getProduct().getId()
        );

        // when
        repository.deleteByPk(pk);

        // then
        Optional<ProductCategory> found = Optional.ofNullable(entityManager.find(
                ProductCategory.class,
                pk
        ));
        assertThatCode(() -> found.orElseThrow(() -> new ProductCategoryNotFoundException(pk))).isInstanceOf(
                ProductCategoryNotFoundException.class);

    }

    @Test
    void deleteByProduct() throws Exception {
        // given
        entityManager.persist(productCategory);
        Pk pk = new Pk(
                productCategory.getCategory().getId(),
                productCategory.getProduct().getId()
        );

        // when
        repository.deleteByProduct(productCategory.getProduct());
        entityManager.flush();
        entityManager.clear();

        // then
        Optional<ProductCategory> found = Optional.ofNullable(entityManager.find(
                ProductCategory.class,
                pk
        ));
        assertThatCode(() -> found.orElseThrow(() -> new ProductCategoryNotFoundException(pk))).isInstanceOf(
                ProductCategoryNotFoundException.class);
    }

}
