package shop.yesaladin.shop.order.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.common.dto.PeriodQueryRequestDto;
import shop.yesaladin.shop.order.dto.OrderSummaryDto;
import shop.yesaladin.shop.order.dto.OrderSummaryResponseDto;
import shop.yesaladin.shop.order.service.inter.QueryOrderService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/member-orders")
public class QueryMemberOrderController {

    private final QueryOrderService queryOrderService;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @GetMapping
    public PaginatedResponseDto<OrderSummaryResponseDto> getAllOrdersByMemberId(
            @RequestParam String startDate,
            @RequestParam String endDate,
            Pageable pageable
    ) {
        // TODO AOP로 멤버 id 가져오기
        Long memberId = 1L;

        Page<OrderSummaryResponseDto> data = queryOrderService.getOrderListInPeriodByMemberId(
                new PeriodQueryRequestDto(LocalDate.parse(startDate,formatter),LocalDate.parse(endDate,formatter)),
                memberId,
                pageable
        );
        return PaginatedResponseDto.<OrderSummaryResponseDto>builder()
                .currentPage(data.getNumber())
                .totalPage(data.getTotalPages())
                .totalDataCount(data.getTotalElements())
                .dataList(data.getContent())
                .build();
    }
}
