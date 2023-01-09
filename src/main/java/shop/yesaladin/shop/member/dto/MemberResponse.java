package shop.yesaladin.shop.member.dto;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberGenderCode;
import shop.yesaladin.shop.member.domain.model.MemberGrade;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponse {
    private Long id;
    private String nickname;
    private String name;
    private String loginId;
    private String password;
    private Integer birthYear;
    private Integer birthMonth;
    private Integer birthDay;
    private String email;
    private LocalDate signUpDate;
    private LocalDate withdrawalDate;
    private boolean isWithdrawal;
    private boolean isBlocked;
    private long point;
    private MemberGrade memberGrade;
    private MemberGenderCode memberGenderCode;

    public Member toEntity() {
        return Member.builder()
                .id(id)
                .name(name)
                .nickname(nickname)
                .loginId(loginId)
                .password(password)
                .birthYear(birthYear)
                .birthMonth(birthMonth)
                .birthDay(birthDay)
                .email(email)
                .signUpDate(signUpDate)
                .isWithdrawal(isWithdrawal)
                .withdrawalDate(withdrawalDate)
                .isBlocked(isBlocked)
                .point(point)
                .memberGrade(memberGrade)
                .memberGenderCode(memberGenderCode)
                .build();
    }
    public static MemberResponse fromEntity(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getNickname(),
                member.getLoginId(),
                member.getPassword(),
                member.getBirthYear(),
                member.getBirthMonth(),
                member.getBirthDay(),
                member.getEmail(),
                member.getSignUpDate(),
                member.getWithdrawalDate(),
                member.isWithdrawal(),
                member.isBlocked(),
                member.getPoint(),
                member.getMemberGrade(),
                member.getMemberGenderCode()
        );
    }
}
