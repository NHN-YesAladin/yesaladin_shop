package shop.yesaladin.shop.coupon.service.inter;

import java.util.List;
import shop.yesaladin.shop.member.dto.MemberCouponRequestDto;

/**
 * 회원 쿠폰을 지급하는 Repository 인터페이스입니다.
 *
 * @author 서민지
 * @since 1.0
 */
public interface InsertMemberCouponRepository {

    /**
     * 회원 쿠폰 테이블에 쿠폰을 저장하는 메서드입니다.
     *
     * @param requestDtoList 회원 쿠폰 지급에 대한 정보가 담긴 리스트
     * @return 삽입된 데이터의 수
     */
    int insertMemberCoupon(List<MemberCouponRequestDto> requestDtoList);
}
