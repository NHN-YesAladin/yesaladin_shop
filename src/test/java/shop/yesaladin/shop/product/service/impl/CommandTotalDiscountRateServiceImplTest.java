package shop.yesaladin.shop.product.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.domain.repository.CommandTotalDiscountRateRepository;
import shop.yesaladin.shop.product.dto.TotalDiscountRateResponseDto;
import shop.yesaladin.shop.product.service.inter.CommandTotalDiscountRateService;

class CommandTotalDiscountRateServiceImplTest {

    private CommandTotalDiscountRateService commandTotalDiscountRateService;
    private CommandTotalDiscountRateRepository commandTotalDiscountRateRepository;

    @BeforeEach
    void setUp() {
        commandTotalDiscountRateRepository = mock(CommandTotalDiscountRateRepository.class);
        commandTotalDiscountRateService = new CommandTotalDiscountRateServiceImpl(commandTotalDiscountRateRepository);
    }

    @Test
    void register() {
        // given
        TotalDiscountRate totalDiscountRate = TotalDiscountRate.builder().discountRate(10).build();

        when(commandTotalDiscountRateRepository.save(any())).thenReturn(totalDiscountRate);

        // when
        TotalDiscountRateResponseDto registeredTotalDiscountRate = commandTotalDiscountRateService.register(totalDiscountRate);

        // then
        assertThat(registeredTotalDiscountRate.getDiscountRate()).isEqualTo(10);
    }
}
