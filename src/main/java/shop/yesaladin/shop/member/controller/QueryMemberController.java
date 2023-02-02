package shop.yesaladin.shop.member.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.dto.MemberGradeQueryResponseDto;
import shop.yesaladin.shop.member.dto.MemberProfileExistResponseDto;
import shop.yesaladin.shop.member.dto.MemberQueryResponseDto;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;

/**
 * 회원 조회에 관련된 RestController 입니다.
 *
 * @author 송학현
 * @author 최예린
 * @since 1.0
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
     * @author 송학현
     * @since 1.0
     */
    @GetMapping("/checkId/{loginId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<MemberProfileExistResponseDto> existsLoginId(@PathVariable String loginId) {
        MemberProfileExistResponseDto response = new MemberProfileExistResponseDto(
                queryMemberService.existsLoginId(loginId));
        return ResponseDto.<MemberProfileExistResponseDto>builder()
                .status(HttpStatus.OK)
                .success(true)
                .data(response)
                .build();
    }

    /**
     * 회원 가입 시 nickname의 중복 여부를 판별 하기 위한 기능 입니다.
     *
     * @param nickname PathVariable로 넘어온 중복 체크 대상 nickname
     * @return nickname 중복 여부
     * @author 송학현
     * @since 1.0
     */
    @GetMapping("/checkNick/{nickname}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<MemberProfileExistResponseDto> existsNickname(@PathVariable String nickname) {
        MemberProfileExistResponseDto response = new MemberProfileExistResponseDto(
                queryMemberService.existsNickname(nickname));
        return ResponseDto.<MemberProfileExistResponseDto>builder()
                .status(HttpStatus.OK)
                .success(true)
                .data(response)
                .build();
    }

    /**
     * 회원 가입 시 email의 중복 여부를 판별 하기 위한 기능 입니다.
     *
     * @param email PathVariable로 넘어온 중복 체크 대상 email
     * @return email 중복 여부
     * @author 송학현
     * @since 1.0
     */
    @GetMapping("/checkEmail/{email}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<MemberProfileExistResponseDto> existsEmail(@PathVariable String email) {
        MemberProfileExistResponseDto response = new MemberProfileExistResponseDto(
                queryMemberService.existsEmail(email));
        return ResponseDto.<MemberProfileExistResponseDto>builder()
                .status(HttpStatus.OK)
                .success(true)
                .data(response)
                .build();
    }

    /**
     * 회원 가입 시 phone의 중복 여부를 판별 하기 위한 기능 입니다.
     *
     * @param phone PathVariable로 넘어온 중복 체크 대상 phone
     * @return phone 중복 여부
     * @author 송학현
     * @since 1.0
     */
    @GetMapping("/checkPhone/{phone}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<MemberProfileExistResponseDto> existsPhone(@PathVariable String phone) {
        MemberProfileExistResponseDto response = new MemberProfileExistResponseDto(
                queryMemberService.existsPhone(phone));
        return ResponseDto.<MemberProfileExistResponseDto>builder()
                .status(HttpStatus.OK)
                .success(true)
                .data(response)
                .build();
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

    /**
     * 회원의 정보를 조회합니다.
     *
     * @param loginId 회원의 아이디
     * @return 회원의 정보
     * @author 최예린
     * @since 1.0
     */
    @GetMapping("{loginId}")
    public MemberQueryResponseDto getMemberInfo(@PathVariable String loginId) {
        return queryMemberService.getByLoginId(loginId);
    }

    @GetMapping(params = {"type=birthday", "laterDays"})
    public List<Member> getBirthdayMember(
            @RequestParam(value = "laterDays", defaultValue = "0") int laterDays
    ) {
        return queryMemberService.findMembersByBirthday(laterDays);
    }
}
