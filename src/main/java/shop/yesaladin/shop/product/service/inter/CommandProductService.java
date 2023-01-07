package shop.yesaladin.shop.product.service.inter;

import shop.yesaladin.shop.product.dto.ProductCreateDto;
import shop.yesaladin.shop.product.dto.ProductResponseDto;

/**
 * 상품 생성을 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandProductService {

    ProductResponseDto create(ProductCreateDto dto);
}
