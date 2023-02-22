package shop.yesaladin.shop.tag.domain.repository;

import java.util.List;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.tag.domain.model.ProductTag;

/**
 * 태그 관계 조회 관련 Repository Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QueryProductTagRepository {

    List<ProductTag> findByProduct(Product product);

    boolean existsByProduct(Product product);
}
