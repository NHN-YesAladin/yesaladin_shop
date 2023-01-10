package shop.yesaladin.shop.product.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shop.yesaladin.shop.file.service.inter.CommandFileService;
import shop.yesaladin.shop.file.service.inter.QueryFileService;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.domain.repository.CommandProductRepository;
import shop.yesaladin.shop.product.domain.repository.QueryProductRepository;
import shop.yesaladin.shop.product.dto.ProductCreateDto;
import shop.yesaladin.shop.product.dto.ProductResponseDto;
import shop.yesaladin.shop.product.dummy.DummyProductCreateDto;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;
import shop.yesaladin.shop.product.service.inter.CommandProductService;
import shop.yesaladin.shop.product.service.inter.CommandSubscribeProductService;
import shop.yesaladin.shop.product.service.inter.CommandTotalDiscountRateService;
import shop.yesaladin.shop.product.service.inter.QuerySubscribeProductService;
import shop.yesaladin.shop.product.service.inter.QueryTotalDiscountRateService;
import shop.yesaladin.shop.publish.domain.model.Publish;
import shop.yesaladin.shop.publish.service.inter.CommandPublishService;
import shop.yesaladin.shop.publish.service.inter.CommandPublisherService;
import shop.yesaladin.shop.publish.service.inter.QueryPublisherService;
import shop.yesaladin.shop.tag.service.inter.CommandProductTagService;
import shop.yesaladin.shop.tag.service.inter.CommandTagService;
import shop.yesaladin.shop.tag.service.inter.QueryTagService;
import shop.yesaladin.shop.writing.service.inter.CommandWritingService;

class CommandProductServiceImplTest {

    private final String ISBN = "00001-...";

    // Product
    private CommandProductService commandProductService;
    private CommandProductRepository commandProductRepository;
    private QueryProductRepository queryProductRepository;

    // File
    private CommandFileService commandFileService;
    private QueryFileService queryFileService;

    // SubscribeProduct
    private CommandSubscribeProductService commandSubscribeProductService;
    private QuerySubscribeProductService querySubscribeProductService;

    // TotalDiscountRate
    private QueryTotalDiscountRateService queryTotalDiscountRateService;

    // Writing
    private CommandWritingService commandWritingService;

    // Member
    private QueryMemberService queryMemberService;

    // Publisher
    private CommandPublisherService commandPublisherService;
    private QueryPublisherService queryPublisherService;
    private CommandPublishService commandPublishService;

    // Tag
    private CommandTagService commandTagService;
    private QueryTagService queryTagService;
    private CommandProductTagService commandProductTagService;

    @BeforeEach
    void setUp() {
        commandProductRepository = mock(CommandProductRepository.class);
        queryProductRepository = mock(QueryProductRepository.class);
        commandFileService = mock(CommandFileService.class);
        queryFileService = mock(QueryFileService.class);
        commandSubscribeProductService = mock(CommandSubscribeProductService.class);
        querySubscribeProductService = mock(QuerySubscribeProductService.class);
        queryTotalDiscountRateService = mock(QueryTotalDiscountRateService.class);
        commandWritingService = mock(CommandWritingService.class);
        queryMemberService = mock(QueryMemberService.class);
        commandPublisherService = mock(CommandPublisherService.class);
        queryPublisherService = mock(QueryPublisherService.class);
        commandPublishService = mock(CommandPublishService.class);
        commandTagService = mock(CommandTagService.class);
        queryTagService = mock(QueryTagService.class);
        commandProductTagService = mock(CommandProductTagService.class);

        commandProductService = new CommandProductServiceImpl(
                commandProductRepository,
                queryProductRepository,
                commandFileService,
                queryFileService,
                commandSubscribeProductService,
                querySubscribeProductService,
                queryTotalDiscountRateService,
                commandWritingService,
                queryMemberService,
                commandPublisherService,
                queryPublisherService,
                commandPublishService,
                commandTagService,
                queryTagService,
                commandProductTagService
        );
    }

    @Test
    void create() {
        // TODO: 수정 필요!!

        // given
        ProductCreateDto productCreateDto = DummyProductCreateDto.dummy(ISBN);
        Product product = productCreateDto.toProductEntity(
                productCreateDto.toSubscribeProductEntity(),
                productCreateDto.toThumbnailFileEntity(),
                productCreateDto.toEbookFileEntity(),
                DummyTotalDiscountRate.dummy()
        );

        when(commandProductRepository.save(any())).thenReturn(product);


        // when
        ProductResponseDto productResponseDto = commandProductService.create(productCreateDto);

        // then
        assertThat(productResponseDto).isNotNull();
    }
}
