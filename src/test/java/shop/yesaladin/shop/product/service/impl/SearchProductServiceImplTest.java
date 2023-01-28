package shop.yesaladin.shop.product.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.shop.product.domain.model.SearchedProductAuthor;
import shop.yesaladin.shop.product.domain.model.SearchedProductCategory;
import shop.yesaladin.shop.product.domain.model.SearchedProductFile;
import shop.yesaladin.shop.product.domain.model.SearchedProductProductType;
import shop.yesaladin.shop.product.domain.model.SearchedProductSubscribProduct;
import shop.yesaladin.shop.product.domain.model.SearchedProductTag;
import shop.yesaladin.shop.product.domain.model.SearchedProductTotalDiscountRate;
import shop.yesaladin.shop.product.domain.repository.SearchProductRepository;
import shop.yesaladin.shop.product.dto.SearchedProductDto;
import shop.yesaladin.shop.product.dto.SearchedProductManagerDto;
import shop.yesaladin.shop.product.dto.SearchedProductManagerResponseDto;
import shop.yesaladin.shop.product.dto.SearchedProductResponseDto;

class SearchProductServiceImplTest {

    SearchProductRepository searchProductRepository;
    SearchProductServiceImpl searchProductService;
    SearchedProductDto dummySearchedProductDto;
    SearchedProductResponseDto dummySearchedProductResponseDto;
    SearchedProductManagerResponseDto dummySearchedProductManagerResponseDto;
    SearchedProductManagerDto dummySearchedProductManagerDto;

    private static final int OFFSET = 0;
    private static final int SIZE = 1;
    private static final long COUNT = 1;
    private static final int ZERO = 0;
    private static final int ONE = 1;

    @BeforeEach
    void setUp() {
        searchProductRepository = Mockito.mock(SearchProductRepository.class);
        searchProductService = new SearchProductServiceImpl(searchProductRepository);
        dummySearchedProductDto = SearchedProductDto.builder()
                .id(-1L)
                .title("title")
                .discountRate(10)
                .sellingPrice(1000L)
                .authors(List.of("author"))
                .isForcedOutOfStack(false)
                .thumbnailFileUrl("깃 허브.jpg")
                .publishedDate(LocalDate.now().toString())
                .categories(List.of(new SearchedProductCategory(12L, null, "국내소설", true, false)))
                .tags(List.of("tag1"))
                .build();

        dummySearchedProductResponseDto = SearchedProductResponseDto.builder()
                .products(List.of(dummySearchedProductDto))
                .count(COUNT)
                .build();

        dummySearchedProductManagerDto = SearchedProductManagerDto.builder()
                .id(-1L)
                .isbn("isbn")
                .title("title")
                .actualPrice(1000L)
                .discountRate(10)
                .isSeparatelyDiscount(false)
                .givenPointRate(10)
                .isGivenPoint(false)
                .isSale(false)
                .quantity(1000L)
                .preferentialShowRanking(1000L)
                .productType(new SearchedProductProductType(1, "type"))
                .searchedTotalDiscountRate(new SearchedProductTotalDiscountRate(1, 10))
                .thumbnailFile(new SearchedProductFile(1L, "file1", LocalDate.now()))
                .ebookFile(new SearchedProductFile(2L, "file2", LocalDate.now()))
                .publishedDate(LocalDate.now())
                .savingMethod("saving")
                .categories(List.of(new SearchedProductCategory(1L, null, "name", true, false)))
                .authors(List.of(new SearchedProductAuthor(1L, "name")))
                .tags(List.of(new SearchedProductTag(1L, "tag")))
                .subscribeProducts(List.of(new SearchedProductSubscribProduct(1L, "issn")))
                .build();

        dummySearchedProductManagerResponseDto = SearchedProductManagerResponseDto.builder()
                .products(List.of(dummySearchedProductManagerDto))
                .count(COUNT)
                .build();
    }

