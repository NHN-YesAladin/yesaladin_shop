package shop.yesaladin.shop.product.exception;

public class RequestedQuantityLargerThanSellQuantityException extends RuntimeException {
    public RequestedQuantityLargerThanSellQuantityException(int requestedQuantity, long sellQuantity) {
        super("구매하고자 하는 수량(" + requestedQuantity + ")이 판매되고 있는 수량(" + sellQuantity + ")보다 많습니다.");
    }
}
