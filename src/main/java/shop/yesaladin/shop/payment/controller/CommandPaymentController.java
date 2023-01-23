package shop.yesaladin.shop.payment.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.payment.dto.PaymentCompleteSimpleResponseDto;
import shop.yesaladin.shop.payment.dto.PaymentRequestDto;
import shop.yesaladin.shop.payment.service.inter.CommandPaymentService;

/**
 * 결제 정보, 카드 정보, 취소 정보를 생성,수정,삭제하는 api를 제공하기위한 RestController
 *
 * @author 배수한
 * @since 1.0
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/payments")
public class CommandPaymentController {

    private final CommandPaymentService paymentService;


    @PostMapping("/confirm")
    public PaymentCompleteSimpleResponseDto confirmPayment(@Valid @RequestBody PaymentRequestDto requestDto) {
        log.info("requestDto : {}", requestDto);
        PaymentCompleteSimpleResponseDto paymentCompleteSimpleResponseDto = paymentService.confirmTossRequest(
                requestDto);
        log.info("return : {}", paymentCompleteSimpleResponseDto);

        return paymentCompleteSimpleResponseDto;
    }
}