    @Test
    @DisplayName("카테고리 id 검색 테스트")
    void testSearchProductsByCategoryId() {
        //given
        Mockito.when(searchProductRepository.searchProductsByCategoryId(1L, OFFSET, SIZE))
                .thenReturn(dummySearchedProductManagerResponseDto);

        //when
        SearchedProductManagerResponseDto result = searchProductService.searchProductsByCategoryId(
                1L,
                OFFSET,
                SIZE
        );

        //then
        assertThat(result.getCount()).isEqualTo(COUNT);
        assertThat(result.getProducts()).hasSize(ONE);
        assertThat(result.getProducts()
                .get(ZERO)
                .getId()).isEqualTo(dummySearchedProductManagerDto.getId());
        assertThat(result.getProducts()
                .get(ZERO)
                .getIsbn()).isEqualTo(dummySearchedProductManagerDto.getIsbn());
        assertThat(result.getProducts()
                .get(ZERO)
                .getTitle()).isEqualTo(dummySearchedProductManagerDto.getTitle());
        assertThat(result.getProducts().get(ZERO).getActualPrice()).isEqualTo(
                dummySearchedProductManagerDto.getActualPrice());
        assertThat(result.getProducts().get(ZERO).getDiscountRate()).isEqualTo(
                dummySearchedProductManagerDto.getDiscountRate());
        assertThat(result.getProducts().get(ZERO).isSeparatelyDiscount()).isEqualTo(
                dummySearchedProductManagerDto.isSeparatelyDiscount());
        assertThat(result.getProducts().get(ZERO).getIsGivenPoint()).isEqualTo(
                dummySearchedProductManagerDto.getIsGivenPoint());
        assertThat(result.getProducts().get(ZERO).getGivenPointRate()).isEqualTo(
                dummySearchedProductManagerDto.getGivenPointRate());
        assertThat(result.getProducts()
                .get(ZERO)
                .getIsSale()).isEqualTo(dummySearchedProductManagerDto.getIsSale());
        assertThat(result.getProducts().get(ZERO).getQuantity()).isEqualTo(
                dummySearchedProductManagerDto.getQuantity());
        assertThat(result.getProducts().get(ZERO).getPreferentialShowRanking()).isEqualTo(
                dummySearchedProductManagerDto.getPreferentialShowRanking());
        assertThat(result.getProducts().get(ZERO).getProductType().getId()).isEqualTo(
                dummySearchedProductManagerDto.getProductType().getId());
        assertThat(result.getProducts().get(ZERO).getProductType().getName()).isEqualTo(
                dummySearchedProductManagerDto.getProductType().getName());
        assertThat(result.getProducts().get(ZERO).getSearchedTotalDiscountRate().getId()).isEqualTo(
                dummySearchedProductManagerDto.getSearchedTotalDiscountRate().getId());
        assertThat(result.getProducts()
                .get(ZERO)
                .getSearchedTotalDiscountRate()
                .getDiscountRate()).isEqualTo(dummySearchedProductManagerDto.getSearchedTotalDiscountRate()
                .getDiscountRate());
        assertThat(result.getProducts().get(ZERO).getThumbnailFile().getId()).isEqualTo(
                dummySearchedProductManagerDto.getThumbnailFile().getId());
        assertThat(result.getProducts().get(ZERO).getThumbnailFile().getName()).isEqualTo(
                dummySearchedProductManagerDto.getThumbnailFile().getName());
        assertThat(result.getProducts().get(ZERO).getThumbnailFile().getUploadDateTime()).isEqualTo(
                dummySearchedProductManagerDto.getThumbnailFile().getUploadDateTime());
        assertThat(result.getProducts().get(ZERO).getEbookFile().getId()).isEqualTo(
                dummySearchedProductManagerDto.getEbookFile().getId());
        assertThat(result.getProducts().get(ZERO).getEbookFile().getName()).isEqualTo(
                dummySearchedProductManagerDto.getEbookFile().getName());
        assertThat(result.getProducts().get(ZERO).getEbookFile().getUploadDateTime()).isEqualTo(
                dummySearchedProductManagerDto.getEbookFile().getUploadDateTime());
        assertThat(result.getProducts().get(ZERO).getPublishedDate()).isEqualTo(
                dummySearchedProductManagerDto.getPublishedDate());
        assertThat(result.getProducts().get(ZERO).getSavingMethod()).isEqualTo(
                dummySearchedProductManagerDto.getSavingMethod());
        assertThat(result.getProducts().get(ZERO).getCategories()).hasSameSizeAs(
                dummySearchedProductManagerDto.getCategories());
        assertThat(result.getProducts().get(ZERO).getCategories().get(ZERO).getId()).isEqualTo(
                dummySearchedProductManagerDto.getCategories().get(ZERO).getId());
        assertThat(result.getProducts().get(ZERO).getCategories().get(ZERO).getName()).isEqualTo(
                dummySearchedProductManagerDto.getCategories().get(ZERO).getName());
        assertThat(result.getProducts().get(ZERO).getCategories().get(ZERO).getParent()).isEqualTo(
                dummySearchedProductManagerDto.getCategories().get(ZERO).getParent());
        assertThat(result.getProducts().get(ZERO).getCategories().get(ZERO).getIsShown()).isEqualTo(
                dummySearchedProductManagerDto.getCategories().get(ZERO).getIsShown());
        assertThat(result.getProducts().get(ZERO).getCategories().get(ZERO).getDisable()).isEqualTo(
                dummySearchedProductManagerDto.getCategories().get(ZERO).getDisable());
        assertThat(result.getProducts().get(ZERO).getAuthors()).hasSameSizeAs(
                dummySearchedProductManagerDto.getAuthors());
        assertThat(result.getProducts().get(ZERO).getAuthors().get(ZERO).getId()).isEqualTo(
                dummySearchedProductManagerDto.getAuthors()
                        .get(ZERO)
                        .getId());
        assertThat(result.getProducts().get(ZERO).getAuthors().get(ZERO).getName()).isEqualTo(
                dummySearchedProductManagerDto.getAuthors()
                        .get(ZERO)
                        .getName());
        assertThat(result.getProducts().get(ZERO).getTags()).hasSameSizeAs(
                dummySearchedProductManagerDto.getTags());
        assertThat(result.getProducts().get(ZERO).getTags().get(ZERO).getId()).isEqualTo(
                dummySearchedProductManagerDto.getTags().get(ZERO).getId());
        assertThat(result.getProducts().get(ZERO).getTags().get(ZERO).getName()).isEqualTo(
                dummySearchedProductManagerDto.getTags()
                        .get(ZERO)
                        .getName());
        assertThat(result.getProducts().get(ZERO).getSubscribeProducts()).hasSameSizeAs(
                dummySearchedProductManagerDto.getSubscribeProducts());
        assertThat(result.getProducts().get(ZERO).getSubscribeProducts().get(ZERO).getId()).isEqualTo(
                dummySearchedProductManagerDto.getSubscribeProducts().get(ZERO).getId());
        assertThat(result.getProducts().get(ZERO).getSubscribeProducts().get(ZERO).getIssn()).isEqualTo(
                dummySearchedProductManagerDto.getSubscribeProducts().get(ZERO).getIssn());

        verify(searchProductRepository, atLeastOnce()).searchProductsByCategoryId(1L, OFFSET, SIZE);
    }

