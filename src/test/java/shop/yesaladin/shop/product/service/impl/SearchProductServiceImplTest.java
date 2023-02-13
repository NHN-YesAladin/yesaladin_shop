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
                .isForcedOutOfStock(false)
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
                .thenReturn(dummySearchedProductResponseDto);

        //when
        SearchedProductResponseDto result = searchProductService.searchProductsByCategoryId(
                1L,
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
                .getIsForcedOutOfStock()).isEqualTo(dummySearchedProductDto.getIsForcedOutOfStock());
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

        verify(searchProductRepository, atLeastOnce()).searchProductsByCategoryId(1L, OFFSET, SIZE);
    }

    @Test
    @DisplayName("카테고리 이름 검색 테스트")
    void testSearchProductsByCategoryName() {
        //given
        String name = "name";
        Mockito.when(searchProductRepository.searchProductsByCategoryName(name, OFFSET, SIZE))
                .thenReturn(dummySearchedProductResponseDto);

        //when
        SearchedProductResponseDto result = searchProductService.searchProductsByCategoryName(
                name,
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
                .getIsForcedOutOfStock()).isEqualTo(dummySearchedProductDto.getIsForcedOutOfStock());
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
                .getIsForcedOutOfStock()).isEqualTo(dummySearchedProductDto.getIsForcedOutOfStock());
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
                .getIsForcedOutOfStock()).isEqualTo(dummySearchedProductDto.getIsForcedOutOfStock());
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
                .getIsForcedOutOfStock()).isEqualTo(dummySearchedProductDto.getIsForcedOutOfStock());
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
                .getIsForcedOutOfStock()).isEqualTo(dummySearchedProductDto.getIsForcedOutOfStock());
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
                .getIsForcedOutOfStock()).isEqualTo(dummySearchedProductDto.getIsForcedOutOfStock());
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
                .getIsForcedOutOfStock()).isEqualTo(dummySearchedProductDto.getIsForcedOutOfStock());
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