package shop.yesaladin.shop.order.persistence.converter;

import java.util.Arrays;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import shop.yesaladin.shop.order.domain.model.OrderStatusCode;

/**
 * 주문상태코드를 변환하기 위한 컨버터입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Converter
public class OrderStatusCodeConverter implements AttributeConverter<OrderStatusCode, Integer> {

    /**
     * 주문상태코드를 Integer 타입으로 변환합니다.
     *
     * @param orderStatusCode 주문상태코드
     * @return 주문상태코드의 Id
     * @author 최예린
     * @since 1.0
     */
    @Override
    public Integer convertToDatabaseColumn(OrderStatusCode orderStatusCode) {
        return orderStatusCode.getStatusCode();
    }

    /**
     * 주문상태코드를 OrderStatusCode 타입으로 변환합니다.
     *
     * @param integer 주문상태코드의 Id
     * @return 주문상태코드
     * @author 최예린
     * @since 1.0
     */
    @Override
    public OrderStatusCode convertToEntityAttribute(Integer integer) {
        return Arrays.stream(OrderStatusCode.values())
                .filter(code -> integer.equals(code.getStatusCode()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
