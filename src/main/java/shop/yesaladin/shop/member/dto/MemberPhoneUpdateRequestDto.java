package shop.yesaladin.shop.member.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 회원의 전화번호 수정을 위해 MemberController에서 받는 요청 DTO 입니다.
 *
 * @author 송학현
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberPhoneUpdateRequestDto {

    @NotBlank
    @Pattern(regexp = "^01([0|1])\\d{4}\\d{4}$", message = "휴대폰 번호를 - 없이 11자 입력해주세요. 앞 자리는 010 또는 011 양식입니다.")
    private String phone;
}
