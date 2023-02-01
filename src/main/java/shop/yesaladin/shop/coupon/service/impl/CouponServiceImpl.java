package shop.yesaladin.shop.coupon.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.coupon.trigger.TriggerTypeCode;
import shop.yesaladin.shop.config.CouponServerMetaConfig;
import shop.yesaladin.shop.coupon.dto.CouponIssueRequestDto;
import shop.yesaladin.shop.coupon.dto.CouponIssueResponseDto;
import shop.yesaladin.shop.coupon.service.inter.CouponService;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberCoupon;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;
import shop.yesaladin.shop.member.service.inter.CommandMemberCouponService;

@Slf4j
@RequiredArgsConstructor
@Service
public class CouponServiceImpl implements CouponService {

    private final QueryMemberRepository queryMemberRepository;
    private final CommandMemberCouponService commandMemberCouponService;
    private final RestTemplate restTemplate;
    private final CouponServerMetaConfig couponServerMetaConfig;

    @Override
    @Transactional
    public void giveBirthdayCoupon(int laterDays) {
        LocalDate birthday = LocalDate.now().plusDays(laterDays);
        log.info("birthday : {}", birthday);

        List<Member> memberList = queryMemberRepository.findMembersByBirthday(
                birthday.getMonthValue(),
                birthday.getDayOfMonth()
        );

        // TODO 생일자가 없으면

        log.info("birthday memberList size {}", memberList.size());

        List<CouponIssueResponseDto> data = getIssuedCouponList(
                TriggerTypeCode.BIRTHDAY,
                null,
                memberList.size()
        );

        List<MemberCoupon> memberCouponList = new ArrayList<>();

        for (int memberCount = 0; memberCount < memberList.size(); memberCount++) {
            for (CouponIssueResponseDto datum : data) {
                MemberCoupon memberCoupon = MemberCoupon.builder()
                        .member(memberList.get(memberCount))
                        .couponCode(datum.getCreatedCouponCodes().get(memberCount))
//                        .couponGroupCode(data.get(couponCount).getGroupCodes().get(memberCount))
                        // TODO dummy group code 수정하기
                        .couponGroupCode("9030ff47-2456-4bb8-a604-cf37f8daef31")
                        .build();
                memberCouponList.add(memberCoupon);
            }
        }

        commandMemberCouponService.createMemberCoupons(memberCouponList);

        // TODO: 쿠폰 서버에 해당 쿠폰 코드 지급 완료 메시지 발행하기
    }

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
