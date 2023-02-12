package shop.yesaladin.shop.product.service.impl;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.category.service.inter.QueryProductCategoryService;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.ProductTypeCode;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.domain.repository.QueryProductRepository;
import shop.yesaladin.shop.product.dto.*;
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;
import shop.yesaladin.shop.product.service.inter.QueryProductService;
import shop.yesaladin.shop.publish.domain.model.Publish;
import shop.yesaladin.shop.publish.domain.model.Publisher;
import shop.yesaladin.shop.publish.dto.PublishResponseDto;
import shop.yesaladin.shop.publish.service.inter.QueryPublishService;
import shop.yesaladin.shop.tag.service.inter.QueryProductTagService;
import shop.yesaladin.shop.writing.service.inter.QueryWritingService;

class QueryProductServiceImplTest {

    private final String URL = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type";

    private QueryProductService service;

    // Product
    private QueryProductRepository queryProductRepository;

    private QueryWritingService queryWritingService;
    private QueryPublishService queryPublishService;
    private QueryProductTagService queryProductTagService;
    private QueryProductCategoryService queryProductCategoryService;

    private final Clock clock = Clock.fixed(
            Instant.parse("2023-01-10T00:00:00.000Z"),
            ZoneId.of("UTC")
    );

    @BeforeEach
    void setUp() {
        queryProductRepository = mock(QueryProductRepository.class);
        queryWritingService = mock(QueryWritingService.class);
        queryPublishService = mock(QueryPublishService.class);
        queryProductTagService = mock(QueryProductTagService.class);
        queryProductCategoryService = mock(QueryProductCategoryService.class);

        service = new QueryProductServiceImpl(
                queryProductRepository,
                queryWritingService,
                queryPublishService,
                queryProductTagService,
                queryProductCategoryService
        );
    }

    @Test
    @DisplayName("상품 ISBN으로 제목 조회 성공")
    void findTitleByIsbn_success() {
        // given
        String isbn = "0000000000001";
        String title = "제목";
        Mockito.when(queryProductRepository.findTitleByIsbn(isbn))
                .thenReturn(new ProductOnlyTitleDto(title));

        // when
        ProductOnlyTitleDto response = service.findTitleByIsbn(isbn);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo(title);
    }

    @Test
    @DisplayName("상품 ISBN으로 제목 조회 실패_ISBN로 조회되는 상품이 없는 경우 예외 발생")
    void findTitleByIsbn_notExistIsbn_throwProductNotFoundException() {
        // given
        String isbn = "0000000000001";
        Mockito.when(queryProductRepository.findTitleByIsbn(isbn)).thenReturn(null);

        // when then
        assertThatThrownBy(() -> service.findTitleByIsbn(isbn)).isInstanceOf(ClientException.class);
    }

    @Test
    @DisplayName("상품 ID로 수량 조회 성공")
    void findQuantityById_success() {
        // given
        Long id = 1L;
        Long quantity = 100L;
        Mockito.when(queryProductRepository.findQuantityById(id))
                .thenReturn(quantity);

        // when
        Long response = service.findQuantityById(id);

        // then
        assertThat(response).isEqualTo(quantity);
    }

    @Test
    @DisplayName("상품 ISBN으로 제목 조회 실패_ISBN로 조회되는 상품이 없는 경우 예외 발생")
    void findQuantityById_notExistId_throwProductNotFoundException() {
        // given
        Long id = 1L;
        Mockito.when(queryProductRepository.findQuantityById(id)).thenReturn(null);

        // when then
        assertThatThrownBy(() -> service.findQuantityById(id)).isInstanceOf(ClientException.class);
    }

