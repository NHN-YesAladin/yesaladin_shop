package shop.yesaladin.shop.product.exception;

/**
 * 이미 존재하는 연관관계인 경우 예외를 던집니다.
 *
 * @author 이수정
 * @since 1.0
 */
public class RelationAlreadyExistsException extends RuntimeException {

    public RelationAlreadyExistsException(Long productMainId, Long productSubId) {
        super("ProductMain " + productMainId + ", ProductSub " + productSubId + " is already exists.");
    }
}