    @Test
    @DisplayName("카테고리 이름 검색 테스트")
    void testSearchProductsByCategoryName() {
        //given
        String name = "name";
        Mockito.when(searchProductRepository.searchProductsByCategoryName(name, OFFSET, SIZE))
                .thenReturn(dummySearchedProductManagerResponseDto);

        //when
        SearchedProductManagerResponseDto result = searchProductService.searchProductsByCategoryName(
                name,
                OFFSET,
                SIZE
        );

        //then
        assertThat(result.getCount()).isEqualTo(1L);
        assertThat(result.getProducts()).hasSize(1);
        assertThat(result.getProducts()
                .get(0)
                .getId()).isEqualTo(dummySearchedProductManagerDto.getId());
        assertThat(result.getProducts()
                .get(0)
                .getIsbn()).isEqualTo(dummySearchedProductManagerDto.getIsbn());
        assertThat(result.getProducts()
                .get(0)
                .getTitle()).isEqualTo(dummySearchedProductManagerDto.getTitle());
        assertThat(result.getProducts().get(0).getActualPrice()).isEqualTo(
                dummySearchedProductManagerDto.getActualPrice());
        assertThat(result.getProducts().get(0).getDiscountRate()).isEqualTo(
                dummySearchedProductManagerDto.getDiscountRate());
        assertThat(result.getProducts().get(0).isSeparatelyDiscount()).isEqualTo(
                dummySearchedProductManagerDto.isSeparatelyDiscount());
        assertThat(result.getProducts().get(0).getIsGivenPoint()).isEqualTo(
                dummySearchedProductManagerDto.getIsGivenPoint());
        assertThat(result.getProducts().get(0).getGivenPointRate()).isEqualTo(
                dummySearchedProductManagerDto.getGivenPointRate());
        assertThat(result.getProducts()
                .get(0)
                .getIsSale()).isEqualTo(dummySearchedProductManagerDto.getIsSale());
        assertThat(result.getProducts().get(0).getQuantity()).isEqualTo(
                dummySearchedProductManagerDto.getQuantity());
        assertThat(result.getProducts().get(0).getPreferentialShowRanking()).isEqualTo(
                dummySearchedProductManagerDto.getPreferentialShowRanking());
        assertThat(result.getProducts().get(0).getProductType().getId()).isEqualTo(
                dummySearchedProductManagerDto.getProductType().getId());
        assertThat(result.getProducts().get(0).getProductType().getName()).isEqualTo(
                dummySearchedProductManagerDto.getProductType().getName());
        assertThat(result.getProducts().get(0).getSearchedTotalDiscountRate().getId()).isEqualTo(
                dummySearchedProductManagerDto.getSearchedTotalDiscountRate().getId());
        assertThat(result.getProducts()
                .get(0)
                .getSearchedTotalDiscountRate()
                .getDiscountRate()).isEqualTo(dummySearchedProductManagerDto.getSearchedTotalDiscountRate()
                .getDiscountRate());
        assertThat(result.getProducts().get(0).getThumbnailFile().getId()).isEqualTo(
                dummySearchedProductManagerDto.getThumbnailFile().getId());
        assertThat(result.getProducts().get(0).getThumbnailFile().getName()).isEqualTo(
                dummySearchedProductManagerDto.getThumbnailFile().getName());
        assertThat(result.getProducts().get(0).getThumbnailFile().getUploadDateTime()).isEqualTo(
                dummySearchedProductManagerDto.getThumbnailFile().getUploadDateTime());
        assertThat(result.getProducts().get(0).getEbookFile().getId()).isEqualTo(
                dummySearchedProductManagerDto.getEbookFile().getId());
        assertThat(result.getProducts().get(0).getEbookFile().getName()).isEqualTo(
                dummySearchedProductManagerDto.getEbookFile().getName());
        assertThat(result.getProducts().get(0).getEbookFile().getUploadDateTime()).isEqualTo(
                dummySearchedProductManagerDto.getEbookFile().getUploadDateTime());
        assertThat(result.getProducts().get(0).getPublishedDate()).isEqualTo(
                dummySearchedProductManagerDto.getPublishedDate());
        assertThat(result.getProducts().get(0).getSavingMethod()).isEqualTo(
                dummySearchedProductManagerDto.getSavingMethod());
        assertThat(result.getProducts().get(0).getCategories()).hasSameSizeAs(
                dummySearchedProductManagerDto.getCategories());
        assertThat(result.getProducts().get(0).getCategories().get(0).getId()).isEqualTo(
                dummySearchedProductManagerDto.getCategories().get(0).getId());
        assertThat(result.getProducts().get(0).getCategories().get(0).getName()).isEqualTo(
                dummySearchedProductManagerDto.getCategories().get(0).getName());
        assertThat(result.getProducts().get(0).getCategories().get(0).getParent()).isEqualTo(
                dummySearchedProductManagerDto.getCategories().get(0).getParent());
        assertThat(result.getProducts().get(0).getCategories().get(0).getIsShown()).isEqualTo(
                dummySearchedProductManagerDto.getCategories().get(0).getIsShown());
        assertThat(result.getProducts().get(0).getCategories().get(0).getDisable()).isEqualTo(
                dummySearchedProductManagerDto.getCategories().get(0).getDisable());
        assertThat(result.getProducts().get(0).getAuthors()).hasSameSizeAs(
                dummySearchedProductManagerDto.getAuthors());
        assertThat(result.getProducts().get(0).getAuthors().get(0).getId()).isEqualTo(
                dummySearchedProductManagerDto.getAuthors()
                        .get(0)
                        .getId());
        assertThat(result.getProducts().get(0).getAuthors().get(0).getName()).isEqualTo(
                dummySearchedProductManagerDto.getAuthors()
                        .get(0)
                        .getName());
        assertThat(result.getProducts().get(0).getTags()).hasSameSizeAs(
                dummySearchedProductManagerDto.getTags());
        assertThat(result.getProducts().get(0).getTags().get(0).getId()).isEqualTo(
                dummySearchedProductManagerDto.getTags().get(0).getId());
        assertThat(result.getProducts().get(0).getTags().get(0).getName()).isEqualTo(
                dummySearchedProductManagerDto.getTags()
                        .get(0)
                        .getName());
        assertThat(result.getProducts().get(0).getSubscribeProducts()).hasSameSizeAs(
                dummySearchedProductManagerDto.getSubscribeProducts());
        assertThat(result.getProducts().get(0).getSubscribeProducts().get(0).getId()).isEqualTo(
                dummySearchedProductManagerDto.getSubscribeProducts().get(0).getId());
        assertThat(result.getProducts().get(0).getSubscribeProducts().get(0).getIssn()).isEqualTo(
                dummySearchedProductManagerDto.getSubscribeProducts().get(0).getIssn());

        verify(searchProductRepository, atLeastOnce()).searchProductsByCategoryName(
                name,
                OFFSET,
                SIZE
        );
    }

