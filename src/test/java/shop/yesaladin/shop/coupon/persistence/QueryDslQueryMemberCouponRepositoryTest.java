package shop.yesaladin.shop.coupon.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.coupon.domain.model.MemberCoupon;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;

@Transactional
@SpringBootTest
class QueryDslQueryMemberCouponRepositoryTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private QueryDslQueryMemberCouponRepository queryDslQueryMemberCouponRepository;
    private Member member;
    private static final String testCouponGroupCode = "test-coupon-group";

    @BeforeEach
    void setup() {
        member = DummyMember.member();
        MemberCoupon memberCoupon = MemberCoupon.builder()
                .member(member)
                .couponCode("123")
                .couponGroupCode(testCouponGroupCode)
                .build();
        em.persist(member);
        em.persist(memberCoupon);
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("전달된 멤버가 전달된 쿠폰 코드를 가지고 있으면 true를 반환한다.")
    void existsByMemberAndCouponGroupCodeListTrueTest() {
        // when
        boolean actual = queryDslQueryMemberCouponRepository.existsByMemberAndCouponGroupCodeList(
                member.getLoginId(),
                List.of(testCouponGroupCode)
        );

        // then
        Assertions.assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("전달된 멤버가 전달된 쿠폰 코드를 가지고 있지 않으면 false를 반환한다.")
    void existsByMemberAndCouponGroupCodeListFalseTest() {
        // when
        boolean actual = queryDslQueryMemberCouponRepository.existsByMemberAndCouponGroupCodeList(
                member.getLoginId(),
                List.of("not found")
        );

        // then
        Assertions.assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("회원이 가지고 있는 쿠폰 정보를 가져온다")
    void findMemberCouponByMemberId() {
        // when
        Page<MemberCoupon> actual = queryDslQueryMemberCouponRepository.findMemberCouponByMemberId(
                PageRequest.of(0, 10),
                member.getLoginId()
        );
        // then
        Assertions.assertThat(actual).hasSize(1);
        Assertions.assertThat(actual.getContent().get(0).getCouponCode()).isEqualTo("123");
    }

    @Test
    void findByCouponCodes() {
        //given
        List<String> couponCodes = setCouponCodeData();

        //when
        List<MemberCoupon> result = queryDslQueryMemberCouponRepository.findByCouponCodes(
                couponCodes);

        //then
        assertThat(result).hasSize(5);
        assertThat(result.get(0).getMember()).isEqualTo(member);
    }

    private List<String> setCouponCodeData() {
        List<MemberCoupon> memberCoupons = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            MemberCoupon memberCoupon = getMemberCoupon();

            memberCoupons.add(memberCoupon);

            em.persist(memberCoupon);
        }
        return memberCoupons.stream().map(MemberCoupon::getCouponCode).collect(Collectors.toList());
    }

    private MemberCoupon getMemberCoupon() {
        String couponCode = UUID.randomUUID().toString();
        String couponGroupCode = UUID.randomUUID().toString();

        return MemberCoupon.builder()
                .couponCode(couponCode)
                .couponGroupCode(couponGroupCode)
                .member(member)
                .build();
    }

}