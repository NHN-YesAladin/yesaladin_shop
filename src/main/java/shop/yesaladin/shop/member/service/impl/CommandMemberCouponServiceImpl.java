package shop.yesaladin.shop.member.service.impl;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.domain.model.MemberCoupon;
import shop.yesaladin.shop.member.dto.MemberCouponRequestDto;
import shop.yesaladin.shop.member.persistence.JpaCommandMemberCouponRepository;
import shop.yesaladin.shop.member.service.inter.CommandMemberCouponService;

@RequiredArgsConstructor
@Service
public class CommandMemberCouponServiceImpl implements CommandMemberCouponService {

    private final JpaCommandMemberCouponRepository memberCouponRepository;

    @Override
    @Transactional
    public List<MemberCoupon> createMemberCoupons(List<MemberCouponRequestDto> requestDtos) {
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
        return memberCouponRepository.saveAll(memberCouponList);
    }
}
