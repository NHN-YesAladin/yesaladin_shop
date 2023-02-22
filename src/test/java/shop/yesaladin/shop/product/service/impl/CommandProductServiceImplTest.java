package shop.yesaladin.shop.product.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.category.service.inter.CommandProductCategoryService;
import shop.yesaladin.shop.category.service.inter.QueryCategoryService;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.file.dto.FileResponseDto;
import shop.yesaladin.shop.file.service.inter.CommandFileService;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.domain.repository.CommandProductRepository;
import shop.yesaladin.shop.product.domain.repository.CommandSubscribeProductRepository;
import shop.yesaladin.shop.product.domain.repository.QueryProductRepository;
import shop.yesaladin.shop.product.domain.repository.QuerySubscribeProductRepository;
import shop.yesaladin.shop.product.domain.repository.QueryTotalDiscountRateRepository;
import shop.yesaladin.shop.product.dto.ProductCreateDto;
import shop.yesaladin.shop.product.dto.ProductOnlyIdDto;
import shop.yesaladin.shop.product.dto.ProductUpdateDto;
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummyProductCreateDto;
import shop.yesaladin.shop.product.dummy.DummyProductUpdateDto;
import shop.yesaladin.shop.product.dummy.DummySubscribeProduct;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;
import shop.yesaladin.shop.product.service.inter.CommandProductService;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.publish.service.inter.CommandPublishService;
import shop.yesaladin.shop.publish.service.inter.QueryPublisherService;
import shop.yesaladin.shop.tag.dto.TagResponseDto;
import shop.yesaladin.shop.tag.service.inter.CommandProductTagService;
import shop.yesaladin.shop.tag.service.inter.QueryTagService;
import shop.yesaladin.shop.writing.dto.AuthorResponseDto;
import shop.yesaladin.shop.writing.service.inter.CommandWritingService;
import shop.yesaladin.shop.writing.service.inter.QueryAuthorService;

class CommandProductServiceImplTest {

    private final String URL = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type";
    private final String ISBN = "0000000000001";
    private final Long ID = 1L;

    private CommandProductService service;
    // Product
    private CommandProductRepository commandProductRepository;
    private QueryProductRepository queryProductRepository;

    // SubscribeProduct
    private CommandSubscribeProductRepository commandSubscribeProductRepository;
    private QuerySubscribeProductRepository querySubscribeProductRepository;

    // TotalDiscountRate
    private QueryTotalDiscountRateRepository queryTotalDiscountRateRepository;

    // File
    private CommandFileService commandFileService;

    // Writing
    private CommandWritingService commandWritingService;
    private QueryAuthorService queryAuthorService;

    // Publish
    private CommandPublishService commandPublishService;
    private QueryPublisherService queryPublisherService;

    // Tag
    private QueryTagService queryTagService;
    private CommandProductTagService commandProductTagService;

    // Category
    private QueryCategoryService queryCategoryService;
    private CommandProductCategoryService commandProductCategoryService;

    @BeforeEach
    void setUp() {
        commandProductRepository = mock(CommandProductRepository.class);
        queryProductRepository = mock(QueryProductRepository.class);
        commandSubscribeProductRepository = mock(CommandSubscribeProductRepository.class);
        querySubscribeProductRepository = mock(QuerySubscribeProductRepository.class);
        queryTotalDiscountRateRepository = mock(QueryTotalDiscountRateRepository.class);
        commandFileService = mock(CommandFileService.class);
        commandWritingService = mock(CommandWritingService.class);
        queryAuthorService = mock(QueryAuthorService.class);
        commandPublishService = mock(CommandPublishService.class);
        queryPublisherService = mock(QueryPublisherService.class);
        queryTagService = mock(QueryTagService.class);
        commandProductTagService = mock(CommandProductTagService.class);
        queryCategoryService = mock(QueryCategoryService.class);
        commandProductCategoryService = mock(CommandProductCategoryService.class);

        service = new CommandProductServiceImpl(
                commandProductRepository,
                queryProductRepository,
                commandSubscribeProductRepository,
                querySubscribeProductRepository,
                queryTotalDiscountRateRepository,
                commandFileService,
                commandWritingService,
                queryAuthorService,
                commandPublishService,
                queryPublisherService,
                queryTagService,
                commandProductTagService,
                queryCategoryService,
                commandProductCategoryService
        );
    }