    @Test
    @DisplayName("상품 제목 검색 테스트")
    void testSearchProductsByProductTitle() {
        //given
        String title = "title";
        Mockito.when(searchProductRepository.searchProductsByProductTitle(title, OFFSET, SIZE))
                .thenReturn(dummySearchedProductResponseDto);

        //when
        SearchedProductResponseDto result = searchProductService.searchProductsByProductTitle(
                title,
                OFFSET,
                SIZE
        );

        //then
        assertThat(result.getCount()).isEqualTo(1L);
        assertThat(result.getProducts()).hasSize(1);
        assertThat(result.getProducts().get(0).getId()).isEqualTo(dummySearchedProductDto.getId());
        assertThat(result.getProducts()
                .get(0)
                .getTitle()).isEqualTo(dummySearchedProductDto.getTitle());
        assertThat(result.getProducts()
                .get(0)
                .getDiscountRate()).isEqualTo(dummySearchedProductDto.getDiscountRate());
        assertThat(result.getProducts()
                .get(0)
                .getSellingPrice()).isEqualTo(dummySearchedProductDto.getSellingPrice());
        assertThat(result.getProducts()
                .get(0)
                .getPublishedDate()).isEqualTo(dummySearchedProductDto.getPublishedDate());
        assertThat(result.getProducts()
                .get(0)
                .getIsForcedOutOfStack()).isEqualTo(dummySearchedProductDto.getIsForcedOutOfStack());
        assertThat(result.getProducts().get(0).getCategories()).hasSameSizeAs(
                dummySearchedProductDto.getCategories());
        assertThat(result.getProducts()
                .get(0)
                .getCategories()
                .get(0)
                .getId()).isEqualTo(dummySearchedProductDto.getCategories()
                .get(0)
                .getId());
        assertThat(result.getProducts()
                .get(0)
                .getCategories()
                .get(0)
                .getName()).isEqualTo(dummySearchedProductDto.getCategories()
                .get(0)
                .getName());
        assertThat(result.getProducts()
                .get(0)
                .getCategories()
                .get(0)
                .getParent()).isEqualTo(dummySearchedProductDto.getCategories()
                .get(0)
                .getParent());
        assertThat(result.getProducts()
                .get(0)
                .getCategories()
                .get(0)
                .getIsShown()).isEqualTo(dummySearchedProductDto.getCategories()
                .get(0)
                .getIsShown());
        assertThat(result.getProducts()
                .get(0)
                .getCategories()
                .get(0)
                .getDisable()).isEqualTo(dummySearchedProductDto.getCategories()
                .get(0)
                .getDisable());
        assertThat(result.getProducts().get(0).getAuthors().get(0)).isEqualTo(
                dummySearchedProductDto.getAuthors()
                        .get(0));
        assertThat(result.getProducts()
                .get(0)
                .getTags()
                .get(0)).isEqualTo(dummySearchedProductDto.getTags()
                .get(0));

        verify(searchProductRepository, atLeastOnce()).searchProductsByProductTitle(
                title,
                OFFSET,
                SIZE
        );
    }

