package shop.yesaladin.shop.coupon.service.impl;

import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.common.exception.ServerException;
import shop.yesaladin.shop.config.GatewayProperties;
import shop.yesaladin.shop.coupon.domain.model.MemberCoupon;
import shop.yesaladin.shop.coupon.domain.repository.QueryMemberCouponRepository;
import shop.yesaladin.shop.coupon.dto.MemberCouponSummaryDto;

class QueryMemberCouponServiceImplTest {

    private QueryMemberCouponRepository repository;
    private RestTemplate restTemplate;
    private QueryMemberCouponServiceImpl service;

    @BeforeEach
    void setup() {
        GatewayProperties gatewayProperties = Mockito.mock(GatewayProperties.class);
        Mockito.when(gatewayProperties.getCouponUrl()).thenReturn("http://localhost:8085");

        repository = Mockito.mock(QueryMemberCouponRepository.class);
        restTemplate = Mockito.mock(RestTemplate.class);
        service = new QueryMemberCouponServiceImpl(repository, gatewayProperties, restTemplate);
    }

    @Test
    @DisplayName("회원 ID로 쿠폰 코드들을 조회하여 쿠폰 서버에서 쿠폰 정보를 가져온다.")
    void getMemberCouponSummaryListSuccess() {
        // given
        Pageable pageable = PageRequest.of(0, 20);
        Mockito.when(repository.findMemberCouponByMemberId(pageable, "member"))
                .thenReturn(new PageImpl<>(List.of(MemberCoupon.builder()
                        .couponCode("coupon-code")
                        .build())));
        List<MemberCouponSummaryDto> expectedData = List.of(Mockito.mock(MemberCouponSummaryDto.class));
        Mockito.when(restTemplate.exchange(
                        Mockito.eq(
                                "http://localhost:8085/v1/coupons?couponCodes=coupon-code"),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.eq(null),
                        Mockito.any(ParameterizedTypeReference.class)
                ))
                .thenReturn(ResponseEntity.of(Optional.of(ResponseDto.<List<MemberCouponSummaryDto>>builder()
                        .success(true)
                        .status(HttpStatus.OK)
                        .data(expectedData)
                        .build())));
        // when
        List<MemberCouponSummaryDto> actual = service.getMemberCouponSummaryList(
                pageable,
                "member"
        );
        // then
        Mockito.verify(repository, Mockito.times(1)).findMemberCouponByMemberId(pageable, "member");
        Mockito.verify(restTemplate, Mockito.times(1))
                .exchange(
                        Mockito.eq("http://localhost:8085/v1/coupons?couponCodes=coupon-code"),
                        Mockito.eq(HttpMethod.GET),
                        Mockito.eq(null),
                        Mockito.any(ParameterizedTypeReference.class)
                );
        Assertions.assertThat(actual).isEqualTo(expectedData);
    }

    @Test
    @DisplayName("쿠폰 서버에서 404를 응답하면 Client Exception이 발생한다.")
    void getMemberCouponSummaryListFailCauseResponseStatusCode404() {
        // given
        Pageable pageable = PageRequest.of(0, 20);
        Mockito.when(repository.findMemberCouponByMemberId(pageable, "member"))
                .thenReturn(new PageImpl<>(List.of(MemberCoupon.builder()
                        .couponCode("coupon-code")
                        .build())));
        Mockito.when(restTemplate.exchange(
                Mockito.eq(
                        "http://localhost:8085/v1/coupons?couponCodes=coupon-code"),
                Mockito.eq(HttpMethod.GET),
                Mockito.eq(null),
                Mockito.any(ParameterizedTypeReference.class)
        )).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        // when
        // then
        Assertions.assertThatThrownBy(() -> service.getMemberCouponSummaryList(pageable, "member"))
                .isInstanceOf(ClientException.class);
    }

    @Test
    @DisplayName("쿠폰 서버에서 2xx, 404외의 다른 코드로 응답하면 Server Exception이 발생한다.")
    void getMemberCouponSummaryListFailCauseUnexpectedResponse() {
        // given
        Pageable pageable = PageRequest.of(0, 20);
        Mockito.when(repository.findMemberCouponByMemberId(pageable, "member"))
                .thenReturn(new PageImpl<>(List.of(MemberCoupon.builder()
                        .couponCode("coupon-code")
                        .build())));
        Mockito.when(restTemplate.exchange(
                Mockito.eq(
                        "http://localhost:8085/v1/coupons?couponCodes=coupon-code"),
                Mockito.eq(HttpMethod.GET),
                Mockito.eq(null),
                Mockito.any(ParameterizedTypeReference.class)
        )).thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
        // when
        // then
        Assertions.assertThatThrownBy(() -> service.getMemberCouponSummaryList(pageable, "member"))
                .isInstanceOf(ServerException.class);
    }

    @Test
    @DisplayName("쿠폰 서버에서 빈 본문을 응답하면 Server Exception이 발생한다.")
    void getMemberCouponSummaryListFailCauseResponseBodyIsEmpty() {
        // given
        Pageable pageable = PageRequest.of(0, 20);
        Mockito.when(repository.findMemberCouponByMemberId(pageable, "member"))
                .thenReturn(new PageImpl<>(List.of(MemberCoupon.builder()
                        .couponCode("coupon-code")
                        .build())));
        Mockito.when(restTemplate.exchange(
                Mockito.eq(
                        "http://localhost:8085/v1/coupons?couponCodes=coupon-code"),
                Mockito.eq(HttpMethod.GET),
                Mockito.eq(null),
                Mockito.any(ParameterizedTypeReference.class)
        )).thenReturn(ResponseEntity.of(Optional.empty()));

        // when
        // then
        Assertions.assertThatThrownBy(() -> service.getMemberCouponSummaryList(pageable, "member"))
                .isInstanceOf(ServerException.class);
    }
}