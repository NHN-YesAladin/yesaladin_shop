package shop.yesaladin.shop.member.dummy;

import java.time.LocalDate;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberGenderCode;
import shop.yesaladin.shop.member.domain.model.MemberGrade;

public class MemberDummy {

    public static Member dummy() {
        String ramos = "Ramos";
        String password = "password";
        String email = "test@test.com";
        int birthDay = 19;
        int birthMonth = 1;
        int birthYear = 1996;
        long point = 0L;

        return Member.builder()
                .nickname(ramos)
                .name(ramos)
                .loginId(ramos)
                .password(password)
                .birthYear(birthYear)
                .birthMonth(birthMonth)
                .birthDay(birthDay)
                .email(email)
                .signUpDate(LocalDate.now())
                .isBlocked(false)
                .point(point)
                .memberGrade(MemberGradeDummy.dummy())
                .memberGenderCode(MemberGenderCode.MALE)
                .build();
    }

    public static Member dummy(MemberGrade memberGrade) {
        String ramos = "Ramos";
        String password = "password";
        String email = "test@test.com";
        int birthDay = 19;
        int birthMonth = 1;
        int birthYear = 1996;
        long point = 0L;

        return Member.builder()
                .nickname(ramos)
                .name(ramos)
                .loginId(ramos)
                .password(password)
                .birthYear(birthYear)
                .birthMonth(birthMonth)
                .birthDay(birthDay)
                .email(email)
                .signUpDate(LocalDate.now())
                .isBlocked(false)
                .point(point)
                .memberGrade(memberGrade)
                .memberGenderCode(MemberGenderCode.MALE)
                .build();
    }
}