    @Test
    @DisplayName("상품 내용으로 검색 테스트")
    void testSearchProductsByProductContent() {
        //given
        String content = "content";
        Mockito.when(searchProductRepository.searchProductsByProductContent(content, OFFSET, SIZE))
                .thenReturn(dummySearchedProductResponseDto);

        //when
        SearchedProductResponseDto result = searchProductService.searchProductsByProductContent(
                content,
                OFFSET,
                SIZE
        );

        //then
        assertThat(result.getCount()).isEqualTo(1L);
        assertThat(result.getProducts()).hasSize(1);
        assertThat(result.getProducts().get(0).getId()).isEqualTo(dummySearchedProductDto.getId());
        assertThat(result.getProducts()
                .get(0)
                .getTitle()).isEqualTo(dummySearchedProductDto.getTitle());
        assertThat(result.getProducts()
                .get(0)
                .getDiscountRate()).isEqualTo(dummySearchedProductDto.getDiscountRate());
        assertThat(result.getProducts()
                .get(0)
                .getSellingPrice()).isEqualTo(dummySearchedProductDto.getSellingPrice());
        assertThat(result.getProducts()
                .get(0)
                .getPublishedDate()).isEqualTo(dummySearchedProductDto.getPublishedDate());
        assertThat(result.getProducts()
                .get(0)
                .getIsForcedOutOfStack()).isEqualTo(dummySearchedProductDto.getIsForcedOutOfStack());
        assertThat(result.getProducts().get(0).getCategories()).hasSameSizeAs(
                dummySearchedProductDto.getCategories());
        assertThat(result.getProducts()
                .get(0)
                .getCategories()
                .get(0)
                .getId()).isEqualTo(dummySearchedProductDto.getCategories()
                .get(0)
                .getId());
        assertThat(result.getProducts()
                .get(0)
                .getCategories()
                .get(0)
                .getName()).isEqualTo(dummySearchedProductDto.getCategories()
                .get(0)
                .getName());
        assertThat(result.getProducts()
                .get(0)
                .getCategories()
                .get(0)
                .getParent()).isEqualTo(dummySearchedProductDto.getCategories()
                .get(0)
                .getParent());
        assertThat(result.getProducts()
                .get(0)
                .getCategories()
                .get(0)
                .getIsShown()).isEqualTo(dummySearchedProductDto.getCategories()
                .get(0)
                .getIsShown());
        assertThat(result.getProducts()
                .get(0)
                .getCategories()
                .get(0)
                .getDisable()).isEqualTo(dummySearchedProductDto.getCategories()
                .get(0)
                .getDisable());
        assertThat(result.getProducts().get(0).getAuthors().get(0)).isEqualTo(
                dummySearchedProductDto.getAuthors()
                        .get(0));
        assertThat(result.getProducts()
                .get(0)
                .getTags()
                .get(0)).isEqualTo(dummySearchedProductDto.getTags()
                .get(0));

        verify(searchProductRepository, atLeastOnce()).searchProductsByProductContent(
                content,
                OFFSET,
                SIZE
        );
    }

