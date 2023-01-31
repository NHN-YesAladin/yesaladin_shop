package shop.yesaladin.shop.product.service.inter;

import shop.yesaladin.shop.product.dto.ProductOnlyIdDto;

/**
 * 상품 연관관계 등록/삭제를 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandRelationService {


    /**
     * 상품 연관관계를 생성하여 등록하고 상품 연관관계 Dto를 반환합니다.
     * 양쪽 다 연결됩니다. (productMain - productSub / productSub - productMain)
     *
     * @param productMainId 연관관계를 이을 메인 상품 Id
     * @param productSubId  연관관계를 이을 서브 상품 Id
     * @return 연결된 서브 상품
     * @author 이수정
     * @since 1.0
     */
    ProductOnlyIdDto create(Long productMainId, Long productSubId);

    /**
     * 상품 연관관계를 삭제합니다.
     * 양쪽 다 삭제됩니다. (productMain - productSub / productSub - productMain)
     *
     * @param productMainId 연관관계를 끊을 메인 상품 Id
     * @param productSubId  연관관계를 끊을 서브 상품 Id
     * @author 이수정
     * @since 1.0
     */
    void delete(Long productMainId, Long productSubId);
}
