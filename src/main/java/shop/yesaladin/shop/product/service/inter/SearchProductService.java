package shop.yesaladin.shop.product.service.inter;

import java.util.List;
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
     * @param id 검색하고 싶은 카테고리 id
     * @return 해당 카테고리 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    List<SearchedProductResponseDto> searchProductsByCategoryId(Long id, int offset, int size);

    /**
     * 카테고리 이름으로 상품을 검색하는 메서드
     *
     * @param name 검색하고 싶은 카테고리 이름
     * @return 해당 카테고리 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    List<SearchedProductResponseDto> searchProductsByCategoryName(String name, int offset, int size);

    /**
     * 상품 제목으로 상품을 검색하는 메서드
     *
     * @param title 검색하고 싶은 상품 제목
     * @param offset 검색하고 싶은 페이지 위치
     * @param size 검색하고 싶은 상품 갯수
     * @return 해당 이름의 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    List<SearchedProductResponseDto> searchProductsByProductTitle(
            String title, int offset, int size
    );

    /**
     * 상품 내용으로 상품을 검색하는 메서드
     *
     * @param content 검색하고 싶은 상품 내용
     * @param offset 검색하고 싶은 페이지 위치
     * @param size 검색하고 싶은 상품 갯수
     * @return 해당 카테고리 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    List<SearchedProductResponseDto> searchProductsByProductContent(
            String content, int offset, int size
    );

    /**
     * 상품 isbn로 상품을 검색하는 메서드
     *
     * @param isbn 검색하고 싶은 카테고리 이름
     * @param offset 검색하고 싶은 페이지 위치
     * @param size 검색하고 싶은 상품 갯수
     * @return 해당 카테고리 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    List<SearchedProductResponseDto> searchProductsByProductISBN(
            String isbn, int offset, int size
    );

    /**
     * 카테고리 이름으로 상품을 검색하는 메서드
     *
     * @param author 검색하고 싶은 카테고리 이름
     * @param offset 검색하고 싶은 페이지 위치
     * @param size 검색하고 싶은 상품 갯수
     * @return 해당 카테고리 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    List<SearchedProductResponseDto> searchProductsByProductAuthor(
            String author, int offset, int size
    );

    /**
     * 카테고리 이름으로 상품을 검색하는 메서드
     *
     * @param publisher 검색하고 싶은 카테고리 이름
     * @param offset 검색하고 싶은 페이지 위치
     * @param size 검색하고 싶은 상품 갯수
     * @return 해당 카테고리 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    List<SearchedProductResponseDto> searchProductsByPublisher(
            String publisher, int offset, int size
    );

    /**
     * 태그 이름으로 상품을 검색하는 메서드
     *
     * @param tag 검색하고 싶은 태그 이름
     * @param offset 검색하고 싶은 페이지 위치
     * @param size 검색하고 싶은 상품 갯수
     * @return 해당 태그의 상품 리스트
     * @author : 김선홍
     * @since : 1.0
     */
    List<SearchedProductResponseDto> searchProductsByTag(String tag, int offset, int size);



}