    @Test
    @DisplayName("ISBN으로 검색")
    void testSearchProductsByProductISBN() {
        //given
        String isbn = "ISBN";
        Mockito.when(searchProductRepository.searchProductsByProductISBN(isbn, OFFSET, SIZE))
                .thenReturn(dummySearchedProductResponseDto);

        //when
        SearchedProductResponseDto result = searchProductService.searchProductsByProductISBN(
                isbn,
                OFFSET,
                SIZE
        );

        //then
        //then
        assertThat(result.getCount()).isEqualTo(1L);
        assertThat(result.getProducts()).hasSize(1);
        assertThat(result.getProducts().get(0).getId()).isEqualTo(dummySearchedProductDto.getId());
        assertThat(result.getProducts()
                .get(0)
                .getTitle()).isEqualTo(dummySearchedProductDto.getTitle());
        assertThat(result.getProducts()
                .get(0)
                .getDiscountRate()).isEqualTo(dummySearchedProductDto.getDiscountRate());
        assertThat(result.getProducts()
                .get(0)
                .getSellingPrice()).isEqualTo(dummySearchedProductDto.getSellingPrice());
        assertThat(result.getProducts()
                .get(0)
                .getPublishedDate()).isEqualTo(dummySearchedProductDto.getPublishedDate());
        assertThat(result.getProducts()
                .get(0)
                .getIsForcedOutOfStack()).isEqualTo(dummySearchedProductDto.getIsForcedOutOfStack());
        assertThat(result.getProducts().get(0).getCategories()).hasSameSizeAs(
                dummySearchedProductDto.getCategories());
        assertThat(result.getProducts()
                .get(0)
                .getCategories()
                .get(0)
                .getId()).isEqualTo(dummySearchedProductDto.getCategories()
                .get(0)
                .getId());
        assertThat(result.getProducts()
                .get(0)
                .getCategories()
                .get(0)
                .getName()).isEqualTo(dummySearchedProductDto.getCategories()
                .get(0)
                .getName());
        assertThat(result.getProducts()
                .get(0)
                .getCategories()
                .get(0)
                .getParent()).isEqualTo(dummySearchedProductDto.getCategories()
                .get(0)
                .getParent());
        assertThat(result.getProducts()
                .get(0)
                .getCategories()
                .get(0)
                .getIsShown()).isEqualTo(dummySearchedProductDto.getCategories()
                .get(0)
                .getIsShown());
        assertThat(result.getProducts()
                .get(0)
                .getCategories()
                .get(0)
                .getDisable()).isEqualTo(dummySearchedProductDto.getCategories()
                .get(0)
                .getDisable());
        assertThat(result.getProducts().get(0).getAuthors().get(0)).isEqualTo(
                dummySearchedProductDto.getAuthors()
                        .get(0));
        assertThat(result.getProducts()
                .get(0)
                .getTags()
                .get(0)).isEqualTo(dummySearchedProductDto.getTags()
                .get(0));

        verify(searchProductRepository, atLeastOnce()).searchProductsByProductISBN(
                isbn,
                OFFSET,
                SIZE
        );
    }

