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
     * @param usable   사용 가능 여부
     * @return 회원이 가진 쿠폰의 요약 정보 리스트
     */
    PaginatedResponseDto<MemberCouponSummaryDto> getMemberCouponSummaryList(
            Pageable pageable,
            String memberId,
            boolean usable
    );

    /**
     * 쿠폰 코드로 쿠폰의 요약 정보를 조회합니다.
     *
     * @param couponCodes 조회할 쿠폰 코드 리스트
     * @return 쿠폰 코드의 요약 정보 리스트
     * @author 서민지
     */
    List<MemberCouponSummaryDto> getMemberCouponSummaryList(List<String> couponCodes);

    /**
     * 회원이 사용할 유효한 쿠폰들의 정보를 반환합니다.
     *
     * @param loginId     회원의 아이디
     * @param couponCodes 사용할 쿠폰 목록
     * @return 유효한 쿠폰 목록
     * @author 최예린
     * @since 1.0
     */
    List<MemberCouponSummaryDto> getValidMemberCouponSummaryListByCouponCodes(
            String loginId,
            List<String> couponCodes
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
