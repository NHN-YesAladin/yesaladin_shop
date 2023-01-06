package shop.yesaladin.shop.product.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.product.domain.model.RelatedProduct;
import shop.yesaladin.shop.product.domain.model.RelatedProduct.Pk;
import shop.yesaladin.shop.product.domain.repository.CommandRelatedProductRepository;

/**
 * 연관상품 repository 구현체입니다.
 *
 * @author : 이수정
 * @since : 1.0
 */
public interface JpaRelatedProductRepository extends Repository<RelatedProduct, Pk>,
        CommandRelatedProductRepository {

}
