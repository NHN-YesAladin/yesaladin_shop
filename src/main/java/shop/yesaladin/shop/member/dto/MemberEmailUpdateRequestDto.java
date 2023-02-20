package shop.yesaladin.shop.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 회원의 이메일 수정을 위해 MemberController에서 받는 요청 DTO 입니다.
 *
 * @author 송학현
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberEmailUpdateRequestDto {

    @Email(message = "이메일 양식을 지켜주세요.")
    @NotBlank(message = "email을 입력해주세요.")
    @Size(max = 100)
    private String email;
}
