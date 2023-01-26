package shop.yesaladin.shop.product.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import shop.yesaladin.shop.member.domain.model.SearchedMember;
import shop.yesaladin.shop.member.domain.repository.SearchMemberRepository;
import shop.yesaladin.shop.product.domain.model.SearchedProduct;
import shop.yesaladin.shop.product.domain.repository.SearchProductRepository;
import shop.yesaladin.shop.product.dto.SearchedProductResponseDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ElasticProductRepositoryTest {

    @Autowired
    SearchProductRepository searchProductRepository;

    @Autowired
    SearchMemberRepository searchMemberRepository;

    SearchedProduct searchedProduct;
    @Autowired
    ElasticsearchOperations elasticsearchOperations;

    @Test
    @DisplayName("카테고라 id로 검색 테스트")
    void testSearchProductsByCategoryId() {
        List<SearchedProductResponseDto> result = searchProductRepository.searchProductsByCategoryId(-1L);
        assertThat(result).isEmpty();
        assertThat(result).isInstanceOf(List.class);
    }

    @Test
    @DisplayName("카테고라 이름으로 검색 테스트")
    void testSearchProductsByCategoryName() {

        List<SearchedProductResponseDto> result = searchProductRepository.searchProductsByCategoryName("소설");
        assertThat(result).isEmpty();
        assertThat(result).isInstanceOf(List.class);
    }

    @Test
    @DisplayName("상품 이름으로 검색 테스트")
    void testSearchProductsByProductTitle() {
        Query query = NativeQuery.builder()
                .withQuery(q -> q.multiMatch(v -> v.query("깃").fields("title^2")))
                .withFilter(NativeQuery.builder().withQuery(q -> q.term(t -> t.field("categories.disable").value(true))).getQuery())
//                .withFilter(NativeQuery.builder().withQuery(q -> q.term(t -> t.field("CATEGORIES_IS_SHOWN").value(true))).getQuery())
                .withPageable(PageRequest.of(0, 1))
                .build();

        List<SearchedProductResponseDto> list = searchProductRepository.searchProductsByProductTitle("깃", 0, 1);

        System.out.println(list.size());
    }

    @Test
    void test() {
        List<SearchedProductResponseDto> list = searchProductRepository.searchProductsByCategoryId(1L);
    }
}