    @Test
    @DisplayName("상품 상세 조회 성공")
    void findById() {
        // given
        String isbn = "0000000000001";

        File thumbnailFile = DummyFile.dummy(URL + "/image1.png");
        File ebookFile = DummyFile.dummy(URL + "/ebook1.pdf");
        SubscribeProduct subscribeProduct = SubscribeProduct.builder()
                .id(1L)
                .ISSN("00000001")
                .build();
        TotalDiscountRate totalDiscountRate = DummyTotalDiscountRate.dummy();

        Product product = DummyProduct.dummy(
                1L,
                isbn,
                subscribeProduct,
                thumbnailFile,
                ebookFile,
                totalDiscountRate
        );
        Mockito.when(queryProductRepository.findProductById(anyLong()))
                .thenReturn(Optional.ofNullable(product));

        Publish publish = Publish.create(
                product,
                Publisher.builder().id(1L).name("출판사").build(),
                LocalDateTime.now(clock).toLocalDate().toString()
        );
        Mockito.when(queryPublishService.findByProduct(any()))
                .thenReturn(new PublishResponseDto(
                        publish.getPk(),
                        publish.getPublishedDate(),
                        publish.getProduct(),
                        publish.getPublisher()
                ));

        // when
        ProductDetailResponseDto response = service.findDetailProductById(1L);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getThumbnailFileUrl()).isEqualTo(URL + "/image1.png");
        assertThat(response.getActualPrice()).isEqualTo(10000L);
        assertThat(response.getSellingPrice()).isEqualTo(9000L);
        assertThat(response.getPointPrice()).isEqualTo(200L);
    }

    @Test
    @DisplayName("상품 수정 View 조회 성공")
    void findProductByIdForForm() {
        // given
        String isbn = "0000000000001";

        File thumbnailFile = DummyFile.dummy(URL + "/image1.png");
        File ebookFile = DummyFile.dummy(URL + "/ebook1.pdf");
        SubscribeProduct subscribeProduct = SubscribeProduct.builder()
                .id(1L)
                .ISSN("00000001")
                .build();
        TotalDiscountRate totalDiscountRate = DummyTotalDiscountRate.dummy();

        Product product = DummyProduct.dummy(
                1L,
                isbn,
                subscribeProduct,
                thumbnailFile,
                ebookFile,
                totalDiscountRate
        );
        Mockito.when(queryProductRepository.findProductById(anyLong()))
                .thenReturn(Optional.ofNullable(product));

        Publish publish = Publish.create(
                product,
                Publisher.builder().id(1L).name("출판사").build(),
                LocalDateTime.now(clock).toLocalDate().toString()
        );
        Mockito.when(queryPublishService.findByProduct(any()))
                .thenReturn(new PublishResponseDto(
                        publish.getPk(),
                        publish.getPublishedDate(),
                        publish.getProduct(),
                        publish.getPublisher()
                ));

        // when
        ProductModifyDto response = service.findProductByIdForForm(1L);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getIsbn()).isEqualTo(product.getIsbn());
        assertThat(response.getThumbnailFile()).isEqualTo(product.getThumbnailFile().getUrl());
        assertThat(response.getTitle()).isEqualTo(product.getTitle());
        assertThat(response.getContents()).isEqualTo(product.getContents());
        assertThat(response.getDescription()).isEqualTo(product.getDescription());
        assertThat(response.getEbookFileUrl()).isEqualTo(URL + "/ebook1.pdf");
        assertThat(response.getActualPrice()).isEqualTo(product.getActualPrice());
        assertThat(response.getIsSeparatelyDiscount()).isEqualTo(product.isSeparatelyDiscount());
        assertThat(response.getDiscountRate()).isEqualTo(product.getDiscountRate());
        assertThat(response.getIsGivenPoint()).isEqualTo(product.isGivenPoint());
        assertThat(response.getGivenPointRate()).isEqualTo(product.getGivenPointRate());
        assertThat(response.getProductTypeCode()).isEqualTo(product.getProductTypeCode().name());
        assertThat(response.getProductSavingMethodCode()).isEqualTo(product.getProductSavingMethodCode()
                .name());
        assertThat(response.getIsSubscriptionAvailable()).isEqualTo(product.isSubscriptionAvailable());
        assertThat(response.getQuantity()).isEqualTo(product.getQuantity());
        assertThat(response.getPreferentialShowRanking()).isEqualTo(product.getPreferentialShowRanking());
    }

    @Test
    @DisplayName("카트 상품 조회 성공")
    void getCartProduct() {
        // given
        String isbn = "0000000000001";
        Map<String, String> cart = new HashMap<>();
        cart.put("1", "100");

        File thumbnailFile = DummyFile.dummy(URL + "/image1.png");
        File ebookFile = DummyFile.dummy(URL + "/ebook1.pdf");
        SubscribeProduct subscribeProduct = SubscribeProduct.builder()
                .id(1L)
                .ISSN("00000001")
                .build();
        TotalDiscountRate totalDiscountRate = DummyTotalDiscountRate.dummy();

        Product product = DummyProduct.dummy(
                1L,
                isbn,
                subscribeProduct,
                thumbnailFile,
                ebookFile,
                totalDiscountRate
        );
        Mockito.when(queryProductRepository.findProductById(anyLong()))
                .thenReturn(Optional.ofNullable(product));

        // when
        List<ViewCartDto> response = service.getCartProduct(cart);

        // then
        assertThat(response).hasSize(1);
        assertThat(response.get(0).getId()).isEqualTo(1L);
        assertThat(response.get(0).getIsbn()).isEqualTo(isbn);

    }

    @Test
    @DisplayName("상품 전체 사용자용 전체 조회 성공_타입 있음")
    void findAll_useType() {
        // given
        List<Product> products = new ArrayList<>();
        for (long i = 1L; i <= 9L; i++) {
            String isbn = "000000000000" + i;

            File thumbnailFile = DummyFile.dummy(URL + "/image" + i + ".png");
            File ebookFile = DummyFile.dummy(URL + "/ebook" + i + ".pdf");
            SubscribeProduct subscribeProduct = SubscribeProduct.builder()
                    .id(1L)
                    .ISSN("00000001")
                    .build();
            TotalDiscountRate totalDiscountRate = DummyTotalDiscountRate.dummy();

            Product product = DummyProduct.dummy(
                    i,
                    isbn,
                    subscribeProduct,
                    thumbnailFile,
                    ebookFile,
                    totalDiscountRate
            );
            if (i < 5L) {
                product.deleteProduct();
                continue;
            }
            products.add(product);

            Publish publish = Publish.create(
                    product,
                    Publisher.builder().id(1L).name("출판사").build(),
                    LocalDateTime.now(clock).toLocalDate().toString()
            );
            Mockito.when(queryPublishService.findByProduct(any()))
                    .thenReturn(new PublishResponseDto(
                            publish.getPk(),
                            publish.getPublishedDate(),
                            publish.getProduct(),
                            publish.getPublisher()
                    ));
        }

        Page<Product> page = new PageImpl<>(
                products,
                PageRequest.of(0, 5),
                products.size()
        );

        Mockito.when(queryProductRepository.findAllByTypeId(any(), any())).thenReturn(page);

        // when
        PaginatedResponseDto<ProductsResponseDto> response = service.findAll(
                PageRequest.of(0, 5),
                ProductTypeCode.NONE.getId()
        );

        // then/
        assertThat(response.getTotalDataCount()).isEqualTo(5);
        assertThat(response.getDataList().get(0).getId()).isEqualTo(5L);
        assertThat(response.getDataList().get(4).getId()).isEqualTo(9L);
    }

    @Test
    @DisplayName("상품 전체 사용자용 전체 조회 성공_타입없음")
    void findAll() {
        // given
        List<Product> products = new ArrayList<>();
        for (long i = 1L; i <= 9L; i++) {
            String isbn = "000000000000" + i;

            File thumbnailFile = DummyFile.dummy(URL + "/image" + i + ".png");
            File ebookFile = DummyFile.dummy(URL + "/ebook" + i + ".pdf");
            SubscribeProduct subscribeProduct = SubscribeProduct.builder()
                    .id(1L)
                    .ISSN("00000001")
                    .build();
            TotalDiscountRate totalDiscountRate = DummyTotalDiscountRate.dummy();

            Product product = DummyProduct.dummy(
                    i,
                    isbn,
                    subscribeProduct,
                    thumbnailFile,
                    ebookFile,
                    totalDiscountRate
            );
            if (i < 5L) {
                product.deleteProduct();
                continue;
            }
            products.add(product);

            Publish publish = Publish.create(
                    product,
                    Publisher.builder().id(1L).name("출판사").build(),
                    LocalDateTime.now(clock).toLocalDate().toString()
            );
            Mockito.when(queryPublishService.findByProduct(any()))
                    .thenReturn(new PublishResponseDto(
                            publish.getPk(),
                            publish.getPublishedDate(),
                            publish.getProduct(),
                            publish.getPublisher()
                    ));
        }

        Page<Product> page = new PageImpl<>(
                products,
                PageRequest.of(0, 5),
                products.size()
        );

        Mockito.when(queryProductRepository.findAll(any())).thenReturn(page);

        // when
        PaginatedResponseDto<ProductsResponseDto> response = service.findAll(
                PageRequest.of(0, 5),
                null
        );

        // then/
        assertThat(response.getTotalDataCount()).isEqualTo(5);
        assertThat(response.getDataList().get(0).getId()).isEqualTo(5L);
        assertThat(response.getDataList().get(4).getId()).isEqualTo(9L);
    }

    @Test
    @DisplayName("상품 관리자용 전체 조회 성공_타입있음")
    void findAllForManager_useType() {
        // given
        List<Product> products = new ArrayList<>();
        for (long i = 1L; i <= 9L; i++) {
            String isbn = "000000000000" + i;

            File thumbnailFile = DummyFile.dummy(URL + "/image" + i + ".png");
            File ebookFile = DummyFile.dummy(URL + "/ebook" + i + ".pdf");
            SubscribeProduct subscribeProduct = SubscribeProduct.builder()
                    .id(1L)
                    .ISSN("00000001")
                    .build();
            TotalDiscountRate totalDiscountRate = DummyTotalDiscountRate.dummy();

            Product product = DummyProduct.dummy(
                    i,
                    isbn,
                    subscribeProduct,
                    thumbnailFile,
                    ebookFile,
                    totalDiscountRate
            );
            if (i < 5L) {
                product.deleteProduct();
            }
            products.add(product);

            Publish publish = Publish.create(
                    product,
                    Publisher.builder().id(1L).name("출판사").build(),
                    LocalDateTime.now(clock).toLocalDate().toString()
            );
            Mockito.when(queryPublishService.findByProduct(any()))
                    .thenReturn(new PublishResponseDto(
                            publish.getPk(),
                            publish.getPublishedDate(),
                            publish.getProduct(),
                            publish.getPublisher()
                    ));
        }

        Page<Product> page = new PageImpl<>(
                products,
                PageRequest.of(0, 5),
                products.size()
        );

        Mockito.when(queryProductRepository.findAllByTypeIdForManager(any(), any()))
                .thenReturn(page);

        // when
        PaginatedResponseDto<ProductsResponseDto> response = service.findAllForManager(PageRequest.of(
                0,
                5
        ), ProductTypeCode.NONE.getId());

        // then
        assertThat(response.getTotalDataCount()).isEqualTo(9);
        assertThat(response.getDataList().get(0).getId()).isEqualTo(1L);
        assertThat(response.getDataList().get(8).getId()).isEqualTo(9L);
    }

    @Test
    @DisplayName("상품 관리자용 전체 조회 성공_타입없음")
    void findAllForManager() {
        // given
        List<Product> products = new ArrayList<>();
        for (long i = 1L; i <= 9L; i++) {
            String isbn = "000000000000" + i;

            File thumbnailFile = DummyFile.dummy(URL + "/image" + i + ".png");
            File ebookFile = DummyFile.dummy(URL + "/ebook" + i + ".pdf");
            SubscribeProduct subscribeProduct = SubscribeProduct.builder()
                    .id(1L)
                    .ISSN("00000001")
                    .build();
            TotalDiscountRate totalDiscountRate = DummyTotalDiscountRate.dummy();

            Product product = DummyProduct.dummy(
                    i,
                    isbn,
                    subscribeProduct,
                    thumbnailFile,
                    ebookFile,
                    totalDiscountRate
            );
            if (i < 5L) {
                product.deleteProduct();
            }
            products.add(product);

            Publish publish = Publish.create(
                    product,
                    Publisher.builder().id(1L).name("출판사").build(),
                    LocalDateTime.now(clock).toLocalDate().toString()
            );
            Mockito.when(queryPublishService.findByProduct(any()))
                    .thenReturn(new PublishResponseDto(
                            publish.getPk(),
                            publish.getPublishedDate(),
                            publish.getProduct(),
                            publish.getPublisher()
                    ));
        }

        Page<Product> page = new PageImpl<>(
                products,
                PageRequest.of(0, 5),
                products.size()
        );

        Mockito.when(queryProductRepository.findAllForManager(any())).thenReturn(page);

        // when
        PaginatedResponseDto<ProductsResponseDto> response = service.findAllForManager(PageRequest.of(
                0,
                5
        ), null);

        // then
        assertThat(response.getTotalDataCount()).isEqualTo(9);
        assertThat(response.getDataList().get(0).getId()).isEqualTo(1L);
        assertThat(response.getDataList().get(8).getId()).isEqualTo(9L);
    }

    @Test
    @DisplayName("상품의 연관 상품 등록을 위한 검색 성공")
    void findProductRelationByTitle() {
        //given
        List<Product> products = new ArrayList<>();
        String isbn = "0000000000001";

        File thumbnailFile = DummyFile.dummy(URL + "/image.png");
        File ebookFile = DummyFile.dummy(URL + "/ebook.pdf");
        SubscribeProduct subscribeProduct = SubscribeProduct.builder()
                .id(1L)
                .ISSN("00000001")
                .build();
        TotalDiscountRate totalDiscountRate = DummyTotalDiscountRate.dummy();

        Product product = DummyProduct.dummy(
                isbn,
                subscribeProduct,
                thumbnailFile,
                ebookFile,
                totalDiscountRate
        );
        products.add(product);
        Mockito.when(queryProductRepository.findProductRelationByTitle(
                2L,
                "ex",
                PageRequest.of(0, 1)
        )).thenReturn(new PageImpl<>(products, PageRequest.of(0, 1), 1L));
        Publish publish = Publish.create(
                product,
                Publisher.builder().id(1L).name("출판사").build(),
                LocalDateTime.now(clock).toLocalDate().toString()
        );
        Mockito.when(queryPublishService.findByProduct(any()))
                .thenReturn(new PublishResponseDto(
                        publish.getPk(),
                        publish.getPublishedDate(),
                        publish.getProduct(),
                        publish.getPublisher()
                ));

        Page<RelationsResponseDto> result = service.findProductRelationByTitle(
                2L,
                "ex",
                PageRequest.of(0, 1)
        );

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getTitle()).contains("ex");
    }

    @Test
    @DisplayName("최신 상품 조회 성공")
    void findRecentProductByPublishedDate() {
        //given
        List<Product> products = new ArrayList<>();
        String isbn = "0000000000001";
        File thumbnailFile = DummyFile.dummy(URL + "/image.png");
        File ebookFile = DummyFile.dummy(URL + "/ebook.pdf");
        SubscribeProduct subscribeProduct = SubscribeProduct.builder()
                .id(1L)
                .ISSN("00000001")
                .build();
        TotalDiscountRate totalDiscountRate = DummyTotalDiscountRate.dummy();

        Product product = DummyProduct.dummy(
                isbn,
                subscribeProduct,
                thumbnailFile,
                ebookFile,
                totalDiscountRate
        );
        Publish publish = Publish.create(
                product,
                Publisher.builder().id(1L).name("출판사").build(),
                LocalDate.of(2011, 11, 11).toString()
        );
        Mockito.when(queryPublishService.findByProduct(product))
                .thenReturn(new PublishResponseDto(
                        publish.getPk(),
                        publish.getPublishedDate(),
                        publish.getProduct(),
                        publish.getPublisher()
                ));

        String isbn2 = "0000000000002";
        File thumbnailFile2 = DummyFile.dummy(URL + "/image2.png");
        File ebookFile2 = DummyFile.dummy(URL + "/ebook2.pdf");
        SubscribeProduct subscribeProduct2 = SubscribeProduct.builder()
                .id(2L)
                .ISSN("0000000000002")
                .build();

        Product product2 = DummyProduct.dummy(
                isbn2,
                subscribeProduct2,
                thumbnailFile2,
                ebookFile2,
                totalDiscountRate
        );

        products.add(product2);
        products.add(product);

        Mockito.when(queryProductRepository.findRecentProductByPublishedDate(PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(products, PageRequest.of(0, 2), 2L));
        Publish publish2 = Publish.create(
                product2,
                Publisher.builder().id(1L).name("출판사").build(),
                LocalDate.of(2011, 12, 12).toString()
        );
        Mockito.when(queryPublishService.findByProduct(product2))
                .thenReturn(new PublishResponseDto(
                        publish2.getPk(),
                        publish2.getPublishedDate(),
                        publish2.getProduct(),
                        publish2.getPublisher()
                ));

        Page<ProductRecentResponseDto> dto = service.findRecentProductByPublishedDate(PageRequest.of(0, 10));
        assertThat(dto.getTotalElements()).isEqualTo(2);
        assertThat(dto.getContent().get(0).getId()).isEqualTo(product2.getId());
        assertThat(dto.getContent().get(1).getId()).isEqualTo(product.getId());
    }
}