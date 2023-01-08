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
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummyPublisher;
import shop.yesaladin.shop.product.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaProductCategoryRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JpaProductCategoryRepository repository;

    private String name = "국내도서";
    String isbn = "00000-000XX-XXX-XXX";

    ProductCategory productCategory;

    @BeforeEach
    void setUp() {
        entityManager.persist(DummySubscribeProduct.dummy());
        entityManager.persist(DummyPublisher.dummy());
        entityManager.persist(DummyFile.dummy());
        entityManager.persist(DummyTotalDiscountRate.dummy());

        Product product = DummyProduct.dummy(isbn);
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
        entityManager.flush();
        entityManager.clear();

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
