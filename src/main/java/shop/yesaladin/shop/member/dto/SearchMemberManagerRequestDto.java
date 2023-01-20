package shop.yesaladin.shop.member.dto;

import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import shop.yesaladin.shop.member.domain.model.MemberGenderCode;
import shop.yesaladin.shop.member.domain.model.SearchedMember;

/**
 * 검색할 회원의 정보입니다.
 *
 * @author : 김선홍
 * @since : 1.0
 */
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SearchMemberManagerRequestDto {

    private Long id;
    @NotBlank
    private String nickname;
    @NotBlank
    private String name;
    @NotBlank
    private String loginId;
    @NotBlank
    private String phone;

    private int birthYear;

    private int birthMonth;
    private int birthDay;
    @Email
    private String email;
    private LocalDate signUpDate;
    private LocalDate withdrawalDate;
    private boolean isWithdrawal;
    private boolean isBlocked;
    private Long point;
    @NotNull
    private String grade;
    private MemberGenderCode gender;

    public SearchedMember toSearchedMember() {
        return SearchedMember.builder()
                .id(id)
                .nickname(nickname)
                .name(name)
                .loginId(loginId)
                .phone(phone)
                .birthYear(birthYear)
                .birthMonth(birthMonth)
                .birthDay(birthDay)
                .isWithdrawal(isWithdrawal)
                .isBlocked(isBlocked)
                .withdrawalDate(withdrawalDate)
                .signUpDate(signUpDate)
                .email(email)
                .point(point)
                .grade(grade)
                .gender(gender)
                .build();
    }
}
