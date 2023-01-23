package shop.yesaladin.shop.publish.service.inter;

import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.publish.dto.PublishResponseDto;

/**
 * 출판 조회를 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QueryPublishService {

    /**
     * 출판한 상품을 기준으로 출판를 조회하여 조회된 출판 Dto를 반환합니다.
     *
     * @param product 출판을 조회할 product
     * @return 조회된 출판 dto
     * @author 이수정
     * @since 1.0
     */
    PublishResponseDto findByProduct(Product product);
}
