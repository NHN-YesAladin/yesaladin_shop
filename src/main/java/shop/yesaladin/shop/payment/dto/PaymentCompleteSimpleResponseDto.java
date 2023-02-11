package shop.yesaladin.shop.payment.dto;

import java.time.LocalDateTime;
import java.util.Objects;
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

    //결제 기본 정보
    private String paymentId;
    private PaymentCode method;
    private String currency;
    private long totalAmount;
    private LocalDateTime approvedDateTime;

    //주문 정보
    private String ordererName;
    private String orderNumber;
    private String orderName;

    //배송 정보
    private String recipientName;
    private String recipientPhoneNumber;
    private String orderAddress;

    //카드 정보 - 카드 결제의 경우
    private PaymentCode cardCode;
    private PaymentCode cardOwnerCode;
    private String cardNumber;
    private int cardInstallmentPlanMonths;
    private String cardApproveNumber;
    private PaymentCardAcquirerCode cardAcquirerCode;

    //간편 결제 정보 - 간편 결제일 경우
    private String easyPayProvider;
    private long easyPayAmount;
    private long easyPayDiscountAmount;

    public static PaymentCompleteSimpleResponseDto fromEntityByCard(Payment payment) {
        return PaymentCompleteSimpleResponseDto.builder()
                .paymentId(payment.getId())
                .method(payment.getMethod())
                .currency(payment.getCurrency())
                .totalAmount(payment.getTotalAmount())
                .approvedDateTime(payment.getApprovedDatetime())
                .orderNumber(payment.getOrder().getOrderNumber())
                .orderName(payment.getOrder().getName())
                .cardCode(payment.getPaymentCard().getCardCode())
                .cardOwnerCode(payment.getPaymentCard().getOwnerCode())
                .cardNumber(payment.getPaymentCard().getNumber())
                .cardInstallmentPlanMonths(payment.getPaymentCard().getInstallmentPlanMonths())
                .cardApproveNumber(payment.getPaymentCard().getApproveNo())
                .cardAcquirerCode(payment.getPaymentCard().getAcquirerCode())
                .build();
    }

    public static PaymentCompleteSimpleResponseDto fromEntityByEasyPay(Payment payment) {
        return PaymentCompleteSimpleResponseDto.builder()
                .paymentId(payment.getId())
                .method(payment.getMethod())
                .currency(payment.getCurrency())
                .totalAmount(payment.getTotalAmount())
                .approvedDateTime(payment.getApprovedDatetime())
                .orderNumber(payment.getOrder().getOrderNumber())
                .orderName(payment.getOrder().getName())
                .easyPayProvider(payment.getPaymentEasyPay().getProvider())
                .easyPayAmount(payment.getPaymentEasyPay().getAmount())
                .easyPayDiscountAmount(payment.getPaymentEasyPay().getDiscountAmount())
                .build();
    }

    public static PaymentCompleteSimpleResponseDto fromRequestDto(PaymentRequestDto request) {
        return PaymentCompleteSimpleResponseDto.builder()
                .paymentId(request.getPaymentKey())
                .totalAmount(request.getAmount())
                .orderNumber(request.getOrderId())
                .build();
    }

    public void setUserInfo(
            String ordererName,
            String orderAddress,
            String recipientName,
            String recipientPhoneNumber
    ) {
        if (Objects.nonNull(ordererName)) {
            this.ordererName = ordererName;
        }
        if (Objects.nonNull(orderAddress)) {
            this.orderAddress = orderAddress;
        }
        if (Objects.nonNull(recipientName)) {
            this.recipientName = recipientName;
        }
        if (Objects.nonNull(recipientPhoneNumber)) {
            this.recipientPhoneNumber = recipientPhoneNumber;
        }
    }

}
