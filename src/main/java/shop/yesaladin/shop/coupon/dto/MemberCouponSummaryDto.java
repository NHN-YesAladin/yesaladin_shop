package shop.yesaladin.shop.coupon.dto;

import java.time.LocalDate;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.coupon.code.CouponBoundCode;
import shop.yesaladin.coupon.code.CouponTypeCode;
import shop.yesaladin.shop.product.dto.ProductWithCategoryResponseDto;

@Slf4j
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberCouponSummaryDto {

    private String name;
    private String couponCode;
    private int amount;     // 금액(포인트, 할인율, 할인금액)
    private Integer minOrderAmount;
    private Integer maxDiscountAmount;
    private Boolean canBeOverlapped;
    private CouponTypeCode couponTypeCode;
    private LocalDate expireDate;
    private Boolean used;
    private String couponBound; // isbn/categoryId/null
    private CouponBoundCode couponBoundCode;

    /**
     * 쿠폰을 적용한 상품의 할인가를 반환합니다
     *
     * @param product 상품정보
     * @param price   상품 가격
     * @return 상품의 할인 가격
     * @author 최예린
     */
    public long discount(ProductWithCategoryResponseDto product, long price) {
        long discountPrice = 0;
        switch (couponBoundCode) {
            case PRODUCT:
                log.error("상품 적용 => ");
                if (Objects.equals(product.getIsbn(), couponBound)) {
                    discountPrice = getDiscountPrice(price);
                }
                break;
            case CATEGORY:
                log.error("카테고리 적용 => ");

                if (product.getCategoryList().contains(Long.parseLong(couponBound))) {
                    discountPrice = getDiscountPrice(price);
                }
                break;
            case ALL:
                log.error("전체 적용 => ");

                discountPrice = getDiscountPrice(price);
                break;
        }
        return discountPrice;
    }


    /**
     * 상품이 할인가를 계산합니다.
     *
     * @param price 상품 가격
     * @return 상품의 할인가
     * @author 최예린
     * @since 1.0
     */
    private long getDiscountPrice(long price) {
        if (couponTypeCode.getCode() == 1) {
            log.error("정액 할인 => ");
            if(price >= minOrderAmount) {
                log.error("{} - {}", price, amount);
                return price - amount;
            }
        } else if (couponTypeCode.getCode() == 2) {
            log.error("정률 할인 => ");
            if(price >= minOrderAmount) {
                long discountAmount = price *(100 - amount) / 100;
                if(discountAmount > maxDiscountAmount) {
                    log.error("{} - 최대할인금액({})", price, maxDiscountAmount);
                    return price - maxDiscountAmount;
                }
                log.error("{} - {}", price, discountAmount);

                return price - discountAmount;
            }
        } else {
            throw new ClientException(
                    ErrorCode.INVALID_COUPON_DATA,
                    "Invalid coupon data."
            );
        }
        return price;
    }
    //private Integer minOrderAmount;
    //    private Integer maxDiscountAmount;
    //    private Boolean canBeOverlapped;
}
