package shop.yesaladin.shop.order.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.common.aspect.annotation.LoginId;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.common.dto.PeriodQueryRequestDto;
import shop.yesaladin.shop.order.domain.model.OrderStatusCode;
import shop.yesaladin.shop.order.dto.OrderStatusResponseDto;
import shop.yesaladin.shop.order.dto.OrderSummaryResponseDto;
import shop.yesaladin.shop.order.service.inter.QueryOrderService;

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

    @GetMapping
    public ResponseDto<PaginatedResponseDto<OrderSummaryResponseDto>> getAllOrdersByMemberId(
            @LoginId(required = true) String loginId,
            @ModelAttribute PeriodQueryRequestDto queryDto,
            Pageable pageable
    ) {
//        String loginId = "id0"; //TODO 테스트용
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

    @GetMapping(params = "status")
    public ResponseDto<PaginatedResponseDto<OrderStatusResponseDto>> getOrdersByStatusAndLoginId(
            @LoginId(required = true) String loginId,
            @RequestParam("status") Long status,
            Pageable pageable
    ) {
//        String loginId = "id0"; //TODO 테스트용

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

}
