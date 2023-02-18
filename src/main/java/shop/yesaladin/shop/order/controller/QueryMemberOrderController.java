package shop.yesaladin.shop.order.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.common.aspect.annotation.LoginId;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.common.dto.PeriodQueryRequestDto;
import shop.yesaladin.shop.order.domain.model.OrderStatusCode;
import shop.yesaladin.shop.order.dto.OrderStatusResponseDto;
import shop.yesaladin.shop.order.dto.OrderSummaryResponseDto;
import shop.yesaladin.shop.order.service.inter.QueryOrderService;

import java.util.Map;

/**
 * 멤버 주문에 대한 조회를 관장하는 컨트롤러
 *
 * @author 배수한
 * @since 1.0
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/member-orders")
public class QueryMemberOrderController {

    private final QueryOrderService queryOrderService;

    /**
     * 회원 전체 주문 조회
     *
     * @param loginId  로그인 id
     * @param queryDto 조회할 날짜가 있는 dto
     * @param pageable 페이징 객체
     * @return 페이징 된 주문 정보
     */
    @GetMapping
    public ResponseDto<PaginatedResponseDto<OrderSummaryResponseDto>> getAllOrdersByMemberId(
            @LoginId(required = true) String loginId,
            @ModelAttribute PeriodQueryRequestDto queryDto,
            Pageable pageable
    ) {
        Page<OrderSummaryResponseDto> data = queryOrderService.getOrderListInPeriodByMemberId(
                queryDto,
                loginId,
                pageable
        );

        PaginatedResponseDto<OrderSummaryResponseDto> paginatedResponseDto = PaginatedResponseDto.<OrderSummaryResponseDto>builder()
                .currentPage(data.getNumber())
                .totalPage(data.getTotalPages())
                .totalDataCount(data.getTotalElements())
                .dataList(data.getContent())
                .build();

        return ResponseDto.<PaginatedResponseDto<OrderSummaryResponseDto>>builder()
                .status(HttpStatus.OK)
                .success(true)
                .data(paginatedResponseDto)
                .build();
    }

    /**
     * 주문 상태에 따른 주문 조회
     *
     * @param loginId  로그인 id
     * @param status   주문 상태
     * @param pageable 페이징 객체
     * @return 페이징된 주문 상태를 기반하는 주문 정보
     */
    @GetMapping(params = "status")
    public ResponseDto<PaginatedResponseDto<OrderStatusResponseDto>> getOrdersByStatusAndLoginId(
            @LoginId(required = true) String loginId,
            @RequestParam("status") Long status,
            Pageable pageable
    ) {

        OrderStatusCode code = OrderStatusCode.getOrderStatusCodeByNumber(status);

        Page<OrderStatusResponseDto> data = queryOrderService.getStatusResponsesByLoginIdAndStatus(
                loginId,
                code,
                pageable
        );

        PaginatedResponseDto<OrderStatusResponseDto> paginatedResponseDto = PaginatedResponseDto.<OrderStatusResponseDto>builder()
                .currentPage(data.getNumber())
                .totalPage(data.getTotalPages())
                .totalDataCount(data.getTotalElements())
                .dataList(data.getContent())
                .build();

        return ResponseDto.<PaginatedResponseDto<OrderStatusResponseDto>>builder()
                .status(HttpStatus.OK)
                .success(true)
                .data(paginatedResponseDto)
                .build();
    }

    /**
     * 주문 상태에 따른 주문 개수를 조회
     *
     * @param loginId 로그인 id
     * @return 주문 상태 코드 & 그에 기반한 주문 개수
     */
    @GetMapping(params = "status-count")
    public ResponseDto<Map<OrderStatusCode, Long>> getOrderCountsByStatusAndLoginId(@LoginId(required = true) String loginId) {

        Map<OrderStatusCode, Long> orderCountByLoginIdStatus = queryOrderService.getOrderCountByLoginIdStatus(
                loginId);
        return ResponseDto.<Map<OrderStatusCode, Long>>builder()
                .status(HttpStatus.OK)
                .success(true)
                .data(orderCountByLoginIdStatus)
                .build();
    }

    /**
     * 숨김처리된 주문 내역을 조회합니다.
     *
     * @param pageable 페이지와 사이즈
     * @param hidden   숨김여부
     * @param loginId  회원의 아이디
     * @return 회원의 숨김처리된 주문 목록
     * @author 최예린
     * @since 1.0
     */
    @GetMapping(params = "hidden")
    public ResponseDto<PaginatedResponseDto<OrderSummaryResponseDto>> getHiddenOrderByLoginId(
            @PageableDefault Pageable pageable,
            @RequestParam Boolean hidden,
            @LoginId(required = true) String loginId
    ) {
        Page<OrderSummaryResponseDto> response = queryOrderService.getHiddenOrderByLoginId(
                loginId,
                pageable
        );

        return ResponseDto.<PaginatedResponseDto<OrderSummaryResponseDto>>builder()
                .status(HttpStatus.OK)
                .success(true)
                .data(PaginatedResponseDto.<OrderSummaryResponseDto>builder()
                        .currentPage(response.getNumber())
                        .totalPage(response.getTotalPages())
                        .totalDataCount(response.getTotalElements())
                        .dataList(response.getContent())
                        .build())
                .build();
    }
}