    @Test
    @DisplayName("상품 등록 성공")
    void create_success() {
        // given
        File thumbnailFile = DummyFile.dummy(URL + "/image.png");
        File ebookFile = DummyFile.dummy(URL + "/ebook.pdf");
        SubscribeProduct subscribeProduct = DummySubscribeProduct.dummy();
        TotalDiscountRate totalDiscountRate = DummyTotalDiscountRate.dummy();

        Mockito.when(commandFileService.register(any()))
                .thenReturn(new FileResponseDto(
                        1L,
                        thumbnailFile.getUrl(),
                        thumbnailFile.getUploadDateTime()
                ));
        Mockito.when(commandFileService.register(ebookFile))
                .thenReturn(new FileResponseDto(
                        2L,
                        ebookFile.getUrl(),
                        ebookFile.getUploadDateTime()
                ));

        Mockito.when(commandSubscribeProductRepository.save(any())).thenReturn(subscribeProduct);

        Mockito.when(queryTotalDiscountRateRepository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(totalDiscountRate));

        Product product = DummyProduct.dummy(
                ID,
                ISBN,
                subscribeProduct,
                thumbnailFile,
                ebookFile,
                totalDiscountRate
        );
        Mockito.when(commandProductRepository.save(any())).thenReturn(product);

        Mockito.when(queryAuthorService.findById(anyLong()))
                .thenReturn(new AuthorResponseDto(1L, "저자1", null));

        Mockito.when(queryPublisherService.findById(anyLong()))
                .thenReturn(new PublisherResponseDto(1L, "출판사1"));

        Mockito.when(queryTagService.findById(1L)).thenReturn(new TagResponseDto(1L, "아름다운"));
        Mockito.when(queryTagService.findById(2L)).thenReturn(new TagResponseDto(2L, "슬픈"));

        Mockito.when(queryCategoryService.findCategoryById(1L))
                .thenReturn(new CategoryResponseDto(1L, "영화", true, 1, 100L, "국내"));
        Mockito.when(queryCategoryService.findCategoryById(2L))
                .thenReturn(new CategoryResponseDto(2L, "소설", true, 1, 100L, "국내"));
        Mockito.when(queryCategoryService.findCategoryById(100L))
                .thenReturn(new CategoryResponseDto(100L, "국내", true, 1, null, null));

        ProductCreateDto dto = DummyProductCreateDto.dummy(ISBN);

        // when
        ProductOnlyIdDto productOnlyIdDto = service.create(dto);

        // then
        assertThat(productOnlyIdDto).isNotNull();
        assertThat(productOnlyIdDto.getId()).isEqualTo(ID);

        verify(commandFileService, times(2)).register(any());
        verify(querySubscribeProductRepository, times(1)).findByISSN(dto.getIssn());
        verify(queryTotalDiscountRateRepository, times(1)).findById(1);
        verify(queryProductRepository, times(1)).findByIsbn(ISBN);
        verify(commandProductRepository, times(1)).save(any());
        verify(queryAuthorService, times(1)).findById(dto.getAuthors().get(0));
        verify(commandWritingService, times(1)).register(any());
        verify(queryPublisherService, times(1)).findById(dto.getPublisherId());
        verify(commandPublishService, times(1)).register(any());
        verify(queryTagService, times(2)).findById(any());
        verify(commandProductTagService, times(2)).register(any());
        verify(queryCategoryService, times(1)).findCategoryById(1L);
        verify(queryCategoryService, times(1)).findCategoryById(2L);
        verify(queryCategoryService, times(2)).findCategoryById(100L);
        verify(commandProductCategoryService, times(2)).register(any());
    }

