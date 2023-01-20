package shop.yesaladin.shop.product.service.inter;

import shop.yesaladin.shop.product.dto.ProductCreateDto;
import shop.yesaladin.shop.product.dto.ProductOnlyIdDto;

/**
 * 상품 등록/수정/삭제을 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandProductService {

    /**
     * 상품을 생성하여 저장합니다. 생성된 상품 객체를 리턴합니다.
     *
     * @param dto 관리자에게서 입력받은 상품 생성정보
     * @return 생성된 상품 객체
     * @author 이수정
     * @since 1.0
     */
    ProductOnlyIdDto create(ProductCreateDto dto);
}
