package shop.yesaladin.shop.product.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.product.domain.repository.SearchProductRepository;
import shop.yesaladin.shop.product.dto.SearchedProductResponseDto;

class SearchProductServiceImplTest {

    SearchProductRepository searchProductRepository;
    SearchProductServiceImpl searchProductService;
    SearchedProductResponseDto responseDto;
    Pageable pageable = PageRequest.of(0, 1);


    @BeforeEach
    void setUp() {
        searchProductRepository = Mockito.mock(SearchProductRepository.class);
        searchProductService = new SearchProductServiceImpl(searchProductRepository);

        responseDto = SearchedProductResponseDto.builder()
                .id(1L)
                .title("title")
                .isbn("isbn")
                .quantity(1000L)
                .sellingPrice(1000L)
                .rate(10)
                .isForcedOutOfStock(false)
                .publisher("publisher")
                .authors(List.of("author"))
                .tags(List.of("tags"))
                .thumbnailFile("file")
                .build();

    }

    @Test
    @DisplayName("카테고리 id 검색 테스트")
    void testSearchProductsByCategoryId() {
        //given
        Mockito.when(searchProductRepository.searchProductsByCategoryId(1L, pageable))
                .thenReturn(new PageImpl<>(List.of(responseDto), pageable, 1L));

        //when
        Page<SearchedProductResponseDto> result = searchProductService.searchProductsByCategoryId(
                1L,
                pageable
        );

        //then
        assertThat(result).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(responseDto.getId());
        assertThat(result.getContent().get(0).getTitle()).isEqualTo(responseDto.getTitle());
        assertThat(result.getContent().get(0).getQuantity()).isEqualTo(responseDto.getQuantity());
        assertThat(result.getContent().get(0).getSellingPrice()).isEqualTo(responseDto.getSellingPrice());
        assertThat(result.getContent().get(0).getRate()).isEqualTo(responseDto.getRate());
        assertThat(result.getContent().get(0).getIsForcedOutOfStock()).isEqualTo(responseDto.getIsForcedOutOfStock());
        assertThat(result.getContent().get(0).getIsbn()).isEqualTo(responseDto.getIsbn());
        assertThat(result.getContent().get(0).getPublisher()).isEqualTo(responseDto.getPublisher());
        assertThat(result.getContent().get(0).getAuthors()).hasSize(responseDto.getAuthors().size());
        assertThat(result.getContent().get(0).getAuthors().get(0)).isEqualTo(responseDto.getAuthors().get(0));
        assertThat(result.getContent().get(0).getThumbnailFile()).isEqualTo(responseDto.getThumbnailFile());
        assertThat(result.getContent().get(0).getTags()).hasSize(responseDto.getTags().size());
        assertThat(result.getContent().get(0).getTags().get(0)).isEqualTo(responseDto.getTags().get(0));
    }

    @Test
    @DisplayName("상품 제목 검색 테스트")
    void testSearchProductsByProductTitle() {
        //given
        Mockito.when(searchProductRepository.searchProductsByProductTitle("title", pageable))
                .thenReturn(new PageImpl<>(List.of(responseDto), pageable, 1L));

        //when
        Page<SearchedProductResponseDto> result = searchProductService.searchProductsByProductTitle(
                "title",
                pageable
        );

        //then
        assertThat(result).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(responseDto.getId());
        assertThat(result.getContent().get(0).getTitle()).isEqualTo(responseDto.getTitle());
        assertThat(result.getContent().get(0).getQuantity()).isEqualTo(responseDto.getQuantity());
        assertThat(result.getContent().get(0).getSellingPrice()).isEqualTo(responseDto.getSellingPrice());
        assertThat(result.getContent().get(0).getRate()).isEqualTo(responseDto.getRate());
        assertThat(result.getContent().get(0).getIsForcedOutOfStock()).isEqualTo(responseDto.getIsForcedOutOfStock());
        assertThat(result.getContent().get(0).getIsbn()).isEqualTo(responseDto.getIsbn());
        assertThat(result.getContent().get(0).getPublisher()).isEqualTo(responseDto.getPublisher());
        assertThat(result.getContent().get(0).getAuthors()).hasSize(responseDto.getAuthors().size());
        assertThat(result.getContent().get(0).getAuthors().get(0)).isEqualTo(responseDto.getAuthors().get(0));
        assertThat(result.getContent().get(0).getThumbnailFile()).isEqualTo(responseDto.getThumbnailFile());
        assertThat(result.getContent().get(0).getTags()).hasSize(responseDto.getTags().size());
        assertThat(result.getContent().get(0).getTags().get(0)).isEqualTo(responseDto.getTags().get(0));
    }

