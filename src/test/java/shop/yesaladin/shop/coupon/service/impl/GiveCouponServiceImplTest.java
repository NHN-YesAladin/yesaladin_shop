package shop.yesaladin.shop.coupon.service.impl;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.common.exception.ServerException;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.coupon.dto.CouponGiveDto;
import shop.yesaladin.coupon.message.CouponCodesAndResultMessage;
import shop.yesaladin.coupon.message.CouponGiveRequestMessage;
import shop.yesaladin.coupon.message.CouponGiveRequestResponseMessage;
import shop.yesaladin.shop.config.GatewayProperties;
import shop.yesaladin.shop.coupon.adapter.kafka.CouponProducer;
import shop.yesaladin.shop.coupon.domain.model.MemberCoupon;
import shop.yesaladin.shop.coupon.domain.repository.CommandMemberCouponRepository;
import shop.yesaladin.shop.coupon.domain.repository.QueryMemberCouponRepository;
import shop.yesaladin.shop.coupon.dto.CouponGroupAndLimitDto;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.dto.MemberDto;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;

//@SuppressWarnings("all")
class GiveCouponServiceImplTest {

    private final static Clock clock = Clock.fixed(Instant.ofEpochSecond(100000), ZoneId.of("UTC"));
    private GatewayProperties gatewayProperties;
    private CouponProducer couponProducer;
    private QueryMemberCouponRepository queryMemberCouponRepository;
    private CommandMemberCouponRepository commandMemberCouponRepository;
    private QueryMemberService queryMemberService;
    private RestTemplate restTemplate;
    private RedisTemplate<String, String> redisTemplate;
    private GiveCouponServiceImpl giveCouponService;
    private ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
    private ApplicationEventPublisher applicationEventPublisher;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setup() {
        gatewayProperties = mock(GatewayProperties.class);
        couponProducer = mock(CouponProducer.class);
        queryMemberCouponRepository = mock(QueryMemberCouponRepository.class);
        commandMemberCouponRepository = mock(CommandMemberCouponRepository.class);
        queryMemberService = mock(QueryMemberService.class);
        restTemplate = mock(RestTemplate.class);
        redisTemplate = mock(RedisTemplate.class);
        applicationEventPublisher = mock(ApplicationEventPublisher.class);
        giveCouponService = new GiveCouponServiceImpl(
                gatewayProperties,
                couponProducer,
                queryMemberCouponRepository,
                commandMemberCouponRepository,
                queryMemberService,
                restTemplate,
                redisTemplate,
                applicationEventPublisher,
                clock
        );
        when(gatewayProperties.getCouponUrl()).thenReturn("http://localhost:8085");
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    @DisplayName("쿠폰 트리거 코드로 쿠폰 지급 요청 메시지 전송에 성공한다.")
    void sendCouponGiveRequestSuccessTest() {
        // given
        String memberId = "mongmeo";
        String couponGroupCode = "testCouponGroupCode";
        List<CouponGroupAndLimitDto> couponGroupAndLimitDtoList = List.of(new CouponGroupAndLimitDto(
                couponGroupCode,
                true
        ));
        when(restTemplate.exchange(
                eq(
                        "http://localhost:8085/v1/coupon-groups?trigger-type=SIGN_UP"),
                eq(HttpMethod.GET),
                any(),
                any(ParameterizedTypeReference.class)
        ))
                .thenReturn(new ResponseEntity<>(ResponseDto.builder()
                        .data(couponGroupAndLimitDtoList)
                        .build(), HttpStatus.OK));

        when(queryMemberCouponRepository.existsByMemberAndCouponGroupCodeList(
                eq(
                        memberId),
                argThat(list -> list.size() == 1 && list.contains(couponGroupCode))
        )).thenReturn(false);

        // when
        giveCouponService.sendCouponGiveRequest(
                memberId,
                TriggerTypeCode.SIGN_UP,
                null,
                LocalDateTime.now()
        );

        // then
        ArgumentCaptor<CouponGiveRequestMessage> requestMessageCaptor = ArgumentCaptor.forClass(
                CouponGiveRequestMessage.class);
        verify(couponProducer, times(1))
                .produceGiveRequestLimitMessage(requestMessageCaptor.capture());
        verify(restTemplate, times(1))
                .exchange(
                        eq("http://localhost:8085/v1/coupon-groups?trigger-type=SIGN_UP"),
                        eq(HttpMethod.GET),
                        any(),
                        any(ParameterizedTypeReference.class)
                );
        verify(valueOperations, times(1))
                .set(anyString(), eq(memberId), eq(Duration.ofMinutes(30)));
        CouponGiveRequestMessage actualRequestMessage = requestMessageCaptor.getValue();
        Assertions.assertThat(actualRequestMessage.getRequestId()).isNotBlank();
        Assertions.assertThat(actualRequestMessage.getCouponId()).isNull();
        Assertions.assertThat(actualRequestMessage.getTriggerTypeCode())
                .isEqualTo(TriggerTypeCode.SIGN_UP);
    }

    @Test
    @DisplayName("무제한 쿠폰인 경우 produceGiveRequestMessage 메서드로 요청을 보낸다.")
    void sendCouponGiveRequestLimitSuccessTest() {
        // given
        String memberId = "mongmeo";
        String couponGroupCode = "testCouponGroupCode";
        List<CouponGroupAndLimitDto> couponGroupAndLimitDtoList = List.of(new CouponGroupAndLimitDto(
                couponGroupCode,
                false
        ));
        when(restTemplate.exchange(
                eq(
                        "http://localhost:8085/v1/coupon-groups?trigger-type=SIGN_UP"),
                eq(HttpMethod.GET),
                any(),
                any(ParameterizedTypeReference.class)
        ))
                .thenReturn(new ResponseEntity<>(ResponseDto.builder()
                        .data(couponGroupAndLimitDtoList)
                        .build(), HttpStatus.OK));

        when(queryMemberCouponRepository.existsByMemberAndCouponGroupCodeList(
                eq(
                        memberId),
                argThat(list -> list.size() == 1 && list.contains(couponGroupCode))
        )).thenReturn(false);

        // when
        giveCouponService.sendCouponGiveRequest(
                memberId,
                TriggerTypeCode.SIGN_UP,
                null,
                LocalDateTime.now()
        );

        // then
        ArgumentCaptor<CouponGiveRequestMessage> requestMessageCaptor = ArgumentCaptor.forClass(
                CouponGiveRequestMessage.class);
        verify(couponProducer, times(1))
                .produceGiveRequestMessage(requestMessageCaptor.capture());
        verify(restTemplate, times(1))
                .exchange(
                        eq("http://localhost:8085/v1/coupon-groups?trigger-type=SIGN_UP"),
                        eq(HttpMethod.GET),
                        any(),
                        any(ParameterizedTypeReference.class)
                );
        verify(valueOperations, times(1))
                .set(anyString(), eq(memberId), eq(Duration.ofMinutes(30)));
        CouponGiveRequestMessage actualRequestMessage = requestMessageCaptor.getValue();
        Assertions.assertThat(actualRequestMessage.getRequestId()).isNotBlank();
        Assertions.assertThat(actualRequestMessage.getCouponId()).isNull();
        Assertions.assertThat(actualRequestMessage.getTriggerTypeCode())
                .isEqualTo(TriggerTypeCode.SIGN_UP);
    }

    @Test
    @DisplayName("회원이 이미 쿠폰을 가지고 있으면 지급을 거부하고 ClientException을 던진다.")
    void sendCouponGiveRequestFailCauseTest() {
        // given
        String memberId = "mongmeo";
        String couponGroupCode = "testCouponGroupCode";
        List<CouponGroupAndLimitDto> couponGroupAndLimitDtoList = List.of(new CouponGroupAndLimitDto(
                couponGroupCode,
                false
        ));
        when(restTemplate.exchange(
                eq(
                        "http://localhost:8085/v1/coupon-groups?trigger-type=SIGN_UP"),
                eq(HttpMethod.GET),
                any(),
                any(ParameterizedTypeReference.class)
        ))
                .thenReturn(new ResponseEntity<>(ResponseDto.builder()
                        .data(couponGroupAndLimitDtoList)
                        .build(), HttpStatus.OK));

        when(queryMemberCouponRepository.existsByMemberAndCouponGroupCodeList(
                eq(
                        memberId),
                argThat(list -> list.size() == 1 && list.contains(couponGroupCode))
        )).thenReturn(true);

        // when
        // then
        Assertions.assertThatThrownBy(() -> giveCouponService.sendCouponGiveRequest(
                memberId,
                TriggerTypeCode.SIGN_UP,
                null, LocalDateTime.now()
        )).isInstanceOf(ClientException.class);
    }

    @Test
    @DisplayName("쿠폰 그룹을 찾지 못해 404를 응답하면 지급에 실패하고 ClientException을 던진다.")
    void sendCouponGiveRequestFailCauseByCouponGroupNotFoundTest() {
        // given
        String memberId = "mongmeo";
        when(restTemplate.exchange(
                eq(
                        "http://localhost:8085/v1/coupon-groups?trigger-type=SIGN_UP"),
                eq(HttpMethod.GET),
                any(),
                any(ParameterizedTypeReference.class)
        )).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // when
        // then
        Assertions.assertThatThrownBy(() -> giveCouponService.sendCouponGiveRequest(
                memberId,
                TriggerTypeCode.SIGN_UP,
                null, LocalDateTime.now()
        )).isInstanceOf(ClientException.class);
    }

    @Test
    @DisplayName("쿠폰 서버가 제대로 작동하지 않아 오류를 응답하면 지급에 실패하고 ServerException을 던진다.")
    void sendCouponGiveRequestFailCauseByCouponServerResponseErrorTest() {
        // given
        String memberId = "mongmeo";
        when(restTemplate.exchange(
                eq(
                        "http://localhost:8085/v1/coupon-groups?trigger-type=SIGN_UP"),
                eq(HttpMethod.GET),
                any(),
                any(ParameterizedTypeReference.class)
        )).thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        // when
        // then
        Assertions.assertThatThrownBy(() -> giveCouponService.sendCouponGiveRequest(
                memberId,
                TriggerTypeCode.SIGN_UP,
                null, LocalDateTime.now()
        )).isInstanceOf(ServerException.class);
    }

    @Test
    @DisplayName("멤버에게 쿠폰을 지급한다.")
    void giveCouponToMemberSuccessTest() {
        // given
        CouponGiveRequestResponseMessage responseMessage = CouponGiveRequestResponseMessage.builder()
                .requestId("requestId")
                .success(true)
                .errorMessage(null)
                .coupons(List.of(CouponGiveDto.builder()
                        .couponGroupCode("couponGroup")
                        .couponCodes(List.of("couponCode1"))
                        .build()))
                .build();
        MemberDto mockMemberDto = mock(MemberDto.class);
        Member member = mock(Member.class);
        ValueOperations mockValueOperation = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(mockValueOperation);
        when(mockValueOperation.get("requestId")).thenReturn("member");
        when(member.getLoginId()).thenReturn("member");
        when(queryMemberService.findMemberByLoginId("member")).thenReturn(mockMemberDto);
        when(mockMemberDto.toEntity()).thenReturn(member);

        // when
        giveCouponService.giveCouponToMember(responseMessage);

        // then
        ArgumentCaptor<MemberCoupon> argumentCaptor = ArgumentCaptor.forClass(MemberCoupon.class);
        verify(mockValueOperation, times(1)).get("requestId");
        verify(queryMemberService, times(1)).findMemberByLoginId("member");
        verify(commandMemberCouponRepository, times(1))
                .save(argumentCaptor.capture());
        verify(couponProducer)
                .produceGivenResultMessage(argThat(CouponCodesAndResultMessage::isSuccess));

        MemberCoupon actualMemberCoupon = argumentCaptor.getValue();
        Assertions.assertThat(actualMemberCoupon.getMember()).isEqualTo(member);
        Assertions.assertThat(actualMemberCoupon.getCouponCode()).isEqualTo("couponCode1");
        Assertions.assertThat(actualMemberCoupon.getCouponGroupCode()).isEqualTo("couponGroup");
    }

    @Test
    @DisplayName("쿠폰 서버에서 지급 요청 처리에 실패하면 쿠폰을 지급하지 않고 예외를 던진다.")
    void giveCouponToMemberFailCauseByRequestNotSucceededTest() {
        // given
        CouponGiveRequestResponseMessage responseMessage = CouponGiveRequestResponseMessage.builder()
                .requestId("requestId")
                .success(false)
                .errorMessage(null)
                .coupons(List.of(CouponGiveDto.builder()
                        .couponGroupCode("couponGroup")
                        .couponCodes(List.of("couponCode1"))
                        .build()))
                .build();

        // when
        // then
        Assertions.assertThatThrownBy(() -> giveCouponService.giveCouponToMember(responseMessage))
                .isInstanceOf(ClientException.class);
        verify(couponProducer)
                .produceGivenResultMessage(argThat(message -> !message.isSuccess()));
    }


    @Test
    @DisplayName("request id가 존재하지 않으면 쿠폰을 지급하지 않고 예외를 던진다.")
    void giveCouponToMemberFailCauseByRequestIdNotFound() {
        // given
        CouponGiveRequestResponseMessage responseMessage = CouponGiveRequestResponseMessage.builder()
                .requestId("requestId")
                .success(true)
                .errorMessage(null)
                .coupons(List.of(CouponGiveDto.builder()
                        .couponGroupCode("couponGroup")
                        .couponCodes(List.of("couponCode1"))
                        .build()))
                .build();
        ValueOperations mockValueOperation = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(mockValueOperation);
        when(mockValueOperation.get("requestId")).thenReturn(null);

        // when
        // then
        Assertions.assertThatThrownBy(() -> giveCouponService.giveCouponToMember(responseMessage))
                .isInstanceOf(ClientException.class);
        verify(couponProducer)
                .produceGivenResultMessage(argThat(message -> !message.isSuccess()));
    }

    @Test
    @DisplayName("이달의 쿠폰 지급을 요청할 때 이달의 쿠폰 정책이 존재하지 않아 예외 발생")
    void checkMonthlyCouponIssueRequestTimeFailCauseByPolicyNotExistTest() {
        HashOperations hashOperations = mock(HashOperations.class);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.hasKey(anyString(), any())).thenReturn(Boolean.FALSE);

        // when
        Assertions.assertThatThrownBy(() -> giveCouponService.sendCouponGiveRequest(
                        "memberId",
                        TriggerTypeCode.COUPON_OF_THE_MONTH,
                        1L,
                        LocalDateTime.now()
                ))
                .isInstanceOf(ClientException.class)
                .hasMessageContaining("Not found any monthly coupon open date time.");
    }

