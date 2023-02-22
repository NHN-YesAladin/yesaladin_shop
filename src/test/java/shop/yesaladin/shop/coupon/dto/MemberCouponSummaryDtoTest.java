package shop.yesaladin.shop.coupon.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.shop.product.dto.ProductWithCategoryResponseDto;

class MemberCouponSummaryDtoTest {

    ProductWithCategoryResponseDto product = Mockito.mock(ProductWithCategoryResponseDto.class);

    @BeforeEach
    void setUp() {
    }

    @Test
    void discount() {
        long price = 1000;
    }
}