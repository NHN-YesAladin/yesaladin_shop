package shop.yesaladin.shop.payment.domain.model;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
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
    INDIVIDUAL(1, "개인"), COMPANY(2, "법인"),  // 카드 소유자
    CREDIT(3, "신용"), CHECK(4, "체크"),        // 카드 코드
    NORMAL(5, "일반결제"), BILLING(6, "자동결제"),         // 결제 타입 코드
    CARD(7, "카드"), SIMPLE_PAY(8, "간편결제"),     // 결제 수단
    READY(9, "초기 상태"), DONE(10, "종료"),         // 결제 처리 상태 및 결제 매입 상태
    CANCELED(11, "취소"), PARTIAL_CANCELED(12, "부분 취소"),
    ABORTED(13, "승인 실패"), COMPLETED(14, "승인 완료"),
    ;

    private final int code;
    private final String krName;

    public static PaymentCode findByName(String krName) {
        return Arrays.stream(PaymentCode.values())
                .filter(code -> code.getKrName().equals(krName))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    @JsonValue
    public String getKrName() {
        return krName;
    }

    @Override
    public String toString() {
        return krName;
    }
}
