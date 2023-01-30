package shop.yesaladin.shop.member.exception;

/**
 * 회원 등급 조회 시 해당 하는 등급이 없는 경우 발생 하는 예외
 *
 * @author 송학현
 * @since 1.0
 */
public class MemberGradeNotFoundException extends RuntimeException {

    public MemberGradeNotFoundException(int id) {
        super("MemberGrade not found : id = " + id);
    }
}
