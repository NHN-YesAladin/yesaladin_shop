package shop.yesaladin.shop.coupon.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
import shop.yesaladin.shop.coupon.service.inter.BirthdayCouponService;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberCoupon;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;
import shop.yesaladin.shop.member.service.inter.CommandMemberCouponService;

@RequiredArgsConstructor
@Service
public class BirthdayCouponServiceImpl implements BirthdayCouponService {

    private final QueryMemberRepository queryMemberRepository;
    private final CommandMemberCouponService commandMemberCouponService;
    private final RestTemplate restTemplate;
    private final CouponServerMetaConfig couponServerMetaConfig;

    @Override
    @Transactional(readOnly = true)
    public void 특정날짜가생일인회원조회해서쿠폰코드요청(int laterDays) {
        LocalDate birthday = LocalDate.now().plusDays(laterDays);
        List<Member> memberList = queryMemberRepository.findMembersByBirthday(birthday.getMonthValue(),
                birthday.getDayOfMonth()
        );

        // 쿠폰 서버에 쿠폰 코드를 요청
        HttpEntity<CouponIssueRequestDto> httpEntity = new HttpEntity<>(new CouponIssueRequestDto(TriggerTypeCode.BIRTHDAY,
                null,
                memberList.size()
        ));

        ResponseEntity<ResponseDto<List<CouponIssueResponseDto>>> responseEntity = restTemplate.exchange(
                couponServerMetaConfig.getCouponServerUrl() + "/v1/issuances",
                HttpMethod.POST,
                httpEntity,
                new ParameterizedTypeReference<>() {}
        );

        List<CouponIssueResponseDto> data = responseEntity.getBody().getData();
        List<MemberCoupon> memberCouponList = new ArrayList<>();

        for (int memberCount = 0; memberCount < memberList.size(); memberCount++) {
            for (int couponCount = 0; couponCount < data.size(); couponCount++) {
                MemberCoupon memberCoupon = MemberCoupon.builder()
                        .member(memberList.get(memberCount))
                        .couponCode(data.get(couponCount).getCreatedCouponCodes().get(memberCount))
//                        .couponGroupCode(data.get(couponCount).getGroupCodes().get(memberCount))
                        .couponGroupCode("9030ff47-2456-4bb8-a604-cf37f8daef31")
                        .build();
                memberCouponList.add(memberCoupon);
            }
        }

        commandMemberCouponService.createMemberCoupons(memberCouponList);

        // TODO: 쿠폰 서버에 해당 쿠폰 코드 지급 완료 메시지 발행하기
    }
}
