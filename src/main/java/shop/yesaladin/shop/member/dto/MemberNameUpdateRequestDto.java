package shop.yesaladin.shop.member.dto;

import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 회원의 이름 수정을 위해 MemberController에서 받는 요청 DTO 입니다.
 *
 * @author 송학현
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberNameUpdateRequestDto {

    @Pattern(regexp = "^[가-힣a-zA-Z]{2,50}$", message = "이름은 한글 또는 영어 2자에서 50자까지 입력 가능합니다.")
    private String name;
}
