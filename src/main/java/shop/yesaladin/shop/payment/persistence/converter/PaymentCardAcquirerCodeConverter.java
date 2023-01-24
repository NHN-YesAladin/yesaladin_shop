package shop.yesaladin.shop.payment.persistence.converter;

import java.util.Arrays;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import shop.yesaladin.shop.payment.domain.model.PaymentCardAcquirerCode;

/**
 * 결제매입사코드 변환을 위한 컨버터입니다.
 *
 * @author 배수한
 * @since 1.0
 */
@Converter
public class PaymentCardAcquirerCodeConverter implements
        AttributeConverter<PaymentCardAcquirerCode, Integer> {

    /**
     * 결제매입사코드를 Integer 타입으로 변환합니다.
     *
     * @param paymentCardAcquirerCode 결제 매입사 코드
     * @return 결제 매입사 코드 Id
     */
    @Override
    public Integer convertToDatabaseColumn(PaymentCardAcquirerCode paymentCardAcquirerCode) {
        return paymentCardAcquirerCode.getId();
    }

    /**
     * 결제 매입사 코드 Id 를 PaymentCardAcquirerCode 타입으로 변환합니다.
     *
     * @param integer 결제 매입사 코드 Id
     * @return 결제 매입사 코드
     */
    @Override
    public PaymentCardAcquirerCode convertToEntityAttribute(Integer integer) {
        return Arrays.stream(PaymentCardAcquirerCode.values())
                .filter(code -> integer.equals(code.getId()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
