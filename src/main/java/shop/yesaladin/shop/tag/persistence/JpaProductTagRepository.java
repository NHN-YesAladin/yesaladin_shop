package shop.yesaladin.shop.tag.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.tag.domain.model.ProductTag;
import shop.yesaladin.shop.tag.domain.model.ProductTag.Pk;
import shop.yesaladin.shop.tag.domain.repository.CommandProductTagRepository;
import shop.yesaladin.shop.tag.domain.repository.QueryProductTagRepository;

/**
 * 상품 태그 관계 Repository 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface JpaProductTagRepository extends Repository<ProductTag, Pk>,
        CommandProductTagRepository, QueryProductTagRepository {

}