    @Test
    @DisplayName("이달의 쿠폰 지급 요청을 오픈시간 전에 시도할시 예외 발생")
    void checkMonthlyCouponIssueRequestTimeFailCauseByNotOpenTest() {
        LocalDateTime openDateTime = LocalDateTime.now().minusDays(1);
        HashOperations hashOperations = mock(HashOperations.class);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.hasKey(anyString(), any())).thenReturn(Boolean.TRUE);
        when(hashOperations.get(any(), any())).thenReturn(openDateTime.toString());

        // when
        Assertions.assertThatThrownBy(() -> giveCouponService.sendCouponGiveRequest(
                "memberId",
                TriggerTypeCode.COUPON_OF_THE_MONTH,
                1L,
                LocalDateTime.now()
        )).isInstanceOf(ClientException.class).hasMessageContaining("before open time");
    }

    @Test
    @DisplayName("수동 발행 타입 쿠폰 지급 요청을 10초 내에 여러번 수행한 경우 예외 발생")
    void checkMonthlyCouponIssueRequestTimeTest() {
        HashOperations hashOperations = mock(HashOperations.class);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.hasKey(anyString(), any())).thenReturn(Boolean.TRUE);
        when(hashOperations.get(anyString(), any())).thenReturn(LocalDateTime.now()
                .minusDays(1)
                .toString());
        when(redisTemplate.hasKey(anyString())).thenReturn(Boolean.TRUE);
        Assertions.assertThatThrownBy(() -> giveCouponService.sendCouponGiveRequest(
                "memberId",
                TriggerTypeCode.SIGN_UP,
                1L,
                LocalDateTime.now()
        )).isInstanceOf(ClientException.class).hasMessageContaining("이미 처리된 요청입니다.");
    }
}
