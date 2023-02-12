package shop.yesaladin.shop.order.persistence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import shop.yesaladin.shop.order.domain.model.Subscribe;
import shop.yesaladin.shop.order.domain.model.SubscribeOrderList;
import shop.yesaladin.shop.order.dto.OrderProductResponseDto;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;


@Transactional
@SpringBootTest
class QueryDslOrderProductRepositoryTest {

    @Autowired
    private QueryDslOrderProductRepository queryDslOrderProductRepository;
    @Autowired
    private EntityManager entityManager;

    private List<Product> products;
    private NonMemberOrder nonMemberOrder;
    private MemberOrder memberOrder;
    private Subscribe subscribe;
    private Member member;
    private List<OrderProduct> orderProducts;

    @BeforeEach
    void setUp() {
        products = new ArrayList<>();
        orderProducts = new ArrayList<>();

        member = Member.builder()
                .nickname("test")
                .name("name")
                .loginId("id")
                .password("password")
                .birthYear(2002)
                .birthMonth(5)
                .birthDay(2)
                .email("admin@mongmeo.dev")
                .phone("0101234567")
                .signUpDate(LocalDate.of(2023, 1, 1))
                .withdrawalDate(null)
                .isWithdrawal(false)
                .isBlocked(false)
                .memberGrade(MemberGrade.WHITE)
                .memberGenderCode(MemberGenderCode.MALE)
                .build();

        MemberAddress memberAddress = MemberAddress.builder()
                .member(member)
                .address("address")
                .build();
        entityManager.persist(member);
        entityManager.persist(memberAddress);

        nonMemberOrder = NonMemberOrder.builder()
                .orderNumber("NonMember-order-")
                .name("non-member-order")
                .orderDateTime(LocalDateTime.of(2023, 1, 1, 0, 0))
                .expectedTransportDate(LocalDate.of(2023, 1, 2))
                .isHidden(false)
                .usedPoint(0)
                .shippingFee(3000)
                .wrappingFee(3000)
                .totalAmount(16000)
                .orderCode(OrderCode.NON_MEMBER_ORDER)
                .address("address")
                .nonMemberName("nonMember")
                .phoneNumber("0101234567")
                .recipientName("수령인 이름")
                .recipientPhoneNumber("수령인 폰번호")
                .build();
        entityManager.persist(nonMemberOrder);

        memberOrder = MemberOrder.builder()
                .orderNumber("Member-order-")
                .name("member-order")
                .orderDateTime(LocalDateTime.of(2023, 1, 1, 0, 0))
                .expectedTransportDate(LocalDate.of(2023, 1, 2))
                .isHidden(false)
                .usedPoint(0)
                .shippingFee(3000)
                .wrappingFee(3000)
                .totalAmount(16000)
                .orderCode(OrderCode.MEMBER_ORDER)
                .recipientName("friend")
                .recipientPhoneNumber("01011111111")
                .member(member)
                .memberAddress(memberAddress)
                .recipientName("수령인 이름")
                .recipientPhoneNumber("수령인 폰번호")
                .build();
        entityManager.persist(memberOrder);

        SubscribeProduct subscribeProduct = SubscribeProduct.builder()
                .ISSN("12345")
                .build();
        subscribe = Subscribe.builder()
                .member(member)
                .name("subscribe")
                .memberAddress(memberAddress)
                .nextRenewalDate(LocalDate.of(2023, 1, 1))
                .subscribeProduct(subscribeProduct)
                .orderNumber("Subscribe-order-")
                .orderDateTime(LocalDateTime.of(2023, 1, 1, 0, 0))
                .expectedTransportDate(LocalDate.of(2023, 1, 2))
                .isHidden(false)
                .usedPoint(0)
                .shippingFee(3000)
                .wrappingFee(3000)
                .totalAmount(16000)
                .orderCode(OrderCode.MEMBER_SUBSCRIBE)
                .recipientName("수령인 이름")
                .recipientPhoneNumber("수령인 폰번호")
                .build();
        SubscribeOrderList subscribeOrder = SubscribeOrderList.builder()
                .isTransported(true)
                .subscribe(subscribe)
                .memberOrder(memberOrder)
                .build();
        entityManager.persist(subscribeProduct);
        entityManager.persist(subscribe);
        entityManager.persist(subscribeOrder);

        String isbn = "000000000000";
        String url = "https://api-storage.cloud.toast.com/v1/AUTH/container/domain/type";

        File thumbnailFile = DummyFile.dummy(url + "/image.png");
        File ebookFile = DummyFile.dummy(url + "/ebook.pdf");
        TotalDiscountRate totalDiscountRate = DummyTotalDiscountRate.dummy();

        for (int i = 0; i < 10; i++) {
            Product product = DummyProduct.dummy(
                    isbn + i,
                    subscribeProduct,
                    thumbnailFile,
                    ebookFile,
                    totalDiscountRate
            );
            entityManager.persist(thumbnailFile);
            entityManager.persist(ebookFile);
            entityManager.persist(totalDiscountRate);
            entityManager.persist(product);
            products.add(product);
        }


    }

