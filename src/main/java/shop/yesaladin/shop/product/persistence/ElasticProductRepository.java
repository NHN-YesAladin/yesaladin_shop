package shop.yesaladin.shop.product.persistence;


import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.product.domain.model.SearchedProduct;
import shop.yesaladin.shop.product.domain.repository.SearchProductRepository;
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
     * @param id 검색할 카테고리 id
     * @param offset 검색하고 싶은 페이지 위치
     * @param size   검색하고 싶은 상품 갯수
     * @return 검색된 상품 리스트
     * @
     */
    @Override
    public List<SearchedProductResponseDto> searchProductsByCategoryId(
            Long id,
            int offset,
            int size
    ) {
        Query query = NativeQuery.builder()
                .withFilter(NativeQuery.builder()
                        .withQuery(q -> q.term(t -> t.field(CATEGORIES_ID).value(id)))
                        .getQuery())
//                .withPageable(PageRequest.of(offset, size))
                .build();

        return elasticsearchOperations.search(query, SearchedProduct.class).stream()
                .map(searchedProductSearchHit -> searchedProductSearchHit.getContent().toDto())
                .collect(Collectors.toList());
    }

    /**
     * 카테고리 이름으로 상품을 검색하는 메서드
     *
     * @param name 검색하고 싶은 카테고리 이름
     * @param offset 검색하고 싶은 페이지 위치
     * @param size   검색하고 싶은 상품 갯수
     * @return 해당 카테고리 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @Override
    public List<SearchedProductResponseDto> searchProductsByCategoryName(
            String name,
            int offset,
            int size
    ) {
        Query query = NativeQuery.builder()
                .withFilter(NativeQuery.builder()
                        .withQuery(q -> q.term(t -> t.field(CATEGORIES_NAME).value(name)))
                        .getQuery())
                .withPageable(PageRequest.of(offset, size))
                .build();

        return elasticsearchOperations.search(query, SearchedProduct.class).stream()
                .map(searchedProductSearchHit -> searchedProductSearchHit.getContent().toDto())
                .collect(Collectors.toList());
    }

    /**
     * 상품 이름으로 상품을 검색하는 메서드
     *
     * @param title  검색하고 싶은 상품 이름
     * @param offset 검색하고 싶은 페이지 위치
     * @param size   검색하고 싶은 상품 갯수
     * @return 해당 이름의 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @Override
    public List<SearchedProductResponseDto> searchProductsByProductTitle(
            String title, int offset, int size
    ) {
        Query query = NativeQuery.builder()
                .withQuery(q -> q.multiMatch(v -> v.query(title).fields(TITLE, TAG)))
                .withFilter(NativeQuery.builder()
                        .withQuery(q -> q.term(t -> t.field(CATEGORIES_DISABLE).value(false)))
                        .getQuery())
                .withFilter(NativeQuery.builder()
                        .withQuery(q -> q.term(t -> t.field(CATEGORIES_IS_SHOWN).value(true)))
                        .getQuery())
                .withPageable(PageRequest.of(offset, size))
                .build();

        return elasticsearchOperations.search(query, SearchedProduct.class).stream()
                .map(searchedProductSearchHit -> searchedProductSearchHit.getContent().toDto())
                .collect(Collectors.toList());
    }

    /**
     * 상품 내용으로 상품을 검색하는 메서드
     *
     * @param content 검색하고 싶은 상품 내용
     * @param offset  검색하고 싶은 페이지 위치
     * @param size    검색하고 싶은 상품 갯수
     * @return 해당 카테고리 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @Override
    public List<SearchedProductResponseDto> searchProductsByProductContent(
            String content, int offset, int size
    ) {
        Query query = NativeQuery.builder()
                .withQuery(q -> q.multiMatch(v -> v.query(content).fields(CONTENT, TAG, DESCRIPTION)))
                .withFilter(NativeQuery.builder()
                        .withQuery(q -> q.term(t -> t.field(CATEGORIES_DISABLE).value(false)))
                        .getQuery())
                .withFilter(NativeQuery.builder()
                        .withQuery(q -> q.term(t -> t.field(CATEGORIES_IS_SHOWN).value(true)))
                        .getQuery())
                .withPageable(PageRequest.of(offset, size))
                .build();

        return elasticsearchOperations.search(query, SearchedProduct.class).stream()
                .map(searchedProductSearchHit -> searchedProductSearchHit.getContent().toDto())
                .collect(Collectors.toList());
    }

    /**
     * 상품 isbn으로 상품을 검색하는 메서드
     *
     * @param isbn   검색하고 싶은 카테고리 이름
     * @param offset 검색하고 싶은 페이지 위치
     * @param size   검색하고 싶은 상품 갯수
     * @return 해당 isbn의 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @Override
    public List<SearchedProductResponseDto> searchProductsByProductISBN(
            String isbn, int offset, int size
    ) {
        Query query = NativeQuery.builder()
                .withFilter(NativeQuery.builder()
                        .withQuery(q -> q.term(t -> t.field(ISBN).value(isbn)))
                        .getQuery())
                .withFilter(NativeQuery.builder()
                        .withQuery(q -> q.term(t -> t.field(CATEGORIES_DISABLE).value(false)))
                        .getQuery())
                .withFilter(NativeQuery.builder()
                        .withQuery(q -> q.term(t -> t.field(CATEGORIES_IS_SHOWN).value(true)))
                        .getQuery())
                .withPageable(PageRequest.of(offset, size))
                .build();

        return elasticsearchOperations.search(query, SearchedProduct.class).stream()
                .map(searchedProductSearchHit -> searchedProductSearchHit.getContent().toDto())
                .collect(Collectors.toList());
    }

    /**
     * 작가 이름으로 상품을 검색하는 메서드
     *
     * @param author 검색하고 싶은 카테고리 이름
     * @param offset 검색하고 싶은 페이지 위치
     * @param size   검색하고 싶은 상품 갯수
     * @return 해당 작가의 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @Override
    public List<SearchedProductResponseDto> searchProductsByProductAuthor(
            String author,
            int offset,
            int size
    ) {
        Query query = NativeQuery.builder()
                .withQuery(q -> q.term(t -> t.field(AUTHORS_NAME).value(author)))
                .withFilter(NativeQuery.builder()
                        .withQuery(q -> q.term(t -> t.field(CATEGORIES_DISABLE).value(false)))
                        .getQuery())
                .withFilter(NativeQuery.builder()
                        .withQuery(q -> q.term(t -> t.field(CATEGORIES_IS_SHOWN).value(true)))
                        .getQuery())
                .withPageable(PageRequest.of(offset, size))
                .build();

        return elasticsearchOperations.search(query, SearchedProduct.class).stream()
                .map(searchedProductSearchHit -> searchedProductSearchHit.getContent().toDto())
                .collect(Collectors.toList());
    }

    /**
     * 출판사 이름으로 상품을 검색하는 메서드
     *
     * @param publisher 검색하고 싶은 출판사 이름
     * @param offset    검색하고 싶은 페이지 위치
     * @param size      검색하고 싶은 상품 갯수
     * @return 해당 출판사의 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @Override
    public List<SearchedProductResponseDto> searchProductsByPublisher(
            String publisher, int offset, int size
    ) {
        Query query = NativeQuery.builder()
                .withQuery(q -> q.term(t -> t.field(PUBLISHER_NAME).value(publisher)))
                .withFilter(NativeQuery.builder()
                        .withQuery(q -> q.term(t -> t.field(CATEGORIES_DISABLE).value(false)))
                        .getQuery())
                .withFilter(NativeQuery.builder()
                        .withQuery(q -> q.term(t -> t.field(CATEGORIES_IS_SHOWN).value(true)))
                        .getQuery())
                .withPageable(PageRequest.of(offset, size))
                .build();

        return elasticsearchOperations.search(query, SearchedProduct.class).stream()
                .map(searchedProductSearchHit -> searchedProductSearchHit.getContent().toDto())
                .collect(Collectors.toList());
    }

    /**
     * 태그 이름으로 상품을 검색하는 메서드
     *
     * @param tag    검색하고 싶은 탸구 이름
     * @param offset 검색하고 싶은 페이지 위치
     * @param size   검색하고 싶은 상품 갯수
     * @return 해당 태그의 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    @Override
    public List<SearchedProductResponseDto> searchProductsByTag(String tag, int offset, int size) {
        Query query = NativeQuery.builder()
                .withQuery(q -> q.term(t -> t.field(TAG).value(tag)))
                .withFilter(NativeQuery.builder()
                        .withQuery(q -> q.term(t -> t.field(CATEGORIES_DISABLE).value(true)))
                        .getQuery())
                .withFilter(NativeQuery.builder()
                        .withQuery(q -> q.term(t -> t.field(CATEGORIES_IS_SHOWN).value(true)))
                        .getQuery())
//                .withPageable(PageRequest.of(offset, size))
                .build();
        return elasticsearchOperations.search(query, SearchedProduct.class).stream()
                .map(searchedProductSearchHit -> searchedProductSearchHit.getContent().toDto())
                .collect(Collectors.toList());
    }
}
