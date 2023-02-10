package shop.yesaladin.shop.order.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;
import shop.yesaladin.shop.order.persistence.dummy.DummyMemberAddress;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaMemberOrderRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private JpaOrderCommandRepository<MemberOrder> memberOrderRepository;

    private String orderNumber = "1234-5678";
    private LocalDateTime orderDateTime = LocalDateTime.now();
    private LocalDate expectedTransportDate = LocalDate.now();
    private boolean isHidden = false;
    private long usedPoint = 0;
    private int shippingFee = 0;
    private int wrappingFee = 0;
    private long totalAmount = 10000L;
    private OrderCode orderCode = OrderCode.NON_MEMBER_ORDER;
    private Member member;
    private MemberAddress memberAddress;
    private String recipientName = "김몽대";
    private String recipientPhoneNumber = "01012341234";
    private MemberOrder memberOrder;

    @BeforeEach
    void setUp() {
        member = DummyMember.member();
        memberAddress = DummyMemberAddress.address(member);

        entityManager.persist(member);
        entityManager.persist(memberAddress);

        memberOrder = createMemberOrder();
    }


    @Test
    void save() {
        //when
        MemberOrder savedOrder = memberOrderRepository.save(memberOrder);

        //then
        assertThat(savedOrder.getOrderNumber()).isEqualTo(orderNumber);
        assertThat(savedOrder.getExpectedTransportDate()).isEqualTo(expectedTransportDate);
        assertThat(savedOrder.isHidden()).isEqualTo(isHidden);
        assertThat(savedOrder.getUsedPoint()).isEqualTo(usedPoint);
        assertThat(savedOrder.getShippingFee()).isEqualTo(shippingFee);
        assertThat(savedOrder.getWrappingFee()).isEqualTo(wrappingFee);
        assertThat(savedOrder.getOrderCode()).isEqualTo(orderCode);
        assertThat(savedOrder.getMember()).isEqualTo(member);
        assertThat(savedOrder.getMemberAddress()).isEqualTo(memberAddress);
        assertThat(savedOrder.getTotalAmount()).isEqualTo(totalAmount);
    }

    MemberOrder createMemberOrder() {
        return MemberOrder.builder()
                .orderNumber(orderNumber)
                .name("memberOrder")
                .orderDateTime(orderDateTime)
                .expectedTransportDate(expectedTransportDate)
                .isHidden(isHidden)
                .usedPoint(usedPoint)
                .shippingFee(shippingFee)
                .wrappingFee(wrappingFee)
                .totalAmount(totalAmount)
                .recipientName(recipientName)
                .recipientPhoneNumber(recipientPhoneNumber)
                .orderCode(orderCode)
                .memberAddress(memberAddress)
                .member(member)
                .build();
    }
}
