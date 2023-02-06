package shop.yesaladin.shop.product.exception;

/**
 * 동일한 상품은 연관관계를 맺을 수 없습니다.
 *
 * @author 이수정
 * @since 1.0
 */
public class SelfRelateException extends RuntimeException {

    public SelfRelateException(Long id) {
        super("Product " + id + " can't self relate.");
    }
}
