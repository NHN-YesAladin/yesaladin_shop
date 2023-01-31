package shop.yesaladin.shop.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.common.dto.PeriodQueryRequestDto;
import shop.yesaladin.shop.order.dto.MemberOrderRequestDto;
import shop.yesaladin.shop.order.dto.MemberOrderResponseDto;
import shop.yesaladin.shop.order.dto.OrderSummaryDto;
import shop.yesaladin.shop.order.service.inter.QueryOrderService;

/**
 * 회원 주문 조회 관련 rest controller 클래스 입니다.
 *
 * @author 최예린
 * @author 김홍대
 * @since 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/orders")
public class QueryOrderController {

    private final QueryOrderService queryOrderService;

    private final String ROLE_USER = "ROLE_USER";

    @GetMapping
    public PaginatedResponseDto<OrderSummaryDto> getAllOrders(
            @RequestBody PeriodQueryRequestDto queryDto, Pageable pageable
    ) {

        Page<OrderSummaryDto> data = queryOrderService.getAllOrderListInPeriod(queryDto, pageable);
        return PaginatedResponseDto.<OrderSummaryDto>builder()
                .currentPage(data.getNumber())
                .totalPage(data.getTotalPages())
                .totalDataCount(data.getTotalElements())
                .dataList(data.getContent())
                .build();
    }

    /**
     * 회원 주문서에 필요한 데이터들을 반환합니다.
     *
     * @param request 주문서 데이터 요청 dto
     * @return 주문서에 필요한 데이터
     * @author 최예린
     * @since 1.0
     */
    @GetMapping("/sheet")
    public ResponseDto<MemberOrderResponseDto> getOrderSheetData(
            @RequestBody MemberOrderRequestDto request,
            Authentication authentication
    ) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        MemberOrderResponseDto response;
        if (checkUserAuthority(userDetails)) {
            String loginId = userDetails.getUsername();
            response = queryOrderService.getMemberOrderSheetData(request, loginId);
        } else {
            response = queryOrderService.getNonMemberOrderSheetData(request);
        }

        return ResponseDto.<MemberOrderResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }

    private boolean checkUserAuthority(UserDetails userDetails) {
        return userDetails.getAuthorities()
                .stream()
                .anyMatch(x -> x.getAuthority().equals(ROLE_USER));
    }

}
