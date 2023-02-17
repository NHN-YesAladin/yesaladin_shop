package shop.yesaladin.shop.coupon.persistence;


import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.coupon.domain.model.MemberCoupon;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;

@Transactional
@SpringBootTest
@ActiveProfiles("local-test")
class QueryDslQueryMemberCouponRepositoryTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private QueryDslQueryMemberCouponRepository queryDslQueryMemberCouponRepository;
    @MockBean
    private Clock clock;
    private Member member;
    private static final String testCouponGroupCode = "test-coupon-group";

    @BeforeEach
    void setup() {
        Mockito.when(clock.instant()).thenReturn(Instant.parse("2023-02-11T00:00:00.000Z"));
        Mockito.when(clock.getZone()).thenReturn(ZoneId.of("UTC"));
        member = DummyMember.member();
        em.persist(member);
        for (int i = 0; i < 20; i++) {
            MemberCoupon memberCoupon = MemberCoupon.builder()
                    .member(member)
                    .couponCode("123" + i)
                    .couponGroupCode(testCouponGroupCode)
                    .isUsed(i % 2 == 0)
                    .expirationDate(LocalDate.of(2023, 2, i + 1))
                    .build();
            em.persist(memberCoupon);
        }
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
    @DisplayName("회원이 가지고 있는 사용 가능한 쿠폰 정보를 가져온다")
    void findUsableMemberCouponByMemberId() {
        // when
        Page<MemberCoupon> actual = queryDslQueryMemberCouponRepository.findMemberCouponByMemberId(
                PageRequest.of(0, 10),
                member.getLoginId(),
                true
        );

        // then
        Assertions.assertThat(actual).hasSize(5);
        Assertions.assertThat(actual.getContent())
                .allMatch(actualElement -> !actualElement.isUsed()
                        && !actualElement.getExpirationDate().isBefore(LocalDate.now(clock)));
    }

    @Test
    @DisplayName("회원이 가지고 있는 사용 불가능한 쿠폰 정보를 가져온다")
    void findUnusableMemberCouponByMemberId() {
        // when
        Page<MemberCoupon> actual = queryDslQueryMemberCouponRepository.findMemberCouponByMemberId(
                PageRequest.of(0, 20),
                member.getLoginId(),
                false
        );

        // then
        Assertions.assertThat(actual).hasSize(15);
        Assertions.assertThat(actual.getContent())
                .allMatch(actualElement -> actualElement.isUsed()
                        || actualElement.getExpirationDate().isBefore(LocalDate.now(clock)));
    }

    @Test
    void findByCouponCodes() {
        //given
        List<String> couponCodes = setCouponCodeData();

        //when
        List<MemberCoupon> result = queryDslQueryMemberCouponRepository.findByCouponCodes(
                couponCodes);

        //then
        Assertions.assertThat(result).hasSize(5);
        Assertions.assertThat(result.get(0).getMember()).isEqualTo(member);
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
                .expirationDate(LocalDate.now().plusDays(1))
                .isUsed(false)
                .build();
    }

}