    @Test
    @DisplayName("상품 내용으로 검색 테스트")
    void testSearchProductsByProductContent() {
        //given
        Mockito.when(searchProductRepository.searchProductsByProductContent("content", pageable))
                .thenReturn(new PageImpl<>(List.of(responseDto), pageable, 1L));

        //when
        Page<SearchedProductResponseDto> result = searchProductService.searchProductsByProductContent(
                "content",
                pageable
        );

        //then
        assertThat(result).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(responseDto.getId());
        assertThat(result.getContent().get(0).getTitle()).isEqualTo(responseDto.getTitle());
        assertThat(result.getContent().get(0).getQuantity()).isEqualTo(responseDto.getQuantity());
        assertThat(result.getContent().get(0).getSellingPrice()).isEqualTo(responseDto.getSellingPrice());
        assertThat(result.getContent().get(0).getRate()).isEqualTo(responseDto.getRate());
        assertThat(result.getContent().get(0).getIsForcedOutOfStock()).isEqualTo(responseDto.getIsForcedOutOfStock());
        assertThat(result.getContent().get(0).getIsbn()).isEqualTo(responseDto.getIsbn());
        assertThat(result.getContent().get(0).getPublisher()).isEqualTo(responseDto.getPublisher());
        assertThat(result.getContent().get(0).getAuthors()).hasSize(responseDto.getAuthors().size());
        assertThat(result.getContent().get(0).getAuthors().get(0)).isEqualTo(responseDto.getAuthors().get(0));
        assertThat(result.getContent().get(0).getThumbnailFile()).isEqualTo(responseDto.getThumbnailFile());
        assertThat(result.getContent().get(0).getTags()).hasSize(responseDto.getTags().size());
        assertThat(result.getContent().get(0).getTags().get(0)).isEqualTo(responseDto.getTags().get(0));
    }

    @Test
    @DisplayName("ISBN으로 검색")
    void testSearchProductsByProductISBN() {
        //given
        Mockito.when(searchProductRepository.searchProductsByProductISBN("isbn", pageable))
                .thenReturn(new PageImpl<>(List.of(responseDto), pageable, 1L));

        //when
        Page<SearchedProductResponseDto> result = searchProductService.searchProductsByProductISBN(
                "isbn",
                pageable
        );

        //then
        assertThat(result).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(responseDto.getId());
        assertThat(result.getContent().get(0).getTitle()).isEqualTo(responseDto.getTitle());
        assertThat(result.getContent().get(0).getQuantity()).isEqualTo(responseDto.getQuantity());
        assertThat(result.getContent().get(0).getSellingPrice()).isEqualTo(responseDto.getSellingPrice());
        assertThat(result.getContent().get(0).getRate()).isEqualTo(responseDto.getRate());
        assertThat(result.getContent().get(0).getIsForcedOutOfStock()).isEqualTo(responseDto.getIsForcedOutOfStock());
        assertThat(result.getContent().get(0).getIsbn()).isEqualTo(responseDto.getIsbn());
        assertThat(result.getContent().get(0).getPublisher()).isEqualTo(responseDto.getPublisher());
        assertThat(result.getContent().get(0).getAuthors()).hasSize(responseDto.getAuthors().size());
        assertThat(result.getContent().get(0).getAuthors().get(0)).isEqualTo(responseDto.getAuthors().get(0));
        assertThat(result.getContent().get(0).getThumbnailFile()).isEqualTo(responseDto.getThumbnailFile());
        assertThat(result.getContent().get(0).getTags()).hasSize(responseDto.getTags().size());
        assertThat(result.getContent().get(0).getTags().get(0)).isEqualTo(responseDto.getTags().get(0));
    }

