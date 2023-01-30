package shop.yesaladin.shop.writing.exception;

import shop.yesaladin.shop.product.domain.model.Product;

/**
 * 찾는 집필 데이터가 없는 경우 예외를 던집니다.
 *
 * @author 이수정
 * @since 1.0
 */
public class WritingNotFoundException extends RuntimeException {

    public WritingNotFoundException(Product product) {
        super("Writing is not found by this product(" + product.getId() + ").");
    }
}
