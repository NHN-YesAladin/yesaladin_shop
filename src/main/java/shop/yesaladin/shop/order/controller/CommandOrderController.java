package shop.yesaladin.shop.order.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.common.aspect.annotation.LoginId;
import shop.yesaladin.shop.order.dto.OrderCreateResponseDto;
import shop.yesaladin.shop.order.dto.OrderMemberCreateRequestDto;
import shop.yesaladin.shop.order.dto.OrderNonMemberCreateRequestDto;
import shop.yesaladin.shop.order.dto.OrderSubscribeCreateRequestDto;
import shop.yesaladin.shop.order.dto.OrderUpdateResponseDto;
import shop.yesaladin.shop.order.service.inter.CommandOrderService;

/**
 * 주문을 생성관련 rest controller 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/orders")
public class CommandOrderController {

    private final CommandOrderService commandOrderService;

    /**
     * 비회원 주문을 생성합니다.
     *
     * @param bindingResult 유효성 검사
     * @return 생성된 주문 정보
     */
    @CrossOrigin(origins = {"http://localhost:9090", "https://www.yesaladin.shop"})
    @PostMapping("/non-member")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<OrderCreateResponseDto> createNonMemberOrder(
            @Valid @RequestBody OrderNonMemberCreateRequestDto request,
            BindingResult bindingResult
    ) {
        checkRequestValidation(bindingResult, "NonMemberOrder");

        OrderCreateResponseDto response = commandOrderService.createNonMemberOrders(request);

        return ResponseDto.<OrderCreateResponseDto>builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .data(response)
                .build();
    }


    /**
     * 회원 주문을 생성합니다.
     *
     * @param request       주문 생성 요청 데이터
     * @param bindingResult 유효성 검사
     * @param loginId       회원의 아이디
     * @param type          회원 주문 시 어떤 경로(바로 주문, 장바구니 주문)로 주문하였는지에 대한 유형
     * @return 생성된 주문 정보
     */
    @PostMapping(value = "/member")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<OrderCreateResponseDto> createMemberOrder(
            @Valid @RequestBody OrderMemberCreateRequestDto request,
            BindingResult bindingResult,
            @LoginId(required = true) String loginId,
            @RequestParam(value = "type", required = false) String type
    ) {
        checkRequestValidation(bindingResult, "MemberOrder");

        OrderCreateResponseDto response = commandOrderService.createMemberOrders(
                request,
                loginId,
                type
        );

        return ResponseDto.<OrderCreateResponseDto>builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .data(response)
                .build();
    }

    /**
     * 정기구독 주문을 생성합니다.
     *
     * @param request       주문 생성 요청 데이터
     * @param bindingResult 유효성 검사
     * @param loginId       회원의 아이디
     * @return 생성된 주문 정보
     */
    @PostMapping("/subscribe")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<OrderCreateResponseDto> createSubscribeOrder(
            @Valid @RequestBody OrderSubscribeCreateRequestDto request,
            BindingResult bindingResult,
            @LoginId(required = true) String loginId
    ) {
        checkRequestValidation(bindingResult, "SubscribeOrder");

        OrderCreateResponseDto response = commandOrderService.createSubscribeOrders(
                request,
                loginId
        );

        return ResponseDto.<OrderCreateResponseDto>builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .data(response)
                .build();
    }

    /**
     * 주문을 숨김 또는 숨김해제 합니다.
     *
     * @param orderId 주문 pk
     * @param hide    숨김여부
     * @param loginId 회원의 아이디
     * @return 숨김또는 숨김해제한 주문
     * @author 최예린
     * @since 1.0
     */
    @PutMapping(path = "/{orderId}", params = "hide")
    public ResponseDto<OrderUpdateResponseDto> hide(
            @PathVariable Long orderId,
            @RequestParam Boolean hide,
            @LoginId(required = true) String loginId
    ) {
        OrderUpdateResponseDto response = commandOrderService.hideOnOrder(loginId, orderId, hide);

        return ResponseDto.<OrderUpdateResponseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }

    private void checkRequestValidation(BindingResult bindingResult, String order) {
        if (bindingResult.hasErrors()) {
            throw new ClientException(
                    ErrorCode.ORDER_BAD_REQUEST,
                    "Validation Error in " + order + "." +
                            bindingResult.getAllErrors()
            );
        }
    }
}
