package shop.yesaladin.shop.member.exception;

/**
 * 이미 삭제된 배송지를 삭제할때 생기는 오류입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public class AlreadyDeletedAddressException extends RuntimeException {

    public AlreadyDeletedAddressException(Long id) {
        super("Already Deleted Address : " + id);
    }
}
