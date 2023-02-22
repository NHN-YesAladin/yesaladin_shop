package shop.yesaladin.shop.member.dto;

import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 회원의 nickname 수정을 위해 MemberController 에서 받는 요청 DTO 입니다.
 *
 * @author 최예린
 * @author 송학현
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberNicknameUpdateRequestDto {

    @Pattern(regexp = "^[가-힣a-zA-Z]{2,15}$", message = "한글과 영문만 가능 합니다")
    private String nickname;
}
