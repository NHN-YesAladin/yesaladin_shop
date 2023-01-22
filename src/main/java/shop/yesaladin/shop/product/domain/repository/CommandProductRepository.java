package shop.yesaladin.shop.product.domain.repository;

import shop.yesaladin.shop.product.domain.model.Product;

/**
 * 상품 등록 및 수정 관련 Repository Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandProductRepository {

    Product save(Product product);
}
