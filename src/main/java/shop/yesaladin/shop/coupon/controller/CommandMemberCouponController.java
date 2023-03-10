package shop.yesaladin.shop.coupon.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;
import shop.yesaladin.shop.coupon.service.inter.CommandMemberCouponService;
import shop.yesaladin.shop.member.dto.MemberCouponRequestDto;
import shop.yesaladin.shop.member.dto.MemberCouponResponseDto;

/**
 * 회원 쿠폰을 등록하는 Rest controller 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/coupons")
public class CommandMemberCouponController {

    private final CommandMemberCouponService commandMemberCouponService;

    /**
     * 회원 쿠폰을 등록합니다.
     *
     * @param requestDtoList 회원과 지급할 쿠폰 정보를 담은 dto 리스트
     * @return 회원 쿠폰 등록 요청 처리 정보를 담은 ResponseDto
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<MemberCouponResponseDto> createMemberCoupon(@Valid @RequestBody List<MemberCouponRequestDto> requestDtoList) {
        for (int i = 0; i < requestDtoList.size(); i++) {
            for (int j = 0; j < requestDtoList.get(i).getCouponCodes().size(); j++) {
                log.info(
                        "==== [COUPON] register member coupon {} to {} ====",
                        requestDtoList.get(i).getCouponCodes().get(j),
                        requestDtoList.get(i).getMemberId()
                );
            }
        }
        MemberCouponResponseDto memberCoupons = commandMemberCouponService.createMemberCoupons(
                requestDtoList);

        return ResponseDto.<MemberCouponResponseDto>builder()
                .status(HttpStatus.CREATED)
                .success(true)
                .data(memberCoupons)
                .build();
    }
}
