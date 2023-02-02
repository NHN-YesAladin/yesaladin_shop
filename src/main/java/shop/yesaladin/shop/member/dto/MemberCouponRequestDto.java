package shop.yesaladin.shop.member.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.member.domain.model.Member;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberCouponRequestDto {

    private Member member;
    private List<String> couponCodes;
    private List<String> groupCodes;
}
