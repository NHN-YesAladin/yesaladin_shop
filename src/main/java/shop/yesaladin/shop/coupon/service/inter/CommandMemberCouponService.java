package shop.yesaladin.shop.coupon.service.inter;

import java.util.List;
import shop.yesaladin.shop.member.dto.MemberCouponRequestDto;
import shop.yesaladin.shop.member.dto.MemberCouponResponseDto;

/**
 * 회원 쿠폰 등록 관련 서비스 인터페이스 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
public interface CommandMemberCouponService {

    /**
     * 회원 쿠폰을 등록합니다.
     *
     * @param requestDtoList 회원과 지급할 쿠폰 정보를 담은 dto 리스트
     * @return 회원 쿠폰 등록 요청에 대한 처리 정보
     */
    MemberCouponResponseDto createMemberCoupons(List<MemberCouponRequestDto> requestDtoList);
}
