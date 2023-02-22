package shop.yesaladin.shop.product.service.inter;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.product.dto.ProductDetailResponseDto;
import shop.yesaladin.shop.product.dto.ProductModifyDto;
import shop.yesaladin.shop.product.dto.ProductOnlyTitleDto;
import shop.yesaladin.shop.product.dto.ProductOrderRequestDto;
import shop.yesaladin.shop.product.dto.ProductOrderSheetResponseDto;
import shop.yesaladin.shop.product.dto.ProductRecentResponseDto;
import shop.yesaladin.shop.product.dto.ProductResponseDto;
import shop.yesaladin.shop.product.dto.ProductWithCategoryResponseDto;
import shop.yesaladin.shop.product.dto.ProductsResponseDto;
import shop.yesaladin.shop.product.dto.RelationsResponseDto;
import shop.yesaladin.shop.product.dto.SubscribeProductOrderResponseDto;
import shop.yesaladin.shop.product.dto.ViewCartDto;


/**
 * 상품 조회를 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @author 최예린
 * @author 김선홍
 * @since 1.0
 */
public interface QueryProductService {

    /**
     * ISBN이 isbn인 상품을 찾아 반환합니다.
     *
     * @param isbn 찾고자하는 상품의 isbn
     * @return 찾은 상품의 제목을 담은 Dto
     * @author 이수정
     * @since 1.0
     */
    ProductOnlyTitleDto findTitleByIsbn(String isbn);

    /**
     * ISBN이 이미 존재하는지 조회하여 반환합니다.
     *
     * @param isbn 찾고자하는 isbn
     * @return ISBN 존재여부
     * @author 이수정
     * @since 1.0
     */
    Boolean existsByIsbn(String isbn);

    /**
     * 아이디가 id인 상품을 찾아 반환합니다.
     *
     * @param id 찾고자하는 상품의 id
     * @return 찾은 상품의 수량
     * @author 이수정
     * @since 1.0
     */
    Long findQuantityById(Long id);

    /**
     * 아이디가 id인 상품을 찾아 반환합니다.
     *
     * @param id 찾고자하는 상품의 id
     * @return 찾은 상품의 Dto
     * @author 이수정
     * @since 1.0
     */
    ProductResponseDto findProductById(Long id);

    /**
     * 아이디가 id인 상품을 찾아 상세정보를 담아 반환합니다.
     *
     * @param id 찾고자하는 상품의 id
     * @return 상세정보를 담은 Dto
     * @author 이수정
     * @since 1.0
     */
    ProductDetailResponseDto findDetailProductById(long id);

    /**
     * 아이디가 id인 상품을 찾아 수정 View에 넣을 정보를 반환합니다.
     *
     * @param id 찾고자하는 상품의 id
     * @return 찾은 상품 엔터티
     * @author 이수정
     * @since 1.0
     */
    ProductModifyDto findProductByIdForForm(long id);

    /**
     * 장바구니에 넣은 상품의 정보를 얻어 반환합니다.
     *
     * @param cart 찾고자하는 Cart의 정보를 담은 Map
     * @return 장바구니의 상품의 정보를 담은 Dto
     * @author 이수정
     * @since 1.0
     */
    List<ViewCartDto> getCartProduct(Map<String, String> cart);

    /**
     * 페이징된 모든사용자용 상품 리스트를 반환합니다.
     *
     * @param pageable 페이징 처리를 위한 객체
     * @return 페이징 조회된 상품 dto를 담은 객체
     * @author 이수정
     * @since 1.0
     */
    PaginatedResponseDto<ProductsResponseDto> findAll(Pageable pageable, Integer typeId);

    /**
     * 페이징된 관리자용 상품 리스트를 반환합니다.
     *
     * @param pageable 페이징 처리를 위한 객체
     * @return 페이징 조회된 상품 dto를 담은 객체
     * @author 이수정
     * @since 1.0
     */
    PaginatedResponseDto<ProductsResponseDto> findAllForManager(Pageable pageable, Integer typeId);

