package shop.yesaladin.shop.payment.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.payment.domain.model.Payment;
import shop.yesaladin.shop.payment.domain.model.PaymentCardAcquirerCode;
import shop.yesaladin.shop.payment.domain.model.PaymentCode;
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

    /**
     * shop서버로 부터 승인 요청이 들어와 toss로 부터 승인을 받고 db에 결제 정보를 저장하는 기능을 가진 컨트롤러
     *
     * @param requestDto 결제 요청 정보가 담긴 dto
     * @return 결제 정보
     */
    @PostMapping("/confirm")
    public PaymentCompleteSimpleResponseDto confirmPayment(@Valid @RequestBody PaymentRequestDto requestDto) {
        // TODO PaymentRequestDto에 orderResponse 추가하고 orderResponse가 null 아닌 경우에 동작시키기
        return PaymentCompleteSimpleResponseDto.builder()
                .paymentId("dummy paymentId")
                .method(PaymentCode.CARD)
                .currency(Payment.CURRENCY_KRW)
                .totalAmount(10000000L)
                .approvedDateTime(LocalDateTime.now())
                .orderNumber("10101010010101010")
                .orderName("dummy orderName")
                .cardCode(PaymentCode.CREDIT)
                .cardOwnerCode(PaymentCode.INDIVIDUAL)
                .cardNumber("1111-xxxx-xxxx-1111")
                .cardInstallmentPlanMonths(0)
                .cardApproveNumber("010023289475832")
                .cardAcquirerCode(PaymentCardAcquirerCode.BC)
                .build();

//        return paymentService.confirmTossRequest(requestDto);
    }
}
