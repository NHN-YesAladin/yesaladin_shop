package shop.yesaladin.shop.product.domain.repository;

import shop.yesaladin.shop.product.domain.model.Relation;

/**
 * 상품 연관관계 등록 및 삭제 관련 Repository Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandRelationRepository {

    Relation save(Relation relatedProduct);

    void deleteByPk(Relation.Pk pk);
}
