package shop.yesaladin.shop.order.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.coupon.dto.CouponOrderSheetRequestDto;
import shop.yesaladin.shop.coupon.dto.CouponOrderSheetResponseDto;
import shop.yesaladin.shop.coupon.dto.MemberCouponSummaryDto;
import shop.yesaladin.shop.coupon.service.inter.QueryMemberCouponService;
import shop.yesaladin.shop.order.service.inter.QueryOrderCouponService;
import shop.yesaladin.shop.product.dto.ProductWithCategoryResponseDto;
import shop.yesaladin.shop.product.service.inter.QueryProductService;

/**
 * 주문에 사용한 쿠폰 조호 관련 service 구현체 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class QueryOrderCouponServiceImpl implements QueryOrderCouponService {

    private final QueryProductService queryProductService;
    private final QueryMemberCouponService queryMemberCouponService;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public CouponOrderSheetResponseDto calculateCoupons(
            String loginId,
            CouponOrderSheetRequestDto request
    ) {
        //상품 정보 불러오기
        ProductWithCategoryResponseDto product = queryProductService.getByIsbn(request.getIsbn());

        //상품에 적용할 쿠폰 목록
        List<String> couponCodes = request.getDuplicateCouponCode();
        if (!Objects.equals(request.getCouponCode(), "")) {
            couponCodes.add(request.getCouponCode());
        }

        //쿠폰 사용 가능 검증
        List<MemberCouponSummaryDto> memberCoupons = queryMemberCouponService.getMemberCouponSummaryListByCouponCodes(
                loginId,
                couponCodes
        );

        log.error("actualPrice : {} * {} = {}", product.getActualPrice(), request.getQuantity(), (product.getActualPrice() * request.getQuantity()));
        //상품의 판매가
        long saleAmount = (product.getActualPrice() * request.getQuantity()) *
                (100 - ((product.isSeparatelyDiscount() ? product.getDiscountRate()
                        : product.getTotalDiscountRate().getDiscountRate()))) / 100;
        log.error("saleAmount : {}", saleAmount);
        //상품의 실판매가
        long couponAppliedAmount = getCouponAppliedAmount(
                request.getCouponCode(),
                product,
                memberCoupons,
                saleAmount
        );
        log.error("couponAppliedAmount : {}", couponAppliedAmount);
        //적립 예정 포인트
        long expectedPoint = getExpectedPoint(product, saleAmount, couponAppliedAmount);

        return new CouponOrderSheetResponseDto(
                product.getIsbn(),
                couponCodes,
                couponAppliedAmount,
                expectedPoint
        );
    }

    private long getExpectedPoint(
            ProductWithCategoryResponseDto product,
            long discountAmount,
            long couponAppliedAmount
    ) {
        return (product.getProductSavingMethodCode().getId() == 2) ?
                discountAmount / (100 - product.getGivenPointRate()) * 100 :
                couponAppliedAmount / (100 - product.getGivenPointRate()) * 100;
    }

    private long getCouponAppliedAmount(
            String couponCode,
            ProductWithCategoryResponseDto product,
            List<MemberCouponSummaryDto> memberCoupons,
            long discountAmount
    ) {
        Map<String, MemberCouponSummaryDto> couponMap = memberCoupons.stream()
                .collect(Collectors.toMap(MemberCouponSummaryDto::getCouponCode, coupon -> coupon));

        long couponAppliedAmount = discountAmount;
        //일반 쿠폰 적용
        if (!Objects.equals(couponCode, "")) {
            couponAppliedAmount = couponMap.get(couponCode).discount(product, discountAmount);
        }
        log.error("일반 쿠폰 적용 : {}", couponAppliedAmount);
        int cnt = 1;
        //중복 쿠폰
        for (MemberCouponSummaryDto memberCoupon : memberCoupons) {
            if (Objects.equals(couponCode, memberCoupon.getCouponCode())) {
                continue;
            }
            couponAppliedAmount = memberCoupon.discount(product, couponAppliedAmount);
            log.error("중복 쿠폰 적용 {} : {}", cnt++, couponAppliedAmount);
        }

        return couponAppliedAmount;
    }
}
