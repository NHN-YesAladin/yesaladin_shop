package shop.yesaladin.shop.product.exception;

/**
 * ID에 맞는 ProductTypeCode를 찾지 못하는 경우 발생하는 예외입니다.
 *
 * @author : 이수정
 * @since : 1.0
 */
public class ProductTypeCodeNotFoundException extends RuntimeException {
    public ProductTypeCodeNotFoundException(Integer integer) {
        super("ProductTypeCode Not Found : " + integer.toString() + "(ID)");
    }
}
