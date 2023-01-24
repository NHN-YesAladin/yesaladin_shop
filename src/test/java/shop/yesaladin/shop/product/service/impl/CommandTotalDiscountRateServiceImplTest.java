package shop.yesaladin.shop.product.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.domain.repository.CommandTotalDiscountRateRepository;
import shop.yesaladin.shop.product.dto.TotalDiscountRateResponseDto;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;
import shop.yesaladin.shop.product.service.inter.CommandTotalDiscountRateService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommandTotalDiscountRateServiceImplTest {

    private CommandTotalDiscountRateService service;
    private CommandTotalDiscountRateRepository commandTotalDiscountRateRepository;

    @BeforeEach
    void setUp() {
        commandTotalDiscountRateRepository = mock(CommandTotalDiscountRateRepository.class);
        service = new CommandTotalDiscountRateServiceImpl(commandTotalDiscountRateRepository);
    }

    @Test
    @DisplayName("전체 할인율 수정 성공")
    void modify() {
        // given
        int rate = 8;
        TotalDiscountRate totalDiscountRate = TotalDiscountRate.builder().id(1).discountRate(rate).build();

        when(commandTotalDiscountRateRepository.save(any())).thenReturn(totalDiscountRate);

        // when
        TotalDiscountRateResponseDto response = service.modify(totalDiscountRate);

        // then
        assertThat(response.getDiscountRate()).isEqualTo(rate);

        verify(commandTotalDiscountRateRepository, times(1)).save(totalDiscountRate);
    }
}