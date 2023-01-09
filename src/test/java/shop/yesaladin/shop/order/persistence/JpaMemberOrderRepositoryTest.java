//package shop.yesaladin.shop.order.persistence;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.Optional;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import shop.yesaladin.shop.member.domain.model.Member;
//import shop.yesaladin.shop.member.domain.model.MemberGrade;
//import shop.yesaladin.shop.order.domain.dummy.MemberAddress;
//import shop.yesaladin.shop.order.domain.model.MemberOrder;
//import shop.yesaladin.shop.order.domain.model.OrderCode;
//import shop.yesaladin.shop.order.persistence.dummy.DummyMember;
//import shop.yesaladin.shop.order.persistence.dummy.DummyMemberAddress;
//import shop.yesaladin.shop.order.persistence.dummy.DummyMemberGrade;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = Replace.NONE)
//class JpaMemberOrderRepositoryTest {
//
//    @PersistenceContext
//    private EntityManager entityManager;
//    @Autowired
//    private JpaOrderRepository<MemberOrder> memberOrderRepository;
//
//    private String orderNumber = "1234-5678";
//    private LocalDateTime orderDateTime = LocalDateTime.now();
//    private LocalDate expectedTransportDate = LocalDate.now();
//    private boolean isHidden = false;
//    private long usedPoint = 0;
//    private int shippingFee = 0;
//    private int wrappingFee = 0;
//    private OrderCode orderCode = OrderCode.NON_MEMBER_ORDER;
//    private Member member;
//    private MemberAddress memberAddress;
//
//    private MemberOrder memberOrder;
//
//    @BeforeEach
//    void setUp() {
//        MemberGrade memberGrade = DummyMemberGrade.memberGrade;
//        member = DummyMember.member(memberGrade);
//        memberAddress = DummyMemberAddress.address(member);
//
//        entityManager.persist(memberGrade);
//        entityManager.persist(member);
//        entityManager.persist(memberAddress);
//
//        memberOrder = createMemberOrder();
//    }
//
//
//    @Test
//    void save() {
//        //when
//        MemberOrder savedOrder = memberOrderRepository.save(memberOrder);
//
//        //then
//        assertThat(savedOrder.getOrderNumber()).isEqualTo(orderNumber);
//        assertThat(savedOrder.getExpectedTransportDate()).isEqualTo(expectedTransportDate);
//        assertThat(savedOrder.isHidden()).isEqualTo(isHidden);
//        assertThat(savedOrder.getUsedPoint()).isEqualTo(usedPoint);
//        assertThat(savedOrder.getShippingFee()).isEqualTo(shippingFee);
//        assertThat(savedOrder.getWrappingFee()).isEqualTo(wrappingFee);
//        assertThat(savedOrder.getOrderCode()).isEqualTo(orderCode);
//        assertThat(savedOrder.getMember()).isEqualTo(member);
//        assertThat(savedOrder.getMemberAddress()).isEqualTo(memberAddress);
//    }
//
//    @Test
//    void findById() {
//        //given
//        entityManager.persist(memberOrder);
//        Long id = memberOrder.getId();
//
//        //when
//        Optional<MemberOrder> foundOrder = memberOrderRepository.findById(id);
//
//        //then
//        assertThat(foundOrder).isPresent();
//        assertThat(foundOrder.get().getId()).isEqualTo(id);
//        assertThat(foundOrder.get().getOrderNumber()).isEqualTo(orderNumber);
//        assertThat(foundOrder.get().getExpectedTransportDate()).isEqualTo(expectedTransportDate);
//        assertThat(foundOrder.get().isHidden()).isEqualTo(isHidden);
//        assertThat(foundOrder.get().getUsedPoint()).isEqualTo(usedPoint);
//        assertThat(foundOrder.get().getShippingFee()).isEqualTo(shippingFee);
//        assertThat(foundOrder.get().getWrappingFee()).isEqualTo(wrappingFee);
//        assertThat(foundOrder.get().getOrderCode()).isEqualTo(orderCode);
//        assertThat(foundOrder.get().getMember()).isEqualTo(member);
//        assertThat(foundOrder.get().getMemberAddress()).isEqualTo(memberAddress);
//    }
//
//    MemberOrder createMemberOrder() {
//        return MemberOrder.builder()
//                .orderNumber(orderNumber)
//                .orderDateTime(orderDateTime)
//                .expectedTransportDate(expectedTransportDate)
//                .isHidden(isHidden)
//                .usedPoint(usedPoint)
//                .shippingFee(shippingFee)
//                .wrappingFee(wrappingFee)
//                .orderCode(orderCode)
//                .memberAddress(memberAddress)
//                .member(member)
//                .build();
//    }
//}