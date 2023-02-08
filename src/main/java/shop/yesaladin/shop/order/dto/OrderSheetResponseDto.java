package shop.yesaladin.shop.order.dto;

import java.util.List;
import lombok.Getter;
import shop.yesaladin.shop.coupon.dto.MemberCouponSummaryDto;
import shop.yesaladin.shop.member.dto.MemberAddressResponseDto;
import shop.yesaladin.shop.member.dto.MemberOrderSheetResponseDto;
import shop.yesaladin.shop.product.dto.ProductOrderSheetResponseDto;

/**
 * 회원 주문서에 필요한 데이터를 반환하는 dto 클래스입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
public class OrderSheetResponseDto {

    private final List<ProductOrderSheetResponseDto> orderProducts;
    private String name;
    private String phoneNumber;
    private Long point;
    private List<MemberAddressResponseDto> memberAddress;
    private List<MemberCouponSummaryDto> memberCoupons;

    public OrderSheetResponseDto(
            MemberOrderSheetResponseDto member,
            long point,
            List<ProductOrderSheetResponseDto> orderProducts,
            List<MemberAddressResponseDto> memberAddress,
            List<MemberCouponSummaryDto> memberCoupons
    ) {
        this.name = member.getName();
        this.phoneNumber = member.getPhoneNumber();
        this.point = point;
        this.orderProducts = orderProducts;
        this.memberAddress = memberAddress;
        this.memberCoupons = memberCoupons;
    }

    public OrderSheetResponseDto(List<ProductOrderSheetResponseDto> orderProducts) {
        this.orderProducts = orderProducts;
    }
}
