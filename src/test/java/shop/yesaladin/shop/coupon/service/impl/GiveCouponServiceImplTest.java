package shop.yesaladin.shop.coupon.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.core.ParameterizedTypeReference;
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
import shop.yesaladin.shop.coupon.adapter.websocket.CouponWebsocketMessageSender;
import shop.yesaladin.shop.coupon.domain.model.MemberCoupon;
import shop.yesaladin.shop.coupon.domain.repository.CommandMemberCouponRepository;
import shop.yesaladin.shop.coupon.domain.repository.QueryMemberCouponRepository;
import shop.yesaladin.shop.coupon.dto.CouponGroupAndLimitDto;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.dto.MemberDto;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;

@Disabled
@SuppressWarnings("unchecked")
class GiveCouponServiceImplTest {

    private GatewayProperties gatewayProperties;
    private CouponProducer couponProducer;
    private QueryMemberCouponRepository queryMemberCouponRepository;
    private CommandMemberCouponRepository commandMemberCouponRepository;
    private QueryMemberService queryMemberService;
    private RestTemplate restTemplate;
    private RedisTemplate<String, String> redisTemplate;
    private GiveCouponServiceImpl giveCouponService;
    private CouponWebsocketMessageSender websocketMessageSender;
    private ValueOperations<String, String> valueOperations = Mockito.mock(ValueOperations.class);

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setup() {
        gatewayProperties = Mockito.mock(GatewayProperties.class);
        couponProducer = Mockito.mock(CouponProducer.class);
        queryMemberCouponRepository = Mockito.mock(QueryMemberCouponRepository.class);
        commandMemberCouponRepository = Mockito.mock(CommandMemberCouponRepository.class);
        queryMemberService = Mockito.mock(QueryMemberService.class);
        restTemplate = Mockito.mock(RestTemplate.class);
        redisTemplate = Mockito.mock(RedisTemplate.class);
        websocketMessageSender = Mockito.mock(CouponWebsocketMessageSender.class);
        giveCouponService = new GiveCouponServiceImpl(
                gatewayProperties,
                couponProducer,
                queryMemberCouponRepository,
                commandMemberCouponRepository,
                queryMemberService,
                restTemplate,
                redisTemplate,
                websocketMessageSender
        );
        Mockito.when(gatewayProperties.getCouponUrl()).thenReturn("http://localhost:8085");
        Mockito.when(redisTemplate.opsForValue()).thenReturn(valueOperations);
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
        Mockito.when(restTemplate.exchange(
                        Mockito.eq(
                                "http://localhost:8085/v1/coupon-groups?trigger-type=SIGN_UP"),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.any(),
                        Mockito.any(ParameterizedTypeReference.class)
                ))
                .thenReturn(new ResponseEntity<>(ResponseDto.builder()
                        .data(couponGroupAndLimitDtoList)
                        .build(), HttpStatus.OK));

        Mockito.when(queryMemberCouponRepository.existsByMemberAndCouponGroupCodeList(
                Mockito.eq(
                        memberId),
                Mockito.argThat(list -> list.size() == 1 && list.contains(couponGroupCode))
        )).thenReturn(false);

        // when
        giveCouponService.sendCouponGiveRequest(memberId, TriggerTypeCode.SIGN_UP, null, LocalDateTime.now());

        // then
        ArgumentCaptor<CouponGiveRequestMessage> requestMessageCaptor = ArgumentCaptor.forClass(
                CouponGiveRequestMessage.class);
        Mockito.verify(couponProducer, Mockito.times(1))
                .produceGiveRequestLimitMessage(requestMessageCaptor.capture());
        Mockito.verify(restTemplate, Mockito.times(1))
                .exchange(
                        Mockito.eq("http://localhost:8085/v1/coupon-groups?trigger-type=SIGN_UP"),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.any(),
                        Mockito.any(ParameterizedTypeReference.class)
                );
        Mockito.verify(valueOperations, Mockito.times(1))
                .set(Mockito.anyString(), Mockito.eq(memberId), Mockito.eq(Duration.ofMinutes(30)));
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
        Mockito.when(restTemplate.exchange(
                        Mockito.eq(
                                "http://localhost:8085/v1/coupon-groups?trigger-type=SIGN_UP"),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.any(),
                        Mockito.any(ParameterizedTypeReference.class)
                ))
                .thenReturn(new ResponseEntity<>(ResponseDto.builder()
                        .data(couponGroupAndLimitDtoList)
                        .build(), HttpStatus.OK));

        Mockito.when(queryMemberCouponRepository.existsByMemberAndCouponGroupCodeList(
                Mockito.eq(
                        memberId),
                Mockito.argThat(list -> list.size() == 1 && list.contains(couponGroupCode))
        )).thenReturn(false);

        // when
        giveCouponService.sendCouponGiveRequest(memberId, TriggerTypeCode.SIGN_UP, null, LocalDateTime.now());

        // then
        ArgumentCaptor<CouponGiveRequestMessage> requestMessageCaptor = ArgumentCaptor.forClass(
                CouponGiveRequestMessage.class);
        Mockito.verify(couponProducer, Mockito.times(1))
                .produceGiveRequestMessage(requestMessageCaptor.capture());
        Mockito.verify(restTemplate, Mockito.times(1))
                .exchange(
                        Mockito.eq("http://localhost:8085/v1/coupon-groups?trigger-type=SIGN_UP"),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.any(),
                        Mockito.any(ParameterizedTypeReference.class)
                );
        Mockito.verify(valueOperations, Mockito.times(1))
                .set(Mockito.anyString(), Mockito.eq(memberId), Mockito.eq(Duration.ofMinutes(30)));
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
        Mockito.when(restTemplate.exchange(
                        Mockito.eq(
                                "http://localhost:8085/v1/coupon-groups?trigger-type=SIGN_UP"),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.any(),
                        Mockito.any(ParameterizedTypeReference.class)
                ))
                .thenReturn(new ResponseEntity<>(ResponseDto.builder()
                        .data(couponGroupAndLimitDtoList)
                        .build(), HttpStatus.OK));

        Mockito.when(queryMemberCouponRepository.existsByMemberAndCouponGroupCodeList(
                Mockito.eq(
                        memberId),
                Mockito.argThat(list -> list.size() == 1 && list.contains(couponGroupCode))
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
        Mockito.when(restTemplate.exchange(
                Mockito.eq(
                        "http://localhost:8085/v1/coupon-groups?trigger-type=SIGN_UP"),
                Mockito.eq(HttpMethod.GET),
                Mockito.any(),
                Mockito.any(ParameterizedTypeReference.class)
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
        Mockito.when(restTemplate.exchange(
                Mockito.eq(
                        "http://localhost:8085/v1/coupon-groups?trigger-type=SIGN_UP"),
                Mockito.eq(HttpMethod.GET),
                Mockito.any(),
                Mockito.any(ParameterizedTypeReference.class)
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
        MemberDto mockMemberDto = Mockito.mock(MemberDto.class);
        Member member = Mockito.mock(Member.class);
        ValueOperations mockValueOperation = Mockito.mock(ValueOperations.class);
        Mockito.when(redisTemplate.opsForValue()).thenReturn(mockValueOperation);
        Mockito.when(mockValueOperation.get("requestId")).thenReturn("member");
        Mockito.when(member.getLoginId()).thenReturn("member");
        Mockito.when(queryMemberService.findMemberByLoginId("member")).thenReturn(mockMemberDto);
        Mockito.when(mockMemberDto.toEntity()).thenReturn(member);

        // when
        giveCouponService.giveCouponToMember(responseMessage);

        // then
        ArgumentCaptor<MemberCoupon> argumentCaptor = ArgumentCaptor.forClass(MemberCoupon.class);
        Mockito.verify(mockValueOperation, Mockito.times(1)).get("requestId");
        Mockito.verify(queryMemberService, Mockito.times(1)).findMemberByLoginId("member");
        Mockito.verify(commandMemberCouponRepository, Mockito.times(1))
                .save(argumentCaptor.capture());
        Mockito.verify(couponProducer)
                .produceGivenResultMessage(Mockito.argThat(CouponCodesAndResultMessage::isSuccess));

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
        Mockito.verify(couponProducer)
                .produceGivenResultMessage(Mockito.argThat(message -> !message.isSuccess()));
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
        ValueOperations mockValueOperation = Mockito.mock(ValueOperations.class);
        Mockito.when(redisTemplate.opsForValue()).thenReturn(mockValueOperation);
        Mockito.when(mockValueOperation.get("requestId")).thenReturn(null);

        // when
        // then
        Assertions.assertThatThrownBy(() -> giveCouponService.giveCouponToMember(responseMessage))
                .isInstanceOf(ClientException.class);
        Mockito.verify(couponProducer)
                .produceGivenResultMessage(Mockito.argThat(message -> !message.isSuccess()));
    }
}
