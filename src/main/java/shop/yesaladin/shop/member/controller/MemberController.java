package shop.yesaladin.shop.member.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.dto.MemberCreateRequest;
import shop.yesaladin.shop.member.dto.MemberCreateResponse;
import shop.yesaladin.shop.member.service.inter.CommandMemberService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/members")
public class MemberController {

    private final CommandMemberService commandMemberService;

    @PostMapping
    public ResponseEntity signUpMember(@Valid @RequestBody MemberCreateRequest createDto) {
        Member member = commandMemberService.create(createDto);
        MemberCreateResponse response = MemberCreateResponse.fromEntity(member);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
