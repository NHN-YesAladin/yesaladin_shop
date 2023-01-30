package shop.yesaladin.shop.product.domain.repository;

import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.RelatedProduct;

import java.util.List;

/**
 * 연관상품 조회 관련 Repository Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QueryRelatedProductRepository {

    List<RelatedProduct> findByProductMain(Product product);
}
