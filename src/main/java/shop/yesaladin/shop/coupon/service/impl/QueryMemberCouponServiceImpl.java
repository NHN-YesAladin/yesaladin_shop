package shop.yesaladin.shop.coupon.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.common.exception.ServerException;
import shop.yesaladin.shop.config.GatewayProperties;
import shop.yesaladin.shop.coupon.domain.model.MemberCoupon;
import shop.yesaladin.shop.coupon.domain.repository.QueryMemberCouponRepository;
import shop.yesaladin.shop.coupon.dto.MemberCouponSummaryDto;
import shop.yesaladin.shop.coupon.service.inter.QueryMemberCouponService;

/**
 * 회원이 가진 쿠폰 정보를 조회하기 위한 서비스 인터페이스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class QueryMemberCouponServiceImpl implements QueryMemberCouponService {

    private final QueryMemberCouponRepository memberCouponRepository;
    private final GatewayProperties gatewayProperties;
    private final RestTemplate restTemplate;

    @Override
    public List<MemberCouponSummaryDto> getMemberCouponSummaryList(
            Pageable pageable, String memberId
    ) {
        List<String> memberCouponCodeList = getMemberCouponCodeList(pageable, memberId);

        ResponseDto<List<MemberCouponSummaryDto>> response = tryGetCouponSummary(
                memberCouponCodeList);

        return response.getData();
    }

    private ResponseDto<List<MemberCouponSummaryDto>> tryGetCouponSummary(List<String> memberCouponCodeList) {

        try {
            String couponSummaryRequestUrl = UriComponentsBuilder.fromUriString(gatewayProperties.getCouponUrl())
                    .pathSegment("v1", "coupons")
                    .queryParam("couponCodes", memberCouponCodeList)
                    .toUriString();
            ResponseDto<List<MemberCouponSummaryDto>> response = restTemplate.exchange(
                    couponSummaryRequestUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ResponseDto<List<MemberCouponSummaryDto>>>() {
                    }
            ).getBody();
            return Optional.ofNullable(response)
                    .orElseThrow(() -> new ServerException(
                            ErrorCode.INTERNAL_SERVER_ERROR,
                            "Server received empty response"
                    ));
        } catch (HttpClientErrorException e) {
            log.error("", e);

            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new ClientException(ErrorCode.NOT_FOUND, "Invalid coupon code");
            }
            throw new ServerException(
                    ErrorCode.INTERNAL_SERVER_ERROR,
                    "Cannot send request to server"
            );
        }
    }

    private List<String> getMemberCouponCodeList(Pageable pageable, String memberId) {
        Page<MemberCoupon> memberCouponList = memberCouponRepository.findMemberCouponByMemberId(
                pageable,
                memberId
        );
        return memberCouponList.stream()
                .map(MemberCoupon::getCouponCode)
                .collect(Collectors.toList());
    }
}
