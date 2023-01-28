package shop.yesaladin.shop.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.member.dto.MemberGradeQueryResponseDto;
import shop.yesaladin.shop.member.dto.MemberProfileExistResponseDto;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;

/**
 * 회원 조회에 관련된 RestController 입니다.
 *
 * @author : 송학현
 * @author 최예린
 * @since : 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/members")
public class QueryMemberController {

    private final QueryMemberService queryMemberService;

    /**
     * 회원 가입 시 loginId의 중복 여부를 판별 하기 위한 기능 입니다.
     *
     * @param loginId PathVariable로 넘어온 중복 체크 대상 loginId
     * @return loginId 중복 여부
     * @author : 송학현
     * @since : 1.0
     */
    @GetMapping("/checkId/{loginId}")
    public MemberProfileExistResponseDto existsLoginId(@PathVariable String loginId) {
        return new MemberProfileExistResponseDto(queryMemberService.existsLoginId(loginId));
    }

    /**
     * 회원 가입 시 nickname의 중복 여부를 판별 하기 위한 기능 입니다.
     *
     * @param nickname PathVariable로 넘어온 중복 체크 대상 nickname
     * @return nickname 중복 여부
     * @author : 송학현
     * @since : 1.0
     */
    @GetMapping("/checkNick/{nickname}")
    public MemberProfileExistResponseDto existsNickname(@PathVariable String nickname) {
        return new MemberProfileExistResponseDto(queryMemberService.existsNickname(nickname));
    }

    /**
     * 회원 가입 시 email의 중복 여부를 판별 하기 위한 기능 입니다.
     *
     * @param email PathVariable로 넘어온 중복 체크 대상 email
     * @return email 중복 여부
     * @author : 송학현
     * @since : 1.0
     */
    @GetMapping("/checkEmail/{email}")
    public MemberProfileExistResponseDto existsEmail(@PathVariable String email) {
        return new MemberProfileExistResponseDto(queryMemberService.existsEmail(email));
    }

    /**
     * 회원 가입 시 phone의 중복 여부를 판별 하기 위한 기능 입니다.
     *
     * @param phone PathVariable로 넘어온 중복 체크 대상 phone
     * @return phone 중복 여부
     * @author : 송학현
     * @since : 1.0
     */
    @GetMapping("/checkPhone/{phone}")
    public MemberProfileExistResponseDto existsPhone(@PathVariable String phone) {
        return new MemberProfileExistResponseDto(queryMemberService.existsPhone(phone));
    }

    /**
     * 회원의 등급을 조회합니다.
     *
     * @param loginId 회원의 아이디
     * @return 회원의 등급
     * @author 최예린
     * @since 1.0
     */
    @GetMapping("/{loginId}/grade")
    public MemberGradeQueryResponseDto getMemberGrade(@PathVariable String loginId) {
        return queryMemberService.getMemberGrade(loginId);
    }
}
