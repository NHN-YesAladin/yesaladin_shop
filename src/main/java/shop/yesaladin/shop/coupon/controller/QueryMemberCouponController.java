package shop.yesaladin.shop.coupon.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.coupon.code.TriggerTypeCode;
import shop.yesaladin.shop.common.aspect.annotation.LoginId;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.coupon.dto.CouponIssueResponseDto;
import shop.yesaladin.shop.coupon.dto.MemberCouponSummaryDto;
import shop.yesaladin.shop.coupon.service.inter.GiveCouponService;
import shop.yesaladin.shop.coupon.service.inter.QueryMemberCouponService;

/**
 *
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/member-coupons")
public class QueryMemberCouponController {

    private final QueryMemberCouponService queryMemberCouponService;
    private final GiveCouponService giveCouponService;

    @GetMapping
    public ResponseDto<PaginatedResponseDto<MemberCouponSummaryDto>> getMemberCouponList(
            @LoginId(required = true) String loginId,
            @PageableDefault(size = 20) Pageable pageable,
            @RequestParam(defaultValue = "true") boolean usable
    ) {

        PaginatedResponseDto<MemberCouponSummaryDto> memberCouponSummaryList = queryMemberCouponService.getMemberCouponSummaryList(pageable,
                loginId,
                usable
        );

        return ResponseDto.<PaginatedResponseDto<MemberCouponSummaryDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .data(memberCouponSummaryList)
                .build();
    }

    @GetMapping(value = "/issuance", params = {"type", "coupon-id"})
    public ResponseDto<CouponIssueResponseDto> processCouponGiveRequest(
            @LoginId(required = true) String loginId,
            @RequestParam("type") String type,
            @RequestParam("coupon-id") Long couponId
    ) {
        log.info("==== [COUPON] issue coupon request is arrived with id {} and type {} ===",
                loginId,
                type
        );
        TriggerTypeCode couponOfTheMonth = TriggerTypeCode.COUPON_OF_THE_MONTH;

        if (couponOfTheMonth.toString().equalsIgnoreCase(type)) {
            log.info("==== [COUPON] coupon of the month requested. ====");
            try {
                giveCouponService.sendCouponGiveRequest(loginId, couponOfTheMonth, couponId);
            } catch (ClientException e) {
                // ignore
                return ResponseDto.<CouponIssueResponseDto>builder()
                        .status(HttpStatus.OK)
                        .success(false)
                        .errorMessages(List.of(e.getMessage()))
                        .build();
            }
        }

        // 메시지를 발행하게 되면 redis 의 쿠폰 수량 감소


        return ResponseDto.<CouponIssueResponseDto>builder()
                .status(HttpStatus.OK)
                .success(true)
                .build();
    }
}
