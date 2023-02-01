package shop.yesaladin.shop.coupon.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;
import shop.yesaladin.shop.config.CouponServerMetaConfig;
import shop.yesaladin.shop.coupon.service.inter.CouponService;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;
import shop.yesaladin.shop.member.service.inter.CommandMemberCouponService;

class CouponServiceImplTest {

    private CouponService couponService;
    private QueryMemberRepository queryMemberRepository;
    private CommandMemberCouponService commandMemberCouponService;
    private RestTemplate restTemplate;
    private CouponServerMetaConfig couponServerMetaConfig;

    @BeforeEach
    void setUp() {
        queryMemberRepository = Mockito.mock(QueryMemberRepository.class);
        commandMemberCouponService = Mockito.mock(CommandMemberCouponService.class);
        restTemplate = Mockito.mock(RestTemplate.class);
        couponServerMetaConfig = Mockito.mock(CouponServerMetaConfig.class);
        couponService = new CouponServiceImpl(queryMemberRepository, commandMemberCouponService, restTemplate, couponServerMetaConfig);
    }
    @Test
    void giveBirthdayCouponTest() {
        // TODO test
//        couponService.giveBirthdayCoupon(8);
    }

}