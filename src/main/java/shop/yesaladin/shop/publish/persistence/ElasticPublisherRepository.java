package shop.yesaladin.shop.publish.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.publish.domain.model.SearchedPublisher;
import shop.yesaladin.shop.publish.domain.repository.SearchPublisherRepository;
import shop.yesaladin.shop.publish.dto.SearchPublisherResponseDto;
import shop.yesaladin.shop.publish.dto.SearchPublisherResponseDto.SearchedPublisherDto;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class ElasticPublisherRepository implements SearchPublisherRepository {

    private final ElasticsearchOperations elasticsearchOperations;
    private static final String NAME = "name";

    @Override
    public SearchPublisherResponseDto searchPublisherByName(
            String name,
            int offset,
            int size
    ) {
        NativeQuery query = NativeQuery.builder()
                .withFilter(NativeQuery.builder()
                        .withQuery(q -> q.term(v -> v.field(NAME).value(name)))
                        .getQuery())
                .withPageable(PageRequest.of(offset, size))
                .build();
        SearchHits<SearchedPublisher> searchHits = elasticsearchOperations.search(
                query,
                SearchedPublisher.class
        );
        return SearchPublisherResponseDto.builder()
                .count(searchHits.stream().count())
                .searchedPublisherDtoList(searchHits.stream()
                        .map(hits -> SearchedPublisherDto.fromIndex(hits.getContent()))
                        .collect(Collectors.toList()))
                .build();
    }
}
