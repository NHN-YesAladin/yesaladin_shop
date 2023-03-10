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
    @DisplayName("?????? ????????? ????????? ?????? ?????? ?????? ????????? ????????? ????????????.")
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
    @DisplayName("????????? ????????? ?????? produceGiveRequestMessage ???????????? ????????? ?????????.")
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
    @DisplayName("????????? ?????? ????????? ????????? ????????? ????????? ???????????? ClientException??? ?????????.")
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
    @DisplayName("?????? ????????? ?????? ?????? 404??? ???????????? ????????? ???????????? ClientException??? ?????????.")
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
    @DisplayName("?????? ????????? ????????? ???????????? ?????? ????????? ???????????? ????????? ???????????? ServerException??? ?????????.")
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
    @DisplayName("???????????? ????????? ????????????.")
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
    @DisplayName("?????? ???????????? ?????? ?????? ????????? ???????????? ????????? ???????????? ?????? ????????? ?????????.")
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
    @DisplayName("request id??? ???????????? ????????? ????????? ???????????? ?????? ????????? ?????????.")
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
    @DisplayName("????????? ?????? ????????? ????????? ??? ????????? ?????? ????????? ???????????? ?????? ?????? ??????")
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
    @DisplayName("????????? ?????? ?????? ????????? ???????????? ?????? ???????????? ?????? ??????")
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
    @DisplayName("?????? ?????? ?????? ?????? ?????? ????????? 10??? ?????? ????????? ????????? ?????? ?????? ??????")
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
        )).isInstanceOf(ClientException.class).hasMessageContaining("?????? ????????? ???????????????.");
    }
}
