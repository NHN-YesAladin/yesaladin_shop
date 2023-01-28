package shop.yesaladin.shop.product.persistence;


import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.product.domain.model.SearchedProduct;
import shop.yesaladin.shop.product.domain.repository.SearchProductRepository;
import shop.yesaladin.shop.product.dto.SearchedProductDto;
import shop.yesaladin.shop.product.dto.SearchedProductManagerDto;
import shop.yesaladin.shop.product.dto.SearchedProductManagerResponseDto;
import shop.yesaladin.shop.product.dto.SearchedProductResponseDto;

/**
 * 상품 검색 레포지토리
 *
 * @author : 김선홍
 * @since : 1.0
 */
@RequiredArgsConstructor
@Repository
public class ElasticProductRepository implements SearchProductRepository {

    private final ElasticsearchOperations elasticsearchOperations;
    private static final String CATEGORIES_ID = "categories.id";
    private static final String CATEGORIES_NAME = "categories.name";
    private static final String TITLE = "title^2";
    private static final String CONTENT = "content^3";
    private static final String DESCRIPTION = "description^2";
    private static final String ISBN = "ISBN";
    private static final String AUTHORS_NAME = "authors.name";
    private static final String PUBLISHER_NAME = "publisher.name";
    private static final String TAG = "tags.name";
    private static final String CATEGORIES_DISABLE = "categories.disable";
    private static final String CATEGORIES_IS_SHOWN = "categories.is_shown";

    /**
     * 카테고리 id를 이용한 검색하는 메소드
     *
     * @param id     검색할 카테고리 id
     * @param offset 검색하고 싶은 페이지 위치
     * @param size   검색하고 싶은 상품 갯수
     * @return 상품 리스트와 총 갯수
     * @
     */
    @Override
    public SearchedProductManagerResponseDto searchProductsByCategoryId(
            Long id,
            int offset,
            int size
    ) {
        return searchProductByCategory(CATEGORIES_ID, String.valueOf(id), offset, size);
    }

    /**
     * 카테고리 이름으로 상품을 검색하는 메서드
     *
     * @param name   검색하고 싶은 카테고리 이름
     * @param offset 검색하고 싶은 페이지 위치
     * @param size   검색하고 싶은 상품 갯수
     * @return 상품 리스트와 총 갯수
     * @author : 김선홍
     * @since : 1.0
     */
    @Override
    public SearchedProductManagerResponseDto searchProductsByCategoryName(
            String name,
            int offset,
            int size
    ) {
        return searchProductByCategory(CATEGORIES_NAME, name, offset, size);
    }

    /**
     * 상품 이름으로 상품을 검색하는 메서드
     *
     * @param title  검색하고 싶은 상품 이름
     * @param offset 검색하고 싶은 페이지 위치
     * @param size   검색하고 싶은 상품 갯수
     * @return 상품 리스트와 총 갯수
     * @author : 김선홍
     * @since : 1.0
     */
    @Override
    public SearchedProductResponseDto searchProductsByProductTitle(
            String title, int offset, int size
    ) {
        return searchProductByMultiQuery(title, offset, size, List.of(TITLE, TAG));
    }

    /**
     * 상품 내용으로 상품을 검색하는 메서드
     *
     * @param content 검색하고 싶은 상품 내용
     * @param offset  검색하고 싶은 페이지 위치
     * @param size    검색하고 싶은 상품 갯수
     * @return 상품 리스트와 총 갯수
     * @author : 김선홍
     * @since : 1.0
     */
    @Override
    public SearchedProductResponseDto searchProductsByProductContent(
            String content, int offset, int size
    ) {
        return searchProductByMultiQuery(content, offset, size, List.of(CONTENT, TAG, DESCRIPTION));
    }

    /**
     * 상품 isbn으로 상품을 검색하는 메서드
     *
     * @param isbn   검색하고 싶은 카테고리 이름
     * @param offset 검색하고 싶은 페이지 위치
     * @param size   검색하고 싶은 상품 갯수
     * @return 상품 리스트와 총 갯수
     * @author : 김선홍
     * @since : 1.0
     */
    @Override
    public SearchedProductResponseDto searchProductsByProductISBN(
            String isbn, int offset, int size
    ) {
        return searchProductByTermQuery(isbn, offset, size, ISBN);
    }

    /**
     * 작가 이름으로 상품을 검색하는 메서드
     *
     * @param author 검색하고 싶은 카테고리 이름
     * @param offset 검색하고 싶은 페이지 위치
     * @param size   검색하고 싶은 상품 갯수
     * @return 상품 리스트와 총 갯수
     * @author : 김선홍
     * @since : 1.0
     */
    @Override
    public SearchedProductResponseDto searchProductsByProductAuthor(
            String author,
            int offset,
            int size
    ) {
        return searchProductByTermQuery(author, offset, size, AUTHORS_NAME);
    }

