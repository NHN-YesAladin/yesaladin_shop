package shop.yesaladin.shop.product.service.inter;

import shop.yesaladin.shop.product.dto.ProductTypeResponseDto;

import java.util.List;

/**
 * 상품유형 조회를 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QueryProductTypeService {

    /**
     * 상품유형을 전체 조회합니다.
     *
     * @return 출판사 전체 조회한 List
     * @author 이수정
     * @since 1.0
     */
    List<ProductTypeResponseDto> findAll();
}
