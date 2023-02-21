package shop.yesaladin.shop.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 회원의 패스워드 변경 시 패스워드 확인을 위한 요청 결과를 담은 Response DTO
 *
 * @author 송학현
 * @since 1.0
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberPasswordResponseDto {

    private String password;
}
