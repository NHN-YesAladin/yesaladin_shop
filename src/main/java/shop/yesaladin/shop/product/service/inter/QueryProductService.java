package shop.yesaladin.shop.product.service.inter;

import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.product.dto.*;

import java.util.List;
import java.util.Map;

/**
 * 상품 조회를 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @author 최예린
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
     * 아이디가 id인 상품을 찾아 반환합니다.
     *
     * @param id 찾고자하는 상품의 id
     * @return 찾은 상품 엔터티
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
}
