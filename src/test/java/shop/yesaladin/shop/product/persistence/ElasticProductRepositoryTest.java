package shop.yesaladin.shop.product.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shop.yesaladin.shop.product.domain.repository.SearchProductRepository;
import shop.yesaladin.shop.product.dto.SearchedProductManagerResponseDto;
import shop.yesaladin.shop.product.dto.SearchedProductResponseDto;

@SpringBootTest
class ElasticProductRepositoryTest {

    @Autowired
    SearchProductRepository searchProductRepository;

    @Test
    @DisplayName("카테고라 id로 검색 테스트")
    void testSearchProductsByCategoryId() {
        SearchedProductManagerResponseDto result = searchProductRepository.searchProductsByCategoryId(-1L, 0, 1);
        assertThat(result.getProducts()).isEmpty();
        assertThat(result.getCount()).isZero();
    }

    @Test
    @DisplayName("카테고라 이름으로 검색 테스트")
    void testSearchProductsByCategoryName() {
        SearchedProductManagerResponseDto result = searchProductRepository.searchProductsByCategoryName("카테고리이름", 0, 1);
        assertThat(result.getProducts()).isEmpty();
        assertThat(result.getCount()).isZero();
    }

    @Test
    @DisplayName("상품 내용으로 검색 테스트")
    void testSearchProductsByProductContent() {
        SearchedProductResponseDto result = searchProductRepository.searchProductsByProductContent("논오ㅠㄴ어=121asas", 0, 1);
        assertThat(result.getProducts()).isEmpty();
        assertThat(result.getCount()).isZero();
    }

    @Test
    @DisplayName("상품 isbn으로 검색 테스트")
    void testSearchProductsByISBN() {
        SearchedProductResponseDto  result = searchProductRepository.searchProductsByProductISBN("isbn", 0, 1);
        assertThat(result.getProducts()).isEmpty();
        assertThat(result.getCount()).isZero();
    }

    @Test
    @DisplayName("작가 이름으로 검색 테스트")
    void testSearchProductsByProductAuthor() {
        SearchedProductResponseDto  result = searchProductRepository.searchProductsByProductAuthor("ㅓㅗ여!!1212", 0, 1);
        assertThat(result.getProducts()).isEmpty();
        assertThat(result.getCount()).isZero();
    }

    @Test
    @DisplayName("상품 이름으로 검색 테스트")
    void testSearchProductsByProductTitle() {
        SearchedProductResponseDto  result = searchProductRepository.searchProductsByProductTitle("!!!!@!@sdsdsdㅁㄴㅁㄴ", 0, 1);
        assertThat(result.getProducts()).isEmpty();
        assertThat(result.getCount()).isZero();
    }

    @Test
    @DisplayName("출판사로 검색 테스트")
    void testSearchProductsByPublisher() {
        SearchedProductResponseDto result = searchProductRepository.searchProductsByPublisher("publisher", 0, 1);
        assertThat(result.getProducts()).isEmpty();
        assertThat(result.getCount()).isZero();
    }

    @Test
    @DisplayName("태그로 검색 테스트")
    void testSearchProductsByTag() {
        SearchedProductResponseDto result = searchProductRepository.searchProductsByTag("12ghghsdg!!담ㅇ노ㅓ", 0, 1);
        assertThat(result.getProducts()).isEmpty();
        assertThat(result.getCount()).isZero();
    }
}