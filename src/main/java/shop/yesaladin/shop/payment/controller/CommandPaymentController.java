package shop.yesaladin.shop.payment.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.payment.dto.PaymentCompleteSimpleResponseDto;
import shop.yesaladin.shop.payment.dto.PaymentRequestDto;
import shop.yesaladin.shop.payment.exception.PaymentFailException;
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
     * <p>
     * 결제 실패시 : success를 false로 두고 프론트에서 다시 결제 하기 화면을 보여줄 예정
     * </p>
     *
     * @param requestDto 결제 요청 정보가 담긴 dto
     * @return 결제 정보
     */
    @PostMapping("/confirm")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<PaymentCompleteSimpleResponseDto> confirmPayment(
            @Valid @RequestBody PaymentRequestDto requestDto
    ) {
        try {
            PaymentCompleteSimpleResponseDto response = paymentService.confirmTossRequest(requestDto);
            return ResponseDto.<PaymentCompleteSimpleResponseDto>builder()
                    .status(HttpStatus.OK)
                    .success(true)
                    .data(response)
                    .build();
        } catch (PaymentFailException e) {
            // exception ignore
            return ResponseDto.<PaymentCompleteSimpleResponseDto>builder()
                    .status(HttpStatus.OK)
                    .success(false)
                    .data(PaymentCompleteSimpleResponseDto.fromRequestDto(requestDto))
                    .errorMessages(List.of(e.getMessage(), e.getCode()))
                    .build();
        } catch (Exception e) {
            // exception ignore
            return ResponseDto.<PaymentCompleteSimpleResponseDto>builder()
                    .status(HttpStatus.OK)
                    .success(false)
                    .data(PaymentCompleteSimpleResponseDto.fromRequestDto(requestDto))
                    .errorMessages(List.of(e.getMessage()))
                    .build();
        }

    }
}
