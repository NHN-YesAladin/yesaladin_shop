package shop.yesaladin.shop.category.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import shop.yesaladin.shop.product.dummy.DummyPublisher;
import shop.yesaladin.shop.product.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;
import shop.yesaladin.shop.publisher.domain.model.Publisher;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaProductCategoryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaProductCategoryRepository repository;

    private String name = "국내도서";
    String isbn = "00000-000XX-XXX-XXX";

    ProductCategory productCategory;

    @BeforeEach
    void setUp() {
        SubscribeProduct subscribeProduct = entityManager.persist(DummySubscribeProduct.dummy());
        Publisher publisher = entityManager.persist(DummyPublisher.dummy());
        File thumbNailFile = entityManager.persist(DummyFile.dummy(".png"));
        File ebookFile = entityManager.persist(DummyFile.dummy(".pdf"));
        TotalDiscountRate totalDiscountRate = entityManager.persist(DummyTotalDiscountRate.dummy());

        Product product = DummyProduct.dummy(isbn,
                subscribeProduct,
                publisher,
                thumbNailFile,
                ebookFile,
                totalDiscountRate);
        Category category = CategoryDummy.dummyParent();

        entityManager.persist(product);
        entityManager.persist(category);

        productCategory = ProductCategoryDummy.dummy(category, product);

        System.out.println("productCategory = " + productCategory);
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
    void findByPk() throws Exception {
        // given
        entityManager.persist(productCategory);

        // when
        ProductCategory foundProductCategory = repository.findByPk(new Pk(
                productCategory.getCategory().getId(),
                productCategory.getProduct().getId()
        )).orElseThrow(() -> new ProductCategoryNotFoundException(productCategory.getPk()));

        // then
        assertThat(foundProductCategory.getCategory()).isEqualTo(productCategory.getCategory());
        assertThat(foundProductCategory.getProduct()).isEqualTo(productCategory.getProduct());
    }
}
