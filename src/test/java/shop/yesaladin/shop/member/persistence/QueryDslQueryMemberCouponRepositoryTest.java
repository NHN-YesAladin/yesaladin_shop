package shop.yesaladin.shop.member.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberCoupon;
import shop.yesaladin.shop.member.domain.repository.QueryMemberCouponRepository;
import shop.yesaladin.shop.member.dummy.MemberDummy;

@Transactional
@SpringBootTest
class QueryDslQueryMemberCouponRepositoryTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    QueryMemberCouponRepository queryMemberCouponRepository;

    Member member;

    @BeforeEach
    void setUp() {
        member = MemberDummy.dummy();

        entityManager.persist(member);

    }

    @Test
    void findByCouponCodes() {
        //given
        List<String> couponCodes = setCouponCodeData();

        //when
        List<MemberCoupon> result = queryMemberCouponRepository.findByCouponCodes(couponCodes);

        //then
        assertThat(result).hasSize(5);
        assertThat(result.get(0).getMember()).isEqualTo(member);
    }

    private List<String> setCouponCodeData() {
        List<MemberCoupon> memberCoupons = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            MemberCoupon memberCoupon = getMemberCoupon();

            memberCoupons.add(memberCoupon);

            entityManager.persist(memberCoupon);
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