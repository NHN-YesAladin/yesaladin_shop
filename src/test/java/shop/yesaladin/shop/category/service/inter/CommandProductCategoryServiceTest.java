package shop.yesaladin.shop.category.service.inter;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.model.ProductCategory;
import shop.yesaladin.shop.category.domain.repository.CommandProductCategoryRepository;
import shop.yesaladin.shop.category.dto.ProductCategoryResponseDto;
import shop.yesaladin.shop.category.dummy.CategoryDummy;
import shop.yesaladin.shop.category.dummy.ProductCategoryDummy;
import shop.yesaladin.shop.category.service.impl.CommandProductCategoryServiceImpl;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;
import shop.yesaladin.shop.publish.domain.model.Publisher;

/**
 * @author 배수한
 * @since 1.0
 */
class CommandProductCategoryServiceTest {

    private CommandProductCategoryService service;
    private CommandProductCategoryRepository repository;

    ProductCategory productCategory;
    SubscribeProduct subscribeProduct;
    Publisher publisher;
    File thumbnailFile;
    File ebookFile;
    TotalDiscountRate totalDiscountRate;

    private final String ISBN = "000000000000";
    private final String url = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type";
    @BeforeEach
    void setUp() {
        repository = Mockito.mock(CommandProductCategoryRepository.class);

        service = new CommandProductCategoryServiceImpl(repository);

        subscribeProduct = DummySubscribeProduct.dummy();
        thumbnailFile = DummyFile.dummy(url + "/image.png");
        ebookFile = DummyFile.dummy(url + "/ebook.pdf");
        totalDiscountRate = DummyTotalDiscountRate.dummy();

        Product product = DummyProduct.dummy(ISBN + 9, subscribeProduct, thumbnailFile, ebookFile, totalDiscountRate);
        Category category = CategoryDummy.dummyParent();

        productCategory = ProductCategoryDummy.dummy(category, product);
    }

    @Test
    void register() {
        // given
        Mockito.when(repository.save(any())).thenReturn(productCategory);

        // when
        ProductCategoryResponseDto register = service.register(productCategory);

        // then
        assertThat(register.getProductId()).isEqualTo(productCategory.getProduct().getId());
        assertThat(register.getCategoryResponseDto()
                .getId()).isEqualTo(productCategory.getCategory().getId());
    }

    @Test
    void deleteByProduct() {
        // given
        Mockito.doNothing().when(repository).deleteByProduct(any());

        // then
        assertThatCode(() -> service.deleteByProduct(productCategory.getProduct())).doesNotThrowAnyException();

    }
}
