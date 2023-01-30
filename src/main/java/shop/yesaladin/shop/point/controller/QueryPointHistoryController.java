package shop.yesaladin.shop.point.controller;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.point.domain.model.PointCode;
import shop.yesaladin.shop.point.dto.PointHistoryResponseDto;
import shop.yesaladin.shop.point.dto.PointResponseDto;
import shop.yesaladin.shop.point.service.inter.QueryPointHistoryService;

/**
 * 포인트 조회 관련 api 를 위한 rest controller 클래스 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/points")
public class QueryPointHistoryController {

    private final QueryPointHistoryService queryPointHistoryService;

    /**
     * 회원의 포인트를 조회합니다.
     *
     * @param loginId 회원의 아이디
     * @return 회원의 포인트
     */
    @GetMapping
    @CrossOrigin(origins = {"http://localhost:9090",
            "https://www.yesaladin.shop"})
    public PointResponseDto getMemberPoint(String loginId) {
        return queryPointHistoryService.getMemberPoint(loginId);
    }

    /**
     * 회원의 포인트 내역을 조회합니다.
     *
     * @param loginId  회원의 아이디
     * @param code     사용/적립/전체 구분
     * @param pageable 페이지와 사이즈
     * @return 회원의 포인트 내역
     * @author 최예린
     * @since 1.0
     */
    @GetMapping("/point-histories")
    public PaginatedResponseDto<PointHistoryResponseDto> getPointHistoriesByLoginId(
            String loginId,
            @RequestParam("code") Optional<String> code,
            Pageable pageable
    ) {
        Page<PointHistoryResponseDto> response;

        if (code.isPresent()) {
            PointCode pointCode = PointCode.findByCode(code.get());
            response = queryPointHistoryService.getPointHistoriesWithLoginIdAndCode(
                    loginId,
                    pointCode,
                    pageable
            );
        } else {
            response = queryPointHistoryService.getPointHistoriesWithLoginId(loginId, pageable);
        }

        return PaginatedResponseDto.<PointHistoryResponseDto>builder()
                .totalPage(response.getTotalPages())
                .currentPage(response.getNumber())
                .totalDataCount(response.getTotalElements())
                .dataList(response.getContent())
                .build();
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
    @GetMapping
    public PaginatedResponseDto<PointHistoryResponseDto> getPointHistories(
            @RequestParam("code") Optional<String> code,
            @RequestParam("loginId") Optional<String> loginId,
            Pageable pageable
    ) {

        Page<PointHistoryResponseDto> response = getPointHistoryForManager(code, loginId, pageable);

        return PaginatedResponseDto.<PointHistoryResponseDto>builder()
                .totalPage(response.getTotalPages())
                .currentPage(response.getNumber())
                .totalDataCount(response.getTotalElements())
                .dataList(response.getContent())
                .build();

    }

    private Page<PointHistoryResponseDto> getPointHistoryForManager(
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