    @Test
    @DisplayName("작가 이름으로 검색")
    void testSearchProductsByProductAuthor() {
        //given
        String author = "author";
        Mockito.when(searchProductRepository.searchProductsByProductAuthor(author, OFFSET, SIZE))
                .thenReturn(dummySearchedProductResponseDto);

        //when
        SearchedProductResponseDto result = searchProductService.searchProductsByProductAuthor(
                author,
                OFFSET,
                SIZE
        );

        //then
        assertThat(result.getCount()).isEqualTo(1);
        assertThat(result.getProducts()).hasSize(1);
        assertThat(result.getProducts().get(0).getId()).isEqualTo(dummySearchedProductDto.getId());
        assertThat(result.getProducts()
                .get(0)
                .getTitle()).isEqualTo(dummySearchedProductDto.getTitle());
        assertThat(result.getProducts()
                .get(0)
                .getDiscountRate()).isEqualTo(dummySearchedProductDto.getDiscountRate());
        assertThat(result.getProducts()
                .get(0)
                .getSellingPrice()).isEqualTo(dummySearchedProductDto.getSellingPrice());
        assertThat(result.getProducts()
                .get(0)
                .getPublishedDate()).isEqualTo(dummySearchedProductDto.getPublishedDate());
        assertThat(result.getProducts()
                .get(0)
                .getIsForcedOutOfStack()).isEqualTo(dummySearchedProductDto.getIsForcedOutOfStack());
        assertThat(result.getProducts().get(0).getCategories()).hasSameSizeAs(
                dummySearchedProductDto.getCategories());
        assertThat(result.getProducts()
                .get(0)
                .getCategories()
                .get(0)
                .getId()).isEqualTo(dummySearchedProductDto.getCategories()
                .get(0)
                .getId());
        assertThat(result.getProducts()
                .get(0)
                .getCategories()
                .get(0)
                .getName()).isEqualTo(dummySearchedProductDto.getCategories()
                .get(0)
                .getName());
        assertThat(result.getProducts()
                .get(0)
                .getCategories()
                .get(0)
                .getParent()).isEqualTo(dummySearchedProductDto.getCategories()
                .get(0)
                .getParent());
        assertThat(result.getProducts()
                .get(0)
                .getCategories()
                .get(0)
                .getIsShown()).isEqualTo(dummySearchedProductDto.getCategories()
                .get(0)
                .getIsShown());
        assertThat(result.getProducts()
                .get(0)
                .getCategories()
                .get(0)
                .getDisable()).isEqualTo(dummySearchedProductDto.getCategories()
                .get(0)
                .getDisable());
        assertThat(result.getProducts().get(0).getAuthors().get(0)).isEqualTo(
                dummySearchedProductDto.getAuthors()
                        .get(0));
        assertThat(result.getProducts()
                .get(0)
                .getTags()
                .get(0)).isEqualTo(dummySearchedProductDto.getTags()
                .get(0));

        verify(searchProductRepository, atLeastOnce()).searchProductsByProductAuthor(
                author,
                OFFSET,
                SIZE
        );
    }