    @Test
    @DisplayName("상품 등록 실패_상품이 이미 존재하는 경우 예외 발생")
    void create_throwProductAlreadyExist() {
        // given
        File thumbnailFile = DummyFile.dummy(URL + "/image.png");
        File ebookFile = DummyFile.dummy(URL + "/ebook.pdf");
        SubscribeProduct subscribeProduct = DummySubscribeProduct.dummy();
        TotalDiscountRate totalDiscountRate = DummyTotalDiscountRate.dummy();

        Mockito.when(commandFileService.register(any()))
                .thenReturn(new FileResponseDto(
                        1L,
                        thumbnailFile.getUrl(),
                        thumbnailFile.getUploadDateTime()
                ));
        Mockito.when(commandFileService.register(ebookFile))
                .thenReturn(new FileResponseDto(
                        2L,
                        ebookFile.getUrl(),
                        ebookFile.getUploadDateTime()
                ));

        Mockito.when(commandSubscribeProductRepository.save(any())).thenReturn(subscribeProduct);

        Mockito.when(queryTotalDiscountRateRepository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(totalDiscountRate));

        Product product = DummyProduct.dummy(
                ID,
                ISBN,
                subscribeProduct,
                thumbnailFile,
                ebookFile,
                totalDiscountRate
        );
        Mockito.when(queryProductRepository.findByIsbn(anyString()))
                .thenReturn(Optional.ofNullable(product));

        ProductCreateDto dto = DummyProductCreateDto.dummy(ISBN);

        // when
        assertThatThrownBy(() -> service.create(dto)).isInstanceOf(ClientException.class);
    }

    @Test
    @DisplayName("상품 등록 실패_전체 할인율을 조회하지 못하는 경우 예외 발생")
    void create_throwProductTotalDiscountRateNotExist() {
        // given
        File thumbnailFile = DummyFile.dummy(URL + "/image.png");
        File ebookFile = DummyFile.dummy(URL + "/ebook.pdf");
        SubscribeProduct subscribeProduct = DummySubscribeProduct.dummy();

        Mockito.when(commandFileService.register(any()))
                .thenReturn(new FileResponseDto(
                        1L,
                        thumbnailFile.getUrl(),
                        thumbnailFile.getUploadDateTime()
                ));
        Mockito.when(commandFileService.register(ebookFile))
                .thenReturn(new FileResponseDto(
                        2L,
                        ebookFile.getUrl(),
                        ebookFile.getUploadDateTime()
                ));

        Mockito.when(commandSubscribeProductRepository.save(any())).thenReturn(subscribeProduct);

        Mockito.when(queryTotalDiscountRateRepository.findById(anyInt()))
                .thenThrow(new ClientException(
                        ErrorCode.PRODUCT_TOTAL_DISCOUNT_RATE_NOT_EXIST,
                        "TotalDiscountRate not exists with id : " + 1L
                ));

        ProductCreateDto dto = DummyProductCreateDto.dummy(ISBN);

        // when
        assertThatThrownBy(() -> service.create(dto)).isInstanceOf(ClientException.class);
    }

