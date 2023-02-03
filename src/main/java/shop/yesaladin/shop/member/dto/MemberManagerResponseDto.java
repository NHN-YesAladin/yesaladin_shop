package shop.yesaladin.shop.member.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.member.domain.model.Member;

/**
 * 관리자의 회원 관리 Dto
 *
 * @author 김선홍
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberManagerResponseDto {

    private Long id;
    private String nickname;
    private String loginId;
    private String email;
    private String phone;
    private String name;
    private LocalDate signUpDate;
    private LocalDate withdrawalDate;
    private Boolean isWithdrawal;
    private Boolean isBlocked;
    private String blockedReason;
    private LocalDate blockedDate;
    private LocalDate unblockedDate;

    public static MemberManagerResponseDto fromEntity(Member member) {
        return MemberManagerResponseDto.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .loginId(member.getLoginId())
                .email(member.getEmail())
                .phone(member.getPhone())
                .name(member.getName())
                .signUpDate(member.getSignUpDate())
                .isWithdrawal(member.isWithdrawal())
                .isBlocked(member.isBlocked())
                .blockedReason(member.getBlockedReason())
                .blockedDate(member.getBlockedDate())
                .unblockedDate(member.getUnblockedDate())
                .build();
    }
}
