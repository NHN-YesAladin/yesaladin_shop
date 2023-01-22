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

    /**
     * 전체 할인율을 수정하여 DB에 저장하고, 수정한 전체 할인율 객체를 리턴합니다.
     *
     * @param totalDiscountRate 전체 할인율 엔터티
     * @return 등록된 전체 할인율 객체
     * @author 이수정
     * @since 1.0
     */
    TotalDiscountRateResponseDto modify(TotalDiscountRate totalDiscountRate);
}