    @Test
    @DisplayName("상품 수정 성공")
    void update() {
        // given
        File thumbnailFile = DummyFile.dummy(URL + "/image1.png");
        TotalDiscountRate totalDiscountRate = DummyTotalDiscountRate.dummy();

        Product product = DummyProduct.dummy(
                ID,
                ISBN,
                null,
                thumbnailFile,
                null,
                totalDiscountRate
        );
        Mockito.when(queryProductRepository.findProductById(anyLong()))
                .thenReturn(Optional.ofNullable(product));

        File updateThumbnailFile = DummyFile.dummy(URL + "/image2.png");
        File updateEbookFile = DummyFile.dummy(URL + "/ebook2.pdf");
        SubscribeProduct updateSubscribeProduct = SubscribeProduct.builder()
                .id(2L)
                .ISSN("00000002")
                .build();

        Mockito.when(commandFileService.register(any()))
                .thenReturn(new FileResponseDto(
                        1L,
                        updateThumbnailFile.getUrl(),
                        updateThumbnailFile.getUploadDateTime()
                ));

        Mockito.when(commandFileService.register(updateEbookFile))
                .thenReturn(new FileResponseDto(
                        2L,
                        updateEbookFile.getUrl(),
                        updateEbookFile.getUploadDateTime()
                ));

        Mockito.when(querySubscribeProductRepository.findByISSN(any()))
                .thenReturn(Optional.ofNullable(null));

        Mockito.when(queryAuthorService.findById(anyLong()))
                .thenReturn(new AuthorResponseDto(2L, "저자2", null));

        Mockito.when(queryPublisherService.findById(anyLong()))
                .thenReturn(new PublisherResponseDto(2L, "출판사2"));

        Mockito.when(queryTagService.findById(1L)).thenReturn(new TagResponseDto(1L, "아름다운"));
        Mockito.when(queryTagService.findById(2L)).thenReturn(new TagResponseDto(2L, "슬픈"));

        Mockito.when(queryCategoryService.findCategoryById(1L))
                .thenReturn(new CategoryResponseDto(1L, "영화", true, 1, 100L, "국내"));
        Mockito.when(queryCategoryService.findCategoryById(2L))
                .thenReturn(new CategoryResponseDto(2L, "소설", true, 1, 100L, "국내"));
        Mockito.when(queryCategoryService.findCategoryById(100L))
                .thenReturn(new CategoryResponseDto(100L, "국내", true, 1, null, null));

        Product updateProduct = DummyProduct.dummy(
                ID,
                ISBN,
                updateSubscribeProduct,
                updateThumbnailFile,
                updateEbookFile,
                totalDiscountRate
        );
        Mockito.when(commandProductRepository.save(any())).thenReturn(updateProduct);

        // when
        ProductUpdateDto dto = DummyProductUpdateDto.dummy("제목2");
        ProductOnlyIdDto productOnlyIdDto = service.update(1L, dto);

        // then
        assertThat(productOnlyIdDto).isNotNull();
        assertThat(productOnlyIdDto.getId()).isEqualTo(ID);

        verify(queryProductRepository, times(1)).findProductById(ID);
        verify(querySubscribeProductRepository, times(1)).findByISSN(dto.getIssn());
        verify(commandFileService, times(2)).register(any());
        verify(commandWritingService, times(1)).deleteByProduct(product);
        verify(queryAuthorService, times(1)).findById(dto.getAuthors().get(0));
        verify(commandWritingService, times(1)).register(any());
        verify(commandPublishService, times(1)).deleteByProduct(product);
        verify(queryPublisherService, times(1)).findById(dto.getPublisherId());
        verify(commandPublishService, times(1)).register(any());
        verify(commandProductTagService, times(1)).deleteByProduct(product);
        verify(queryTagService, times(2)).findById(any());
        verify(commandProductTagService, times(2)).register(any());
        verify(commandProductRepository, times(1)).save(any());
        verify(queryCategoryService, times(1)).findCategoryById(1L);
        verify(queryCategoryService, times(1)).findCategoryById(2L);
        verify(queryCategoryService, times(2)).findCategoryById(100L);
        verify(commandProductCategoryService, times(2)).register(any());
    }

    @Test
    @DisplayName("상품 SoftDelete 성공")
    void softDelete() {
        // given
        File thumbnailFile = DummyFile.dummy(URL + "/image1.png");
        File ebookFile = DummyFile.dummy(URL + "/ebook1.pdf");
        SubscribeProduct subscribeProduct = SubscribeProduct.builder()
                .id(1L)
                .ISSN("00000001")
                .build();
        TotalDiscountRate totalDiscountRate = DummyTotalDiscountRate.dummy();

        Product product = DummyProduct.dummy(
                ID,
                ISBN,
                subscribeProduct,
                thumbnailFile,
                ebookFile,
                totalDiscountRate
        );
        Mockito.when(queryProductRepository.findProductById(anyLong()))
                .thenReturn(Optional.ofNullable(product));

        // when
        service.softDelete(ID);

        // then
        assertThat(product).isNotNull();
        assertThat(product.isDeleted()).isTrue();

        verify(queryProductRepository, times(1)).findProductById(ID);
        verify(commandProductRepository, times(1)).save(product);
    }

    @Test
    @DisplayName("상품 판매여부 변경 성공")
    void changeIsSale() {
        // given
        File thumbnailFile = DummyFile.dummy(URL + "/image1.png");
        File ebookFile = DummyFile.dummy(URL + "/ebook1.pdf");
        SubscribeProduct subscribeProduct = SubscribeProduct.builder()
                .id(1L)
                .ISSN("00000001")
                .build();
        TotalDiscountRate totalDiscountRate = DummyTotalDiscountRate.dummy();

        Product product = DummyProduct.dummy(
                ID,
                ISBN,
                subscribeProduct,
                thumbnailFile,
                ebookFile,
                totalDiscountRate
        );
        Mockito.when(queryProductRepository.findProductById(anyLong()))
                .thenReturn(Optional.ofNullable(product));

        // when
        service.changeIsSale(ID);

        // then
        assertThat(product).isNotNull();
        assertThat(product.isSale()).isFalse();

        verify(queryProductRepository, times(1)).findProductById(ID);
        verify(commandProductRepository, times(1)).save(product);
    }

