package shop.yesaladin.shop.product.service.inter;

import shop.yesaladin.shop.product.dto.SubscribeProductResponseDto;

/**
 * 구독상품 조회를 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QuerySubscribeProductService {

    SubscribeProductResponseDto findByISSN(String ISSN);
}
