package shop.yesaladin.shop.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
     * 비회원 주문을 생성합니다.
     *
     * @param authentication 인증
     * @return 생성된 주문 정보
     */
    @PostMapping("/non-member")
    public ResponseDto<OrderCreateResponseDto> createNonMemberOrder(
            @RequestBody OrderNonMemberCreateRequestDto request,
            Authentication authentication
    ) {
        checkUnAuthorizedUserDetails(authentication);
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
     * @param authentication 인증
     * @return 생성된 주문 정보
     */
    @PostMapping("/member")
    public ResponseDto<OrderCreateResponseDto> createMemberOrder(
            @RequestBody OrderMemberCreateRequestDto request,
            Authentication authentication
    ) {
        String loginId = getAuthorizedUserDetails(authentication);

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
     * @param authentication 인증
     * @return 생성된 주문 정보
     */
    @PostMapping("/subscribe")
    public ResponseDto<OrderCreateResponseDto> createSubscribeOrder(
            @RequestBody OrderSubscribeCreateRequestDto request,
            Authentication authentication
    ) {
        String loginId = getAuthorizedUserDetails(authentication);

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

    private void checkUnAuthorizedUserDetails(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (!AuthorityUtils.isAnonymous(userDetails)) {
            throw new ClientException(ErrorCode.UNAUTHORIZED, "");
        }
    }

    private static String getAuthorizedUserDetails(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (!AuthorityUtils.isAuthorized(userDetails)) {
            throw new ClientException(ErrorCode.ORDER_BAD_REQUEST, "");
        }
        return userDetails.getUsername();
    }
}
