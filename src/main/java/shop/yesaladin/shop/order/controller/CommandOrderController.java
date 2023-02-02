package shop.yesaladin.shop.order.controller;

import java.security.InvalidParameterException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.dto.OrderCreateRequestDto;
import shop.yesaladin.shop.order.dto.OrderCreateResponseDto;
import shop.yesaladin.shop.order.service.inter.CommandOrderService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/orders")
public class CommandOrderController {

    private final CommandOrderService commandOrderService;

    /**
     * 주문을 생성합니다.
     *
     * @param type           주문 타입(회원/비회원/정기구독)
     * @param authentication 인증
     * @return 생성된 주문 정보
     */
    @PostMapping
    public ResponseDto<OrderCreateResponseDto> createOrder(
            @RequestParam String type,
            @RequestBody OrderCreateRequestDto request,
            Authentication authentication
    ) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        OrderCode orderCode = OrderCode.findByType(type)
                .orElseThrow(InvalidParameterException::new);

        OrderCreateResponseDto response = commandOrderService.createOrderWith(orderCode, request, userDetails);

        return ResponseDto.<OrderCreateResponseDto>builder()
                .success(true)
                .status(HttpStatus.CREATED)
                .data(response)
                .build();
    }
}
