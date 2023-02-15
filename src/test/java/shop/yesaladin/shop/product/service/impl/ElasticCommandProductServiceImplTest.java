package shop.yesaladin.shop.product.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
import shop.yesaladin.shop.category.service.inter.QueryCategoryService;
import shop.yesaladin.shop.product.domain.model.SearchedProduct;
import shop.yesaladin.shop.product.domain.model.SearchedProductAuthor;
import shop.yesaladin.shop.product.domain.model.SearchedProductCategory;
import shop.yesaladin.shop.product.domain.model.SearchedProductPublisher;
import shop.yesaladin.shop.product.domain.model.SearchedProductTag;
import shop.yesaladin.shop.product.domain.model.SearchedProductTotalDiscountRate;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.domain.repository.QueryTotalDiscountRateRepository;
import shop.yesaladin.shop.product.dto.SearchedProductUpdateDto;
import shop.yesaladin.shop.product.persistence.ElasticCommandProductRepository;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.publish.service.inter.QueryPublisherService;
import shop.yesaladin.shop.tag.dto.TagResponseDto;
import shop.yesaladin.shop.tag.service.inter.QueryTagService;
import shop.yesaladin.shop.writing.dto.AuthorResponseDto;
import shop.yesaladin.shop.writing.service.inter.QueryAuthorService;

class ElasticCommandProductServiceImplTest {

    private ElasticCommandProductServiceImpl elasticCommandProductService;
    private ElasticCommandProductRepository elasticCommandProductRepository;
    private QueryTotalDiscountRateRepository queryTotalDiscountRateRepository;
    private QueryAuthorService queryAuthorService;
    private QueryPublisherService queryPublisherService;
    private QueryTagService queryTagService;
    private QueryCategoryService queryCategoryService;
    private SearchedProductUpdateDto searchedProductUpdateDto;
    private SearchedProduct searchedProduct;

    @BeforeEach
    void setUp() {
        elasticCommandProductRepository = Mockito.mock(ElasticCommandProductRepository.class);
        queryTotalDiscountRateRepository = Mockito.mock(QueryTotalDiscountRateRepository.class);
        queryAuthorService = Mockito.mock(QueryAuthorService.class);
        queryPublisherService = Mockito.mock(QueryPublisherService.class);
        queryTagService = Mockito.mock(QueryTagService.class);
        queryCategoryService = Mockito.mock(QueryCategoryService.class);
        elasticCommandProductService = new ElasticCommandProductServiceImpl(
                elasticCommandProductRepository,
                queryTotalDiscountRateRepository,
                queryAuthorService,
                queryPublisherService,
                queryTagService,
                queryCategoryService
        );
        searchedProductUpdateDto = SearchedProductUpdateDto.builder()
                .title("title")
                .contents("content")
                .description("description")
                .isbn("isbn")
                .actualPrice(1000L)
                .isSeparatelyDiscount(false)
                .preferentialShowRanking(1000)
                .thumbnailFileUrl("file")
                .isSale(true)
                .isForcedOutOfStock(false)
                .publishedDate(LocalDate.of(2000, 10, 10))
                .publisherId(1L)
                .authors(List.of(1L))
                .tags(List.of(1L))
                .categories(List.of(1L))
                .build();

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
        Mockito.when(elasticCommandProductRepository.existsById(1L)).thenThrow(new ClientException(
                ErrorCode.PRODUCT_NOT_FOUND,
                "Original product not found with id : " + 1
        ));

        assertThatThrownBy(() -> elasticCommandProductService.update(
                1L,
                searchedProductUpdateDto
        )).isInstanceOf(ClientException.class);
    }

    @Test
    void update_PRODUCT_TOTAL_DISCOUNT_RATE_NOT_EXIST() {
        Mockito.when(elasticCommandProductRepository.existsById(1L)).thenReturn(true);
        Mockito.when(queryTotalDiscountRateRepository.findById(anyInt()))
                .thenThrow(new ClientException(
                        ErrorCode.PRODUCT_TOTAL_DISCOUNT_RATE_NOT_EXIST,
                        "TotalDiscountRate not exists with id : "
                ));

        assertThatThrownBy(() -> elasticCommandProductService.update(
                1L,
                searchedProductUpdateDto
        )).isInstanceOf(ClientException.class);
    }

    @Test
    void update_TAG_NOT_FOUND() {
        Mockito.when(elasticCommandProductRepository.existsById(1L)).thenReturn(true);
        Mockito.when(queryTotalDiscountRateRepository.findById(anyInt()))
                .thenReturn(Optional.of(TotalDiscountRate.builder()
                        .id(1)
                        .discountRate(10)
                        .build()));
        Mockito.when(queryTagService.findById(1L))
                .thenThrow(new ClientException(
                        ErrorCode.TAG_NOT_FOUND,
                        "Tag not found with id : "
                ));
        assertThatThrownBy(() -> elasticCommandProductService.update(
                1L,
                searchedProductUpdateDto
        )).isInstanceOf(ClientException.class);
    }

    @Test
    void update_CATEGORY_NOT_FOUND() {
        Mockito.when(elasticCommandProductRepository.existsById(1L)).thenReturn(true);
        Mockito.when(queryTotalDiscountRateRepository.findById(anyInt()))
                .thenReturn(Optional.of(TotalDiscountRate.builder()
                        .id(1)
                        .discountRate(10)
                        .build()));
        Mockito.when(queryTagService.findById(1L))
                .thenReturn(TagResponseDto.builder().id(1L).name("tag").build());
        Mockito.when(queryCategoryService.findCategoryById(1L)).thenThrow(new ClientException(
                ErrorCode.CATEGORY_NOT_FOUND,
                "Category not found with id : "
        ));

        assertThatThrownBy(() -> elasticCommandProductService.update(
                1L,
                searchedProductUpdateDto
        )).isInstanceOf(ClientException.class);
    }

