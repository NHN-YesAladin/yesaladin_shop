package shop.yesaladin.shop.coupon.persistence;

import javax.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.yesaladin.shop.coupon.domain.model.MemberCoupon;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;

@DataJpaTest
class JpaCommandMemberCouponRepositoryTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private JpaCommandMemberCouponRepository repository;
    private Member member;

    @BeforeEach
    void setup() {
        member = DummyMember.member();
        entityManager.persist(member);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("멤버 쿠폰 정보가 저장된다.")
    void saveTest() {
        // given
        MemberCoupon memberCoupon = MemberCoupon.builder()
                .member(member)
                .couponCode("couponCode")
                .couponGroupCode("couponGroupCode")
                .build();

        // when
        MemberCoupon actual = repository.save(memberCoupon);

        // then
        entityManager.find(MemberCoupon.class, actual.getId());
        Assertions.assertThat(actual.getMember()).isEqualTo(member);
        Assertions.assertThat(actual.getCouponGroupCode()).isEqualTo("couponGroupCode");
        Assertions.assertThat(actual.getCouponCode()).isEqualTo("couponCode");
    }
}