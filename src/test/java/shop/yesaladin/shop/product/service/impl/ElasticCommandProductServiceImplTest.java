package shop.yesaladin.shop.product.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.category.service.inter.QueryProductCategoryService;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.SearchedProduct;
import shop.yesaladin.shop.product.domain.model.SearchedProductAuthor;
import shop.yesaladin.shop.product.domain.model.SearchedProductCategory;
import shop.yesaladin.shop.product.domain.model.SearchedProductPublisher;
import shop.yesaladin.shop.product.domain.model.SearchedProductTag;
import shop.yesaladin.shop.product.domain.model.SearchedProductTotalDiscountRate;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.domain.repository.QueryProductRepository;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.persistence.ElasticCommandProductRepository;
import shop.yesaladin.shop.publish.domain.model.Publish;
import shop.yesaladin.shop.publish.domain.model.Publisher;
import shop.yesaladin.shop.publish.dto.PublishResponseDto;
import shop.yesaladin.shop.publish.service.inter.QueryPublishService;
import shop.yesaladin.shop.tag.domain.model.ProductTag.Pk;
import shop.yesaladin.shop.tag.domain.model.Tag;
import shop.yesaladin.shop.tag.dto.ProductTagResponseDto;
import shop.yesaladin.shop.tag.service.inter.QueryProductTagService;
import shop.yesaladin.shop.writing.domain.model.Author;
import shop.yesaladin.shop.writing.dto.WritingResponseDto;
import shop.yesaladin.shop.writing.service.inter.QueryWritingService;

class ElasticCommandProductServiceImplTest {

    private ElasticCommandProductServiceImpl elasticCommandProductService;
    private ElasticCommandProductRepository elasticCommandProductRepository;
    private QueryProductRepository queryProductRepository;
    private QueryWritingService queryWritingService;
    private QueryPublishService queryPublishService;
    private QueryProductTagService queryProductTagService;
    private QueryProductCategoryService queryProductCategoryService;
    private SearchedProduct searchedProduct;

    @BeforeEach
    void setUp() {
        elasticCommandProductRepository = Mockito.mock(ElasticCommandProductRepository.class);
        queryProductRepository = Mockito.mock(QueryProductRepository.class);
        queryWritingService = Mockito.mock(QueryWritingService.class);
        queryPublishService = Mockito.mock(QueryPublishService.class);
        queryProductTagService = Mockito.mock(QueryProductTagService.class);
        queryProductCategoryService = Mockito.mock(QueryProductCategoryService.class);
        elasticCommandProductService = new ElasticCommandProductServiceImpl(
                elasticCommandProductRepository,
                queryProductRepository,
                queryWritingService,
                queryPublishService,
                queryProductTagService,
                queryProductCategoryService
        );

        searchedProduct = SearchedProduct
                .builder()
                .id(1L)
                .isbn("isbn")
                .title("title")
                .contents("content")
                .description("description")
                .actualPrice(1000)
                .discountRate(10)
                .isSeparatelyDiscount(false)
                .isSale(true)
                .quantity(1000L)
                .isForcedOutOfStock(false)
                .preferentialShowRanking(1000)
                .searchedTotalDiscountRate(new SearchedProductTotalDiscountRate(1, 10))
                .thumbnailFile("file")
                .publisher(new SearchedProductPublisher(1L, "name"))
                .publishedDate(LocalDate.of(2000, 10, 10))
                .isDeleted(false)
                .categories(List.of(new SearchedProductCategory(
                        1L,
                        2L,
                        "name",
                        true,
                        false
                )))
                .authors(List.of(new SearchedProductAuthor(1L, "name")))
                .tags(List.of(new SearchedProductTag(1L, "name")))
                .build();
    }

    @Test
    @DisplayName("상품을 못 찾을 경우 발생")
    void update_Product_Not_Found() {
        Mockito.when(queryProductRepository.findProductById(1L)).thenThrow(new ClientException(
                ErrorCode.PRODUCT_NOT_FOUND,
                "Original product not found with id : " + 1
        ));

        assertThatThrownBy(() -> elasticCommandProductService.update(
                1L
        )).isInstanceOf(ClientException.class);
    }

    @Test
    void update_Tag_Not_Found() {
        Product product = Product.builder().id(1L).build();
        Mockito.when(queryProductRepository.findProductById(anyInt()))
                .thenReturn(Optional.of(product));
        Mockito.when(queryProductTagService.findByProduct(product))
                .thenThrow(new ClientException(
                        ErrorCode.TAG_NOT_FOUND,
                        "Tag not found with id : "
                ));
        assertThatThrownBy(() -> elasticCommandProductService.update(
                1L
        )).isInstanceOf(ClientException.class);
    }

