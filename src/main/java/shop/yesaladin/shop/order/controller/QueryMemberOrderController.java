package shop.yesaladin.shop.order.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public PaginatedResponseDto<OrderSummaryResponseDto> getAllOrdersByMemberId(
            @ModelAttribute PeriodQueryRequestDto queryDto,
            Pageable pageable
    ) {
        // TODO AOP로 멤버 id 가져오기
        Long memberId = 1L;
        log.info("startDate: {} | endDate : {}  | pageable : {}", queryDto.getStartDate(), queryDto.getEndDate(), pageable);

        Page<OrderSummaryResponseDto> data = queryOrderService.getOrderListInPeriodByMemberId(
                queryDto,
                memberId,
                pageable
        );

        log.info("{}", data.getContent());

        return PaginatedResponseDto.<OrderSummaryResponseDto>builder()
                .currentPage(data.getNumber())
                .totalPage(data.getTotalPages())
                .totalDataCount(data.getTotalElements())
                .dataList(data.getContent())
                .build();
    }
}
