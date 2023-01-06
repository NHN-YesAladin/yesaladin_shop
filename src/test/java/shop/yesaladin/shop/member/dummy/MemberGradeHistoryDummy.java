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
                .memberGrade(MemberGradeDummy.dummy())
                .member(MemberDummy.dummy())
                .build();
    }

    public static MemberGradeHistory dummy(MemberGrade memberGrade, Member member) {
        long previousPaidAmount = 0L;

        return MemberGradeHistory.builder()
                .updateDate(LocalDate.now())
                .previousPaidAmount(previousPaidAmount)
                .memberGrade(memberGrade)
                .member(member)
                .build();
    }
}
