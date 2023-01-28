package shop.yesaladin.shop.product.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shop.yesaladin.shop.product.domain.repository.SearchProductRepository;
import shop.yesaladin.shop.product.dto.SearchedProductResponseDto;

@SpringBootTest
class ElasticProductRepositoryTest {

    @Autowired
    SearchProductRepository searchProductRepository;

    @Test
    @DisplayName("카테고라 id로 검색 테스트")
    void testSearchProductsByCategoryId() {
        List<SearchedProductResponseDto> result = searchProductRepository.searchProductsByCategoryId(-1L, 0, 1);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("카테고라 이름으로 검색 테스트")
    void testSearchProductsByCategoryName() {
        List<SearchedProductResponseDto> result = searchProductRepository.searchProductsByCategoryName("카테고리이름", 0, 1);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("상품 내용으로 검색 테스트")
    void testSearchProductsByProductContent() {
        List<SearchedProductResponseDto> result = searchProductRepository.searchProductsByProductContent("content", 0, 1);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("상품 isbn으로 검색 테스트")
    void testSearchProductsByISBN() {
        List<SearchedProductResponseDto> result = searchProductRepository.searchProductsByProductISBN("isbn", 0, 1);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("작가 이름으로 검색 테스트")
    void testSearchProductsByProductAuthor() {
        List<SearchedProductResponseDto> result = searchProductRepository.searchProductsByProductAuthor("author", 0, 1);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("상품 이름으로 검색 테스트")
    void testSearchProductsByProductTitle() {
        List<SearchedProductResponseDto> result = searchProductRepository.searchProductsByProductTitle("title", 0, 1);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("출판사로 검색 테스트")
    void testSearchProductsByPublisher() {
        List<SearchedProductResponseDto> result = searchProductRepository.searchProductsByPublisher("publisher", 0, 1);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("태그로 검색 테스트")
    void testSearchProductsByTag() {
        List<SearchedProductResponseDto> result = searchProductRepository.searchProductsByTag("tag", 0, 1);
        assertThat(result).isEmpty();
    }
}