package shop.yesaladin.shop.payment.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 결제코드
 *
 * @author 서민지
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor
public enum PaymentCode {
    INDIVIDUAL(1), COMPANY(2),  // 카드 소유자
    CREDIT(3), CHECK(4),        // 카드 코드
    NORMAL(5), AUTO(6),         // 결제 타입 코드
    CARD(7), SIMPLE_PAY(8),     // 결제 수단
    READY(9), DONE(10),         // 결제 처리 상태 및 결제 매입 상태
    CANCELED(11), PARTIAL_CANCELED(12),
    ABORTED(13), COMPLETED(14),
    ;

    private final int code;
}
