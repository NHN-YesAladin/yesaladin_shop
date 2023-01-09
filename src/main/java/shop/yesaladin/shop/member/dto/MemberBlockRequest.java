package shop.yesaladin.shop.member.dto;

import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원 차단/해지를 위해 MemberController 에서 받는 요청 dto 클래스 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberBlockRequest {

    @NotNull
    private Long id;
    @NotNull
    private Boolean block;
}