package shop.yesaladin.shop.payment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
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
//TODO security에서 처리 가능한지 확인 필요
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class QueryPaymentController {

    private final QueryPaymentService queryPaymentService;

    @GetMapping(value = "/{orderId}",params = "id=order")
    public PaymentCompleteSimpleResponseDto getPaymentByOrderId(@PathVariable Long orderId) {
        return queryPaymentService.findByOrderId(orderId);
    }

    //TODO /v1/payments/1 -> 은 페이먼트 아이디 용으로 두는게 나을듯
}
