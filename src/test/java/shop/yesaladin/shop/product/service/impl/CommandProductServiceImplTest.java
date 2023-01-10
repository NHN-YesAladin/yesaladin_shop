package shop.yesaladin.shop.product.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.file.dto.FileResponseDto;
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
import shop.yesaladin.shop.product.dto.SubscribeProductResponseDto;
import shop.yesaladin.shop.product.dto.TotalDiscountRateResponseDto;
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
import shop.yesaladin.shop.publish.dto.PublishResponseDto;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.publish.service.inter.CommandPublishService;
import shop.yesaladin.shop.publish.service.inter.CommandPublisherService;
import shop.yesaladin.shop.publish.service.inter.QueryPublisherService;
import shop.yesaladin.shop.tag.domain.model.Tag;
import shop.yesaladin.shop.tag.dto.TagResponseDto;
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

        SubscribeProductResponseDto subscribeProductResponseDto = new SubscribeProductResponseDto(subscribeProduct.getId(), subscribeProduct.getISSN());
        when(commandSubscribeProductService.register(any())).thenReturn(subscribeProductResponseDto);

        FileResponseDto thumbnailFileDto = new FileResponseDto(thumbnailFile.getId(), thumbnailFile.getName(), thumbnailFile.getUploadDateTime());
        when(queryFileService.findByName("UUID.png")).thenReturn(thumbnailFileDto);

        FileResponseDto ebookFileDto = new FileResponseDto(ebookFile.getId(), ebookFile.getName(), ebookFile.getUploadDateTime());
        when(queryFileService.findByName("UUID.pdf")).thenReturn(ebookFileDto);

        TotalDiscountRateResponseDto totalDiscountRateResponseDto = new TotalDiscountRateResponseDto(totalDiscountRate.getId(), totalDiscountRate.getDiscountRate());
        when(queryTotalDiscountRateService.findById(anyInt())).thenReturn(totalDiscountRateResponseDto);


        Publisher publisher = DummyPublisher.dummy();
        commandPublisherService.register(publisher);

        PublisherResponseDto publisherResponseDto = new PublisherResponseDto(publisher.getId(), publisher.getName());
        when(queryPublisherService.findByName(anyString())).thenReturn(publisherResponseDto);


        Tag tag = Tag.builder().id(1L).name("아름다운").build();
        commandTagService.register(tag);

        TagResponseDto tagResponseDto = new TagResponseDto(tag.getId(), tag.getName());
        when(queryTagService.findByName(anyString())).thenReturn(tagResponseDto);

        // when
        ProductResponseDto productResponseDto = commandProductService.create(productCreateDto);

        // then
        assertThat(productResponseDto).isNotNull();

        verify(queryFileService, times(2)).findByName(anyString());
        verify(querySubscribeProductService, times(1)).findByISSN(anyString());
        verify(queryTotalDiscountRateService, times(1)).findById(anyInt());
        verify(queryProductRepository, times(1)).findByISBN(anyString());

        verify(commandProductRepository, times(1)).save(any());

        verify(commandWritingService, times(1)).create(anyString(), any(), any());
        verify(commandPublishService, times(1)).register(any());
        verify(commandProductTagService, times(2)).register(any());
    }
}
