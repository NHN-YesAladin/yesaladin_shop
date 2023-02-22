package shop.yesaladin.shop.writing.persistence;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.writing.domain.model.SearchedAuthor;
import shop.yesaladin.shop.writing.domain.repository.SearchAuthorRepository;
import shop.yesaladin.shop.writing.dto.SearchedAuthorResponseDto;
import shop.yesaladin.shop.writing.dto.SearchedAuthorResponseDto.SearchedAuthorDto;

@RequiredArgsConstructor
@Repository
public class ElasticAuthorRepository implements SearchAuthorRepository {

    private static final String NAME = "name";
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public SearchedAuthorResponseDto searchAuthorByName(String name, int offset, int size) {
        NativeQuery query = NativeQuery.builder()
                .withFilter(QueryBuilders.termQueryAsQuery(NAME, name))
                .withPageable(PageRequest.of(offset, size))
                .build();
        SearchHits<SearchedAuthor> list = elasticsearchOperations.search(
                query,
                SearchedAuthor.class
        );
        return SearchedAuthorResponseDto.builder()
                .count(list.getTotalHits())
                .searchedAuthorDtoList(list.stream()
                        .map(
                                hit -> SearchedAuthorDto.fromIndex(hit.getContent()))
                        .collect(Collectors.toList()))
                .build();
    }
}
