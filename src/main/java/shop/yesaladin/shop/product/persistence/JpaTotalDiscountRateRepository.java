package shop.yesaladin.shop.product.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.domain.repository.CommandTotalDiscountRateRepository;
import shop.yesaladin.shop.product.domain.repository.QueryTotalDiscountRateRepository;

/**
 * 전체 할인율 repository 구현체입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface JpaTotalDiscountRateRepository extends Repository<TotalDiscountRate, Integer>,
        CommandTotalDiscountRateRepository, QueryTotalDiscountRateRepository {

}
