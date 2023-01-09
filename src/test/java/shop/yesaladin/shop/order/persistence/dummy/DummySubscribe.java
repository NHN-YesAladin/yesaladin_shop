package shop.yesaladin.shop.order.persistence.dummy;

import java.time.LocalDate;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.order.domain.model.Subscribe;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;

public class DummySubscribe {

    public static Subscribe subscribe(
            MemberAddress memberAddress,
            Member member,
            SubscribeProduct subscribeProduct
    ) {
        return Subscribe.builder()
                .intervalMonth(6)
                .nextRenewalDate(LocalDate.now())
                .memberAddress(memberAddress)
                .member(member)
                .subscribeProduct(subscribeProduct)
                .build();
    }
}
