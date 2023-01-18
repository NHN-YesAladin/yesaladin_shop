package shop.yesaladin.shop.payment.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 결제 카드 매입사 코드
 *
 * @author 배수한
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor
public enum PaymentCardAcquirerCode {
    BC(31,"비씨카드"), HYUNDAI(61, "현대카드"),;

    private final int code;
    private final String krName;

    @JsonValue
    public String getKrName() {
        return krName;
    }

    @JsonCreator
    public static PaymentCardAcquirerCode fromJson(@JsonProperty("cardAcquirerCode") String krName) {
        return Arrays.stream(PaymentCardAcquirerCode.values())
                .filter(code -> krName.equals(code.getKrName()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public String toString() {
        return krName;
    }
}