    /**
     * 관리자용 상품 제목 검색 메서드
     *
     * @param title    검색할 제목
     * @param pageable 페이지 정보
     * @return 검색 결과
     * @author 김선홍
     * @since 1, 0
     */
    PaginatedResponseDto<ProductsResponseDto> findByTitleForManager(
            String title,
            Pageable pageable
    );

    /**
     * 관리자용 상품 isbn 검색 메서드
     *
     * @param isbn     검색할 isbn
     * @param pageable 페이지 정보
     * @return 검색 결과
     * @author 김선홍
     * @since 1, 0
     */
    PaginatedResponseDto<ProductsResponseDto> findByISBNForManager(String isbn, Pageable pageable);

    /**
     * 관리자용 상품 내용 검색 메서드
     *
     * @param content  검색할 제목
     * @param pageable 페이지 정보
     * @return 검색 결과
     * @author 김선홍
     * @since 1, 0
     */
    PaginatedResponseDto<ProductsResponseDto> findByContentForManager(
            String content,
            Pageable pageable
    );

    /**
     * 관리자용 상품 출판사 검색 메서드
     *
     * @param publisher 검색할 제목
     * @param pageable  페이지 정보
     * @return 검색 결과
     * @author 김선홍
     * @since 1, 0
     */
    PaginatedResponseDto<ProductsResponseDto> findByPublisherForManager(
            String publisher,
            Pageable pageable
    );

    /**
     * 관리자용 상품 저자 검색 메서드
     *
     * @param author   검색할 제목
     * @param pageable 페이지 정보
     * @return 검색 결과
     * @author 김선홍
     * @since 1, 0
     */
    PaginatedResponseDto<ProductsResponseDto> findByAuthorForManager(
            String author,
            Pageable pageable
    );

    /**
     * 주문에 사용될 상품 관련 데이터 리스트를 반환합니다.
     *
     * @param orderProducts 구독 상품 + 수량
     * @return 주문 상품 데이터 리스트
     * @author 최예린
     * @since 1.0
     */
    List<ProductOrderSheetResponseDto> getByOrderProducts(Map<String, Integer> orderProducts);

    /**
     * 주문에 사용될 구독 주문 상품을 조회합니다.
     *
     * @param orderProduct 구독 상품 + 수량
     * @return 정기구독 상품과 제목
     * @author 최예린
     * @since 1.0
     */
    SubscribeProductOrderResponseDto getIssnByOrderProduct(ProductOrderRequestDto orderProduct);

    /**
     * 연관관계를 등록하기 위한 상품 제목 contains 검색 메서드 입니다.
     *
     * @param id       메인 상품의 id
     * @param title    검색할 상품의 제목
     * @param pageable 페이징 정보
     * @return 검색된 상품들
     * @author 김선홍
     * @since 1.0
     */
    Page<RelationsResponseDto> findProductRelationByTitle(Long id, String title, Pageable pageable);

    /**
     * 상품의 isbn을 통해 상품과 카테고리를 반환합니다.
     *
     * @param isbn isbn
     * @return 상품
     * @author 최예린
     * @since 1.0
     */
    ProductWithCategoryResponseDto getByIsbn(String isbn);

    /**
     * 최신 상품 조회 메서드
     *
     * @param pageable 페이지 정보
     * @return 촤신 상품 리스트
     * @author 김선홍
     * @since 1, 0
     */
    List<ProductRecentResponseDto> findRecentProductByPublishedDate(Pageable pageable);

    /**
     * 최근 본 상품 메소드
     *
     * @param totalIds 본 상품들의 id 리스트
     * @param pageIds  페이지에 보여줄 상품들
     * @param pageable 페이지 정보
     * @return 최근 본 상품 리스트
     * @author 김선홍
     * @since 1, 0
     */
    Page<ProductRecentResponseDto> findRecentViewProductById(
            List<Long> totalIds,
            List<Long> pageIds,
            Pageable pageable
    );
}