    @Test
    @DisplayName("상품 강제품절여부 변경 성공")
    void changeIsForcedOutOfStock() {
        // given
        File thumbnailFile = DummyFile.dummy(URL + "/image1.png");
        File ebookFile = DummyFile.dummy(URL + "/ebook1.pdf");
        SubscribeProduct subscribeProduct = SubscribeProduct.builder()
                .id(1L)
                .ISSN("00000001")
                .build();
        TotalDiscountRate totalDiscountRate = DummyTotalDiscountRate.dummy();

        Product product = DummyProduct.dummy(
                ID,
                ISBN,
                subscribeProduct,
                thumbnailFile,
                ebookFile,
                totalDiscountRate
        );
        Mockito.when(queryProductRepository.findProductById(anyLong()))
                .thenReturn(Optional.ofNullable(product));

        // when
        service.changeIsForcedOutOfStock(ID);

        // then
        assertThat(product).isNotNull();
        assertThat(product.isForcedOutOfStock()).isTrue();

        verify(queryProductRepository, times(1)).findProductById(ID);
        verify(commandProductRepository, times(1)).save(product);
    }

    @Test
    @DisplayName("상품 재고수량 차감 성공")
    void deductQuantity_success() {
        // given
        int quantity = 1000;

        File thumbnailFile = DummyFile.dummy(URL + "/image.png");
        File ebookFile = DummyFile.dummy(URL + "/ebook.pdf");
        SubscribeProduct subscribeProduct = SubscribeProduct.builder()
                .id(1L)
                .ISSN("00000001")
                .build();
        TotalDiscountRate totalDiscountRate = DummyTotalDiscountRate.dummy();

        Product product = DummyProduct.dummy(
                ID,
                ISBN,
                subscribeProduct,
                thumbnailFile,
                ebookFile,
                totalDiscountRate
        );
        Mockito.when(queryProductRepository.findProductById(anyLong()))
                .thenReturn(Optional.ofNullable(product));

        // when
        service.deductQuantity(ID, quantity);

        // then
        assertThat(product).isNotNull();
        assertThat(product.getQuantity()).isZero();

        verify(queryProductRepository, times(1)).findProductById(ID);
        verify(commandProductRepository, times(1)).save(product);
    }

    @Test
    @DisplayName("상품 재고수량 차감 실패_존재하지 않는 상품 조회 시 예외 발생")
    void deductQuantity_notFoundProduct_throwProductNotFoundException() {
        // given
        int quantity = 1000;

        // when then
        assertThatThrownBy(() -> service.deductQuantity(
                ID,
                quantity
        )).isInstanceOf(ClientException.class);
    }

    @Test
    @DisplayName("상품 재고수량 차감 실패_구매하고자 하는 수량이 판매하는 수량보다 많은 경우 예외 발생")
    void deductQuantity_throwRequestedQuantityLargerThanSellQuantityException() {
        // given
        int quantity = 1001;

        File thumbnailFile = DummyFile.dummy(URL + "/image.png");
        File ebookFile = DummyFile.dummy(URL + "/ebook.pdf");
        SubscribeProduct subscribeProduct = SubscribeProduct.builder()
                .id(1L)
                .ISSN("00000001")
                .build();
        TotalDiscountRate totalDiscountRate = DummyTotalDiscountRate.dummy();

        Product product = DummyProduct.dummy(
                ID,
                ISBN,
                subscribeProduct,
                thumbnailFile,
                ebookFile,
                totalDiscountRate
        );
        Mockito.when(queryProductRepository.findProductById(anyLong()))
                .thenReturn(Optional.ofNullable(product));

        // when
        assertThatThrownBy(() -> service.deductQuantity(
                ID,
                quantity
        )).isInstanceOf(ClientException.class);
    }

    @Test
    @DisplayName("상품 재고수량 차감 실패_구매하고자 하는 수량이 음수인 경우 예외 발생")
    void deductQuantity_quantityIsNegative_throwNegativeOrZeroQuantityException() {
        // given
        int quantity = -1;

        // when
        assertThatThrownBy(() -> service.deductQuantity(
                ID,
                quantity
        )).isInstanceOf(ClientException.class);
    }
}
