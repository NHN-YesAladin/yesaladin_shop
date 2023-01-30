package shop.yesaladin.shop.product.exception;

/**
 * 구매하고자 하는 수량이 판매되고 있는 수량보다 많은 경우 예외를 던집니다.
 *
 * @author 이수정
 * @since 1.0
 */
public class RequestedQuantityLargerThanSellQuantityException extends RuntimeException {
    public RequestedQuantityLargerThanSellQuantityException(int requestedQuantity, long sellQuantity) {
        super("구매하고자 하는 수량(" + requestedQuantity + ")이 판매되고 있는 수량(" + sellQuantity + ")보다 많습니다.");
    }
}
