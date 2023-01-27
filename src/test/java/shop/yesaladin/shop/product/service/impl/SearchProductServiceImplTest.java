package shop.yesaladin.shop.product.service.impl;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.shop.product.domain.model.SearchedProduct.SearchedAuthor;
import shop.yesaladin.shop.product.domain.model.SearchedProduct.SearchedCategories;
import shop.yesaladin.shop.product.domain.model.SearchedProduct.SearchedFile;
import shop.yesaladin.shop.product.domain.model.SearchedProduct.SearchedTags;
import shop.yesaladin.shop.product.domain.repository.SearchProductRepository;
import shop.yesaladin.shop.product.dto.SearchedProductResponseDto;

class SearchProductServiceImplTest {

    SearchProductRepository searchProductRepository;
    SearchProductServiceImpl searchProductService;
    SearchedProductResponseDto dto;
    List<SearchedProductResponseDto> dummy;

    private static final int OFFSET = 0;
    private static final int SIZE = 1;

    @BeforeEach
    void setUp() {
        searchProductRepository = Mockito.mock(SearchProductRepository.class);
        searchProductService = new SearchProductServiceImpl(searchProductRepository);
        dto = SearchedProductResponseDto.builder()
                .id(-1L)
                .title("title")
                .discountRate(10)
                .sellingPrice(1000L)
                .authors(List.of(new SearchedAuthor(1L, "author")))
                .isForcedOutOfStack(false)
                .thumbnailFileUrl(new SearchedFile(1L, "깃 허브.jpg", LocalDate.now()))
                .publishedDate(LocalDate.now().toString())
                .categories(List.of(new SearchedCategories(12L, null, "국내소설", true, false)))
                .tags(List.of(new SearchedTags(1L, "tag1")))
                .build();

        dummy = List.of(dto);
    }

    @Test
    @DisplayName("카테고리 id 검색 테스트")
    void testSearchProductsByCategoryId() {
        //given
        Mockito.when(searchProductRepository.searchProductsByCategoryId(1L, OFFSET, SIZE))
                .thenReturn(dummy);

        //when
        List<SearchedProductResponseDto> result = searchProductService.searchProductsByCategoryId(
                1L,
                OFFSET,
                SIZE
        );

        //then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(dto.getId());
        assertThat(result.get(0).getTitle()).isEqualTo(dto.getTitle());
        assertThat(result.get(0).getDiscountRate()).isEqualTo(dto.getDiscountRate());
        assertThat(result.get(0).getSellingPrice()).isEqualTo(dto.getSellingPrice());
        assertThat(result.get(0).getPublishedDate()).isEqualTo(dto.getPublishedDate());
        assertThat(result.get(0).getIsForcedOutOfStack()).isEqualTo(dto.getIsForcedOutOfStack());
        assertThat(result.get(0).getCategories()).hasSameSizeAs(dto.getCategories());
        assertThat(result.get(0).getCategories().get(0).getId()).isEqualTo(dto.getCategories()
                .get(0)
                .getId());
        assertThat(result.get(0).getCategories().get(0).getName()).isEqualTo(dto.getCategories()
                .get(0)
                .getName());
        assertThat(result.get(0).getCategories().get(0).getParent()).isEqualTo(dto.getCategories()
                .get(0)
                .getParent());
        assertThat(result.get(0).getCategories().get(0).getIsShown()).isEqualTo(dto.getCategories()
                .get(0)
                .getIsShown());
        assertThat(result.get(0).getCategories().get(0).getDisable()).isEqualTo(dto.getCategories()
                .get(0)
                .getDisable());
        assertThat(result.get(0).getAuthors().get(0).getId()).isEqualTo(dto.getAuthors()
                .get(0)
                .getId());
        assertThat(result.get(0).getAuthors().get(0).getName()).isEqualTo(dto.getAuthors()
                .get(0)
                .getName());
        assertThat(result.get(0).getTags().get(0).getId()).isEqualTo(dto.getTags().get(0).getId());
        assertThat(result.get(0).getTags().get(0).getName()).isEqualTo(dto.getTags()
                .get(0)
                .getName());

        verify(searchProductRepository, atLeastOnce()).searchProductsByCategoryId(1L, OFFSET, SIZE);
    }

