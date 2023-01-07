package shop.yesaladin.shop.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberGrade;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberCreateResponse {

    private String name;
    private String nickname;
    private String loginId;
    private MemberGrade memberGrade;

    public static MemberCreateResponse fromEntity(Member member) {
        return new MemberCreateResponse(
                member.getName(),
                member.getNickname(),
                member.getLoginId(),
                member.getMemberGrade()
        );
    }
}
