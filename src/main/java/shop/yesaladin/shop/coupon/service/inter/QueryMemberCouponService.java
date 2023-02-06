package shop.yesaladin.shop.coupon.service.inter;

import java.util.List;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.coupon.domain.model.MemberCoupon;
import shop.yesaladin.shop.coupon.dto.MemberCouponSummaryDto;

/**
 * 쿠폰 조회와 관련된 기능을 제공하는 서비스 인터페이스입니다.
 *
 * @author 김홍대
 * @author 최예린
 * @since 1.0
 */
public interface QueryMemberCouponService {

    /**
     * 회원이 가진 쿠폰의 요약 정보 리스트를 반환합니다.
     *
     * @param memberId 조회할 회원의 로그인 아이디
     * @return 회원이 가진 쿠폰의 요약 정보 리스트
     */
    PaginatedResponseDto<MemberCouponSummaryDto> getMemberCouponSummaryList(
            Pageable pageable,
            String memberId
    );

    /**
     * 쿠폰의 코드를 통해 회원 쿠폰을 조회합니다.
     *
     * @param couponCodes 쿠폰 코드 리스트
     * @return 회원의 쿠폰 리스트
     * @author 최예린
     * @since 1.0
     */
    List<MemberCoupon> findByCouponCodes(List<String> couponCodes);
}
