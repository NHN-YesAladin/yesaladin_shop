package shop.yesaladin.shop.writing.domain.repository;

import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.writing.domain.model.Writing;

import java.util.List;

/**
 * 집필 조회 관련 Repository Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QueryWritingRepository {

    List<Writing> findByProduct(Product product);

    boolean existsByProduct(Product product);
}
