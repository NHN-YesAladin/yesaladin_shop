package shop.yesaladin.shop.member.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Auth 서버에서 login을 위해 필요한 member 정보를 담은 클래스입니다.
 *
 * @author : 송학현
 * @since : 1.0
 */
@Getter
@AllArgsConstructor
public class MemberLoginResponseDto {

    private Long id;
    private String name;
    private String nickname;
    private String loginId;
    private String email;
    private String password;
    private List<String> roles;
}
