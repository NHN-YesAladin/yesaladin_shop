package shop.yesaladin.shop.writing.domain.repository;

import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.writing.domain.model.Writing;

/**
 * 집필 등록/수정/삭제 관련 Repository Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandWritingRepository {

    Writing save(Writing writing);

    void deleteByProduct(Product product);
}