    @Test
    @DisplayName("카테고리 이름 검색 테스트")
    void testSearchProductsByCategoryName() {
        //given
        String name = "name";
        Mockito.when(searchProductRepository.searchProductsByCategoryName(name, OFFSET, SIZE))
                .thenReturn(dummy);

        //when
        List<SearchedProductResponseDto> result = searchProductService.searchProductsByCategoryName(
                name,
                OFFSET,
                SIZE
        );

        //then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(dto.getId());
        assertThat(result.get(0).getTitle()).isEqualTo(dto.getTitle());
        assertThat(result.get(0).getDiscountRate()).isEqualTo(dto.getDiscountRate());
        assertThat(result.get(0).getSellingPrice()).isEqualTo(dto.getSellingPrice());
        assertThat(result.get(0).getPublishedDate()).isEqualTo(dto.getPublishedDate());
        assertThat(result.get(0).getIsForcedOutOfStack()).isEqualTo(dto.getIsForcedOutOfStack());
        assertThat(result.get(0).getCategories()).hasSameSizeAs(dto.getCategories());
        assertThat(result.get(0).getCategories().get(0).getId()).isEqualTo(dto.getCategories()
                .get(0)
                .getId());
        assertThat(result.get(0).getCategories().get(0).getName()).isEqualTo(dto.getCategories()
                .get(0)
                .getName());
        assertThat(result.get(0).getCategories().get(0).getParent()).isEqualTo(dto.getCategories()
                .get(0)
                .getParent());
        assertThat(result.get(0).getCategories().get(0).getIsShown()).isEqualTo(dto.getCategories()
                .get(0)
                .getIsShown());
        assertThat(result.get(0).getCategories().get(0).getDisable()).isEqualTo(dto.getCategories()
                .get(0)
                .getDisable());
        assertThat(result.get(0).getAuthors().get(0).getId()).isEqualTo(dto.getAuthors()
                .get(0)
                .getId());
        assertThat(result.get(0).getAuthors().get(0).getName()).isEqualTo(dto.getAuthors()
                .get(0)
                .getName());
        assertThat(result.get(0).getTags().get(0).getId()).isEqualTo(dto.getTags().get(0).getId());
        assertThat(result.get(0).getTags().get(0).getName()).isEqualTo(dto.getTags()
                .get(0)
                .getName());

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
                .thenReturn(dummy);

        //when
        List<SearchedProductResponseDto> result = searchProductService.searchProductsByProductTitle(
                title,
                OFFSET,
                SIZE
        );

        //then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(dto.getId());
        assertThat(result.get(0).getTitle()).isEqualTo(dto.getTitle());
        assertThat(result.get(0).getDiscountRate()).isEqualTo(dto.getDiscountRate());
        assertThat(result.get(0).getSellingPrice()).isEqualTo(dto.getSellingPrice());
        assertThat(result.get(0).getPublishedDate()).isEqualTo(dto.getPublishedDate());
        assertThat(result.get(0).getIsForcedOutOfStack()).isEqualTo(dto.getIsForcedOutOfStack());
        assertThat(result.get(0).getCategories()).hasSameSizeAs(dto.getCategories());
        assertThat(result.get(0).getCategories().get(0).getId()).isEqualTo(dto.getCategories()
                .get(0)
                .getId());
        assertThat(result.get(0).getCategories().get(0).getName()).isEqualTo(dto.getCategories()
                .get(0)
                .getName());
        assertThat(result.get(0).getCategories().get(0).getParent()).isEqualTo(dto.getCategories()
                .get(0)
                .getParent());
        assertThat(result.get(0).getCategories().get(0).getIsShown()).isEqualTo(dto.getCategories()
                .get(0)
                .getIsShown());
        assertThat(result.get(0).getCategories().get(0).getDisable()).isEqualTo(dto.getCategories()
                .get(0)
                .getDisable());
        assertThat(result.get(0).getAuthors().get(0).getId()).isEqualTo(dto.getAuthors()
                .get(0)
                .getId());
        assertThat(result.get(0).getAuthors().get(0).getName()).isEqualTo(dto.getAuthors()
                .get(0)
                .getName());
        assertThat(result.get(0).getTags().get(0).getId()).isEqualTo(dto.getTags().get(0).getId());
        assertThat(result.get(0).getTags().get(0).getName()).isEqualTo(dto.getTags()
                .get(0)
                .getName());

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
                .thenReturn(dummy);

        //when
        List<SearchedProductResponseDto> result = searchProductService.searchProductsByProductContent(
                content,
                OFFSET,
                SIZE
        );

        //then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(dto.getId());
        assertThat(result.get(0).getTitle()).isEqualTo(dto.getTitle());
        assertThat(result.get(0).getDiscountRate()).isEqualTo(dto.getDiscountRate());
        assertThat(result.get(0).getSellingPrice()).isEqualTo(dto.getSellingPrice());
        assertThat(result.get(0).getPublishedDate()).isEqualTo(dto.getPublishedDate());
        assertThat(result.get(0).getIsForcedOutOfStack()).isEqualTo(dto.getIsForcedOutOfStack());
        assertThat(result.get(0).getCategories()).hasSameSizeAs(dto.getCategories());
        assertThat(result.get(0).getCategories().get(0).getId()).isEqualTo(dto.getCategories()
                .get(0)
                .getId());
        assertThat(result.get(0).getCategories().get(0).getName()).isEqualTo(dto.getCategories()
                .get(0)
                .getName());
        assertThat(result.get(0).getCategories().get(0).getParent()).isEqualTo(dto.getCategories()
                .get(0)
                .getParent());
        assertThat(result.get(0).getCategories().get(0).getIsShown()).isEqualTo(dto.getCategories()
                .get(0)
                .getIsShown());
        assertThat(result.get(0).getCategories().get(0).getDisable()).isEqualTo(dto.getCategories()
                .get(0)
                .getDisable());
        assertThat(result.get(0).getAuthors().get(0).getId()).isEqualTo(dto.getAuthors()
                .get(0)
                .getId());
        assertThat(result.get(0).getAuthors().get(0).getName()).isEqualTo(dto.getAuthors()
                .get(0)
                .getName());
        assertThat(result.get(0).getTags().get(0).getId()).isEqualTo(dto.getTags().get(0).getId());
        assertThat(result.get(0).getTags().get(0).getName()).isEqualTo(dto.getTags()
                .get(0)
                .getName());

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
                .thenReturn(dummy);

        //when
        List<SearchedProductResponseDto> result = searchProductService.searchProductsByProductISBN(
                isbn,
                OFFSET,
                SIZE
        );

        //then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(dto.getId());
        assertThat(result.get(0).getTitle()).isEqualTo(dto.getTitle());
        assertThat(result.get(0).getDiscountRate()).isEqualTo(dto.getDiscountRate());
        assertThat(result.get(0).getSellingPrice()).isEqualTo(dto.getSellingPrice());
        assertThat(result.get(0).getPublishedDate()).isEqualTo(dto.getPublishedDate());
        assertThat(result.get(0).getIsForcedOutOfStack()).isEqualTo(dto.getIsForcedOutOfStack());
        assertThat(result.get(0).getCategories()).hasSameSizeAs(dto.getCategories());
        assertThat(result.get(0).getCategories().get(0).getId()).isEqualTo(dto.getCategories()
                .get(0)
                .getId());
        assertThat(result.get(0).getCategories().get(0).getName()).isEqualTo(dto.getCategories()
                .get(0)
                .getName());
        assertThat(result.get(0).getCategories().get(0).getParent()).isEqualTo(dto.getCategories()
                .get(0)
                .getParent());
        assertThat(result.get(0).getCategories().get(0).getIsShown()).isEqualTo(dto.getCategories()
                .get(0)
                .getIsShown());
        assertThat(result.get(0).getCategories().get(0).getDisable()).isEqualTo(dto.getCategories()
                .get(0)
                .getDisable());
        assertThat(result.get(0).getAuthors().get(0).getId()).isEqualTo(dto.getAuthors()
                .get(0)
                .getId());
        assertThat(result.get(0).getAuthors().get(0).getName()).isEqualTo(dto.getAuthors()
                .get(0)
                .getName());
        assertThat(result.get(0).getTags().get(0).getId()).isEqualTo(dto.getTags().get(0).getId());
        assertThat(result.get(0).getTags().get(0).getName()).isEqualTo(dto.getTags()
                .get(0)
                .getName());

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
        String publisher = "publisher";
        Mockito.when(searchProductRepository.searchProductsByPublisher(publisher, OFFSET, SIZE))
                .thenReturn(dummy);

        //when
        List<SearchedProductResponseDto> result = searchProductService.searchProductsByPublisher(
                publisher,
                OFFSET,
                SIZE
        );

        //then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(dto.getId());
        assertThat(result.get(0).getTitle()).isEqualTo(dto.getTitle());
        assertThat(result.get(0).getDiscountRate()).isEqualTo(dto.getDiscountRate());
        assertThat(result.get(0).getSellingPrice()).isEqualTo(dto.getSellingPrice());
        assertThat(result.get(0).getPublishedDate()).isEqualTo(dto.getPublishedDate());
        assertThat(result.get(0).getIsForcedOutOfStack()).isEqualTo(dto.getIsForcedOutOfStack());
        assertThat(result.get(0).getCategories()).hasSameSizeAs(dto.getCategories());
        assertThat(result.get(0).getCategories().get(0).getId()).isEqualTo(dto.getCategories()
                .get(0)
                .getId());
        assertThat(result.get(0).getCategories().get(0).getName()).isEqualTo(dto.getCategories()
                .get(0)
                .getName());
        assertThat(result.get(0).getCategories().get(0).getParent()).isEqualTo(dto.getCategories()
                .get(0)
                .getParent());
        assertThat(result.get(0).getCategories().get(0).getIsShown()).isEqualTo(dto.getCategories()
                .get(0)
                .getIsShown());
        assertThat(result.get(0).getCategories().get(0).getDisable()).isEqualTo(dto.getCategories()
                .get(0)
                .getDisable());
        assertThat(result.get(0).getAuthors().get(0).getId()).isEqualTo(dto.getAuthors()
                .get(0)
                .getId());
        assertThat(result.get(0).getAuthors().get(0).getName()).isEqualTo(dto.getAuthors()
                .get(0)
                .getName());
        assertThat(result.get(0).getTags().get(0).getId()).isEqualTo(dto.getTags().get(0).getId());
        assertThat(result.get(0).getTags().get(0).getName()).isEqualTo(dto.getTags()
                .get(0)
                .getName());

        verify(searchProductRepository, atLeastOnce()).searchProductsByPublisher(
                publisher,
                OFFSET,
                SIZE
        );
    }

