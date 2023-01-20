package shop.yesaladin.shop.product.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.repository.CommandSubscribeProductRepository;
import shop.yesaladin.shop.product.dto.SubscribeProductResponseDto;
import shop.yesaladin.shop.product.service.inter.CommandSubscribeProductService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CommandSubscribeProductServiceImplTest {

    private final String ISSN = "0000-XXXX";

    private CommandSubscribeProductService commandSubscribeProductService;
    private CommandSubscribeProductRepository commandSubscribeProductRepository;

    @BeforeEach
    void setUp() {
        commandSubscribeProductRepository = mock(CommandSubscribeProductRepository.class);
        commandSubscribeProductService = new CommandSubscribeProductServiceImpl(
                commandSubscribeProductRepository);
    }

    @Test
    void register() {
        // given
        SubscribeProduct subscribeProduct = SubscribeProduct.builder().ISSN(ISSN).build();

        when(commandSubscribeProductRepository.save(any())).thenReturn(subscribeProduct);

        // when
        SubscribeProductResponseDto registeredSubscribeProduct = commandSubscribeProductService.register(
                subscribeProduct);

        // then
        assertThat(registeredSubscribeProduct.getISSN()).isEqualTo(ISSN);
    }
}
