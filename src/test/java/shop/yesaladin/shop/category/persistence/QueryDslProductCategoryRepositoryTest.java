package shop.yesaladin.shop.category.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.model.ProductCategory;
import shop.yesaladin.shop.category.domain.model.ProductCategory.Pk;
import shop.yesaladin.shop.category.domain.repository.QueryProductCategoryRepository;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
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

@Transactional
@SpringBootTest
class QueryDslProductCategoryRepositoryTest {

    private final String ISBN = "000000000000";
    private final String url = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type";
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private QueryProductCategoryRepository repository;
    private String name = "국내도서";

    private ProductCategory productCategory;
    private SubscribeProduct subscribeProduct;
    private Publisher publisher;
    private File thumbnailFile;
    private File ebookFile;
    private TotalDiscountRate totalDiscountRate;
    private Product product;
    private Category category;
    private Category parentCategory;

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
        parentCategory = CategoryDummy.dummyParent();
        category = CategoryDummy.dummyChild(parentCategory);

        entityManager.persist(parentCategory);
        entityManager.persist(product);
        entityManager.persist(category);

        productCategory = ProductCategoryDummy.dummy(category, product);
        entityManager.persist(productCategory);
    }

    @Test
    @DisplayName("pk를 통해 상품 카테고리 조회")
    void findByPk() throws Exception {
        // when
        ProductCategory foundProductCategory = repository.findByPk(new Pk(
                productCategory.getCategory().getId(),
                productCategory.getProduct().getId()
        )).orElseThrow(() -> new ProductCategoryNotFoundException(productCategory.getPk()));

        // then
        assertThat(foundProductCategory.getCategory()).isEqualTo(productCategory.getCategory());
        assertThat(foundProductCategory.getProduct()).isEqualTo(productCategory.getProduct());
    }

    @Test
    @DisplayName("특정 상품을 통해 카테고리 조회")
    void findCategoriesByProduct() throws Exception {
        // given
        Category category2 = CategoryDummy.dummyChild(10200L, parentCategory);
        entityManager.persist(category2);
        ProductCategory productCategory2 = ProductCategoryDummy.dummy(category2, product);
        entityManager.persist(productCategory2);

        // when
        List<CategoryResponseDto> categories = repository.findCategoriesByProduct(product);

        // then
        assertThat(categories).hasSize(2);
        assertThat(categories.get(1).getId()).isEqualTo(category2.getId());
        assertThat(categories.get(1).getOrder()).isEqualTo(category2.getOrder());
        assertThat(categories.get(1).getName()).isEqualTo(category2.getName());
    }
}
