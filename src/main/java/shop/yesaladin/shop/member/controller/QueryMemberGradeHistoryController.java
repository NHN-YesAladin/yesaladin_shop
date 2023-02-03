package shop.yesaladin.shop.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.common.dto.PeriodQueryRequestDto;
import shop.yesaladin.shop.common.utils.AuthorityUtils;
import shop.yesaladin.shop.member.dto.MemberGradeHistoryQueryResponseDto;
import shop.yesaladin.shop.member.service.inter.QueryMemberGradeHistoryService;

/**
 * 회원의 등급 변경 내역을 조회하기 위한 rest controller 클래스 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/member-grades")
public class QueryMemberGradeHistoryController {

    private final QueryMemberGradeHistoryService queryMemberGradeHistoryService;

    /**
     * 회원의 등급 변경 내역을 조회합니다.
     *
     * @param request        회원 등급 변경 내역 기간
     * @param pageable       페이지와 사이즈
     * @param authentication 인증
     * @return 회원의 등급 변경 내역
     * @author 최예린
     * @since 1.0
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<PaginatedResponseDto<MemberGradeHistoryQueryResponseDto>> getMemberGrades(
            @RequestBody PeriodQueryRequestDto request,
            Pageable pageable,
            Authentication authentication
    ) {
        String loginId = AuthorityUtils.getAuthorizedUserName(authentication);

        Page<MemberGradeHistoryQueryResponseDto> response = queryMemberGradeHistoryService.getByLoginId(
                loginId,
                request,
                pageable
        );

        return ResponseDto.<PaginatedResponseDto<MemberGradeHistoryQueryResponseDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(PaginatedResponseDto.<MemberGradeHistoryQueryResponseDto>builder()
                        .totalPage(response.getTotalPages())
                        .currentPage(response.getNumber())
                        .totalDataCount(response.getTotalElements())
                        .dataList(response.getContent())
                        .build())
                .build();
    }
}
