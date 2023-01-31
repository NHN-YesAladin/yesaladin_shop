package shop.yesaladin.shop.product.exception;

/**
 * 존재하는 상품 연관관계가 없는 경우 예외를 던집니다.
 *
 * @author 이수정
 * @since 1.0
 */
public class RelationNotFoundException extends RuntimeException {
    public RelationNotFoundException(Long productMainId, Long productSubId) {
        super("ProductMain " + productMainId + ", ProductSub " + productSubId + " is not found.");
    }
}
