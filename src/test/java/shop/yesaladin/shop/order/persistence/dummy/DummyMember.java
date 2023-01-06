package shop.yesaladin.shop.order.persistence.dummy;

import java.time.LocalDate;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberGenderCode;
import shop.yesaladin.shop.member.domain.model.MemberGrade;

public class DummyMember {

    public static Member member(MemberGrade grade) {
        return Member.builder()
                .nickname("member")
                .name("name")
                .loginId("member1")
                .password("1234")
                .birthYear(2023)
                .birthMonth(1)
                .birthDay(6)
                .email("member@yesaladin.shop")
                .signUpDate(LocalDate.now())
                .withdrawalDate(null)
                .isWithdrawal(false)
                .isBlocked(false)
                .point(0)
                .memberGrade(grade)
                .memberGenderCode(MemberGenderCode.MALE)
                .build();
    }

}
