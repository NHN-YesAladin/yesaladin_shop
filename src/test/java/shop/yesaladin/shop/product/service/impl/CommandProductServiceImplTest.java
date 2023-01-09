package shop.yesaladin.shop.product.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shop.yesaladin.shop.file.service.inter.CommandFileService;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.repository.CommandProductRepository;
import shop.yesaladin.shop.product.dto.ProductCreateDto;
import shop.yesaladin.shop.product.dto.ProductResponseDto;
import shop.yesaladin.shop.product.dummy.DummyProductCreateDto;
import shop.yesaladin.shop.product.service.inter.CommandProductService;
import shop.yesaladin.shop.product.service.inter.CommandSubscribeProductService;
import shop.yesaladin.shop.product.service.inter.CommandTotalDiscountRateService;
import shop.yesaladin.shop.publish.service.inter.CommandPublisherService;
import shop.yesaladin.shop.writing.service.inter.CommandWritingService;

class CommandProductServiceImplTest {

    private final String ISBN = "00001-...";

    private CommandProductService commandProductService;
    private CommandPublisherService commandPublisherService;
    private CommandSubscribeProductService commandSubscribeProductService;
    private CommandFileService commandFileService;
    private CommandTotalDiscountRateService commandTotalDiscountRateService;
    private CommandWritingService commandWritingService;
    private QueryMemberService queryMemberService;

    private CommandProductRepository commandProductRepository;

    @BeforeEach
    void setUp() {
        commandProductRepository = mock(CommandProductRepository.class);
        commandPublisherService = mock(CommandPublisherService.class);
        commandSubscribeProductService = mock(CommandSubscribeProductService.class);
        commandFileService = mock(CommandFileService.class);
        commandTotalDiscountRateService = mock(CommandTotalDiscountRateService.class);
        commandWritingService = mock(CommandWritingService.class);
        queryMemberService = mock(QueryMemberService.class);

        commandProductService = new CommandProductServiceImpl(
                commandProductRepository,
                commandPublisherService,
                commandSubscribeProductService,
                commandFileService,
                commandTotalDiscountRateService,
                commandWritingService,
                queryMemberService
        );
    }

    @Test
    void create() {
        // given
        ProductCreateDto productCreateDto = DummyProductCreateDto.dummy(ISBN);
        Product product = productCreateDto.toProductEntity(
                productCreateDto.toPublisherEntity(),
                productCreateDto.toSubscribeProductEntity(),
                productCreateDto.toThumbnailFileEntity(),
                productCreateDto.toEbookFileEntity(),
                productCreateDto.toTotalDiscountRateEntity()
        );

        when(commandProductRepository.save(any())).thenReturn(product);

        // when
        ProductResponseDto productResponseDto = commandProductService.create(productCreateDto);

        // then
        assertThat(productResponseDto).isNotNull();
    }
}
