package shop.yesaladin.shop.member.controller;

import java.net.URI;
import java.net.URISyntaxException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.member.dto.MemberCreateRequest;
import shop.yesaladin.shop.member.dto.MemberCreateResponse;
import shop.yesaladin.shop.member.service.inter.CommandMemberService;

/**
 * 회원에 관련된 RestController 입니다.
 *
 * @author : 송학현
 * @since : 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/members")
public class CommandMemberController {

    private final CommandMemberService commandMemberService;

    /**
     * 회원 가입을 위한 Post 요청을 처리 하는 기능 입니다.
     *
     * @param createDto 회원 가입을 위한 요청 파라미터의 모음입니다.
     * @return ResponseEntity로 회원 등록 성공 이후 등록된 일부 데이터들을 반환합니다.
     * @author : 송학현
     * @since : 1.0
     */
    @PostMapping
    public ResponseEntity signUpMember(@Valid @RequestBody MemberCreateRequest createDto)
            throws URISyntaxException {
        MemberCreateResponse response = commandMemberService.create(createDto);

        return ResponseEntity.created(new URI(response.getId().toString())).body(response);
    }
}
