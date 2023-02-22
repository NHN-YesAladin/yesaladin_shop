package shop.yesaladin.shop.category.persistence;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.category.domain.model.SearchedCategory;
import shop.yesaladin.shop.category.domain.repository.SearchCategoryRepository;
import shop.yesaladin.shop.category.dto.SearchCategoryResponseDto;
import shop.yesaladin.shop.category.dto.SearchCategoryResponseDto.SearchedCategoryDto;

/**
 * 엘라스틱에서 카테고리 검색 구현체
 */
@RequiredArgsConstructor
@Repository
public class ElasticCategoryRepository implements SearchCategoryRepository {

    private static final String NAME = "name";
    private static final String CATEGORIES_DISABLE = "disable";
    private static final String CATEGORIES_IS_SHOWN = "is_shown";
    private final ElasticsearchOperations elasticsearchOperations;

    /**
     * {@inheritDoc}
     */
    @Override
    public SearchCategoryResponseDto searchCategoryByName(String name, int offset, int size) {
        NativeQuery query = NativeQuery.builder()
                .withFilter(QueryBuilders.bool(v -> v.must(
                        getTermQuery(NAME, name),
                        getCategoryDisableFilter(),
                        getCategoryIsShownFilter()
                )))
                .withPageable(PageRequest.of(offset, size))
                .build();
        SearchHits<SearchedCategory> result = elasticsearchOperations.search(
                query,
                SearchedCategory.class
        );

        return SearchCategoryResponseDto.builder()
                .count(result.getTotalHits())
                .searchedCategoryDtoList(
                        result.stream()
                                .map(hit -> SearchedCategoryDto.fromIndex(hit.getContent()))
                                .collect(Collectors.toList()))
                .build();
    }

    /**
     * 카테고리의 disable 상태를 확인하는 쿼리를 반환하는 메서드
     *
     * @return 카테고리의 disable 상태를 확인하는 쿼리
     * @author : 김선홍
     * @since : 1.0
     */
    private Query getCategoryDisableFilter() {
        return NativeQuery.builder()
                .withQuery(q -> q.term(t -> t.field(CATEGORIES_DISABLE).value(false)))
                .getQuery();
    }

    private Query getCategoryIsShownFilter() {
        return NativeQuery.builder()
                .withQuery(q -> q.term(t -> t.field(CATEGORIES_IS_SHOWN).value(true)))
                .getQuery();
    }

    /**
     * Term 쿼리를 얻는 메서드
     *
     * @param field 필드
     * @param value 밸류
     * @return 쿼리
     * @author : 김선홍
     * @since : 1.0
     */
    private Query getTermQuery(String field, String value) {
        return NativeQuery.builder()
                .withQuery(q -> q.term(t -> t.field(field).value(value)))
                .getQuery();
    }
}
