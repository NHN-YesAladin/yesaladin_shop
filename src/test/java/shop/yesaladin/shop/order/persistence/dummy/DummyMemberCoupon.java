package shop.yesaladin.shop.order.persistence.dummy;


import java.time.LocalDate;
import java.time.LocalDateTime;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberCoupon;

public class DummyMemberCoupon {

    public static MemberCoupon memberCoupon(Member member) {
        LocalDateTime createdDatetime = LocalDateTime.of(2023, 1, 10, 0, 0, 0);
        LocalDate expirationDate = LocalDate.of(2023, 3, 10);

        return MemberCoupon.builder()
                .member(member)
                .couponCode("")
                .isUsed(false)
                .createdDatetime(createdDatetime)
                .expirationDate(expirationDate)
                .build();
    }

}
