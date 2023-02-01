package shop.yesaladin.shop.order.persistence;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.member.domain.model.MemberGenderCode;
import shop.yesaladin.shop.member.domain.model.MemberGrade;
import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.domain.model.NonMemberOrder;
import shop.yesaladin.shop.order.domain.model.OrderCode;
import shop.yesaladin.shop.order.domain.model.OrderProduct;
import shop.yesaladin.shop.order.domain.model.OrderStatusChangeLog;
import shop.yesaladin.shop.order.domain.model.OrderStatusCode;
import shop.yesaladin.shop.order.domain.model.Subscribe;
import shop.yesaladin.shop.order.domain.model.SubscribeOrderList;
import shop.yesaladin.shop.order.domain.repository.QueryOrderProductRepository;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;

@Transactional
@SpringBootTest
class QueryDslOrderProductQueryRepositoryTest {
    @Autowired
    private QueryOrderProductRepository queryRepository;
    @Autowired
    private EntityManager entityManager;
    private List<NonMemberOrder> nonMemberOrderList;
    private List<MemberOrder> memberOrderList;
    private List<Subscribe> subscribeList;
    private List<Member> memberList;
    private List<MemberAddress> memberAddressList;

    @BeforeEach
    void setUp() {
        nonMemberOrderList = new ArrayList<>();
        memberOrderList = new ArrayList<>();
        subscribeList = new ArrayList<>();
        memberList = new ArrayList<>();
        memberAddressList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Member member = Member.builder()
                    .nickname("test" + i)
                    .name("name" + i)
                    .loginId("id" + i)
                    .password("password")
                    .birthYear(2002)
                    .birthMonth(5)
                    .birthDay(i)
                    .email("admin+" + i + "@mongmeo.dev")
                    .phone("0101234567" + i)
                    .signUpDate(LocalDate.of(2023, 1, i + 1))
                    .withdrawalDate(null)
                    .isWithdrawal(false)
                    .isBlocked(false)
                    .memberGrade(MemberGrade.WHITE)
                    .memberGenderCode(MemberGenderCode.MALE)
                    .build();

            MemberAddress memberAddress = MemberAddress.builder()
                    .member(member)
                    .address("address" + i)
                    .build();

            memberList.add(member);
            memberAddressList.add(memberAddress);

            entityManager.persist(member);
            entityManager.persist(memberAddress);
            memberList.add(member);
        }
        for (int i = 0; i < 3; i++) {
            NonMemberOrder nonMemberOrder = NonMemberOrder.builder()
                    .orderNumber("NM-" + i)
                    .name("non-member-order" + i)
                    .orderDateTime(LocalDateTime.of(2023, 1, i + 1, 0, 0))
                    .expectedTransportDate(LocalDate.of(2023, 1, i + 2))
                    .isHidden(false)
                    .usedPoint(0)
                    .shippingFee(0)
                    .wrappingFee(0)
                    .orderCode(OrderCode.NON_MEMBER_ORDER)
                    .address("address" + i)
                    .nonMemberName("nonMember" + i)
                    .phoneNumber("0101234567" + i)
                    .build();
            nonMemberOrderList.add(nonMemberOrder);
            entityManager.persist(nonMemberOrder);
        }
        for (int i = 0; i < 3; i++) {
            MemberOrder memberOrder = MemberOrder.builder()
                    .orderNumber("M-" + i)
                    .name("member-order" + i)
                    .orderDateTime(LocalDateTime.of(2023, 1, i + 1, 0, 0))
                    .expectedTransportDate(LocalDate.of(2023, 1, i + 2))
                    .isHidden(false)
                    .usedPoint(0)
                    .shippingFee(0)
                    .wrappingFee(0)
                    .orderCode(OrderCode.MEMBER_ORDER)
                    .member(memberList.get(i % 5))
                    .memberAddress(memberAddressList.get(i % 5))
                    .build();
            memberOrderList.add(memberOrder);
            entityManager.persist(memberOrder);

            OrderStatusChangeLog orderStatusChangeLog= OrderStatusChangeLog.create(
                    memberOrder,
                    LocalDateTime.now(),
                    OrderStatusCode.ORDER
            );
            entityManager.persist(orderStatusChangeLog);
        }
        for (int i = 0; i < 3; i++) {
            SubscribeProduct subscribeProduct = SubscribeProduct.builder()
                    .ISSN("12345" + i)
                    .build();
            Subscribe subscribe = Subscribe.builder()
                    .member(memberList.get(i % 5))
                    .name("subscribe" + i)
                    .memberAddress(memberAddressList.get(i % 5))
                    .nextRenewalDate(LocalDate.of(2023, i + 1, 1))
                    .subscribeProduct(subscribeProduct)
                    .orderNumber("S-" + i)
                    .orderDateTime(LocalDateTime.of(2023, 1, i + 1, 0, 0))
                    .expectedTransportDate(LocalDate.of(2023, 1, i + 2))
                    .isHidden(false)
                    .usedPoint(0)
                    .shippingFee(0)
                    .wrappingFee(0)
                    .orderCode(OrderCode.MEMBER_SUBSCRIBE)
                    .build();
            SubscribeOrderList subscribeOrder = SubscribeOrderList.builder()
                    .isTransported(true)
                    .subscribe(subscribe)
                    .memberOrder(memberOrderList.get(i % 5))
                    .build();
            entityManager.persist(subscribeProduct);
            entityManager.persist(subscribe);
            entityManager.persist(subscribeOrder);
            this.subscribeList.add(subscribe);
        }

        entityManager.flush();
    }

    @Test
    @DisplayName("특정 주문을통해 주문 상품 개수를 조회한다")
    void getCountOfOrderProductByOrderId() throws Exception {
        // given
        String URL = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type";
        String ISBN = "0000000000001";
        int size = 10;

        SubscribeProduct subscribeProduct = DummySubscribeProduct.dummy();
        File thumbnailFile = DummyFile.dummy(URL + "/image.png");
        File ebookFile = DummyFile.dummy(URL + "/ebook.pdf");
        TotalDiscountRate totalDiscountRate = DummyTotalDiscountRate.dummy();
        entityManager.persist(subscribeProduct);
        entityManager.persist(thumbnailFile);
        entityManager.persist(ebookFile);
        entityManager.persist(totalDiscountRate);

        MemberOrder memberOrder = memberOrderList.get(0);
        for (int i = 0; i < size; i++) {
            Product product = DummyProduct.dummy(
                    ISBN + i,
                    subscribeProduct,
                    thumbnailFile,
                    ebookFile,
                    totalDiscountRate
            );
            entityManager.persist(product);

            OrderProduct orderProduct = OrderProduct.builder()
                    .order(memberOrder)
                    .product(product)
                    .isCancelled(false)
                    .quantity(10)
                    .build();
            entityManager.persist(orderProduct);
        }

        // when
        Long count = queryRepository.getCountOfOrderProductByOrderId(
                memberOrder.getId());

        // then
        assertThat(count).isEqualTo(size);
    }
}
