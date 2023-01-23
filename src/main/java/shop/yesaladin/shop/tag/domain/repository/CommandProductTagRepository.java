package shop.yesaladin.shop.tag.domain.repository;

import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.tag.domain.model.ProductTag;

/**
 * 상품 태그 관계 등록/수정/삭제 관련 Repository Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandProductTagRepository {

    ProductTag save(ProductTag productTag);

    void deleteByProduct(Product product);
}
