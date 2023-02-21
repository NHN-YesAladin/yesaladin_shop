package shop.yesaladin.shop.order.persistence.dummy;

import java.time.LocalDate;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberGenderCode;
import shop.yesaladin.shop.member.domain.model.MemberGrade;

public class DummyMember {

    public static Member member() {
        return Member.builder()
                .nickname("익명의 오소리")
                .name("김홍대")
                .loginId("mongmeo")
                .password("1234")
                .birthYear(2000)
                .birthMonth(1)
                .birthDay(1)
                .email("mongemo@yesaladin.shop")
                .phone("01012345678")
                .signUpDate(LocalDate.now())
                .withdrawalDate(null)
                .isWithdrawal(false)
                .isBlocked(false)
                .memberGrade(MemberGrade.WHITE)
                .memberGenderCode(MemberGenderCode.MALE)
                .build();
    }

    public static Member memberWithId() {
        return Member.builder()
                .id(1L)
                .nickname("익명의 오소리")
                .name("김홍대")
                .loginId("mongmeo")
                .password("1234")
                .birthYear(2000)
                .birthMonth(1)
                .birthDay(1)
                .email("mongemo@yesaladin.shop")
                .phone("01012345678")
                .signUpDate(LocalDate.now())
                .withdrawalDate(null)
                .isWithdrawal(false)
                .isBlocked(false)
                .memberGrade(MemberGrade.WHITE)
                .memberGenderCode(MemberGenderCode.MALE)
                .build();
    }

    public static Member memberWithBirthdayWithId(LocalDate birthday) {
        return Member.builder()
                .id(1L)
                .nickname("익명의 오소리")
                .name("김홍대")
                .loginId("mongmeo")
                .password("1234")
                .birthYear(birthday.getYear())
                .birthMonth(birthday.getMonthValue())
                .birthDay(birthday.getDayOfMonth())
                .email("mongemo@yesaladin.shop")
                .phone("01012345678")
                .signUpDate(LocalDate.now())
                .withdrawalDate(null)
                .isWithdrawal(false)
                .isBlocked(false)
                .memberGrade(MemberGrade.WHITE)
                .memberGenderCode(MemberGenderCode.MALE)
                .build();
    }
}
