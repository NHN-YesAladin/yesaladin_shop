package shop.yesaladin.shop.order.exception;

/**
 * 주문 정보를 찾을수 없을 때 생기는 예외
 *
 * @author 배수한
 * @since 1.0
 */
public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(Object orderInfo) {
        super("Order not found : " + orderInfo);
    }
}
