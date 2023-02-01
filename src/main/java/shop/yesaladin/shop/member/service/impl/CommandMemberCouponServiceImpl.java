package shop.yesaladin.shop.member.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.domain.model.MemberCoupon;
import shop.yesaladin.shop.member.persistence.JpaCommandMemberCouponRepository;
import shop.yesaladin.shop.member.service.inter.CommandMemberCouponService;

@RequiredArgsConstructor
@Service
public class CommandMemberCouponServiceImpl implements CommandMemberCouponService {

    private final JpaCommandMemberCouponRepository memberCouponRepository;

    @Override
    @Transactional
    public List<MemberCoupon> createMemberCoupons(List<MemberCoupon> memberCouponList) {
        return memberCouponRepository.saveAll(memberCouponList);
    }
}
