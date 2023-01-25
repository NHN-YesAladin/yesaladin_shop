package shop.yesaladin.shop.payment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.payment.dto.PaymentCompleteSimpleResponseDto;
import shop.yesaladin.shop.payment.service.inter.QueryPaymentService;

/**
 * 결제 정보를 조회하는 api를 제공하기위한 RestController
 *
 * @author 배수한
 * @since 1.0
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/payments")
public class QueryPaymentController {

    private final QueryPaymentService queryPaymentService;

    /**
     * OrderId를 통해 결제정보 조회를 하는 컨트롤러
     *
     * @param orderId 주문 id
     * @return 결제 정보
     */
    @GetMapping(value = "/{orderId}", params = "id=order")
    public PaymentCompleteSimpleResponseDto getPaymentByOrderId(@PathVariable Long orderId) {
        return queryPaymentService.findByOrderId(orderId);
    }

}
