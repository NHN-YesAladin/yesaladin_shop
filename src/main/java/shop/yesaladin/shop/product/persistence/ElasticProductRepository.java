package shop.yesaladin.shop.product.persistence;


import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.product.domain.model.SearchedProduct;
import shop.yesaladin.shop.product.domain.repository.SearchProductRepository;
import shop.yesaladin.shop.product.dto.SearchedProductResponseDto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 상품 검색 레포지토리
 *
 * @author : 김선홍
 * @since : 1.0
 */
@RequiredArgsConstructor
@Repository
public class ElasticProductRepository implements SearchProductRepository {

    private static final String CATEGORIES_ID = "categories.id";
    private static final String CATEGORIES_NAME = "categories.name";
    private static final String TITLE = "title^2";
    private static final String CONTENT = "contents^3";
    private static final String DESCRIPTION = "description^2";
    private static final String ISBN = "ISBN";
    private static final String AUTHORS_NAME = "authors.name";
    private static final String PUBLISHER_NAME = "publisher.name";
    private static final String TAG = "tags.name";
    private static final String IS_SALE = "is_sale";
    private static final String IS_DELETE = "is_deleted";
    private static final float PERCENT_DENOMINATOR_VALUE = 100;
    private static final long ROUND_OFF_VALUE = 10;
    private final ElasticsearchOperations elasticsearchOperations;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<SearchedProductResponseDto> searchProductsByCategoryId(
            Long id,
            Pageable pageable
    ) {
        return searchResponseProductByTermQuery(String.valueOf(id), pageable, CATEGORIES_ID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<SearchedProductResponseDto> searchProductsByCategoryName(
            String name,
            Pageable pageable
    ) {
        return searchResponseProductByTermQuery(CATEGORIES_NAME, pageable, name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<SearchedProductResponseDto> searchProductsByProductTitle(
            String title, Pageable pageable
    ) {
        return searchResponseProductByMultiQuery(title, pageable, List.of(TITLE, TAG));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<SearchedProductResponseDto> searchProductsByProductContent(
            String content, Pageable pageable
    ) {
        return searchResponseProductByMultiQuery(
                content,
                pageable,
                List.of(CONTENT, TAG, DESCRIPTION)
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<SearchedProductResponseDto> searchProductsByProductISBN(
            String isbn, Pageable pageable
    ) {
        return searchResponseProductByTermQuery(isbn, pageable, ISBN);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<SearchedProductResponseDto> searchProductsByProductAuthor(
            String author,
            Pageable pageable
    ) {
        return searchResponseProductByTermQuery(author, pageable, AUTHORS_NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<SearchedProductResponseDto> searchProductsByPublisher(
            String publisher, Pageable pageable
    ) {
        return searchResponseProductByTermQuery(publisher, pageable, PUBLISHER_NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<SearchedProductResponseDto> searchProductsByTag(String tag, Pageable pageable) {
        return searchResponseProductByTermQuery(tag, pageable, TAG);
    }

    /**
     * {@inheritDoc}
     */
    public Page<SearchedProductResponseDto> searchResponseProductByMultiQuery(
            String value,
            Pageable pageable,
            List<String> fields
    ) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.multiMatch(v -> v.query(value).fields(fields)))
                .withFilter(QueryBuilders.bool(v -> v.must(
                        getTermQueryByBoolean(IS_SALE, true),
                        getTermQueryByBoolean(IS_DELETE, false)
                )))
                .withPageable(pageable)
                .build();

        SearchHits<SearchedProduct> result = elasticsearchOperations.search(
                query,
                SearchedProduct.class
        );

        List<SearchedProductResponseDto> list = result.stream()
                .map(product -> SearchedProductResponseDto.fromIndex(
                                product.getContent(),
                                calcSellingPrice(
                                        product.getContent().getActualPrice(),
                                        getRateByProduct(product.getContent())),
                                getRateByProduct(product.getContent()),
                                isEbook(product.getContent())
                        )
                ).collect(Collectors.toList());

        return new PageImpl<>(list, pageable, result.getTotalHits());
    }

    /**
     * {@inheritDoc}
     */
    public Page<SearchedProductResponseDto> searchResponseProductByTermQuery(
            String value,
            Pageable pageable,
            String field
    ) {
        NativeQuery query = getDefaultSearchProductTermQuery(field, value, pageable);

        SearchHits<SearchedProduct> result = elasticsearchOperations.search(
                query,
                SearchedProduct.class
        );

        List<SearchedProductResponseDto> list = result.stream()
                .map(product -> SearchedProductResponseDto.fromIndex(
                                product.getContent(),
                                calcSellingPrice(
                                        product.getContent().getActualPrice(),
                                        getRateByProduct(product.getContent())),
                                getRateByProduct(product.getContent()),
                                isEbook(product.getContent())
                        )
                ).collect(Collectors.toList());

        return new PageImpl<>(list, pageable, result.getTotalHits());
    }

    /**
     * Term 쿼리를 이용한 상품 검색 아래와 같은 기본 조건을 가지고 있다. categories.is_shown: true categories.disable: false
     * products.is_deleted: false
     *
     * @param field    필드 이름
     * @param value    밸류 이름
     * @param pageable 패이지정보
     * @return 해당 쿼리
     */
    private NativeQuery getDefaultSearchProductTermQuery(
            String field,
            String value,
            Pageable pageable
    ) {
        return NativeQuery.builder()
                .withFilter(QueryBuilders.bool(v -> v.must(
                        getMatchQuery(field, value),
                        getTermQueryByBoolean(IS_SALE, true),
                        getTermQueryByBoolean(IS_DELETE, false)
                )))
                .withPageable(pageable)
                .build();
    }

    /**
     * 밸류가 boolean 인 Term 쿼리를 반환하는 메서드
     *
     * @param field 필드 이름
     * @param value boolean 밸류 값
     * @return Term 쿼리
     */
    private Query getTermQueryByBoolean(String field, boolean value) {
        return NativeQuery.builder()
                .withQuery(q -> q.term(t -> t.field(field).value(value)))
                .getQuery();
    }

    private Query getMatchQuery(String field, String value) {
        return NativeQuery.builder()
                .withQuery(q -> q.match(v -> v.query(value).field(field)))
                .getQuery();
    }

    /**
     * 밸류가 String 인 Term 쿼리를 반환하는 메서드
     *
     * @param field 필드
     * @param value 문자열 밸류
     * @return 쿼리
     * @author : 김선홍
     * @since : 1.0
     */
    private Query getTermQueryByString(String field, String value) {
        return NativeQuery.builder()
                .withQuery(q -> q.term(t -> t.field(field).value(value)))
                .getQuery();
    }

    /**
     * 상품의 정가, 할인율을 바탕으로 판매가를 계산해 반환합니다.
     *
     * @param actualPrice 상품의 정가
     * @param rate        상품의 할인율(전체 / 개별)
     * @return 계산된 상품의 판매가
     * @author 이수정
     * @since 1.0
     */
    private long calcSellingPrice(long actualPrice, int rate) {
        if (rate > 0) {
            return Math.round((actualPrice - actualPrice * rate / PERCENT_DENOMINATOR_VALUE)
                    / ROUND_OFF_VALUE) * ROUND_OFF_VALUE;
        }
        return actualPrice;
    }

    /**
     * 상품의 할인율을 얻어 반환합니다.
     *
     * @param product 할인율을 구할 상품
     * @return 상품의 할인율
     * @author 이수정
     * @since 1.0
     */
    private int getRateByProduct(SearchedProduct product) {
        return product.isSeparatelyDiscount()
                ? product.getDiscountRate()
                : product.getSearchedTotalDiscountRate().getDiscountRate();
    }

    private Boolean isEbook(SearchedProduct searchedProduct) {
        return Objects.nonNull(searchedProduct.getEbookId());
    }
}
