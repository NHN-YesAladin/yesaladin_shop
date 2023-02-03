package shop.yesaladin.shop.coupon.service.impl;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.coupon.trigger.TriggerTypeCode;
import shop.yesaladin.shop.config.CouponServerMetaConfig;
import shop.yesaladin.shop.coupon.dto.CouponIssueRequestDto;
import shop.yesaladin.shop.coupon.dto.CouponIssueResponseDto;
import shop.yesaladin.shop.coupon.service.inter.CouponService;

/**
 * 회원 쿠폰 요청 및 지급 관련 서비스 구현 클래스 입니다.
 *
 * @author
 * @since
 */
@RequiredArgsConstructor
@Service
public class CouponServiceImpl implements CouponService {

    private final RestTemplate restTemplate;
    private final CouponServerMetaConfig couponServerMetaConfig;

    private List<CouponIssueResponseDto> getIssuedCouponList(
            TriggerTypeCode triggerTypeCode,
            Long couponId,
            int quantity
    ) {
        HttpEntity<CouponIssueRequestDto> httpEntity = new HttpEntity<>(new CouponIssueRequestDto(
                triggerTypeCode,
                couponId,
                quantity
        ));

        ResponseEntity<ResponseDto<List<CouponIssueResponseDto>>> responseEntity = restTemplate.exchange(
                couponServerMetaConfig.getCouponServerUrl() + "/v1/issuances",
                HttpMethod.POST,
                httpEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        return Objects.requireNonNull(responseEntity.getBody()).getData();
    }
}
