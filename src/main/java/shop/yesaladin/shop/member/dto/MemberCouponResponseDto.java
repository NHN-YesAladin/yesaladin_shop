package shop.yesaladin.shop.member.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원 쿠폰 등록 요청 처리 후 응답를 담은 dto 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemberCouponResponseDto {

    private List<String> givenCouponCodeList;
}
