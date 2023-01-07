package shop.yesaladin.shop.member.dto;

import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberGenderCode;
import shop.yesaladin.shop.member.domain.model.MemberGrade;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberCreateDto {

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    @NotBlank
    @Size(min = 2, max = 15)
    @Pattern(regexp = "^[가-힣a-zA-Z]{2,15}$", message = "한글과 영문만 가능 합니다")
    private String nickname;

    @NotBlank
    @Size(min = 8, max = 15)
    @Pattern(regexp = "^[a-zA-Z]{8,15}[0-9]*$", message = "영문(필수)과 숫자(옵션) 순서 로만 가능 합니다")
    private String loginId;

    @NotBlank
    @Size(min = 8)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "최소 8자, 하나 이상의 문자와 하나의 숫자 및 하나의 특수 문자")
    private String password;

    @NotBlank
    @Size(min = 8, max = 8)
    // TODO: 정규식 수정할 것
    @Pattern(regexp = "^[0-9]{8}")
    private String birth;

    @Email
    @NotBlank
    @Size(max = 100)
    private String email;

    @NotBlank
    // TODO: MALE, FEMALE
    private String gender;

    public Member toEntity(MemberGrade memberGrade) {
        return Member.builder()
                .name(name)
                .nickname(nickname)
                .loginId(loginId)
                .birthYear(Integer.parseInt(birth.substring(0, 4)))
                .birthMonth(Integer.parseInt(birth.substring(4, 6)))
                .birthDay(Integer.parseInt(birth.substring(6)))
                .email(email)
                .signUpDate(LocalDate.now())
                .isWithdrawal(false)
                .isBlocked(false)
                .point(0)
                .memberGrade(memberGrade)
                .memberGenderCode(MemberGenderCode.valueOf(gender))
                .build();
    }
}
