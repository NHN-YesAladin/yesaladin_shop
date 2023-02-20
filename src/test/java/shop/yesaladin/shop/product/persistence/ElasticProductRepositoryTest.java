package shop.yesaladin.shop.product.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import shop.yesaladin.shop.product.domain.repository.SearchProductRepository;
import shop.yesaladin.shop.product.dto.SearchedProductResponseDto;

@SpringBootTest
@ActiveProfiles("local-test")
class ElasticProductRepositoryTest {

    @Autowired
    private SearchProductRepository searchProductRepository;
    private Pageable pageable = PageRequest.of(0, 1);

    @Test
    @DisplayName("카테고라 id로 검색 테스트")
    void testSearchProductsByCategoryId() {
        Page<SearchedProductResponseDto> result = searchProductRepository.searchProductsByCategoryId(-1L, pageable);
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
    }

    @Test
    @DisplayName("상품 내용으로 검색 테스트")
    void testSearchProductsByProductContent() {
        Page<SearchedProductResponseDto> result = searchProductRepository.searchProductsByProductContent("논오ㅠㄴ어=asas", pageable);
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
    }

    @Test
    @DisplayName("상품 isbn으로 검색 테스트")
    void testSearchProductsByISBN() {
        Page<SearchedProductResponseDto> result = searchProductRepository.searchProductsByProductISBN("isbn", pageable);
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
    }

    @Test
    @DisplayName("작가 이름으로 검색 테스트")
    void testSearchProductsByProductAuthor() {
        Page<SearchedProductResponseDto> result = searchProductRepository.searchProductsByProductAuthor("ㅓㅗ여!!sdf", pageable);
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
    }

    @Test
    @DisplayName("상품 이름으로 검색 테스트")
    void testSearchProductsByProductTitle() {
        Page<SearchedProductResponseDto> result = searchProductRepository.searchProductsByProductTitle("!!!!@!@sdsdsdㅁㄴㅁㄴ", pageable);
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
    }

    @Test
    @DisplayName("출판사로 검색 테스트")
    void testSearchProductsByPublisher() {
        Page<SearchedProductResponseDto> result = searchProductRepository.searchProductsByPublisher("아ㅓㅇ라ㅓ", pageable);
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
    }

    @Test
    @DisplayName("태그로 검색 테스트")
    void testSearchProductsByTag() {
        Page<SearchedProductResponseDto> result = searchProductRepository.searchProductsByTag("12ghghsdg!!담ㅇ노ㅓ", pageable);
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
    }
}