package shop.yesaladin.shop.payment.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import shop.yesaladin.shop.payment.domain.model.Payment;
import shop.yesaladin.shop.payment.domain.model.PaymentCardAcquirerCode;
import shop.yesaladin.shop.payment.domain.model.PaymentCode;

/**
 * 결제 정보에 대한 간략한 정보만 담을 dto
 *
 * @author 배수한
 * @since 1.0
 */

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCompleteSimpleResponseDto {
    private String paymentId;
    private PaymentCode method;
    private String currency;
    private long totalAmount;
    private LocalDateTime approvedDateTime;

    private Long orderId;
    private String orderName;

    private PaymentCode cardCode;
    private PaymentCode cardOwnerCode;
    private String cardNumber;
    private int cardInstallmentPlanMonths;
    private String cardApproveNumber;
    private PaymentCardAcquirerCode cardAcquirerCode;

    public static PaymentCompleteSimpleResponseDto fromEntity(Payment payment) {
       return PaymentCompleteSimpleResponseDto.builder()
                .paymentId(payment.getId())
                .method(payment.getMethod())
                .currency(payment.getCurrency())
                .totalAmount(payment.getTotalAmount())
                .approvedDateTime(payment.getApprovedDatetime())
                .orderId(payment.getOrder().getId())
                .orderName(payment.getOrder().getName())
                .cardCode(payment.getPaymentCard().getCardCode())
                .cardOwnerCode(payment.getPaymentCard().getOwnerCode())
                .cardNumber(payment.getPaymentCard().getNumber())
                .cardInstallmentPlanMonths(payment.getPaymentCard().getInstallmentPlanMonths())
                .cardApproveNumber(payment.getPaymentCard().getApproveNo())
                .cardAcquirerCode(payment.getPaymentCard().getAcquirerCode())
                .build();

    }
}
