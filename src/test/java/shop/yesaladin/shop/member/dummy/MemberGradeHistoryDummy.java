package shop.yesaladin.shop.member.dummy;

import java.time.LocalDate;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberGrade;
import shop.yesaladin.shop.member.domain.model.MemberGradeHistory;

public class MemberGradeHistoryDummy {

    public static MemberGradeHistory dummy() {
        long previousPaidAmount = 0L;

        return MemberGradeHistory.builder()
                .updateDate(LocalDate.now())
                .previousPaidAmount(previousPaidAmount)
                .memberGrade(MemberGrade.WHITE)
                .member(MemberDummy.dummy())
                .build();
    }

    public static MemberGradeHistory dummy(Member member) {
        long previousPaidAmount = 0L;

        return MemberGradeHistory.builder()
                .updateDate(LocalDate.now())
                .previousPaidAmount(previousPaidAmount)
                .memberGrade(MemberGrade.WHITE)
                .member(member)
                .build();
    }

    public static MemberGradeHistory dummyWithDate(Member member, LocalDate date) {
        return MemberGradeHistory.builder()
                .updateDate(date)
                .previousPaidAmount(0L)
                .memberGrade(MemberGrade.WHITE)
                .member(member)
                .build();
    }
}
