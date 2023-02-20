package shop.yesaladin.shop.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 회원의 패스워드 수정을 위해 MemberController에서 받는 요청 DTO 입니다.
 *
 * @author 송학현
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberPasswordUpdateRequestDto {

    private String password;
}
