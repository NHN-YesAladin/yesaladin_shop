package shop.yesaladin.shop.product.service.inter;

import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.dto.TotalDiscountRateResponseDto;

/**
 * 전체 할인율 등록을 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandTotalDiscountRateService {

    TotalDiscountRateResponseDto register(TotalDiscountRate totalDiscountRate);
}
