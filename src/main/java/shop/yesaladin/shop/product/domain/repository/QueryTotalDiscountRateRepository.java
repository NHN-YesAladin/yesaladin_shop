package shop.yesaladin.shop.product.domain.repository;

import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;

import java.util.Optional;

/**
 * 전체 할인율 조회 관련 Repository Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QueryTotalDiscountRateRepository {

    Optional<TotalDiscountRate> findById(int id);
}
