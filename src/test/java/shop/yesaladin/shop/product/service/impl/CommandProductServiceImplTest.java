package shop.yesaladin.shop.product.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.file.service.inter.CommandFileService;
import shop.yesaladin.shop.file.service.inter.QueryFileService;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.domain.repository.CommandProductRepository;
import shop.yesaladin.shop.product.domain.repository.QueryProductRepository;
import shop.yesaladin.shop.product.dto.ProductCreateDto;
import shop.yesaladin.shop.product.dto.ProductResponseDto;
import shop.yesaladin.shop.product.dummy.DummyProductCreateDto;
import shop.yesaladin.shop.product.dummy.DummyPublisher;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;
import shop.yesaladin.shop.product.service.inter.CommandProductService;
import shop.yesaladin.shop.product.service.inter.CommandSubscribeProductService;
import shop.yesaladin.shop.product.service.inter.CommandTotalDiscountRateService;
import shop.yesaladin.shop.product.service.inter.QuerySubscribeProductService;
import shop.yesaladin.shop.product.service.inter.QueryTotalDiscountRateService;
import shop.yesaladin.shop.publish.domain.model.Publish;
import shop.yesaladin.shop.publish.domain.model.Publisher;
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

    // TotalDiscountRate
    private CommandTotalDiscountRateService commandTotalDiscountRateService;

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

        commandTotalDiscountRateService = mock(CommandTotalDiscountRateService.class);

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
        // given
        ProductCreateDto productCreateDto = DummyProductCreateDto.dummy(ISBN);

        SubscribeProduct subscribeProduct = productCreateDto.toSubscribeProductEntity();
        File thumbnailFile = productCreateDto.toThumbnailFileEntity();
        File ebookFile = productCreateDto.toEbookFileEntity();
        TotalDiscountRate totalDiscountRate = DummyTotalDiscountRate.dummy();

        commandSubscribeProductService.register(subscribeProduct);
        commandFileService.register(thumbnailFile);
        commandFileService.register(ebookFile);
        commandTotalDiscountRateService.register(totalDiscountRate);

        Product product = productCreateDto.toProductEntity(
                subscribeProduct,
                thumbnailFile,
                ebookFile,
                totalDiscountRate
        );

        when(commandProductRepository.save(any())).thenReturn(product);

        Publisher publisher = DummyPublisher.dummy();
        commandPublisherService.register(publisher);

        // TODO: test......모르겠어요... 다시해요...
//        mockStatic(Publish.class);

        String now = LocalDateTime.now().toLocalDate().toString();
        Publish publish = Publish.create(product, publisher, now);
        when(commandPublishService.register(any())).thenReturn(publish);

        // when
        ProductResponseDto productResponseDto = commandProductService.create(productCreateDto);

        // then
        assertThat(productResponseDto).isNotNull();
    }
}
