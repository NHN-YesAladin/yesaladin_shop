package shop.yesaladin.shop.member.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.domain.model.MemberCoupon;
import shop.yesaladin.shop.member.dto.MemberCouponRequestDto;
import shop.yesaladin.shop.member.dto.MemberCouponResponseDto;
import shop.yesaladin.shop.member.persistence.JpaCommandMemberCouponRepository;
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

    private final JpaCommandMemberCouponRepository memberCouponRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public MemberCouponResponseDto createMemberCoupons(List<MemberCouponRequestDto> requestDtos) {
        List<MemberCoupon> memberCouponList = new ArrayList<>();
        for (MemberCouponRequestDto requestDto : requestDtos) {
            for (int i = 0; i < requestDto.getCouponCodes().size(); i++) {
                MemberCoupon memberCoupon = MemberCoupon.builder()
                        .member(requestDto.getMember())
                        .couponCode(requestDto.getCouponCodes().get(i))
                        .couponGroupCode(requestDto.getGroupCodes().get(i))
                        .build();
                memberCouponList.add(memberCoupon);
            }
        }
        List<MemberCoupon> createdMemberCouponList = memberCouponRepository.saveAll(memberCouponList);
        List<String> givenCouponCodeList = createdMemberCouponList.stream()
                .map(MemberCoupon::getCouponCode)
                .collect(Collectors.toList());

        return new MemberCouponResponseDto(givenCouponCodeList);
    }
}
