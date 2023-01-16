package shop.yesaladin.shop.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.member.dto.MemberLoginResponseDto;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;

/**
 * 회원의 login 처리를 위해 Auth 서버와 통신하는 API Controller 입니다.
 *
 * @author : 송학현
 * @since : 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/members")
public class QueryMemberLoginController {

    private final QueryMemberService queryMemberService;

    /**
     * 회원의 loginId를 기준으로 회원의 정보와 권한 정보를 조회합니다.
     *
     * @param loginId 회원의 loginId
     * @return Auth 서버에서 필요한 회원의 정보와 권한 정보를 담은 DTO
     * @author : 송학현
     * @since : 1.0
     */
    @GetMapping("/login/{loginId}")
    public ResponseEntity doLogin(@PathVariable String loginId) {
        MemberLoginResponseDto response = queryMemberService.findMemberLoginInfoByLoginId(
                loginId);
        return ResponseEntity.ok(response);
    }
}