    @Test
    @DisplayName("작가 이름으로 검색")
    void testSearchProductsByProductAuthor() {
        //given
        Mockito.when(searchProductRepository.searchProductsByProductAuthor("name", pageable))
                .thenReturn(new PageImpl<>(List.of(responseDto), pageable, 1L));

        //when
        Page<SearchedProductResponseDto> result = searchProductService.searchProductsByProductAuthor(
                "name",
                pageable
        );

        //then
        assertThat(result).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(responseDto.getId());
        assertThat(result.getContent().get(0).getTitle()).isEqualTo(responseDto.getTitle());
        assertThat(result.getContent().get(0).getQuantity()).isEqualTo(responseDto.getQuantity());
        assertThat(result.getContent().get(0).getSellingPrice()).isEqualTo(responseDto.getSellingPrice());
        assertThat(result.getContent().get(0).getRate()).isEqualTo(responseDto.getRate());
        assertThat(result.getContent().get(0).getIsForcedOutOfStock()).isEqualTo(responseDto.getIsForcedOutOfStock());
        assertThat(result.getContent().get(0).getIsbn()).isEqualTo(responseDto.getIsbn());
        assertThat(result.getContent().get(0).getPublisher()).isEqualTo(responseDto.getPublisher());
        assertThat(result.getContent().get(0).getAuthors()).hasSize(responseDto.getAuthors().size());
        assertThat(result.getContent().get(0).getAuthors().get(0)).isEqualTo(responseDto.getAuthors().get(0));
        assertThat(result.getContent().get(0).getThumbnailFile()).isEqualTo(responseDto.getThumbnailFile());
        assertThat(result.getContent().get(0).getTags()).hasSize(responseDto.getTags().size());
        assertThat(result.getContent().get(0).getTags().get(0)).isEqualTo(responseDto.getTags().get(0));
    }

    @Test
    @DisplayName("출판사로 검색")
    void testSearchProductsByPublisher() {
        //given
        Mockito.when(searchProductRepository.searchProductsByPublisher("publisher", pageable))
                .thenReturn(new PageImpl<>(List.of(responseDto), pageable, 1L));

        //when
        Page<SearchedProductResponseDto> result = searchProductService.searchProductsByPublisher(
                "publisher",
                pageable
        );

        //then
        assertThat(result).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(responseDto.getId());
        assertThat(result.getContent().get(0).getTitle()).isEqualTo(responseDto.getTitle());
        assertThat(result.getContent().get(0).getQuantity()).isEqualTo(responseDto.getQuantity());
        assertThat(result.getContent().get(0).getSellingPrice()).isEqualTo(responseDto.getSellingPrice());
        assertThat(result.getContent().get(0).getRate()).isEqualTo(responseDto.getRate());
        assertThat(result.getContent().get(0).getIsForcedOutOfStock()).isEqualTo(responseDto.getIsForcedOutOfStock());
        assertThat(result.getContent().get(0).getIsbn()).isEqualTo(responseDto.getIsbn());
        assertThat(result.getContent().get(0).getPublisher()).isEqualTo(responseDto.getPublisher());
        assertThat(result.getContent().get(0).getAuthors()).hasSize(responseDto.getAuthors().size());
        assertThat(result.getContent().get(0).getAuthors().get(0)).isEqualTo(responseDto.getAuthors().get(0));
        assertThat(result.getContent().get(0).getThumbnailFile()).isEqualTo(responseDto.getThumbnailFile());
        assertThat(result.getContent().get(0).getTags()).hasSize(responseDto.getTags().size());
        assertThat(result.getContent().get(0).getTags().get(0)).isEqualTo(responseDto.getTags().get(0));
    }

    @Test
    @DisplayName("태그로 검색")
    void testSearchProductsByTag() {
        //given
        Mockito.when(searchProductRepository.searchProductsByTag("tag", pageable))
                .thenReturn(new PageImpl<>(List.of(responseDto), pageable, 1L));

        //when
        Page<SearchedProductResponseDto> result = searchProductService.searchProductsByTag(
                "tag",
                pageable
        );

        //then
        assertThat(result).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(responseDto.getId());
        assertThat(result.getContent().get(0).getTitle()).isEqualTo(responseDto.getTitle());
        assertThat(result.getContent().get(0).getQuantity()).isEqualTo(responseDto.getQuantity());
        assertThat(result.getContent().get(0).getSellingPrice()).isEqualTo(responseDto.getSellingPrice());
        assertThat(result.getContent().get(0).getRate()).isEqualTo(responseDto.getRate());
        assertThat(result.getContent().get(0).getIsForcedOutOfStock()).isEqualTo(responseDto.getIsForcedOutOfStock());
        assertThat(result.getContent().get(0).getIsbn()).isEqualTo(responseDto.getIsbn());
        assertThat(result.getContent().get(0).getPublisher()).isEqualTo(responseDto.getPublisher());
        assertThat(result.getContent().get(0).getAuthors()).hasSize(responseDto.getAuthors().size());
        assertThat(result.getContent().get(0).getAuthors().get(0)).isEqualTo(responseDto.getAuthors().get(0));
        assertThat(result.getContent().get(0).getThumbnailFile()).isEqualTo(responseDto.getThumbnailFile());
        assertThat(result.getContent().get(0).getTags()).hasSize(responseDto.getTags().size());
        assertThat(result.getContent().get(0).getTags().get(0)).isEqualTo(responseDto.getTags().get(0));
    }
}