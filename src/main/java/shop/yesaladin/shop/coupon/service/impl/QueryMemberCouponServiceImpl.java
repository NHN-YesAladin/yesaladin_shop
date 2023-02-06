package shop.yesaladin.shop.coupon.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.common.exception.ServerException;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.config.GatewayProperties;
import shop.yesaladin.shop.coupon.domain.model.MemberCoupon;
import shop.yesaladin.shop.coupon.domain.repository.QueryMemberCouponRepository;
import shop.yesaladin.shop.coupon.dto.MemberCouponSummaryDto;
import shop.yesaladin.shop.coupon.service.inter.QueryMemberCouponService;


/**
 * 회원 쿠폰 조회와 관련한 서비스 구현체 입니다.
 *
 * @author 최예린
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
    public PaginatedResponseDto<MemberCouponSummaryDto> getMemberCouponSummaryList(
            Pageable pageable, String memberId,
            boolean usable
    ) {
        Page<String> memberCouponCodeList = getMemberCouponCodeList(pageable, memberId, usable);

        ResponseDto<List<MemberCouponSummaryDto>> response = tryGetCouponSummary(
                memberCouponCodeList.getContent());

        return PaginatedResponseDto.<MemberCouponSummaryDto>builder()
                .currentPage(pageable.getPageNumber())
                .totalPage(memberCouponCodeList.getTotalPages())
                .totalDataCount(memberCouponCodeList.getTotalElements())
                .dataList(response.getData())
                .build();
    }

    private Page<String> getMemberCouponCodeList(Pageable pageable, String memberId, boolean usable) {
        Page<MemberCoupon> memberCouponList = memberCouponRepository.findMemberCouponByMemberId(
                pageable,
                memberId,
                usable
        );
        List<String> couponCodes = memberCouponList.stream()
                .map(MemberCoupon::getCouponCode)
                .collect(Collectors.toList());
        return new PageImpl<>(couponCodes, pageable, memberCouponList.getTotalElements());
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

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<MemberCoupon> findByCouponCodes(List<String> couponCodes) {
        List<MemberCoupon> memberCoupons = memberCouponRepository.findByCouponCodes(couponCodes);

        checkAllCouponCodesAreAvailable(couponCodes, memberCoupons);

        return memberCoupons;
    }

    private void checkAllCouponCodesAreAvailable(
            List<String> couponCodes,
            List<MemberCoupon> memberCoupons
    ) {
        if (memberCoupons.size() != couponCodes.size()) {
            throw new ClientException(ErrorCode.COUPON_NOT_FOUND, "MemberCoupon not found.");
        }
    }
}
