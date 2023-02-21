package shop.yesaladin.shop.order.controller;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.common.aspect.annotation.LoginId;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.common.dto.PeriodQueryRequestDto;
import shop.yesaladin.shop.order.dto.BestsellerResponseDto;
import shop.yesaladin.shop.order.dto.OrderDetailsResponseDto;
import shop.yesaladin.shop.order.dto.OrderSheetRequestDto;
import shop.yesaladin.shop.order.dto.OrderSheetResponseDto;
import shop.yesaladin.shop.order.dto.OrderSummaryDto;
import shop.yesaladin.shop.order.dto.SalesStatisticsResponseDto;
import shop.yesaladin.shop.order.service.inter.QueryOrderService;

/**
 * 전체 주문 조회 관련 rest controller 클래스 입니다.
 *
 * @author 최예린
 * @author 김홍대
 * @author 이수정
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

        return ResponseDto.<OrderSheetResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }

    /**
     * 주문 상세 조회를 위한 컨트롤러 메서드
     *
     * @param orderNumber 조회할 주문 번호
     * @return 주문 상세 조회 관련 정보
     */
    @GetMapping("/v1/orders/{orderNumber}")
    public ResponseDto<OrderDetailsResponseDto> getOrderDetails(
            @PathVariable String orderNumber,
            @RequestParam(value = "type", required = false) String type
    ) {
        // 비회원 주문 번호 조회로 회원 주문번호 조회했을 경우, 권한 없음 예외 발생
        if (Objects.equals(type, "none") && queryOrderService.isMemberOrder(orderNumber)) {
            throw new ClientException(
                    ErrorCode.FORBIDDEN,
                    ErrorCode.FORBIDDEN.getDisplayName() + " 회원 주문번호는 비회원 주문 조회에서는 조회가 불가합니다."
            );
        }

        OrderDetailsResponseDto response = queryOrderService.getDetailsDtoByOrderNumber(
                orderNumber);

        return ResponseDto.<OrderDetailsResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }

    /**
     * [GET /orders/statistics] 요청을 받아 정해진 기간의 매출 통계 정보를 조회하여 반환합니다.
     *
     * @param start 시작일
     * @param end   종료일
     * @return 매출 통계 정보
     * @author 이수정
     * @since 1.0
     */
    @GetMapping("/v1/orders/statistics")
    public ResponseDto<PaginatedResponseDto<SalesStatisticsResponseDto>> getSalesStatistics(
            @RequestParam String start,
            @RequestParam String end,
            Pageable pageable
    ) {

        PaginatedResponseDto<SalesStatisticsResponseDto> response = queryOrderService.getSalesStatistics(
                start,
                end,
                pageable
        );

        return ResponseDto.<PaginatedResponseDto<SalesStatisticsResponseDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }

    /**
     * [GET /bestseller] 요청을 받아 지난 1년간의 베스트셀러를 조회합니다.
     *
     * @return 조회된 1년 기준 베스트셀러
     * @author 이수정
     * @since 1.0
     */
    @GetMapping("/v1/bestseller")
    public ResponseDto<List<BestsellerResponseDto>> getBestseller() {
        return ResponseDto.<List<BestsellerResponseDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(queryOrderService.getBestseller())
                .build();
    }

}
