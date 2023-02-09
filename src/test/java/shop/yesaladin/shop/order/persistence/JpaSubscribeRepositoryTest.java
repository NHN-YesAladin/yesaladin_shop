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
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.domain.model.Subscribe;
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;
import shop.yesaladin.shop.order.persistence.dummy.DummyMemberAddress;
import shop.yesaladin.shop.order.persistence.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;

@DataJpaTest
@Disabled
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaSubscribeRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JpaOrderCommandRepository<Subscribe> orderCommandRepository;

    private String orderNumber = "1234-5678";
    private LocalDateTime orderDateTime = LocalDateTime.now();
    private LocalDate expectedTransportDate = LocalDate.now();
    private boolean isHidden = false;
    private long usedPoint = 0;
    private int shippingFee = 0;
    private int wrappingFee = 0;
    private long totalAmount = 10000L;
    private OrderCode orderCode = OrderCode.NON_MEMBER_ORDER;
    private String address = "Gwang-ju";
    private String name = "yerin";
    private String phoneNumber = "010-1234-1234";
    private int expectedDay = 15;
    private int intervalMonth = 6;
    private LocalDate nextRenewalDate = LocalDate.now();
    private MemberAddress memberAddress;
    private Member member;
    private SubscribeProduct subscribeProduct;

    @BeforeEach
    void setUp() {
        member = DummyMember.member();
        entityManager.persist(member);

        memberAddress = DummyMemberAddress.address(member);
        subscribeProduct = DummySubscribeProduct.subscribeProduct();

        entityManager.persist(memberAddress);
        entityManager.persist(subscribeProduct);
    }


    @Test
    void save() {
        Subscribe subscribe = createSubscribe();
        //when
        Subscribe savedSubscribe = orderCommandRepository.save(subscribe);

        //then
        assertThat(savedSubscribe.getIntervalMonth()).isEqualTo(intervalMonth);
        assertThat(savedSubscribe.getNextRenewalDate()).isEqualTo(nextRenewalDate);
        assertThat(savedSubscribe.getMemberAddress()).isEqualTo(memberAddress);
        assertThat(savedSubscribe.getMember()).isEqualTo(member);
        assertThat(savedSubscribe.getSubscribeProduct()).isEqualTo(subscribeProduct);
        assertThat(savedSubscribe.getTotalAmount()).isEqualTo(totalAmount);

    }

    Subscribe createSubscribe() {
        return Subscribe.builder()
                .orderNumber(orderNumber)
                .name("subscribe")
                .orderDateTime(orderDateTime)
                .expectedTransportDate(expectedTransportDate)
                .isHidden(isHidden)
                .usedPoint(usedPoint)
                .shippingFee(shippingFee)
                .wrappingFee(wrappingFee)
                .totalAmount(totalAmount)
                .orderCode(orderCode)
                .memberAddress(memberAddress)
                .member(member)
                .expectedDay(expectedDay)
                .intervalMonth(intervalMonth)
                .nextRenewalDate(nextRenewalDate)
                .subscribeProduct(subscribeProduct)
                .build();
    }
}
