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
import shop.yesaladin.shop.order.domain.model.OrderCoupon;
import shop.yesaladin.shop.order.domain.model.OrderCoupon.Pk;
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;
import shop.yesaladin.shop.order.persistence.dummy.DummyMemberAddress;
import shop.yesaladin.shop.order.persistence.dummy.DummyMemberCoupon;
import shop.yesaladin.shop.order.persistence.dummy.DummyOrder;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaOrderUsedCouponRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JpaOrderUsedCouponRepository orderUsedCouponRepository;

    private MemberOrder memberOrder;
    private MemberCoupon memberCoupon;

    private OrderCoupon orderUsedCoupon;


    @BeforeEach
    void setUp() {
        Member member = DummyMember.member();
        entityManager.persist(member);

        MemberAddress memberAddress = DummyMemberAddress.address(member);
        entityManager.persist(memberAddress);

        memberOrder = DummyOrder.memberOrder(member, memberAddress);
        memberCoupon = DummyMemberCoupon.memberCoupon(member);

        entityManager.persist(memberOrder);
        entityManager.persist(memberCoupon);

        orderUsedCoupon = OrderCoupon.create(memberOrder, memberCoupon);
    }

    @Test
    void save() {
        //when
        OrderCoupon savedOrderUsedCoupon = orderUsedCouponRepository.save(orderUsedCoupon);

        //then
        assertThat(savedOrderUsedCoupon.getMemberOrder()).isSameAs(memberOrder);
        assertThat(savedOrderUsedCoupon.getMemberCoupon()).isSameAs(memberCoupon);
    }

    @Test
    void findById() {
        //given
        entityManager.persist(orderUsedCoupon);
        Pk pk = orderUsedCoupon.getPk();

        //when
        Optional<OrderCoupon> foundOrderUsedCoupon = orderUsedCouponRepository.findById(pk);

        //then
        assertThat(foundOrderUsedCoupon).isPresent();
        assertThat(foundOrderUsedCoupon.get().getPk()).isEqualTo(pk);
        assertThat(foundOrderUsedCoupon.get().getMemberOrder()).isSameAs(memberOrder);
        assertThat(foundOrderUsedCoupon.get().getMemberCoupon()).isSameAs(memberCoupon);
    }
}
