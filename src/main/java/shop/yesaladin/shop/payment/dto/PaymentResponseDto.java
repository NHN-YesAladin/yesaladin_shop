package shop.yesaladin.shop.payment.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.payment.domain.model.Payment;
import shop.yesaladin.shop.payment.domain.model.PaymentCardAcquirerCode;
import shop.yesaladin.shop.payment.domain.model.PaymentCode;

/**
 * 결제정보의 대부분을 지니고있는 dto
 *
 * @author 배수한
 * @since 1.0
 */

@Getter
@NoArgsConstructor
public class PaymentResponseDto {

    //결제 기본 정보
    private String paymentId;
    private PaymentCode method;
    private String currency;
    private long paymentTotalAmount;
    private LocalDateTime approvedDateTime;

    //카드 정보 - 카드 결제의 경우 : nullable
    private PaymentCode cardCode;
    private PaymentCode cardOwnerCode;
    private String cardNumber;
    private int cardInstallmentPlanMonths;
    private String cardApproveNumber;
    private PaymentCardAcquirerCode cardAcquirerCode;

    //간편 결제 정보 - 간편 결제일 경우 : nullable
    private String easyPayProvider;
    private long easyPayAmount;
    private long easyPayDiscountAmount;

    public void setEasyPayInfo(Payment payment) {
        setCommonPayment(payment);
        this.easyPayProvider = payment.getPaymentEasyPay().getProvider();
        this.easyPayAmount = payment.getPaymentEasyPay().getAmount();
        this.easyPayDiscountAmount = payment.getPaymentEasyPay().getDiscountAmount();
    }

    public void setCardPayInfo(Payment payment) {
        setCommonPayment(payment);
        this.cardCode = payment.getPaymentCard().getCardCode();
        this.cardOwnerCode = payment.getPaymentCard().getOwnerCode();
        this.cardNumber = payment.getPaymentCard().getNumber();
        this.cardInstallmentPlanMonths = payment.getPaymentCard().getInstallmentPlanMonths();
        this.cardApproveNumber = payment.getPaymentCard().getApproveNo();
        this.cardAcquirerCode = payment.getPaymentCard().getAcquirerCode();
    }

    private void setCommonPayment(Payment payment) {
        this.paymentId = payment.getId();
        this.method = payment.getMethod();
        this.currency = payment.getCurrency();
        this.paymentTotalAmount = payment.getTotalAmount();
        this.approvedDateTime = payment.getApprovedDatetime();
    }

}
