package shop.yesaladin.shop.order.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.coupon.domain.model.MemberCoupon;
import shop.yesaladin.shop.coupon.service.inter.QueryMemberCouponService;
import shop.yesaladin.shop.order.domain.model.MemberOrder;
import shop.yesaladin.shop.order.domain.model.OrderCoupon;
import shop.yesaladin.shop.order.domain.repository.CommandOrderCouponRepository;
import shop.yesaladin.shop.order.domain.repository.QueryOrderRepository;
import shop.yesaladin.shop.order.service.inter.CommandOrderCouponService;

/**
 * 주문에 사용한 쿠폰의 생성과 관련한 서비스 구현체 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class CommandOrderCouponServiceImpl implements CommandOrderCouponService {

    private final CommandOrderCouponRepository commandOrderCouponRepository;
    private final QueryMemberCouponService queryMemberCouponService;
    private final QueryOrderRepository queryOrderRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<OrderCoupon> createOrderCoupons(Long orderId, List<String> couponCodes) {
        MemberOrder memberOrder = (MemberOrder) queryOrderRepository.findById(orderId)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.ORDER_NOT_FOUND,
                        "Order not found with id : " + orderId
                ));

        List<MemberCoupon> memberCoupons = queryMemberCouponService.findByCouponCodes(couponCodes);

        List<OrderCoupon> orderCoupons = memberCoupons.stream().map(memberCoupon -> {
            OrderCoupon orderCoupon = OrderCoupon.create(memberOrder, memberCoupon);
            return commandOrderCouponRepository.save(orderCoupon);
        }).collect(Collectors.toList());

        return orderCoupons;
    }
}
