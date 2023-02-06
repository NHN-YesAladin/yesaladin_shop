package shop.yesaladin.shop.product.service.inter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.product.dto.ProductDetailResponseDto;
import shop.yesaladin.shop.product.dto.ProductModifyDto;
import shop.yesaladin.shop.product.dto.ProductsResponseDto;
import shop.yesaladin.shop.product.dto.ViewCartDto;

import java.util.List;
import java.util.Map;

/**
 * 상품 조회를 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QueryProductService {

    /**
     * 아이디가 id인 상품을 찾아 반환합니다.
     *
     * @param id 찾고자하는 상품의 id
     * @return 찾은 상품 엔터티
     * @author 이수정
     * @since 1.0
     */
    ProductDetailResponseDto findById(long id);

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
     * 페이징된 모든사용자용 상품 리스트를 반환합니다.
     *
     * @param pageable 페이징 처리를 위한 객체
     * @return 페이징 조회된 상품 dto를 담은 객체
     * @author 이수정
     * @since 1.0
     */
    Page<ProductsResponseDto> findAll(Pageable pageable, Integer typeId);

    /**
     * 페이징된 관리자용 상품 리스트를 반환합니다.
     *
     * @param pageable 페이징 처리를 위한 객체
     * @return 페이징 조회된 상품 dto를 담은 객체
     * @author 이수정
     * @since 1.0
     */
    Page<ProductsResponseDto> findAllForManager(Pageable pageable, Integer typeId);

    /**
     * 장바구니에 넣은 상품의 정보를 얻어 반환합니다.
     *
     * @param cart 찾고자하는 Cart의 정보를 담은 Map
     * @return 장바구니의 상품의 정보를 담은 Dto
     * @author 이수정
     * @since 1.0
     */
    List<ViewCartDto> getCartProduct(Map<String, String> cart);
}
