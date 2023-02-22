package shop.yesaladin.shop.coupon.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.shop.coupon.domain.repository.InsertMemberCouponRepository;
import shop.yesaladin.shop.coupon.service.inter.CommandMemberCouponService;
import shop.yesaladin.shop.member.dto.MemberCouponRequestDto;
import shop.yesaladin.shop.member.dto.MemberCouponResponseDto;

class CommandMemberCouponServiceImplTest {

    private CommandMemberCouponService commandMemberCouponService;
    private InsertMemberCouponRepository insertMemberCouponRepository;

    @BeforeEach
    void setUp() {
        insertMemberCouponRepository = Mockito.mock(InsertMemberCouponRepository.class);
        commandMemberCouponService = new CommandMemberCouponServiceImpl(insertMemberCouponRepository);
    }

    @Test
    void createMemberCoupons() throws Exception {
        //given
        long memberId = 1L;
        String couponCode = "c";
        String couponGroupCode = "codes";

        MemberCouponRequestDto couponRequestDto = new MemberCouponRequestDto(
                memberId,
                List.of(couponCode),
                List.of(couponGroupCode),
                List.of(LocalDate.now())
        );

        when(insertMemberCouponRepository.insertMemberCoupon(List.of(couponRequestDto))).thenReturn(1);

        //when
        MemberCouponResponseDto memberCoupons = commandMemberCouponService.createMemberCoupons(List.of(
                couponRequestDto));

        //then
        assertThat(memberCoupons.getGivenCouponCodeList()).hasSize(1);
        String actual = memberCoupons.getGivenCouponCodeList().get(0);
        assertThat(actual).isEqualTo(couponCode);
    }
}