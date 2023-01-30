package shop.yesaladin.shop.member.exception;

/**
 * 조회 대상 회원의 권한이 없을 때 발생 하는 예외
 *
 * @author 송학현
 * @since 1.0
 */
public class MemberRoleNotFoundException extends RuntimeException {

    public MemberRoleNotFoundException(Integer roleId) {
        super("Member Role not found - " + roleId);
    }
}
