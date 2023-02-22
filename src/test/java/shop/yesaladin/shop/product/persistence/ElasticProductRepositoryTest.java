package shop.yesaladin.shop.product.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import shop.yesaladin.shop.product.domain.model.SearchedProduct;
import shop.yesaladin.shop.product.domain.model.SearchedProductAuthor;
import shop.yesaladin.shop.product.domain.model.SearchedProductPublisher;
import shop.yesaladin.shop.product.domain.model.SearchedProductTag;
import shop.yesaladin.shop.product.domain.model.SearchedProductTotalDiscountRate;
import shop.yesaladin.shop.product.domain.repository.SearchProductRepository;
import shop.yesaladin.shop.product.dto.SearchedProductResponseDto;

@SpringBootTest
@ActiveProfiles("local-test")
class ElasticProductRepositoryTest {

    @Autowired
    private SearchProductRepository searchProductRepository;
    @Autowired
    private ElasticCommandProductRepository elasticCommandProductRepository;
    private Pageable pageable = PageRequest.of(0, 1);

    @BeforeEach
    void setUp() {
        SearchedProduct searchedProduct = SearchedProduct.builder()
                .id(-1L)
                .title("title")
                .contents("내용")
                .isbn("isbn")
                .publisher(new SearchedProductPublisher(1L, "name"))
                .tags(List.of(new SearchedProductTag(1L, "tag")))
                .isSale(true)
                .isDeleted(false)
                .authors(List.of(new SearchedProductAuthor(1L, "author")))
                .actualPrice(10000L)
                .discountRate(10)
                .isSeparatelyDiscount(false)
                .searchedTotalDiscountRate(new SearchedProductTotalDiscountRate(1, 10))
                .build();
        elasticCommandProductRepository.save(searchedProduct);
    }

    @Test
    @DisplayName("카테고라 id로 검색 테스트")
    void testSearchProductsByCategoryId() {
        Page<SearchedProductResponseDto> result = searchProductRepository.searchProductsByCategoryId(
                -1L,
                pageable
        );
        assertThat(result.getContent()).isEmpty();
    }

    @Test
    @DisplayName("상품 내용으로 검색 테스트")
    void testSearchProductsByProductContent() {
        Page<SearchedProductResponseDto> result = searchProductRepository.searchProductsByProductContent(
                "내용",
                pageable
        );
        assertThat(result.getContent()).hasSize(1);
        //assertThat(result.getTotalElements()).isZero();
    }

    @Test
    @DisplayName("상품 isbn으로 검색 테스트")
    void testSearchProductsByISBN() {
        Page<SearchedProductResponseDto> result = searchProductRepository.searchProductsByProductISBN(
                "isbn",
                pageable
        );
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getIsbn()).isEqualTo("isbn");
    }

    @Test
    @DisplayName("작가 이름으로 검색 테스트")
    void testSearchProductsByProductAuthor() {
        Page<SearchedProductResponseDto> result = searchProductRepository.searchProductsByProductAuthor(
                "ㅓㅗ여!!sdf",
                pageable
        );
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
    }

    @Test
    @DisplayName("상품 이름으로 검색 테스트")
    void testSearchProductsByProductTitle() {
        Page<SearchedProductResponseDto> result = searchProductRepository.searchProductsByProductTitle(
                "!!!!@!@sdsdsdㅁㄴㅁㄴ",
                pageable
        );
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
    }

    @Test
    @DisplayName("출판사로 검색 테스트")
    void testSearchProductsByPublisher() {
        Page<SearchedProductResponseDto> result = searchProductRepository.searchProductsByPublisher(
                "아ㅓㅇ라ㅓ",
                pageable
        );
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
    }

    @Test
    @DisplayName("태그로 검색 테스트")
    void testSearchProductsByTag() {
        Page<SearchedProductResponseDto> result = searchProductRepository.searchProductsByTag(
                "12ghghsdg!!담ㅇ노ㅓ",
                pageable
        );
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
    }

    @AfterEach
    void setDown() {
        elasticCommandProductRepository.deleteByIdEquals(-1L);
    }
}