    /**
     * 출판사 이름으로 상품을 검색하는 메서드
     *
     * @param publisher 검색하고 싶은 출판사 이름
     * @param offset    검색하고 싶은 페이지 위치
     * @param size      검색하고 싶은 상품 갯수
     * @return 상품 리스트와 총 갯수
     * @author : 김선홍
     * @since : 1.0
     */
    @Override
    public SearchedProductResponseDto searchProductsByPublisher(
            String publisher, int offset, int size
    ) {
        return searchProductByTermQuery(publisher, offset, size, PUBLISHER_NAME);
    }

    /**
     * 태그 이름으로 상품을 검색하는 메서드
     *
     * @param tag    검색하고 싶은 태그 이름
     * @param offset 검색하고 싶은 페이지 위치
     * @param size   검색하고 싶은 상품 갯수
     * @return 상품 리스트와 총 갯수
     * @author : 김선홍
     * @since : 1.0
     */
    @Override
    public SearchedProductResponseDto searchProductsByTag(String tag, int offset, int size) {
        return searchProductByTermQuery(tag, offset, size, TAG);
    }

    /**
     * 멀티 필드와 형태소분석을 통해상품을 검색하는 메서드
     *
     * @param value  멀티 필드에 검색하고 싶은 값
     * @param offset 검색하고 싶은 페이지 위치
     * @param size   검색하고 싶은 상품 갯수
     * @param fields 검색할 필드들
     * @return 상품 리스트와 총 갯수
     * @author : 김선홍
     * @since : 1.0
     */
    public SearchedProductResponseDto searchProductByMultiQuery(
            String value,
            int offset,
            int size,
            List<String> fields
    ) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.multiMatch(v -> v.query(value).fields(fields)))
                .withFilter(QueryBuilders.bool(v -> v.must(
                        getCategoryDisableFilter(),
                        getCategoryIsShownFilter()
                )))
                .withPageable(PageRequest.of(offset, size))
                .build();

        SearchHits<SearchedProduct> result = elasticsearchOperations.search(
                query,
                SearchedProduct.class
        );

        return SearchedProductResponseDto.builder()
                .products(result.stream()
                        .map(searchedProductSearchHit -> SearchedProductDto.fromIndex(
                                searchedProductSearchHit.getContent()))
                        .collect(Collectors.toList()))
                .count(result.getTotalHits())
                .build();
    }

    /**
     * 필터에서 TermQuery를 통해 상품을 검색하는 메서드
     *
     * @param value  필드에 검색하고 싶은 값
     * @param offset 검색하고 싶은 페이지 위치
     * @param size   검색하고 싶은 상품 갯수
     * @param field  검색할 필드
     * @return 상품 리스트와 총 갯수
     * @author : 김선홍
     * @since : 1.0
     */
    public SearchedProductResponseDto searchProductByTermQuery(
            String value,
            int offset,
            int size,
            String field
    ) {
        NativeQuery query = NativeQuery.builder()
                .withFilter(QueryBuilders.bool(v -> v.must(
                        getTermQuery(field, value),
                        getCategoryDisableFilter(),
                        getCategoryIsShownFilter()
                )))
                .withPageable(PageRequest.of(offset, size))
                .build();

        SearchHits<SearchedProduct> result = elasticsearchOperations.search(
                query,
                SearchedProduct.class
        );

        return SearchedProductResponseDto.builder()
                .products(result.stream()
                        .map(searchedProductSearchHit -> SearchedProductDto.fromIndex(
                                searchedProductSearchHit.getContent()))
                        .collect(Collectors.toList()))
                .count(result.getTotalHits())
                .build();
    }

    /**
     * 카테고리를 기준으로 검색하는 메서드
     *
     * @param value  필드에 검색하고 싶은 값
     * @param offset 검색하고 싶은 페이지 위치
     * @param size   검색하고 싶은 상품 갯수
     * @param field  검색할 필드
     * @return 상품 리스트와 총 갯수
     * @author : 김선홍
     * @since : 1.0
     */
    public SearchedProductManagerResponseDto searchProductByCategory(
            String field,
            String value,
            int offset,
            int size
    ) {
        NativeQuery query = NativeQuery.builder()
                .withFilter(getTermQuery(field, value))
                .withPageable(PageRequest.of(offset, size))
                .build();
        SearchHits<SearchedProduct> result = elasticsearchOperations.search(
                query,
                SearchedProduct.class
        );

        return SearchedProductManagerResponseDto.builder()
                .products(result.stream()
                        .map(searchedProductSearchHit -> SearchedProductManagerDto.fromIndex(
                                searchedProductSearchHit.getContent()))
                        .collect(Collectors.toList()))
                .count(result.getTotalHits())
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

    /**
     * 카테고리의 isShown 상태를 확인하는 쿼리를 반환하는 메서드
     *
     * @return 카테고리의 isShown 상태를 확인하는 쿼리
     * @author : 김선홍
     * @since : 1.0
     */
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
