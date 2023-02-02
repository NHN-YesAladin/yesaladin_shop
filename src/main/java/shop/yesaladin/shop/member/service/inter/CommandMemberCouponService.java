package shop.yesaladin.shop.member.service.inter;

import java.util.List;
import shop.yesaladin.shop.member.domain.model.MemberCoupon;
import shop.yesaladin.shop.member.dto.MemberCouponRequestDto;

public interface CommandMemberCouponService {

    List<MemberCoupon> createMemberCoupons(List<MemberCouponRequestDto> requestDtos);
}
