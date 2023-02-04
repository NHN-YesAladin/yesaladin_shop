package shop.yesaladin.shop.member.service.impl;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.coupon.service.inter.InsertMemberCouponRepository;
import shop.yesaladin.shop.member.dto.MemberCouponRequestDto;
import shop.yesaladin.shop.member.dto.MemberCouponResponseDto;
import shop.yesaladin.shop.member.service.inter.CommandMemberCouponService;

/**
 * 회원 쿠폰 등록 관련 서비스 구현 클래스 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class CommandMemberCouponServiceImpl implements CommandMemberCouponService {

    private final InsertMemberCouponRepository insertMemberCouponRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public MemberCouponResponseDto createMemberCoupons(List<MemberCouponRequestDto> requestDtoList) {
        insertMemberCouponRepository.insertMemberCoupon(requestDtoList);
        List<String> givenCouponCodeList = new ArrayList<>();
        requestDtoList
                .forEach(memberCouponRequestDto -> givenCouponCodeList.addAll(memberCouponRequestDto.getCouponCodes()));

        return new MemberCouponResponseDto(givenCouponCodeList);
    }
}