    @Test
    void update_PUBLISH_NOT_FOUND() {
        Product product = Product.builder().id(1L).build();
        Mockito.when(queryProductRepository.findProductById(anyInt()))
                .thenReturn(Optional.of(product));
        Mockito.when(queryPublishService.findByProduct(product))
                .thenThrow(new ClientException(
                        ErrorCode.PUBLISH_NOT_FOUND,
                        "Publish not found with id : " + product.getId()
                ));
        assertThatThrownBy(() -> elasticCommandProductService.update(
                1L
        )).isInstanceOf(ClientException.class);
    }

    @Test
    @DisplayName("업데이트 성공")
    void update_success() {
        //given
        Product product = DummyProduct.dummy(
                "isbn",
                SubscribeProduct.builder().id(1L).ISSN("issn").build(),
                File.builder().id(1L).url("file1").build(),
                File.builder().id(2L).url("file2").build(),
                TotalDiscountRate.builder().id(1).discountRate(10).build()
        );
        Tag tag = Tag.builder().id(1L).name("tag").build();
        List<ProductTagResponseDto> productTagResponseDtos = List.of(new ProductTagResponseDto(Pk.builder()
                .productId(product.getId())
                .tagId(tag.getId())
                .build(), product, tag));
        Author author = Author.builder().id(1L).name("name").build();
        List<WritingResponseDto> writingResponseDtos = List.of(new WritingResponseDto(
                product,
                author
        ));
        List<CategoryResponseDto> categoryResponseDtos = List.of(new CategoryResponseDto(
                1L,
                "name",
                true,
                1,
                2L,
                "parent"
        ));
        LocalDate localDate = LocalDate.of(2000, 10, 10);
        Publisher publisher = Publisher.builder().id(1L).name("name").build();
        PublishResponseDto publishResponseDto = new PublishResponseDto(
                Publish.Pk.builder().build(),
                localDate,
                product,
                publisher
        );
        Mockito.when(queryProductRepository.findProductById(1L)).thenReturn(Optional.of(product));
        Mockito.when(queryPublishService.findByProduct(product)).thenReturn(publishResponseDto);
        Mockito.when(queryProductTagService.findByProduct(product))
                .thenReturn(productTagResponseDtos);
        Mockito.when(queryProductCategoryService.findCategoriesByProduct(product))
                .thenReturn(categoryResponseDtos);
        Mockito.when(queryWritingService.findByProduct(product)).thenReturn(writingResponseDtos);

        Long result = elasticCommandProductService.update(1L);
        assertThat(result).isEqualTo(1);
    }

    @Test
    void changeIsSale_Product_Not_Found() {
        Mockito.when(elasticCommandProductRepository.findById(1L)).thenThrow(new ClientException(
                ErrorCode.PRODUCT_NOT_FOUND,
                "Original product not found with id : " + 1
        ));

        assertThatThrownBy(() -> elasticCommandProductService.changeIsSale(1L)).isInstanceOf(
                ClientException.class);
    }

    @Test
    void changeIsSale_success() {
        Mockito.when(elasticCommandProductRepository.findById(1L)).thenReturn(Optional.of(
                SearchedProduct.builder().id(1L).isSale(false).build()));

        Long result = elasticCommandProductService.changeIsSale(1L);
        assertThat(result).isEqualTo(1);
    }

    @Test
    void changeIsForcedOutOfStock_Product_Not_Found() {
        Mockito.when(elasticCommandProductRepository.findById(1L)).thenThrow(new ClientException(
                ErrorCode.PRODUCT_NOT_FOUND,
                "Original product not found with id : " + 1
        ));

        assertThatThrownBy(() -> elasticCommandProductService.changeIsForcedOutOfStock(1L)).isInstanceOf(
                ClientException.class);
    }

    @Test
    void changeIsForcedOutOfStock_success() {
        Mockito.when(elasticCommandProductRepository.findById(1L)).thenReturn(Optional.of(
                SearchedProduct.builder().id(1L).isForcedOutOfStock(false).build()));

        Long result = elasticCommandProductService.changeIsForcedOutOfStock(1L);
        assertThat(result).isEqualTo(1);
    }

    @Test
    void delete_success() {
        elasticCommandProductService.delete(1L);
        verify(elasticCommandProductRepository, atLeastOnce()).deleteByIdEquals(1L);
    }
}