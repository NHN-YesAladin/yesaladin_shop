package shop.yesaladin.shop.member.dummy;

import shop.yesaladin.shop.member.domain.model.MemberGrade;

public class MemberGradeDummy {

    public static MemberGrade dummy() {
        int id = 1;
        String gradeName = "플래티넘";
        long baseGivenPoint = 2000L;
        long baseOrderAmount = 200000L;

        return MemberGrade.builder()
                .id(id)
                .name(gradeName)
                .baseGivenPoint(baseGivenPoint)
                .baseOrderAmount(baseOrderAmount)
                .build();
    }
}
