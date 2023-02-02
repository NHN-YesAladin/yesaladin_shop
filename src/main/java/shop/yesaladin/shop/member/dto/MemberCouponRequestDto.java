package shop.yesaladin.shop.member.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.member.domain.model.Member;

/**
 * 등록할 회원 쿠폰 정보를 담은 dto 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberCouponRequestDto {

    private Member member;
    private List<String> couponCodes;
    private List<String> groupCodes;
}
