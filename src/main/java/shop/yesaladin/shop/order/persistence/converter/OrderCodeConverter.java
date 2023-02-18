package shop.yesaladin.shop.order.persistence.converter;

import shop.yesaladin.shop.order.domain.model.OrderCode;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;

/**
 * 주문코드 변환을 위한 컨버터입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Converter
public class OrderCodeConverter implements AttributeConverter<OrderCode, Integer> {

    /**
     * 주문코드를 Integer 타입으로 변환합니다.
     *
     * @param orderCode 주문코드
     * @return 주문코드의 Id
     * @author 최예린
     * @since 1.0
     */
    @Override
    public Integer convertToDatabaseColumn(OrderCode orderCode) {
        return orderCode.getCode();
    }

    /**
     * 주문코드를 OrderCode 타입으로 변환합니다.
     *
     * @param integer 주문코드의 Id
     * @return 주문코드
     * @author 최예린
     * @since 1.0
     */
    @Override
    public OrderCode convertToEntityAttribute(Integer integer) {
        return Arrays.stream(OrderCode.values())
                .filter(code -> integer.equals(code.getCode()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}