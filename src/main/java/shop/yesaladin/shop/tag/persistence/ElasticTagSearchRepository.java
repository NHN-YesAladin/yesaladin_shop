package shop.yesaladin.shop.tag.persistence;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.tag.domain.model.SearchedTag;
import shop.yesaladin.shop.tag.domain.repository.SearchTagRepository;
import shop.yesaladin.shop.tag.dto.TagsResponseDto;

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
    private final String NAME = "name";

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TagsResponseDto> searchTagByName(String name) {
        NativeQuery query = NativeQuery.builder()
                .withFilter(NativeQuery.builder()
                        .withQuery(q -> q.term(v -> v.field(NAME).value(name)))
                        .getQuery())
                .build();
        return elasticsearchOperations.search(query, SearchedTag.class)
                .getSearchHits()
                .stream()
                .map(tag -> tag.getContent().toDto())
                .collect(Collectors.toList());
    }
}
