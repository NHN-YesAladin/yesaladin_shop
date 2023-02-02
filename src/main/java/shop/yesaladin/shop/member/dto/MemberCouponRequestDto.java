package shop.yesaladin.shop.member.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private MemberDto memberDto;
    private List<String> couponCodes;
    private List<String> groupCodes;
}
