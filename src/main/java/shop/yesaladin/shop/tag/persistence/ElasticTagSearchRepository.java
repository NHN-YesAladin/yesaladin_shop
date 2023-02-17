package shop.yesaladin.shop.tag.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.tag.domain.model.SearchedTag;
import shop.yesaladin.shop.tag.domain.repository.SearchTagRepository;
import shop.yesaladin.shop.tag.dto.SearchedTagResponseDto;
import shop.yesaladin.shop.tag.dto.SearchedTagResponseDto.SearchedTagDto;

import java.util.stream.Collectors;

/**
 * 엘라스틱 서치 태그 검색 구현체
 *
 * @author : 김선홍
 * @since : 1.0
 */
@Repository
@RequiredArgsConstructor
public class ElasticTagSearchRepository implements SearchTagRepository {

    private final ElasticsearchOperations elasticsearchOperations;
    private static final String NAME = "name";

    /**
     * {@inheritDoc}
     */
    @Override
    public SearchedTagResponseDto searchTagByName(String name, int offset, int size) {
        NativeQuery query = NativeQuery.builder()
                .withFilter(NativeQuery.builder()
                        .withQuery(q -> q.term(v -> v.field(NAME).value(name)))
                        .getQuery())
                .withPageable(PageRequest.of(offset, size))
                .build();
        SearchHits<SearchedTag> list = elasticsearchOperations.search(query, SearchedTag.class);
        return SearchedTagResponseDto.builder()
                .count(list.getTotalHits())
                .searchedTagDtoList(list.getSearchHits()
                        .stream()
                        .map(hit -> SearchedTagDto.fromIndex(hit.getContent()))
                        .collect(Collectors.toList()))
                .build();
    }
}
