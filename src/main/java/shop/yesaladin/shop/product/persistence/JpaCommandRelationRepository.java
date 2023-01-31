package shop.yesaladin.shop.product.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.product.domain.model.Relation;
import shop.yesaladin.shop.product.domain.repository.CommandRelationRepository;

/**
 * 상품 연관관계 Repository 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface JpaCommandRelationRepository extends Repository<Relation, Relation.Pk>,
        CommandRelationRepository {
}
