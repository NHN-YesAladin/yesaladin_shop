package shop.yesaladin.shop.order.controller;

import java.time.LocalDateTime;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.category.dto.ResultCodeDto;
import shop.yesaladin.shop.common.aspect.annotation.LoginId;
import shop.yesaladin.shop.order.domain.model.OrderStatusCode;
import shop.yesaladin.shop.order.dto.NonMemberRequestDto;
import shop.yesaladin.shop.order.dto.OrderStatusChangeLogResponseDto;
import shop.yesaladin.shop.order.service.inter.CommandOrderStatusChangeLogService;

/**
 * 주문 상태 변경 내역 생성과 관련한 rest controller 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/orders/{orderId}")
public class CommandOrderStatusChangeLogController {

    private final CommandOrderStatusChangeLogService commandOrderStatusChangeLogService;

    /**
     * 회원의 주문 상태 변경 내역을 생성합니다.
     *
     * @param orderId 주문 pk
     * @param status  주문 상태
     * @param loginId 회원의 아이디
     * @return 생성된 주문 상태 변경 내역
     */
    @PostMapping(params = "status")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<OrderStatusChangeLogResponseDto> changeMemberOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String status,
            @LoginId String loginId
    ) {
        OrderStatusCode orderStatus = getValidOrderStatus(status);

        OrderStatusChangeLogResponseDto response = commandOrderStatusChangeLogService.createMemberOrderStatusChangeLog(
                orderId,
                loginId,
                orderStatus
        );

        return ResponseDto.<OrderStatusChangeLogResponseDto>builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .data(response)
                .build();
    }

    /**
     * 비회원의 주문 상태 변경 내역을 생성합니다.
     *
     * @param orderId       주문 pk
     * @param status        주문 상태
     * @param request       비회원 데이터
     * @param bindingResult 유효성 검사
     * @return 생성된 주문 상태 변경 내역
     */
    @PostMapping(path = "/non-member", params = "status")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<OrderStatusChangeLogResponseDto> changeNonMemberOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String status,
            @Valid @RequestBody NonMemberRequestDto request,
            BindingResult bindingResult
    ) {
        checkRequestValidation(bindingResult);

        OrderStatusCode orderStatus = getValidOrderStatus(status);

        OrderStatusChangeLogResponseDto response = commandOrderStatusChangeLogService.createNonMemberOrderStatusChangeLog(
                orderId,
                request,
                orderStatus
        );

        return ResponseDto.<OrderStatusChangeLogResponseDto>builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .data(response)
                .build();
    }

    @PostMapping("/delivery-complete")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<ResultCodeDto> changeDeliveryCompleteStatusByOrderId(@PathVariable Long orderId) {
        commandOrderStatusChangeLogService.appendOrderStatusChangeLogByOrderId(LocalDateTime.now()
                .plusSeconds(1), orderId, OrderStatusCode.COMPLETE);
        return ResponseDto.<ResultCodeDto>builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .data(new ResultCodeDto("Success"))
                .build();
    }

    /**
     * 주문상태내역을 생성하기 위한 status 파라미터가 유효한지 검사합니다.
     *
     * @param status 상태 파라미터 값
     * @return 유효한 주문상태코드
     * @author 최예린
     * @since 1.0
     */
    private OrderStatusCode getValidOrderStatus(String status) {
        return OrderStatusCode.findByStatus(status)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.ORDER_INVALID_PARAMETER,
                        "Invalid parameter status with " + status
                ));
    }

    private void checkRequestValidation(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ClientException(
                    ErrorCode.BAD_REQUEST,
                    "Validation Error in non member's request create order status change log."
                            + bindingResult.getAllErrors()
            );
        }
    }
}
