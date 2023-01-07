package shop.yesaladin.shop.product.exception;

/**
 * ID에 맞는 ProductSavingMethodCode를 찾지 못하는 경우 예외를 던집니다.
 *
 * @author 이수정
 * @since 1.0
 */
public class ProductSavingMethodCodeNotFoundException extends RuntimeException {
    public ProductSavingMethodCodeNotFoundException(int id) {
        super("ProductSavingMethodCode Not Found : " + id + "(ID)");
    }
}
