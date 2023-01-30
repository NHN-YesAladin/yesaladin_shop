package shop.yesaladin.shop.member.exception;

/**
 * 회원이 배송지 등록 한계 개수보다 더 등록할 때 발생합니다.
 *
 * @author 최예린
 * @since 1.0
 */
public class AlreadyRegisteredUpToLimit extends RuntimeException {

    public AlreadyRegisteredUpToLimit(String loginId) {
        super("Already Registered Up To Limit 10 : " + loginId);
    }
}
