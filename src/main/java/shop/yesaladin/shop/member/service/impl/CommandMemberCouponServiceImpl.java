package shop.yesaladin.shop.member.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.domain.model.MemberCoupon;
import shop.yesaladin.shop.member.domain.repository.CommandMemberCouponRepository;
import shop.yesaladin.shop.member.service.inter.CommandMemberCouponService;

@RequiredArgsConstructor
@Service
public class CommandMemberCouponServiceImpl implements CommandMemberCouponService {

    private final CommandMemberCouponRepository memberCouponRepository;

    @Override
    @Transactional
    public void createMemberCoupons(List<MemberCoupon> memberCouponList) {
        memberCouponRepository.saveAll(memberCouponList);
    }
}
