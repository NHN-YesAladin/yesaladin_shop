package shop.yesaladin.shop.member.dto;

import javax.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원 정보 수정을 위해 MemberController 에서 받는 요청 DTO 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberUpdateRequestDto {

    @Pattern(regexp = "^[가-힣a-zA-Z]{2,15}$", message = "한글과 영문만 가능 합니다")
    private String nickname;
}
