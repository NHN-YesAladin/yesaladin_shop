package shop.yesaladin.shop.member.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원이 보유한 쿠폰의 요약 정보를 포함하는 DTO클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public class MemberCouponSummaryDto {

    private final String couponCode;
    private final String couponName;
    private final String minOrderAmount;
    private final String maxDiscountAmount;
    private final Integer discountRate;
    private final Integer discountAmount;
    private final String ISBN;
    private final Long categoryId;
    private final LocalDate expirationDate;
    private final boolean canBeOverlapped;
    private final String couponBound;
    private final String couponType;
}
