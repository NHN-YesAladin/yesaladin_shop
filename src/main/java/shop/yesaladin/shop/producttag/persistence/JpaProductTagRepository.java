package shop.yesaladin.shop.producttag.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.producttag.domain.model.ProductTag;
import shop.yesaladin.shop.producttag.domain.model.ProductTag.Pk;
import shop.yesaladin.shop.producttag.domain.repository.CommandProductTagRepository;

/**
 * 상품 태그 관계 repository 구현체입니다.
 *
 * @author : 이수정
 * @since : 1.0
 */
public interface JpaProductTagRepository extends Repository<ProductTag, Pk>,
        CommandProductTagRepository {

}
