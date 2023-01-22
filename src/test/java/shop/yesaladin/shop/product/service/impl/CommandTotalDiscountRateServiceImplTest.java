package shop.yesaladin.shop.product.service.impl;

import org.junit.jupiter.api.BeforeEach;
import shop.yesaladin.shop.product.domain.repository.CommandTotalDiscountRateRepository;
import shop.yesaladin.shop.product.service.inter.CommandTotalDiscountRateService;

import static org.mockito.Mockito.mock;

class CommandTotalDiscountRateServiceImplTest {

    private CommandTotalDiscountRateService commandTotalDiscountRateService;
    private CommandTotalDiscountRateRepository commandTotalDiscountRateRepository;

    @BeforeEach
    void setUp() {
        commandTotalDiscountRateRepository = mock(CommandTotalDiscountRateRepository.class);
        commandTotalDiscountRateService = new CommandTotalDiscountRateServiceImpl(
                commandTotalDiscountRateRepository);
    }

}
