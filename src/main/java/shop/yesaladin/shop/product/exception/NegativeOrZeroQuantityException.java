package shop.yesaladin.shop.product.exception;

public class NegativeOrZeroQuantityException extends RuntimeException {
    public NegativeOrZeroQuantityException(int requestedQuantity) {
        super("음수 혹은 0인 수량을 입력하였습니다. (입력값: " + requestedQuantity + ")");
    }
}
