package shop.yesaladin.shop.member.exception;

public class MemberGradeNotFoundException extends RuntimeException {

    public MemberGradeNotFoundException(int id) {
        super("MemberGrade not found : id = " + id);
    }
}
