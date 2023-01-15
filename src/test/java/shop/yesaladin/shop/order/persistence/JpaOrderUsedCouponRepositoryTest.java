package shop.yesaladin.shop.order.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.member.domain.model.MemberCoupon;
import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.domain.model.OrderUsedCoupon;
import shop.yesaladin.shop.order.domain.model.OrderUsedCoupon.Pk;
import shop.yesaladin.shop.order.persistence.dummy.DummyCouponIssuance;
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;
import shop.yesaladin.shop.order.persistence.dummy.DummyMemberAddress;
import shop.yesaladin.shop.order.persistence.dummy.DummyOrder;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaOrderUsedCouponRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JpaOrderUsedCouponRepository orderUsedCouponRepository;

    private Member member;
    private MemberAddress memberAddress;
    private MemberOrder memberOrder;
    private MemberCoupon memberCoupon;

    private OrderUsedCoupon orderUsedCoupon;


    @BeforeEach
    void setUp() {
        member = DummyMember.member();
        entityManager.persist(member);

        memberAddress = DummyMemberAddress.address(member);
        entityManager.persist(memberAddress);

        memberOrder = DummyOrder.memberOrder(member, memberAddress);
        memberCoupon = DummyCouponIssuance.memberCoupon;

        entityManager.persist(memberOrder);
        entityManager.persist(memberCoupon);

        orderUsedCoupon = OrderUsedCoupon.create(memberOrder, memberCoupon);
    }

    @Test
    void save() {
        //when
        OrderUsedCoupon savedOrderUsedCoupon = orderUsedCouponRepository.save(orderUsedCoupon);

        //then
        assertThat(savedOrderUsedCoupon.getMemberOrder()).isEqualTo(memberOrder);
        assertThat(savedOrderUsedCoupon.getMemberCoupon()).isEqualTo(memberCoupon);
    }

    @Test
    void findById() {
        //given
        entityManager.persist(orderUsedCoupon);
        Pk pk = orderUsedCoupon.getPk();

        //when
        Optional<OrderUsedCoupon> foundOrderUsedCoupon = orderUsedCouponRepository.findById(pk);

        //then
        assertThat(foundOrderUsedCoupon).isPresent();
        assertThat(foundOrderUsedCoupon.get().getPk()).isEqualTo(pk);
        assertThat(foundOrderUsedCoupon.get().getMemberOrder()).isEqualTo(memberOrder);
        assertThat(foundOrderUsedCoupon.get().getMemberCoupon()).isEqualTo(memberCoupon);
    }
}