package shop.yesaladin.shop.member.dummy;

import java.time.LocalDate;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberGenderCode;
import shop.yesaladin.shop.member.domain.model.MemberGrade;

public class MemberDummy {

    public static Member dummyWithId(Long id) {
        String ramos = "Ramos";
        String password = "password";
        String email = "test@test.com";
        int birthDay = 19;
        int birthMonth = 1;
        int birthYear = 1996;
        String phone = "01012345678";

        return Member.builder()
                .id(id)
                .nickname(ramos)
                .name(ramos)
                .loginId(ramos)
                .password(password)
                .birthYear(birthYear)
                .birthMonth(birthMonth)
                .birthDay(birthDay)
                .email(email)
                .phone(phone)
                .signUpDate(LocalDate.now())
                .isBlocked(false)
                .memberGrade(MemberGrade.WHITE)
                .memberGenderCode(MemberGenderCode.MALE)
                .build();
    }

    public static Member dummyWithLoginIdAndId(String loginId) {
        String password = "1234";
        String email = "test@test.com";
        int birthDay = 1;
        int birthMonth = 1;
        int birthYear = 2000;
        String phone = "01012345678";

        return Member.builder()
                .id(1L)
                .nickname(loginId)
                .name(loginId)
                .loginId(loginId)
                .password(password)
                .birthYear(birthYear)
                .birthMonth(birthMonth)
                .birthDay(birthDay)
                .email(email)
                .phone(phone)
                .signUpDate(LocalDate.now())
                .isWithdrawal(false)
                .isBlocked(false)
                .memberGrade(MemberGrade.WHITE)
                .memberGenderCode(MemberGenderCode.MALE)
                .build();
    }

    public static Member dummyWithLoginId(String loginId) {
        String password = "1234";
        String email = "test@test.com";
        int birthDay = 1;
        int birthMonth = 1;
        int birthYear = 2000;
        String phone = "01012345678";

        return Member.builder()
                .nickname(loginId)
                .name(loginId)
                .loginId(loginId)
                .password(password)
                .birthYear(birthYear)
                .birthMonth(birthMonth)
                .birthDay(birthDay)
                .email(email)
                .phone(phone)
                .signUpDate(LocalDate.now())
                .isBlocked(false)
                .memberGrade(MemberGrade.WHITE)
                .memberGenderCode(MemberGenderCode.MALE)
                .build();
    }

    public static Member dummy() {
        String ramos = "Ramos";
        String password = "password";
        String email = "test@test.com";
        int birthDay = 19;
        int birthMonth = 1;
        int birthYear = 1996;
        String phone = "01012345678";

        return Member.builder()
                .nickname(ramos)
                .name(ramos)
                .loginId(ramos)
                .password(password)
                .birthYear(birthYear)
                .birthMonth(birthMonth)
                .birthDay(birthDay)
                .email(email)
                .phone(phone)
                .signUpDate(LocalDate.now())
                .isBlocked(false)
                .memberGrade(MemberGrade.WHITE)
                .memberGenderCode(MemberGenderCode.MALE)
                .build();
    }

}