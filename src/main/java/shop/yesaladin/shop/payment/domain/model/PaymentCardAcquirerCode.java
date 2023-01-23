package shop.yesaladin.shop.payment.domain.model;

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
    IBK_BC(1, "3K", "BC카드-IBK"), GWANGJUBANK(2, "46", "광주은행"),
    LOTTE(3, "71", "롯데카드"), KDBBANK(4, "30", "KDB산업은행"),
    BC(5, "31", "비씨카드"), SAMSUNG(6, "51", "삼성카드"),
    SAEMAUL(7, "38", "새마을금고"), SHINHAN(8, "41", "신한카드"),
    SHINHYEOP(9, "62", "신협"), CITI(10, "36", "씨티카드"),
    WOORI(11, "33", "우리카드"), POST(12, "37", "우체국예금보험"),
    SAVINGBANK(13, "39", "저축은행중앙회"), JEONBUKBANK(14, "35", "전북은행"),
    JEJUBANK(15, "42", "제주은행"), KAKAOBANK(16, "15", "카카오뱅크"),
    KBANK(17, "3A", "케이뱅크"), TOSSBANK(18, "24", "토스뱅크"),
    HANA(19, "21", "하나카드"), HYUNDAI(20, "61", "현대카드"),
    KOOKMIN(21, "11", "KB국민카드"), NONGHYEOP(22, "91", "NH농협카드"),
    SUHYEOP(23, "34", "Sh수협은행"), DINERS(24, "6D", "다이너스 클럽"),
    DISCOVER(25, "6I", "디스커버"), MASTER(26, "4M", "마스터카드"),
    UNIONPAY(27, "3C", "유니온페이"), AMEX(28, "7A", "아메리칸 익스프레스"),
    JCB(29, "4J", "JCB"), VISA(30, "4V", "VISA"),
    ;

    private final int id;
    private final String code;
    private final String krName;

    public static PaymentCardAcquirerCode findByName(String codeName) {
        return Arrays.stream(PaymentCardAcquirerCode.values())
                .filter(code -> code.getCode().equals(codeName))
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
