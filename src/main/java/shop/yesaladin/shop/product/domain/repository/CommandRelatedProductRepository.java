package shop.yesaladin.shop.product.domain.repository;

import shop.yesaladin.shop.product.domain.model.RelatedProduct;

/**
 * 연관상품 등록 및 수정 관련 repository 클래스입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandRelatedProductRepository {
    RelatedProduct save(RelatedProduct relatedProduct);
}
