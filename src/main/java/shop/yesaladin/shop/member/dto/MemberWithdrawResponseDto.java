package shop.yesaladin.shop.member.dto;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.member.domain.model.Member;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberWithdrawResponseDto {

    private Long id;
    private String name;
    private boolean withdrawal;
    private LocalDate withdrawalDate;

    public static MemberWithdrawResponseDto fromEntity(Member member) {
        return new MemberWithdrawResponseDto(
                member.getId(),
                member.getName(),
                member.isWithdrawal(),
                member.getWithdrawalDate()
        );
    }
}
