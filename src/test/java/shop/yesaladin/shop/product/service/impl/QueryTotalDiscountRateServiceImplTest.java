package shop.yesaladin.shop.product.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.domain.repository.QueryTotalDiscountRateRepository;
import shop.yesaladin.shop.product.dto.TotalDiscountRateResponseDto;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;
import shop.yesaladin.shop.product.service.inter.QueryTotalDiscountRateService;

class QueryTotalDiscountRateServiceImplTest {

    private QueryTotalDiscountRateService queryTotalDiscountRateService;
    private QueryTotalDiscountRateRepository queryTotalDiscountRateRepository;

    private TotalDiscountRate totalDiscountRate;

    @BeforeEach
    void setUp() {
        totalDiscountRate = DummyTotalDiscountRate.dummy();

        queryTotalDiscountRateRepository = mock(QueryTotalDiscountRateRepository.class);
        queryTotalDiscountRateService = new QueryTotalDiscountRateServiceImpl(
                queryTotalDiscountRateRepository);
    }

    @Test
    void findById() {
        // given
        int id = 1;
        when(queryTotalDiscountRateRepository.findById(id)).thenReturn(Optional.of(totalDiscountRate));

        // when
        TotalDiscountRateResponseDto foundTotalDiscountRate = queryTotalDiscountRateService.findById(
                id);

        // then
        assertThat(foundTotalDiscountRate).isNotNull();
        assertThat(foundTotalDiscountRate.getDiscountRate()).isEqualTo(10);
    }
}
