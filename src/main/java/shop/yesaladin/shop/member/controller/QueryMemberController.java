package shop.yesaladin.shop.member.controller;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.common.utils.AuthorityUtils;
import shop.yesaladin.shop.member.dto.MemberGradeQueryResponseDto;
import shop.yesaladin.shop.member.dto.MemberManagerListResponseDto;
import shop.yesaladin.shop.member.dto.MemberManagerResponseDto;
import shop.yesaladin.shop.member.dto.MemberIdDto;
import shop.yesaladin.shop.member.dto.MemberProfileExistResponseDto;
import shop.yesaladin.shop.member.dto.MemberQueryResponseDto;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;

/**
 * 회원 조회에 관련된 RestController 입니다.
 *
 * @author 송학현
 * @author 최예린
 * @author 김선홍
 * @since 1.0
 */
@Slf4j
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
     * @param type           조회 정보 구분
     * @param authentication 인증 정보
     * @return 회원의 등급
     * @author 최예린
     * @since 1.0
     */
    @GetMapping(params = "type")
    @CrossOrigin(origins = {"http://localhost:9090", "https://www.yesaladin.shop"})
    public ResponseDto<MemberGradeQueryResponseDto> getMemberGrade(
            @RequestParam String type,
            Authentication authentication
    ) {
        String loginId = AuthorityUtils.getAuthorizedUserName(authentication);

        checkValidTypeParameterForMemberGrade(type);

        MemberGradeQueryResponseDto response = queryMemberService.getMemberGradeByLoginId(loginId);

        return ResponseDto.<MemberGradeQueryResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }

    private static void checkValidTypeParameterForMemberGrade(String type) {
        if (!type.equals("grade")) {
            throw new ClientException(
                    ErrorCode.MEMBER_BAD_REQUEST,
                    "Invalid type parameter for Member about grade"
            );
        }
    }

    /**
     * 회원의 정보를 조회합니다.
     *
     * @param authentication 인증 정보
     * @return 회원의 정보
     * @author 최예린
     * @since 1.0
     */
    @GetMapping
    public ResponseDto<MemberQueryResponseDto> getMemberInfo(Authentication authentication) {
        String loginId = AuthorityUtils.getAuthorizedUserName(authentication);

        MemberQueryResponseDto response = queryMemberService.getByLoginId(loginId);

        return ResponseDto.<MemberQueryResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }

    /**
     * 관리자가 회원의 loginId 를 이용해 회원의 정보를 조회
     *
     * @param loginId 조회할 회원의 loginId
     * @return 조회된 회원의 정보
     * @author 김선홍
     * @since 1.0
     */
    @GetMapping(value = "/manage", params = "loginid")
    public ResponseDto<MemberManagerResponseDto> manageMemberInfoByLoginId(@RequestParam(name = "loginid") String loginId) {
        return ResponseDto.<MemberManagerResponseDto>builder()
                .data(queryMemberService.findMemberManageByLoginId(loginId))
                .status(HttpStatus.OK)
                .success(true)
                .build();
    }

    /**
     * 관리자가 회원의 nickname 을 이용해 회원의 정보를 조회
     *
     * @param nickname 조회할 회원의 nickname
     * @return 조회된 회원의 정보
     * @author 김선홍
     * @since 1.0
     */
    @GetMapping(value = "/manage", params = "nickname")
    public ResponseDto<MemberManagerResponseDto> manageMemberInfoByNickname(@RequestParam String nickname) {
        return ResponseDto.<MemberManagerResponseDto>builder()
                .data(queryMemberService.findMemberManageByNickName(nickname))
                .status(HttpStatus.OK)
                .success(true)
                .build();
    }

    /**
     * 관리자가 회원의 phone 를 이용해 회원의 정보를 조회
     *
     * @param phone 조회할 회원의 phone
     * @return 조회된 회원의 정보
     * @author 김선홍
     * @since 1.0
     */
    @GetMapping(value = "/manage", params = "phone")
    public ResponseDto<MemberManagerResponseDto> manageMemberInfoByPhone(@RequestParam String phone) {
        return ResponseDto.<MemberManagerResponseDto>builder()
                .data(queryMemberService.findMemberManageByPhone(phone))
                .status(HttpStatus.OK)
                .success(true)
                .build();
    }

    /**
     * 관리자가 회원의 name 을 이용해 회원의 정보를 조회
     *
     * @param name 조회할 회원의 name
     * @param pageable 페이지 위치 및 데이터 갯수
     * @return 조회된 회원들의 정보
     * @author 김선홍
     * @since 1.0
     */
    @GetMapping(value = "/manage", params = "name")
    public ResponseDto<MemberManagerListResponseDto> manageMembersInfoByName(
            @RequestParam String name,
            @PageableDefault Pageable pageable
    ) {
        return ResponseDto.<MemberManagerListResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(queryMemberService.findMemberManagesByName(
                        name,
                        pageable.getPageNumber(),
                        pageable.getPageSize()
                ))
                .build();
    }

    /**
     * 관리자가 회원의  signUpDate 을 이용해 회원의 정보를 조회
     *
     * @param signUpDate 조회할 회원의 signUpDate
     * @param pageable 페이지 위치 및 데이터 갯수
     * @return 조회된 회원들의 정보
     * @author 김선홍
     * @since 1.0
     */
    @GetMapping(value = "/manage", params = "signupdate")
    public ResponseDto<MemberManagerListResponseDto> manageMembersInfoBySignUpDate(
            @RequestParam(name = "signupdate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate signUpDate,
            @PageableDefault Pageable pageable
    ) {
        return ResponseDto.<MemberManagerListResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(queryMemberService.findMemberManagesBySignUpDate(
                        signUpDate,
                        pageable.getPageNumber(),
                        pageable.getPageSize()
                )).build();
     }
     
    /**
     * n 일 후가 생일인 회원을 조회합니다.
     *
     * @param laterDays 오늘 날짜를 기준으로 생일을 계산할 일수
     * @return n 일 후가 생일인 회원 목록
     * @author 서민지
     * @since 1.0
     */
    @GetMapping(params = {"type=birthday", "laterDays"})
    public ResponseDto<List<MemberIdDto>> getBirthdayMember(
            @RequestParam(value = "laterDays", defaultValue = "0") int laterDays
    ) {
        List<MemberIdDto> data = queryMemberService.findMemberIdsByBirthday(laterDays);

        return ResponseDto.<List<MemberIdDto>>builder()
                .success(true)
                .data(data)
                .status(HttpStatus.OK)
                .build();
    }
}
