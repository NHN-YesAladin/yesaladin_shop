package shop.yesaladin.shop.member.exception;


/**
 * 조회 대상 member 가 없을 때 발생 하는 예외
 *
 * @author : 송학현
 * @since : 1.0
 */
public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException(long id) {
        super("Member not found" + id);
    }
}
