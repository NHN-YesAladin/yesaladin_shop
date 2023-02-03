package shop.yesaladin.shop.member.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
@AllArgsConstructor
public class MemberCouponRequestDto {

    @NotNull
    private Long memberId;
    @NotEmpty
    private List<String> couponCodes;
    @NotEmpty
    private List<String> couponGroupCodes;
}
