package shop.yesaladin.shop.member.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.common.dto.PeriodQueryRequestDto;
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
@RequestMapping("/v1/members/{memberId}/grade-histories")
public class QueryMemberGradeHistoryController {

    private final QueryMemberGradeHistoryService queryMemberGradeHistoryService;

    /**
     * 회원의 등급 변경 내역을 조회합니다.
     *
     * @param memberId 회원의 id
     * @return 회원의 등급 변경 내역
     * @author 최예린
     * @since 1.0
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MemberGradeHistoryQueryResponseDto> getMemberGrades(
            @PathVariable Long memberId,
            @RequestBody PeriodQueryRequestDto request
    ) {
        return queryMemberGradeHistoryService.findByMemberId(memberId, request);
    }
}
