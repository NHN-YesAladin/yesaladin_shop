package shop.yesaladin.shop.publish.service.inter;

import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.publish.domain.model.Publish;
import shop.yesaladin.shop.publish.dto.PublishResponseDto;

/**
 * 출판 등록/수정/삭제를 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandPublishService {

    /**
     * 출판을 DB에 등록하고 등록된 출판 Dto를 반환합니다.
     *
     * @param publish 출판관계 엔터티
     * @return 등록된 출판 Dto
     * @author 이수정
     * @since 1.0
     */
    PublishResponseDto register(Publish publish);

    /**
     * 상품을 기준으로 출판 관계로 삭제합니다.
     *
     * @param product 기준이 될 상품
     * @author 이수정
     * @since 1.0
     */
    void deleteByProduct(Product product);
}
