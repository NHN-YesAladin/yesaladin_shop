package shop.yesaladin.shop.product.domain.repository;

import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;

/**
 * 전체 할인율 수정 관련 repository 클래스입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandTotalDiscountRateRepository {

    TotalDiscountRate save(TotalDiscountRate totalDiscountRate);
}
