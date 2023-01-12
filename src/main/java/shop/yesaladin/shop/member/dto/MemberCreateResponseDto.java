package shop.yesaladin.shop.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberGrade;
import shop.yesaladin.shop.member.domain.model.Role;

/**
 * 회원 등록 이후 MemberController 에서 클라이언트 에게 반환하기 위한 결과 DTO 입니다.
 *
 * @author : 송학현
 * @since : 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberCreateResponseDto {

    private Long id;
    private String name;
    private String nickname;
    private String loginId;
    private String memberGrade;
    private String role;

    /**
     * Member Entity를 MemberCreateResponse DTO로 변한하기 위한 메서드 입니다.
     *
     * @param member Repository에서 등록된 결과 엔티티 입니다.
     * @return 결과로 반환된 엔티티의 일부 데이터를 사용하여 Response DTO로 반환합니다.
     *          회원 가입 시 회원의 등급은 WHITE 등급으로 고정되는 결과 입니다.
     *          이 Dto는 회원 가입 기능에만 사용되는 dto 입니다.
     * @author : 송학현
     * @since : 1.0
     */
    public static MemberCreateResponseDto fromEntity(Member member, Role role) {
        return new MemberCreateResponseDto(
                member.getId(),
                member.getName(),
                member.getNickname(),
                member.getLoginId(),
                MemberGrade.WHITE.getName(),
                role.getName()
        );
    }
}