    @Test
    @DisplayName("출판사로 검색")
    void testSearchProductsByPublisher() {
        //given
        String publisher = "publisher";
        Mockito.when(searchProductRepository.searchProductsByPublisher(publisher, OFFSET, SIZE))
                .thenReturn(dummySearchedProductResponseDto);

        //when
        SearchedProductResponseDto result = searchProductService.searchProductsByPublisher(
                publisher,
                OFFSET,
                SIZE
        );

        //then
        assertThat(result.getCount()).isEqualTo(1L);
        assertThat(result.getProducts()).hasSize(1);
        assertThat(result.getProducts().get(0).getId()).isEqualTo(dummySearchedProductDto.getId());
        assertThat(result.getProducts()
                .get(0)
                .getTitle()).isEqualTo(dummySearchedProductDto.getTitle());
        assertThat(result.getProducts()
                .get(0)
                .getDiscountRate()).isEqualTo(dummySearchedProductDto.getDiscountRate());
        assertThat(result.getProducts()
                .get(0)
                .getSellingPrice()).isEqualTo(dummySearchedProductDto.getSellingPrice());
        assertThat(result.getProducts()
                .get(0)
                .getPublishedDate()).isEqualTo(dummySearchedProductDto.getPublishedDate());
        assertThat(result.getProducts()
                .get(0)
                .getIsForcedOutOfStack()).isEqualTo(dummySearchedProductDto.getIsForcedOutOfStack());
        assertThat(result.getProducts().get(0).getCategories()).hasSameSizeAs(
                dummySearchedProductDto.getCategories());
        assertThat(result.getProducts()
                .get(0)
                .getCategories()
                .get(0)
                .getId()).isEqualTo(dummySearchedProductDto.getCategories()
                .get(0)
                .getId());
        assertThat(result.getProducts()
                .get(0)
                .getCategories()
                .get(0)
                .getName()).isEqualTo(dummySearchedProductDto.getCategories()
                .get(0)
                .getName());
        assertThat(result.getProducts()
                .get(0)
                .getCategories()
                .get(0)
                .getParent()).isEqualTo(dummySearchedProductDto.getCategories()
                .get(0)
                .getParent());
        assertThat(result.getProducts()
                .get(0)
                .getCategories()
                .get(0)
                .getIsShown()).isEqualTo(dummySearchedProductDto.getCategories()
                .get(0)
                .getIsShown());
        assertThat(result.getProducts()
                .get(0)
                .getCategories()
                .get(0)
                .getDisable()).isEqualTo(dummySearchedProductDto.getCategories()
                .get(0)
                .getDisable());
        assertThat(result.getProducts().get(0).getAuthors().get(0)).isEqualTo(
                dummySearchedProductDto.getAuthors()
                        .get(0));
        assertThat(result.getProducts()
                .get(0)
                .getTags()
                .get(0)).isEqualTo(dummySearchedProductDto.getTags()
                .get(0));

        verify(searchProductRepository, atLeastOnce()).searchProductsByPublisher(
                publisher,
                OFFSET,
                SIZE
        );
    }

    @Test
    @DisplayName("태그로 검색")
    void testSearchProductsByTag() {
        //given
        String tag = "tag";
        Mockito.when(searchProductRepository.searchProductsByTag(tag, OFFSET, SIZE))
                .thenReturn(dummySearchedProductResponseDto);

        //when
        SearchedProductResponseDto result = searchProductService.searchProductsByTag(
                tag,
                OFFSET,
                SIZE
        );

        //then
        assertThat(result.getCount()).isEqualTo(1L);
        assertThat(result.getProducts()).hasSize(1);
        assertThat(result.getProducts().get(0).getId()).isEqualTo(dummySearchedProductDto.getId());
        assertThat(result.getProducts()
                .get(0)
                .getTitle()).isEqualTo(dummySearchedProductDto.getTitle());
        assertThat(result.getProducts()
                .get(0)
                .getDiscountRate()).isEqualTo(dummySearchedProductDto.getDiscountRate());
        assertThat(result.getProducts()
                .get(0)
                .getSellingPrice()).isEqualTo(dummySearchedProductDto.getSellingPrice());
        assertThat(result.getProducts()
                .get(0)
                .getPublishedDate()).isEqualTo(dummySearchedProductDto.getPublishedDate());
        assertThat(result.getProducts()
                .get(0)
                .getIsForcedOutOfStack()).isEqualTo(dummySearchedProductDto.getIsForcedOutOfStack());
        assertThat(result.getProducts().get(0).getCategories()).hasSameSizeAs(
                dummySearchedProductDto.getCategories());
        assertThat(result.getProducts()
                .get(0)
                .getCategories()
                .get(0)
                .getId()).isEqualTo(dummySearchedProductDto.getCategories()
                .get(0)
                .getId());
        assertThat(result.getProducts()
                .get(0)
                .getCategories()
                .get(0)
                .getName()).isEqualTo(dummySearchedProductDto.getCategories()
                .get(0)
                .getName());
        assertThat(result.getProducts()
                .get(0)
                .getCategories()
                .get(0)
                .getParent()).isEqualTo(dummySearchedProductDto.getCategories()
                .get(0)
                .getParent());
        assertThat(result.getProducts()
                .get(0)
                .getCategories()
                .get(0)
                .getIsShown()).isEqualTo(dummySearchedProductDto.getCategories()
                .get(0)
                .getIsShown());
        assertThat(result.getProducts()
                .get(0)
                .getCategories()
                .get(0)
                .getDisable()).isEqualTo(dummySearchedProductDto.getCategories()
                .get(0)
                .getDisable());
        assertThat(result.getProducts().get(0).getAuthors().get(0)).isEqualTo(
                dummySearchedProductDto.getAuthors()
                        .get(0));
        assertThat(result.getProducts()
                .get(0)
                .getTags()
                .get(0)).isEqualTo(dummySearchedProductDto.getTags()
                .get(0));

        verify(searchProductRepository, atLeastOnce()).searchProductsByTag(
                tag,
                OFFSET,
                SIZE
        );
    }
}