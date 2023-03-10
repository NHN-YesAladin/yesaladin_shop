package shop.yesaladin.shop.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;

/**
 * 전체 할인율 데이터를 반환받는 Dto 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TotalDiscountRateResponseDto {

    private int id;
    private int discountRate;

    public TotalDiscountRate toEntity() {
        return TotalDiscountRate.builder().id(id).discountRate(discountRate).build();
    }
}
