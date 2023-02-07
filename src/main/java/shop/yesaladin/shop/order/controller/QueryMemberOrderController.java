package shop.yesaladin.shop.order.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field.Str;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.category.dto.ResultCodeDto;
import shop.yesaladin.shop.common.aspect.annotation.LoginId;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.common.dto.PeriodQueryRequestDto;
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
}