    @Test
    @DisplayName("출판사로 검색")
    void testSearchProductsByPublisher() {
        //given
        String author = "name";
        Mockito.when(searchProductRepository.searchProductsByProductAuthor(author, OFFSET, SIZE))
                .thenReturn(dummy);

        //when
        List<SearchedProductResponseDto> result = searchProductService.searchProductsByProductAuthor(
                author,
                OFFSET,
                SIZE
        );

        //then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(dto.getId());
        assertThat(result.get(0).getTitle()).isEqualTo(dto.getTitle());
        assertThat(result.get(0).getDiscountRate()).isEqualTo(dto.getDiscountRate());
        assertThat(result.get(0).getSellingPrice()).isEqualTo(dto.getSellingPrice());
        assertThat(result.get(0).getPublishedDate()).isEqualTo(dto.getPublishedDate());
        assertThat(result.get(0).getIsForcedOutOfStack()).isEqualTo(dto.getIsForcedOutOfStack());
        assertThat(result.get(0).getCategories()).hasSameSizeAs(dto.getCategories());
        assertThat(result.get(0).getCategories().get(0).getId()).isEqualTo(dto.getCategories()
                .get(0)
                .getId());
        assertThat(result.get(0).getCategories().get(0).getName()).isEqualTo(dto.getCategories()
                .get(0)
                .getName());
        assertThat(result.get(0).getCategories().get(0).getParent()).isEqualTo(dto.getCategories()
                .get(0)
                .getParent());
        assertThat(result.get(0).getCategories().get(0).getIsShown()).isEqualTo(dto.getCategories()
                .get(0)
                .getIsShown());
        assertThat(result.get(0).getCategories().get(0).getDisable()).isEqualTo(dto.getCategories()
                .get(0)
                .getDisable());
        assertThat(result.get(0).getAuthors().get(0).getId()).isEqualTo(dto.getAuthors()
                .get(0)
                .getId());
        assertThat(result.get(0).getAuthors().get(0).getName()).isEqualTo(dto.getAuthors()
                .get(0)
                .getName());
        assertThat(result.get(0).getTags().get(0).getId()).isEqualTo(dto.getTags().get(0).getId());
        assertThat(result.get(0).getTags().get(0).getName()).isEqualTo(dto.getTags()
                .get(0)
                .getName());

        verify(searchProductRepository, atLeastOnce()).searchProductsByProductAuthor(
                author,
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
                .thenReturn(dummy);

        //when
        List<SearchedProductResponseDto> result = searchProductService.searchProductsByTag(
                tag,
                OFFSET,
                SIZE
        );

        //then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(dto.getId());
        assertThat(result.get(0).getTitle()).isEqualTo(dto.getTitle());
        assertThat(result.get(0).getDiscountRate()).isEqualTo(dto.getDiscountRate());
        assertThat(result.get(0).getSellingPrice()).isEqualTo(dto.getSellingPrice());
        assertThat(result.get(0).getPublishedDate()).isEqualTo(dto.getPublishedDate());
        assertThat(result.get(0).getIsForcedOutOfStack()).isEqualTo(dto.getIsForcedOutOfStack());
        assertThat(result.get(0).getCategories()).hasSameSizeAs(dto.getCategories());
        assertThat(result.get(0).getCategories().get(0).getId()).isEqualTo(dto.getCategories()
                .get(0)
                .getId());
        assertThat(result.get(0).getCategories().get(0).getName()).isEqualTo(dto.getCategories()
                .get(0)
                .getName());
        assertThat(result.get(0).getCategories().get(0).getParent()).isEqualTo(dto.getCategories()
                .get(0)
                .getParent());
        assertThat(result.get(0).getCategories().get(0).getIsShown()).isEqualTo(dto.getCategories()
                .get(0)
                .getIsShown());
        assertThat(result.get(0).getCategories().get(0).getDisable()).isEqualTo(dto.getCategories()
                .get(0)
                .getDisable());
        assertThat(result.get(0).getAuthors().get(0).getId()).isEqualTo(dto.getAuthors()
                .get(0)
                .getId());
        assertThat(result.get(0).getAuthors().get(0).getName()).isEqualTo(dto.getAuthors()
                .get(0)
                .getName());
        assertThat(result.get(0).getTags().get(0).getId()).isEqualTo(dto.getTags().get(0).getId());
        assertThat(result.get(0).getTags().get(0).getName()).isEqualTo(dto.getTags()
                .get(0)
                .getName());

        verify(searchProductRepository, atLeastOnce()).searchProductsByTag(
                tag,
                OFFSET,
                SIZE
        );
    }
}