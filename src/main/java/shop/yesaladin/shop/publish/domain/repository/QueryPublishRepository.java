package shop.yesaladin.shop.publish.domain.repository;

import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.publish.domain.model.Publish;

import java.util.Optional;

/**
 * 출판 조회 관련 repository 클래스입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QueryPublishRepository {

    Optional<Publish> findByProduct(Product product);
}
