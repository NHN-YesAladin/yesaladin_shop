package shop.yesaladin.shop.member.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginResponseDto {

    private Long id;
    private String name;
    private String nickname;
    private String loginId;
    private String email;
    private String password;
    private List<String> role;
}
