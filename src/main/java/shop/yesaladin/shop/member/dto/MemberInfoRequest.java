package shop.yesaladin.shop.member.dto;

import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import shop.yesaladin.shop.member.domain.model.MemberGenderCode;
import shop.yesaladin.shop.member.domain.model.SearchedMember;

/**
 * 엘라스틱 서치에서 회원에 대한 정보 수정에 관한 요청 DTO 이다.
 *
 * @author : 김선홍
 * @since : 1.0
 */

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoRequest {

    @NotBlank
    private Long id;

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    @NotBlank
    @Size(min = 2, max = 15)
    @Pattern(regexp = "^[가-힣a-zA-Z]{2,15}$", message = "한글과 영문만 가능 합니다")
    private String nickname;

    @NotBlank
    @Size(min = 8, max = 15)
    @Pattern(regexp = "^[a-zA-Z]+[0-9]*$", message = "영문(필수)과 숫자(옵션) 순서 로만 가능 합니다")
    private String loginId;

    @Email
    @NotBlank
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(min = 11, max = 11)
    private String phone;

    @NotBlank
    private int birthYear;

    @NotBlank
    @Size(min = 1, max = 12)
    private int birthMonth;

    @NotBlank
    private int birthDay;

    @NotBlank
    private LocalDate signUpDate;

    private LocalDate withdrawalDate;

    private boolean isWithdrawal;

    private boolean isBlocked;

    @NotBlank
    @Size
    private Long point;

    @NotBlank
    private String gender;

    @NotBlank
    private String grade;

    /**
     * MemberInfoRequest DTO를 SearchedMember로 변환하기 위한 메서드 이다.
     */
    public SearchedMember toSearchedMember() {
        return SearchedMember.builder()
                .id(id)
                .name(name)
                .nickname(nickname)
                .loginId(loginId)
                .email(email)
                .phone(phone)
                .birthYear(birthYear)
                .birthMonth(birthMonth)
                .birthDay(birthDay)
                .signUpDate(signUpDate)
                .withdrawalDate(withdrawalDate)
                .isWithdrawal(isWithdrawal)
                .isBlocked(isBlocked)
                .point(point)
                .gender(MemberGenderCode.valueOf(gender))
                .grade(grade)
                .build();
    }
}