    @Test
    void update_WRITING_AUTHOR_NOT_FOUND() {
        Mockito.when(elasticCommandProductRepository.existsById(1L)).thenReturn(true);
        Mockito.when(queryTotalDiscountRateRepository.findById(anyInt()))
                .thenReturn(Optional.of(TotalDiscountRate.builder()
                        .id(1)
                        .discountRate(10)
                        .build()));
        Mockito.when(queryTagService.findById(1L))
                .thenReturn(TagResponseDto.builder().id(1L).name("tag").build());
        Mockito.when(queryCategoryService.findCategoryById(1L))
                .thenReturn(CategoryResponseDto.builder().id(1L).name("name").build());
        Mockito.when(queryAuthorService.findById(1L)).thenThrow(new ClientException(
                ErrorCode.WRITING_AUTHOR_NOT_FOUND,
                "Author is not found with id : "
        ));

        assertThatThrownBy(() -> elasticCommandProductService.update(
                1L,
                searchedProductUpdateDto
        )).isInstanceOf(ClientException.class);
    }

    @Test
    void update_PUBLISH_PUBLISHER_NOT_FOUND() {
        Mockito.when(elasticCommandProductRepository.existsById(1L)).thenReturn(true);
        Mockito.when(queryTotalDiscountRateRepository.findById(anyInt()))
                .thenReturn(Optional.of(TotalDiscountRate.builder()
                        .id(1)
                        .discountRate(10)
                        .build()));
        Mockito.when(queryTagService.findById(1L))
                .thenReturn(TagResponseDto.builder().id(1L).name("tag").build());
        Mockito.when(queryCategoryService.findCategoryById(1L))
                .thenReturn(CategoryResponseDto.builder().id(1L).name("name").build());
        Mockito.when(queryAuthorService.findById(1L))
                .thenReturn(new AuthorResponseDto(1L, "name", null));
        Mockito.when(queryPublisherService.findById(1L)).thenThrow(new ClientException(
                ErrorCode.PUBLISH_PUBLISHER_NOT_FOUND,
                "Publisher not found with id : "
        ));

        assertThatThrownBy(() -> elasticCommandProductService.update(
                1L,
                searchedProductUpdateDto
        )).isInstanceOf(ClientException.class);
    }

    @Test
    @DisplayName("업데이트 성공")
    void update_success() {
        Mockito.when(elasticCommandProductRepository.existsById(1L)).thenReturn(true);
        Mockito.when(queryTotalDiscountRateRepository.findById(anyInt()))
                .thenReturn(Optional.of(TotalDiscountRate.builder()
                        .id(1)
                        .discountRate(10)
                        .build()));
        Mockito.when(queryTagService.findById(1L))
                .thenReturn(TagResponseDto.builder().id(1L).name("tag").build());
        Mockito.when(queryCategoryService.findCategoryById(1L))
                .thenReturn(CategoryResponseDto.builder().id(1L).name("name").build());
        Mockito.when(queryAuthorService.findById(1L))
                .thenReturn(new AuthorResponseDto(1L, "name", null));
        Mockito.when(queryPublisherService.findById(1L))
                .thenReturn(new PublisherResponseDto(1L, "name"));
        Mockito.when(elasticCommandProductRepository.save(any()))
                .thenReturn(searchedProduct);
        SearchedProduct result = elasticCommandProductService.update(1L, searchedProductUpdateDto);

        assertThat(result.getTitle()).isEqualTo(searchedProduct.getTitle());
        assertThat(result.getContents()).isEqualTo(searchedProduct.getContents());
        assertThat(result.getIsbn()).isEqualTo(searchedProduct.getIsbn());
        assertThat(result.getDescription()).isEqualTo(searchedProduct.getDescription());
        assertThat(result.getActualPrice()).isEqualTo(searchedProduct.getActualPrice());
        assertThat(result.getDiscountRate()).isEqualTo(searchedProduct.getDiscountRate());
        assertThat(result.getIsForcedOutOfStock()).isEqualTo(searchedProduct.getIsForcedOutOfStock());
        assertThat(result.getPreferentialShowRanking()).isEqualTo(searchedProduct.getPreferentialShowRanking());
        assertThat(result.getPublishedDate()).isEqualTo(searchedProduct.getPublishedDate());
        assertThat(result.getPublisher()).isEqualTo(searchedProduct.getPublisher());
        assertThat(result.getThumbnailFile()).isEqualTo(searchedProduct.getThumbnailFile());
        assertThat(result.getIsDeleted()).isEqualTo(searchedProduct.getIsDeleted());
        assertThat(result.getCategories()).hasSize(searchedProduct.getCategories().size());
        assertThat(result.getCategories().get(0).getId()).isEqualTo(searchedProduct.getCategories().get(0).getId());
        assertThat(result.getAuthors()).hasSize(searchedProduct.getAuthors().size());
        assertThat(result.getAuthors().get(0).getId()).isEqualTo(searchedProduct.getAuthors().get(0).getId());
        assertThat(result.getTags()).hasSize(searchedProduct.getTags().size());
        assertThat(result.getTags().get(0).getId()).isEqualTo(searchedProduct.getTags().get(0).getId());
    }

    @Test
    void delete_success() {
        elasticCommandProductService.delete(1L);
        verify(elasticCommandProductRepository, atLeastOnce()).deleteByIdEquals(1L);
    }
}