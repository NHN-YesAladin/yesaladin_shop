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

/**
 * 회원 등록을 위해 MemberController 에서 받는 요청 DTO 입니다.
 *
 * @author : 송학현
 * @since : 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberCreateRequestDto {

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
    private String password;

    @NotBlank
    @Size(min = 8, max = 8)
    // TODO: 정규식 수정할 것
    @Pattern(regexp = "^[0-9]{8}")
    private String birth;

    @NotBlank
    private String gender;

    /**
     * MemberCreateRequest DTO를 Member Entity로 변한하기 위한 메서드 입니다.
     *
     * @return RequestDto를 Entity로 변환된 결과 입니다.
     * @author : 송학현
     * @since : 1.0
     */
    public Member toEntity() {
        return Member.builder()
                .name(name)
                .nickname(nickname)
                .loginId(loginId)
                .password(password)
                .birthYear(Integer.parseInt(birth.substring(0, 4)))
                .birthMonth(Integer.parseInt(birth.substring(4, 6)))
                .birthDay(Integer.parseInt(birth.substring(6)))
                .email(email)
                .phone(phone)
                .signUpDate(LocalDate.now())
                .isWithdrawal(false)
                .isBlocked(false)
                .memberGrade(MemberGrade.WHITE)
                .memberGenderCode(MemberGenderCode.valueOf(gender))
                .build();
    }
}