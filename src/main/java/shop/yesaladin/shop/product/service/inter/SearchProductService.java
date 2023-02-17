package shop.yesaladin.shop.product.service.inter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.product.dto.SearchedProductResponseDto;

/**
 * 상품 검색의 서비스 인터페이스 입니다.
 *
 * @author : 김선홍
 * @since : 1.0
 */
public interface SearchProductService {

    /**
     * 카테고리 id로 상품을 검색하는 메서드
     *
     * @param id       검색하고 싶은 카테고리 id
     * @param pageable 페이지정보
     * @return 상품 리스트와 총 갯수
     * @author : 김선홍
     * @since : 1.0
     */
    Page<SearchedProductResponseDto> searchProductsByCategoryId(Long id, Pageable pageable);

    /**
     * 카테고리 이름으로 상품을 검색하는 메서드
     *
     * @param name     검색하고 싶은 카테고리 이름
     * @param pageable 페이지정보
     * @return 상품 리스트와 총 갯수
     * @author : 김선홍
     * @since : 1.0
     */
    Page<SearchedProductResponseDto> searchProductsByCategoryName(String name, Pageable pageable);

    /**
     * 상품 제목으로 상품을 검색하는 메서드
     *
     * @param title    검색하고 싶은 상품 제목
     * @param pageable 페이지정보
     * @return 상품 리스트와 총 갯수
     * @author : 김선홍
     * @since : 1.0
     */
    Page<SearchedProductResponseDto> searchProductsByProductTitle(
            String title, Pageable pageable
    );

    /**
     * 상품 내용으로 상품을 검색하는 메서드
     *
     * @param content  검색하고 싶은 상품 내용
     * @param pageable 페이지정보
     * @return 상품 리스트와 총 갯수
     * @author : 김선홍
     * @since : 1.0
     */
    Page<SearchedProductResponseDto> searchProductsByProductContent(
            String content, Pageable pageable
    );

    /**
     * 상품 isbn로 상품을 검색하는 메서드
     *
     * @param isbn     검색하고 싶은 카테고리 이름
     * @param pageable 페이지정보
     * @return 상품 리스트와 총 갯수
     * @author : 김선홍
     * @since : 1.0
     */
    Page<SearchedProductResponseDto> searchProductsByProductISBN(
            String isbn, Pageable pageable
    );

    /**
     * 카테고리 이름으로 상품을 검색하는 메서드
     *
     * @param author   검색하고 싶은 카테고리 이름
     * @param pageable 페이지정보
     * @return 상품 리스트와 총 갯수
     * @author : 김선홍
     * @since : 1.0
     */
    Page<SearchedProductResponseDto> searchProductsByProductAuthor(
            String author, Pageable pageable
    );

    /**
     * 카테고리 이름으로 상품을 검색하는 메서드
     *
     * @param publisher 검색하고 싶은 카테고리 이름
     * @param pageable  페이지정보
     * @return 상품 리스트와 총 갯수
     * @author : 김선홍
     * @since : 1.0
     */
    Page<SearchedProductResponseDto> searchProductsByPublisher(
            String publisher, Pageable pageable
    );

    /**
     * 태그 이름으로 상품을 검색하는 메서드
     *
     * @param tag      검색하고 싶은 태그 이름
     * @param pageable 페이지정보
     * @return 상품 리스트와 총 갯수
     * @author : 김선홍
     * @since : 1.0
     */
    Page<SearchedProductResponseDto> searchProductsByTag(String tag, Pageable pageable);


}
