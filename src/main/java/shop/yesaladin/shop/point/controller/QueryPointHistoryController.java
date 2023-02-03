package shop.yesaladin.shop.point.controller;

import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.common.utils.AuthorityUtils;
import shop.yesaladin.shop.point.domain.model.PointCode;
import shop.yesaladin.shop.point.dto.PointHistoryResponseDto;
import shop.yesaladin.shop.point.service.inter.QueryPointHistoryService;

/**
 * 포인트 조회 관련 api 를 위한 rest controller 클래스 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@RequiredArgsConstructor
@RestController
public class QueryPointHistoryController {

    private final QueryPointHistoryService queryPointHistoryService;

    /**
     * 회원의 포인트를 조회합니다.
     *
     * @param authentication 인증
     * @return 회원의 포인트
     * @author 최예린
     * @since 1.0
     */
    @GetMapping("/v1/points")
    @CrossOrigin(origins = {"http://localhost:9090", "https://www.yesaladin.shop"})
    public ResponseDto<Long> getMemberPoint(Authentication authentication) {
        String loginId = AuthorityUtils.getAuthorizedUserName(authentication);

        long memberPoint = queryPointHistoryService.getMemberPoint(loginId);

        return ResponseDto.<Long>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(memberPoint)
                .build();
    }

    /**
     * 회원의 포인트 내역을 조회합니다.
     *
     * @param code           사용/적립/전체 구분
     * @param pageable       페이지와 사이즈
     * @param authentication 인증
     * @return 회원의 포인트 내역
     * @author 최예린
     * @since 1.0
     */
    @GetMapping(path = "/v1/point-histories")
    public ResponseDto<PaginatedResponseDto<PointHistoryResponseDto>> getPointHistories(
            @RequestParam(required = false) String code,
            @PageableDefault Pageable pageable,
            Authentication authentication
    ) {
        String loginId = AuthorityUtils.getAuthorizedUserName(authentication);

        Page<PointHistoryResponseDto> response = getPointHistoriesWith(
                code,
                pageable,
                loginId
        );

        return ResponseDto.<PaginatedResponseDto<PointHistoryResponseDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(PaginatedResponseDto.<PointHistoryResponseDto>builder()
                        .totalPage(response.getTotalPages())
                        .currentPage(response.getNumber())
                        .totalDataCount(response.getTotalElements())
                        .dataList(response.getContent())
                        .build())
                .build();
    }

    private Page<PointHistoryResponseDto> getPointHistoriesWith(
            String code,
            Pageable pageable,
            String loginId
    ) {
        if (!Objects.isNull(code)) {
            return queryPointHistoryService.getPointHistoriesWithLoginIdAndCode(
                    loginId,
                    PointCode.findByCode(code),
                    pageable
            );
        }
        return queryPointHistoryService.getPointHistoriesWithLoginId(loginId, pageable);
    }

    /**
     * 관리자용 포인트 내역을 조회합니다.
     *
     * @param loginId  회원의 아이디
     * @param code     사용/적립/전체 구분
     * @param pageable 페이지와 사이즈
     * @return 회원의 포인트 내역
     * @author 최예린
     * @since 1.0
     */
    @GetMapping("/manager")
    public PaginatedResponseDto<PointHistoryResponseDto> getPointHistoriesForManager(
            @RequestParam("code") Optional<String> code,
            @RequestParam("loginId") Optional<String> loginId,
            Pageable pageable
    ) {
        Page<PointHistoryResponseDto> response = getPointHistoryListForManager(
                code,
                loginId,
                pageable
        );

        return PaginatedResponseDto.<PointHistoryResponseDto>builder()
                .totalPage(response.getTotalPages())
                .currentPage(response.getNumber())
                .totalDataCount(response.getTotalElements())
                .dataList(response.getContent())
                .build();

    }

    private Page<PointHistoryResponseDto> getPointHistoryListForManager(
            Optional<String> code,
            Optional<String> loginId,
            Pageable pageable
    ) {
        Page<PointHistoryResponseDto> result;

        if (code.isPresent()) {
            PointCode pointCode = PointCode.findByCode(code.get());
            if (loginId.isPresent()) {
                result = queryPointHistoryService.getPointHistoriesWithLoginIdAndCode(
                        loginId.get(),
                        pointCode,
                        pageable
                );
            } else {
                result = queryPointHistoryService.getPointHistoriesWithCode(pointCode, pageable);
            }
        } else if (loginId.isPresent()) {
            result = queryPointHistoryService.getPointHistoriesWithLoginId(loginId.get(), pageable);
        } else {
            result = queryPointHistoryService.getPointHistories(pageable);
        }

        return result;
    }
}
