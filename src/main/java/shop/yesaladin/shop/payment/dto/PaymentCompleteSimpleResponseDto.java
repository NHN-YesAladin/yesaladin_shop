package shop.yesaladin.shop.payment.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 결제 정보에 대한 간략한 정보만 담을 dto
 *
 * @author 배수한
 * @since 1.0
 */

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentCompleteSimpleResponseDto {
    private String paymentId;
    private String method;
    private String currency;
    private Long totalAmount;
    private LocalDateTime approvedDateTime;

    private Long orderId;
    private String orderName;

    private String cardCode;
    private String cardOwnerCode;
    private String cardNumber;
    private Integer cardInstallmentPlanMonths;
    private String cardApproveNumber;
    private String acquirerName;
}
