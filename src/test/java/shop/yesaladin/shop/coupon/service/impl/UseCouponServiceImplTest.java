package shop.yesaladin.shop.coupon.service.impl;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.coupon.message.CouponUseRequestResponseMessage;
import shop.yesaladin.shop.coupon.adapter.kafka.CouponProducer;
import shop.yesaladin.shop.coupon.domain.model.MemberCoupon;
import shop.yesaladin.shop.coupon.domain.repository.QueryMemberCouponRepository;
import shop.yesaladin.shop.coupon.dto.CouponCodeOnlyDto;
import shop.yesaladin.shop.coupon.dto.RequestIdOnlyDto;

@SuppressWarnings("unchecked")
class UseCouponServiceImplTest {

    private UseCouponServiceImpl useCouponService;
    private QueryMemberCouponRepository queryMemberCouponRepository;
    private CouponProducer couponProducer;
    private RedisTemplate<String, String> redisTemplate;
    private ListOperations<String, String> listOperations;
    private ValueOperations<String, String> valueOperations;
    private final static Clock clock = Clock.fixed(
            Instant.parse("2023-02-01T00:00:00.00Z"),
            ZoneId.of("UTC")
    );

    @BeforeEach
    void setUp() {
        queryMemberCouponRepository = Mockito.mock(QueryMemberCouponRepository.class);
        couponProducer = Mockito.mock(CouponProducer.class);
        redisTemplate = Mockito.mock(RedisTemplate.class);
        listOperations = Mockito.mock(ListOperations.class);
        valueOperations = Mockito.mock(ValueOperations.class);
        useCouponService = new UseCouponServiceImpl(
                queryMemberCouponRepository,
                couponProducer,
                redisTemplate,
                clock
        );

        Mockito.when(redisTemplate.opsForList()).thenReturn(listOperations);
        Mockito.when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    @DisplayName("쿠폰 사용 요청 메시지를 발행한다.")
    void sendCouponUseRequestSuccess() {
        // given
        List<String> couponCodes = List.of("1", "2", "3");
        List<MemberCoupon> expectedCoupons = List.of(
                Mockito.mock(MemberCoupon.class),
                Mockito.mock(MemberCoupon.class),
                Mockito.mock(MemberCoupon.class)
        );
        Mockito.when(queryMemberCouponRepository.findByCouponCodes(couponCodes))
                .thenReturn(expectedCoupons);
        expectedCoupons.forEach(coupon -> Mockito.when(coupon.isUsed()).thenReturn(false));
        // when
        RequestIdOnlyDto actual = useCouponService.sendCouponUseRequest("mongmeo", couponCodes);

        // then
        Mockito.verify(valueOperations, Mockito.times(1))
                .set(
                        Mockito.anyString(),
                        Mockito.eq("mongmeo"),
                        Mockito.eq(Duration.ofMinutes(30))
                );
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getRequestId()).hasSize(36);
    }

    @Test
    @DisplayName("쿠폰 중 하나라도 사용 불가능 상태라면 예외가 발생한다.")
    void sendCouponUseRequestFailCauseByCannotUseTest() {
        // given
        List<String> couponCodes = List.of("1", "2", "3");
        List<MemberCoupon> expectedCoupons = List.of(
                Mockito.mock(MemberCoupon.class),
                Mockito.mock(MemberCoupon.class),
                Mockito.mock(MemberCoupon.class)
        );
        Mockito.when(expectedCoupons.get(2).isUsed()).thenReturn(true);
        Mockito.when(queryMemberCouponRepository.findByCouponCodes(couponCodes))
                .thenReturn(expectedCoupons);

        // when
        // then
        Assertions.assertThatThrownBy(() -> useCouponService.sendCouponUseRequest(
                "mongmeo",
                couponCodes
        )).isInstanceOf(ClientException.class);
    }

    @Test
    @DisplayName("쿠폰 사용 요청에 대한 응답 메시지를 받아 쿠폰을 사용한다.")
    void useCouponSuccessTest() {
        // given
        CouponUseRequestResponseMessage requestResponseMessage = CouponUseRequestResponseMessage.builder()
                .success(true)
                .requestId("requestId")
                .build();
        List<String> expectedCouponCodes = List.of("1", "2", "3", "4");
        Mockito.when(listOperations.range("requestId", 0, -1)).thenReturn(expectedCouponCodes);
        List<MemberCoupon> expectedMemberCoupons = List.of(
                Mockito.mock(MemberCoupon.class),
                Mockito.mock(MemberCoupon.class),
                Mockito.mock(MemberCoupon.class),
                Mockito.mock(MemberCoupon.class)
        );
        Mockito.when(queryMemberCouponRepository.findByCouponCodes(expectedCouponCodes))
                .thenReturn(expectedMemberCoupons);

        // when
        List<CouponCodeOnlyDto> actual = useCouponService.useCoupon(requestResponseMessage);

        // then
        Mockito.verify(redisTemplate, Mockito.times(1)).opsForList();
        Mockito.verify(listOperations, Mockito.times(1)).range("requestId", 0, -1);
        Mockito.verify(queryMemberCouponRepository, Mockito.times(1))
                .findByCouponCodes(expectedCouponCodes);
        Mockito.verify(couponProducer, Mockito.times(1))
                .produceUsedResultMessage(Mockito.argThat(arg -> arg.isSuccess()
                        && arg.getCouponCodes().containsAll(expectedCouponCodes)));
        expectedMemberCoupons.forEach(memberCoupon -> Mockito.verify(memberCoupon, Mockito.times(1))
                .use());
        Assertions.assertThat(actual.get(0).getCouponCode()).isEqualTo("1");
        Assertions.assertThat(actual.get(1).getCouponCode()).isEqualTo("2");
        Assertions.assertThat(actual.get(2).getCouponCode()).isEqualTo("3");
        Assertions.assertThat(actual.get(3).getCouponCode()).isEqualTo("4");

    }
}