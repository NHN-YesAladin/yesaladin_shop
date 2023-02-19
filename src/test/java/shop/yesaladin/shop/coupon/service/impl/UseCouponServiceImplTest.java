package shop.yesaladin.shop.coupon.service.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.coupon.code.CouponTypeCode;
import shop.yesaladin.coupon.message.CouponUseRequestResponseMessage;
import shop.yesaladin.shop.coupon.adapter.kafka.CouponProducer;
import shop.yesaladin.shop.coupon.domain.model.MemberCoupon;
import shop.yesaladin.shop.coupon.domain.repository.QueryMemberCouponRepository;
import shop.yesaladin.shop.coupon.dto.CouponCodeOnlyDto;
import shop.yesaladin.shop.coupon.dto.MemberCouponSummaryDto;
import shop.yesaladin.shop.coupon.dto.RequestIdOnlyDto;
import shop.yesaladin.shop.coupon.service.inter.QueryMemberCouponService;
import shop.yesaladin.shop.point.service.inter.CommandPointHistoryService;

@SuppressWarnings("all")
class UseCouponServiceImplTest {

    private UseCouponServiceImpl useCouponService;
    private QueryMemberCouponRepository queryMemberCouponRepository;
    private CouponProducer couponProducer;
    private QueryMemberCouponService queryMemberCouponService;
    private CommandPointHistoryService commandPointHistoryService;
    private RedisTemplate<String, String> redisTemplate;
    private ListOperations<String, String> listOperations;
    private ApplicationEventPublisher applicationEventPublisher;
    private ValueOperations<String, String> valueOperations;
    private RedisOperations<String, String> redisOperations;
    private final static Clock clock = Clock.fixed(
            Instant.parse("2023-02-01T00:00:00.00Z"),
            ZoneId.of("UTC")
    );

    @BeforeEach
    void setUp() {
        queryMemberCouponRepository = Mockito.mock(QueryMemberCouponRepository.class);
        couponProducer = Mockito.mock(CouponProducer.class);
        queryMemberCouponService = Mockito.mock(QueryMemberCouponService.class);
        commandPointHistoryService = Mockito.mock(CommandPointHistoryService.class);
        redisTemplate = Mockito.mock(RedisTemplate.class);
        listOperations = Mockito.mock(ListOperations.class);
        redisOperations = Mockito.mock(RedisOperations.class);
        valueOperations = Mockito.mock(ValueOperations.class);
        applicationEventPublisher = Mockito.mock(ApplicationEventPublisher.class);
        useCouponService = new UseCouponServiceImpl(
                queryMemberCouponRepository,
                couponProducer,
                queryMemberCouponService,
                commandPointHistoryService,
                applicationEventPublisher,
                redisTemplate,
                clock
        );

        when(redisTemplate.opsForList()).thenReturn(listOperations);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(listOperations.getOperations()).thenReturn(redisOperations);
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
        when(queryMemberCouponRepository.findByCouponCodes(couponCodes))
                .thenReturn(expectedCoupons);
        expectedCoupons.forEach(coupon -> when(coupon.isUsed()).thenReturn(false));
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
        when(expectedCoupons.get(2).isUsed()).thenReturn(true);
        when(queryMemberCouponRepository.findByCouponCodes(couponCodes))
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
        when(listOperations.range("requestId-codes", 0, -1)).thenReturn(expectedCouponCodes);
        List<MemberCoupon> expectedMemberCoupons = List.of(
                Mockito.mock(MemberCoupon.class),
                Mockito.mock(MemberCoupon.class),
                Mockito.mock(MemberCoupon.class),
                Mockito.mock(MemberCoupon.class)
        );
        when(queryMemberCouponRepository.findByCouponCodes(expectedCouponCodes))
                .thenReturn(expectedMemberCoupons);

        // when
        List<CouponCodeOnlyDto> actual = useCouponService.useCoupon(requestResponseMessage);

        // then
        Mockito.verify(redisTemplate, Mockito.times(2)).opsForList();
        Mockito.verify(listOperations, Mockito.times(1)).range("requestId-codes", 0, -1);
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

    @Test
    @DisplayName("쿠폰 사용 요청 결과가 실패라면 예외가 발생한다.")
    void useCouponFailCauseByRequestNotSuccessTest() {
        // given
        CouponUseRequestResponseMessage responseMessage = new CouponUseRequestResponseMessage(
                "1",
                false,
                "fail"
        );

        // when
        // then
        Assertions.assertThatThrownBy(() -> useCouponService.useCoupon(responseMessage))
                .isInstanceOf(ClientException.class);
    }

    @Test
    @DisplayName("requestId가 존재하지 않으면 예외가 발생한다.")
    void useCouponFailCauseByRequestIdNotExistsTest() {
        // given
        CouponUseRequestResponseMessage responseMessage = new CouponUseRequestResponseMessage(
                "1",
                true,
                null
        );
        when(listOperations.range("1-codes" , 0, -1)).thenReturn(null);

        // when
        // then
        Assertions.assertThatThrownBy(() -> useCouponService.useCoupon(responseMessage))
                .isInstanceOf(ClientException.class);
    }

    @Test
    @DisplayName("쿠폰 사용 취소 메시지를 발행한다.")
    void cancelUseCouponTest() {
        // given
        List<String> couponCodes = List.of("1", "2", "3");

        // when
        List<CouponCodeOnlyDto> actual = useCouponService.cancelCouponUse(couponCodes);

        // then
        Mockito.verify(couponProducer, Mockito.times(1))
                .produceUseRequestCancelMessage(Mockito.argThat(arg -> arg.getCouponCodes()
                        .equals(couponCodes)));
        Assertions.assertThat(actual.get(0).getCouponCode()).isEqualTo(couponCodes.get(0));
        Assertions.assertThat(actual.get(1).getCouponCode()).isEqualTo(couponCodes.get(1));
        Assertions.assertThat(actual.get(2).getCouponCode()).isEqualTo(couponCodes.get(2));
    }

    @Test
    @DisplayName("포인트 쿠폰 사용시 포인트 적립 성공")
    void savePointWithCouponUsageTest() {
        // given
        CouponUseRequestResponseMessage requestResponseMessage = CouponUseRequestResponseMessage.builder()
                .success(true)
                .requestId("requestId")
                .build();

        List<String> expectedCouponCodes = List.of("1");
        when(listOperations.range("requestId", 0, -1)).thenReturn(expectedCouponCodes);

        List<MemberCoupon> expectedMemberCoupons = List.of(
                Mockito.mock(MemberCoupon.class)
        );

        MemberCouponSummaryDto memberCouponSummaryDto = MemberCouponSummaryDto.builder()
                .couponTypeCode(CouponTypeCode.POINT)
                .amount(500)
                .build();
        List<MemberCouponSummaryDto> expectedMemberCouponSummaryList = List.of(
                memberCouponSummaryDto
        );

        String memberId = "memberId";

        when(queryMemberCouponRepository.findByCouponCodes(expectedCouponCodes))
                .thenReturn(expectedMemberCoupons);
        when(queryMemberCouponService.getMemberCouponSummaryList(Mockito.anyList()))
                .thenReturn(expectedMemberCouponSummaryList);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(Mockito.anyString())).thenReturn(memberId);

        // when
        List<CouponCodeOnlyDto> actual = useCouponService.useCoupon(requestResponseMessage);

        // then
        Mockito.verify(queryMemberCouponService).getMemberCouponSummaryList(Mockito.anyList());
        Mockito.verify(commandPointHistoryService, times(1)).save(Mockito.any());
    }
}