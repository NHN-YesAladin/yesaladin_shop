package shop.yesaladin.shop.product.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.yesaladin.shop.product.domain.model.ProductTypeCode;
import shop.yesaladin.shop.product.dto.ProductTypeResponseDto;
import shop.yesaladin.shop.product.service.inter.QueryProductTypeService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class QueryProductTypeServiceImplTest {

    private QueryProductTypeService service;

    @BeforeEach
    void setUp() {
        service = new QueryProductTypeServiceImpl();
    }

    @Test
    @DisplayName("상품 유형 전체 조회 성공")
    void findAll() {
        // when
        List<ProductTypeResponseDto> response = service.findAll();

        // then
        assertThat(response.size()).isEqualTo(6);
        assertThat(response.get(0).getType()).isEqualTo(ProductTypeCode.NONE.toString());
    }
}