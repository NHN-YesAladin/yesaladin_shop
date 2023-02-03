package shop.yesaladin.shop.order.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.common.utils.AuthorityUtils;
import shop.yesaladin.shop.order.dto.OrderCreateResponseDto;
import shop.yesaladin.shop.order.dto.OrderMemberCreateRequestDto;
import shop.yesaladin.shop.order.dto.OrderNonMemberCreateRequestDto;
import shop.yesaladin.shop.order.dto.OrderSubscribeCreateRequestDto;
import shop.yesaladin.shop.order.service.inter.CommandOrderService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/orders")
public class CommandOrderController {

    private final CommandOrderService commandOrderService;

    /**
     * 비회원 주문을 음
     *
     * @param bindingResult  유효성 검사
     * @param authentication 인증
     * @return 생성된 주문 정보
     */
    @PostMapping("/non-member")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<OrderCreateResponseDto> createNonMemberOrder(
            @Valid @RequestBody OrderNonMemberCreateRequestDto request,
            BindingResult bindingResult,
            Authentication authentication
    ) {
        checkRequestValidation(bindingResult, "NonMemberOrder");

        AuthorityUtils.checkAnonymousClient(authentication);

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
     * @param request        주문 생성 요청 데이터
     * @param bindingResult  유효성 검사
     * @param authentication 인증
     * @return 생성된 주문 정보
     */
    @PostMapping("/member")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<OrderCreateResponseDto> createMemberOrder(
            @Valid @RequestBody OrderMemberCreateRequestDto request,
            BindingResult bindingResult,
            Authentication authentication
    ) {
        checkRequestValidation(bindingResult, "MemberOrder");

        String loginId = AuthorityUtils.getAuthorizedUserName(authentication);

        OrderCreateResponseDto response = commandOrderService.createMemberOrders(request, loginId);

        return ResponseDto.<OrderCreateResponseDto>builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .data(response)
                .build();
    }

    /**
     * 정기구독 주문을 생성합니다.
     *
     * @param request        주문 생성 요청 데이터
     * @param bindingResult  유효성 검사
     * @param authentication 인증
     * @return 생성된 주문 정보
     */
    @PostMapping("/subscribe")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<OrderCreateResponseDto> createSubscribeOrder(
            @Valid @RequestBody OrderSubscribeCreateRequestDto request,
            BindingResult bindingResult,
            Authentication authentication
    ) {
        checkRequestValidation(bindingResult, "SubscribeOrder");

        String loginId = AuthorityUtils.getAuthorizedUserName(authentication);

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