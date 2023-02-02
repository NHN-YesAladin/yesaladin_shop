package shop.yesaladin.shop.category.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.repository.QueryProductCategoryRepository;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.category.dummy.CategoryDummy;
import shop.yesaladin.shop.category.service.inter.QueryProductCategoryService;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;
import shop.yesaladin.shop.publish.domain.model.Publisher;


class QueryProductCategoryServiceImplTest {

    private final String ISBN = "000000000000";
    private final String url = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type";
    private QueryProductCategoryService service;
    private QueryProductCategoryRepository repository;
    private Category parentCategory;
    private SubscribeProduct subscribeProduct;
    private Publisher publisher;
    private File thumbnailFile;
    private File ebookFile;
    private TotalDiscountRate totalDiscountRate;
    private Product product;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(QueryProductCategoryRepository.class);

        service = new QueryProductCategoryServiceImpl(repository);

        parentCategory = CategoryDummy.dummyParent();

        subscribeProduct = DummySubscribeProduct.dummy();
        thumbnailFile = DummyFile.dummy(url + "/image.png");
        ebookFile = DummyFile.dummy(url + "/ebook.pdf");
        totalDiscountRate = DummyTotalDiscountRate.dummy();

        product = DummyProduct.dummy(
                ISBN + 9,
                subscribeProduct,
                thumbnailFile,
                ebookFile,
                totalDiscountRate
        );
    }

    @Test
    void findCategoriesByProduct() {
        // given
        List<CategoryResponseDto> dtoList = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            Category category = Category.builder()
                    .id((long) i)
                    .name("name" + i)
                    .parent(parentCategory)
                    .depth(Category.DEPTH_CHILD)
                    .order(i)
                    .isShown(true)
                    .build();
            dtoList.add(CategoryResponseDto.fromEntity(category));
        }
        Mockito.when(repository.findCategoriesByProduct(Mockito.any())).thenReturn(dtoList);

        // when
        List<CategoryResponseDto> categories = service.findCategoriesByProduct(product);

        // then
        assertThat(categories.get(0).getId()).isEqualTo(dtoList.get(0).getId());
        assertThat(categories.get(0).getName()).isEqualTo(dtoList.get(0).getName());
        assertThat(categories.get(14).getId()).isEqualTo(dtoList.get(14).getId());

        Mockito.verify(repository, Mockito.times(1)).findCategoriesByProduct(Mockito.any());

    }
}
