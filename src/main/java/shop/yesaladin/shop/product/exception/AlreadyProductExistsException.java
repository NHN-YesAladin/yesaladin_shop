package shop.yesaladin.shop.product.exception;

public class AlreadyProductExistsException extends RuntimeException {

    public AlreadyProductExistsException(String ISBN) {
        super("Product " + ISBN + " is already exists.");
    }
}
