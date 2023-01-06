package shop.yesaladin.shop.payment.persistence.converter;

import java.util.Arrays;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import shop.yesaladin.shop.payment.domain.model.PaymentCode;

/**
 * 결제코드 변환을 위한 컨버터입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Converter
public class PaymentCodeConverter implements AttributeConverter<PaymentCode, Integer> {

    /**
     * 결제코드를 Integer 타입으로 변환합니다.
     *
     * @param paymentCode 결제코드
     * @return 결제코드 Id
     * @author 서민지
     * @since 1.0
     */
    @Override
    public Integer convertToDatabaseColumn(PaymentCode paymentCode) {
        return paymentCode.getCode();
    }

    /**
     * 결제코드를 PaymentCode 타입으로 변환합니다.
     *
     * @param integer 결제코드의 Id
     * @return 결제코드
     * @author 서민지
     * @since 1.0
     */
    @Override
    public PaymentCode convertToEntityAttribute(Integer integer) {
        return Arrays.stream(PaymentCode.values())
                .filter(code -> integer.equals(code.getCode()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}