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
    NORMAL(5), AUTO(6);         // 결제 타입 코드

    private final int code;
}