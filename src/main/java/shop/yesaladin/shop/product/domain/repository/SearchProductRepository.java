package shop.yesaladin.shop.product.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.product.dto.SearchedProductResponseDto;

import java.util.List;

/**
 * 상품 검색 레포지토리의 인터페이스
 *
 * @author : 김선홍
 * @since : 1.0
 */
public interface SearchProductRepository {

    /**
     * 카테고리 id를 이용한 검색하는 메소드
     *
     * @param id       검색할 카테고리 id
     * @param pageable 페이지 정보
     * @return 상품 리스트와 총 갯수
     * @
     */
    Page<SearchedProductResponseDto> searchProductsByCategoryId(Long id, Pageable pageable);

    /**
     * 상품 이름으로 상품을 검색하는 메서드
     *
     * @param title    검색하고 싶은 상품 이름
     * @param pageable 페이지 정보
     * @return 상품 리스트와 총 갯수
     * @author : 김선홍
     * @since : 1.0
     */
    Page<SearchedProductResponseDto> searchProductsByProductTitle(
            String title,
            Pageable pageable
    );

    /**
     * 상품 내용으로 상품을 검색하는 메서드
     *
     * @param content  검색하고 싶은 상품 내용
     * @param pageable 페이지 정보
     * @return 상품 리스트와 총 갯수
     * @author : 김선홍
     * @since : 1.0
     */
    Page<SearchedProductResponseDto> searchProductsByProductContent(
            String content,
            Pageable pageable
    );

    /**
     * 상품 isbn으로 상품을 검색하는 메서드
     *
     * @param isbn     검색하고 싶은 카테고리 이름
     * @param pageable 페이지 정보
     * @return 상품 리스트와 총 갯수
     * @author : 김선홍
     * @since : 1.0
     */
    Page<SearchedProductResponseDto> searchProductsByProductISBN(
            String isbn,
            Pageable pageable
    );

    /**
     * 작가 이름으로 상품을 검색하는 메서드
     *
     * @param author   검색하고 싶은 카테고리 이름
     * @param pageable 페이지 정보
     * @return 상품 리스트와 총 갯수
     * @author : 김선홍
     * @since : 1.0
     */
    Page<SearchedProductResponseDto> searchProductsByProductAuthor(
            String author,
            Pageable pageable
    );

    /**
     * 출판사 이름으로 상품을 검색하는 메서드
     *
     * @param publisher 검색하고 싶은 출판사 이름
     * @param pageable  페이지 정보
     * @return 상품 리스트와 총 갯수
     * @author : 김선홍
     * @since : 1.0
     */
    Page<SearchedProductResponseDto> searchProductsByPublisher(
            String publisher,
            Pageable pageable
    );

    /**
     * 태그 이름으로 상품을 검색하는 메서드
     *
     * @param tag      검색하고 싶은 태그 이름
     * @param pageable 페이지 정보
     * @return 상품 리스트와 총 갯수
     * @author : 김선홍
     * @since : 1.0
     */
    Page<SearchedProductResponseDto> searchProductsByTag(String tag, Pageable pageable);

    /**
     * 멀티 필드와 형태소분석을 통해상품을 검색하는 메서드
     *
     * @param value    멀티 필드에 검색하고 싶은 값
     * @param pageable 페이지 정보
     * @param fields   검색할 필드들
     * @return 상품 리스트와 총 갯수
     * @author : 김선홍
     * @since : 1.0
     */
    Page<SearchedProductResponseDto> searchResponseProductByMultiQuery(
            String value,
            Pageable pageable,
            List<String> fields
    );

    /**
     * 필터에서 TermQuery를 통해 상품을 검색하는 메서드
     *
     * @param value    필드에 검색하고 싶은 값
     * @param pageable 페이지 정보
     * @param field    검색할 필드
     * @return 상품 리스트와 총 갯수
     * @author : 김선홍
     * @since : 1.0
     */
    Page<SearchedProductResponseDto> searchResponseProductByTermQuery(
            String value,
            Pageable pageable,
            String field
    );
}