    @Test
    void findAllByOrderNumber_memberOrder() {
        // given
        int count = 0;
        for (Product product : products) {
            OrderProduct orderProduct = OrderProduct.builder()
                    .product(product)
                    .order(memberOrder)
                    .quantity(count++)
                    .isCanceled(false)
                    .build();
            entityManager.persist(orderProduct);
            orderProducts.add(orderProduct);
        }
        entityManager.flush();

        // when
        List<OrderProductResponseDto> responseDtos = queryDslOrderProductRepository.findAllByOrderNumber(
                memberOrder.getOrderNumber());

        // then
        Assertions.assertThat(responseDtos).hasSize(10);
        Assertions.assertThat(responseDtos.get(0).getProductDto().getProductId())
                .isEqualTo(orderProducts.get(9).getProduct().getId());
        Assertions.assertThat(responseDtos.get(0).getProductDto().getIsbn())
                .isEqualTo(orderProducts.get(9).getProduct().getIsbn());
    }

    @Test
    void findAllByOrderNumber_nonMemberOrder() {
        // given
        int count = 0;
        for (Product product : products) {
            OrderProduct orderProduct = OrderProduct.builder()
                    .product(product)
                    .order(nonMemberOrder)
                    .quantity(count++)
                    .isCanceled(false)
                    .build();
            entityManager.persist(orderProduct);
            orderProducts.add(orderProduct);
        }
        entityManager.flush();

        // when
        List<OrderProductResponseDto> responseDtos = queryDslOrderProductRepository.findAllByOrderNumber(
                nonMemberOrder.getOrderNumber());

        // then
        Assertions.assertThat(responseDtos).hasSize(10);
        Assertions.assertThat(responseDtos.get(0).getProductDto().getProductId())
                .isEqualTo(orderProducts.get(9).getProduct().getId());
        Assertions.assertThat(responseDtos.get(0).getProductDto().getIsbn())
                .isEqualTo(orderProducts.get(9).getProduct().getIsbn());
    }

    @Test
    void findAllByOrderNumber_subscribe() {
        // given
        int count = 0;
        for (Product product : products) {
            OrderProduct orderProduct = OrderProduct.builder()
                    .product(product)
                    .order(subscribe)
                    .quantity(count++)
                    .isCanceled(false)
                    .build();
            entityManager.persist(orderProduct);
            orderProducts.add(orderProduct);
        }
        entityManager.flush();

        // when
        List<OrderProductResponseDto> responseDtos = queryDslOrderProductRepository.findAllByOrderNumber(
                subscribe.getOrderNumber());

        // then
        Assertions.assertThat(responseDtos).hasSize(10);
        Assertions.assertThat(responseDtos.get(0).getProductDto().getProductId())
                .isEqualTo(orderProducts.get(9).getProduct().getId());
        Assertions.assertThat(responseDtos.get(0).getProductDto().getIsbn())
                .isEqualTo(orderProducts.get(9).getProduct().getIsbn());
    }
}
