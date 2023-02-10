package shop.yesaladin.shop.order.controller;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.common.aspect.annotation.LoginId;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.common.dto.PeriodQueryRequestDto;
import shop.yesaladin.shop.order.dto.OrderSheetRequestDto;
import shop.yesaladin.shop.order.dto.OrderSheetResponseDto;
import shop.yesaladin.shop.order.dto.OrderSummaryDto;
import shop.yesaladin.shop.order.service.inter.QueryOrderService;

/**
 * 회원 주문 조회 관련 rest controller 클래스 입니다.
 *
 * @author 최예린
 * @author 김홍대
 * @since 1.0
 */
@Slf4j
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
     * @param products 상품 정보
     * @param loginId  회원의 아이디
     * @return 주문서에 필요한 데이터
     * @author 최예린
     * @since 1.0
     */
    @GetMapping("/v1/order-sheets")
    public ResponseDto<OrderSheetResponseDto> getOrderSheetData(
            @ModelAttribute OrderSheetRequestDto products,
            @LoginId String loginId
    ) {
        OrderSheetResponseDto response = (Objects.isNull(loginId)) ?
                queryOrderService.getNonMemberOrderSheetData(products)
                : queryOrderService.getMemberOrderSheetData(products, loginId);

        log.error("coupon list : : {}", response.getMemberCoupons());

        return ResponseDto.<OrderSheetResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }
}
