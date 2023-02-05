package shop.yesaladin.shop.order.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.common.dto.PeriodQueryRequestDto;
import shop.yesaladin.shop.order.dto.OrderSheetRequestDto;
import shop.yesaladin.shop.order.dto.OrderSheetResponseDto;
import shop.yesaladin.shop.order.dto.OrderSummaryDto;
import shop.yesaladin.shop.order.dto.OrderSummaryResponseDto;
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
@RequestMapping
public class QueryOrderController {

    private final QueryOrderService queryOrderService;

    /**
     * 회원의 주문을 조회합니다.
     *
     * @param queryDto 조회 기간
     * @param pageable 페이지와 사이즈
     * @return 회원의 주문 목록
     * @author 김홍대
     * @since 1.0
     */
    @GetMapping("/v1/orders")
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
     * @param isbnList       상품의 isbn 리스트
     * @param quantityList   상품의 수량 리스트
     * @param authentication 인증
     * @return 주문서에 필요한 데이터
     * @author 최예린
     * @since 1.0
     */
    @GetMapping("/v1/order-sheets")
    public ResponseDto<OrderSheetResponseDto> getOrderSheetData(
            @RequestParam(value = "isbnList") List<String> isbnList,
            @RequestParam(value = "quantityList") List<Integer> quantityList,
            Authentication authentication
    ) {
        OrderSheetRequestDto request = new OrderSheetRequestDto(isbnList, quantityList);
        OrderSheetResponseDto response = getOrderSheetData(request, authentication);

        return ResponseDto.<OrderSheetResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }

    private OrderSheetResponseDto getOrderSheetData(
            OrderSheetRequestDto request,
            Authentication authentication
    ) {
//        if (Objects.isNull(authentication.getName())) {
        return queryOrderService.getNonMemberOrderSheetData(request);
//        }
//        String loginId = authentication.getName();
//        return queryOrderService.getMemberOrderSheetData(request, loginId);
    }

    private void checkRequestValidation(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ClientException(
                    ErrorCode.ORDER_BAD_REQUEST,
                    "Validation Error in order sheet request."
            );
        }
    }

    @GetMapping("/{memberId}")
    public PaginatedResponseDto<OrderSummaryResponseDto> getAllOrdersByMemberId(
            @PathVariable Long memberId,
            @RequestBody PeriodQueryRequestDto queryDto,
            Pageable pageable
    ) {
        Page<OrderSummaryResponseDto> data = queryOrderService.getOrderListInPeriodByMemberId(
                queryDto,
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
