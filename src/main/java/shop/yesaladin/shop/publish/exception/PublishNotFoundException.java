package shop.yesaladin.shop.publish.exception;

import shop.yesaladin.shop.product.domain.model.Product;

/**
 * 출판된 상품에 맞는 출판이 없는 경우 예외를 던집니다.
 *
 * @author 이수정
 * @since 1.0
 */
public class PublishNotFoundException extends RuntimeException {

    public PublishNotFoundException(Product product) {
        super("Publish is not found by this product " + product.getId() + "(ID).");
    }
}
