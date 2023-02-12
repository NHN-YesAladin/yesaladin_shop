package shop.yesaladin.shop.coupon.controller;

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
import shop.yesaladin.shop.common.aspect.annotation.LoginId;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.coupon.dto.MemberCouponSummaryDto;
import shop.yesaladin.shop.coupon.service.inter.QueryMemberCouponService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/member-coupons")
public class QueryMemberCouponController {

    private final QueryMemberCouponService queryMemberCouponService;

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
}
