package shop.yesaladin.shop.product.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import shop.yesaladin.shop.category.service.inter.QueryProductCategoryService;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.domain.repository.QueryProductRepository;
import shop.yesaladin.shop.product.dto.ProductDetailResponseDto;
import shop.yesaladin.shop.product.dto.ProductsResponseDto;
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

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;

class QueryProductServiceImplTest {

    private final String URL = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type";

    private QueryProductService service;

    // Product
    private QueryProductRepository queryProductRepository;

    private QueryWritingService queryWritingService;
    private QueryPublishService queryPublishService;
    private QueryProductTagService queryProductTagService;
    private QueryProductCategoryService queryProductCategoryService;
    ;

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
    @DisplayName("상품 상세 조회 성공")
    void findById() {
        // given
        String isbn = "0000000000001";

        File thumbnailFile = DummyFile.dummy(URL + "/image1.png");
        File ebookFile = DummyFile.dummy(URL + "/ebook1.pdf");
        SubscribeProduct subscribeProduct = SubscribeProduct.builder().id(1L).ISSN("00000001").build();
        TotalDiscountRate totalDiscountRate = DummyTotalDiscountRate.dummy();

        Product product = DummyProduct.dummy(1L, isbn, subscribeProduct, thumbnailFile, ebookFile, totalDiscountRate);
        Mockito.when(queryProductRepository.findById(anyLong())).thenReturn(Optional.ofNullable(product));

        Publish publish = Publish.create(product, Publisher.builder().id(1L).name("출판사").build(), LocalDateTime.now(clock).toLocalDate().toString());
        Mockito.when(queryPublishService.findByProduct(any()))
                .thenReturn(new PublishResponseDto(publish.getPk(), publish.getPublishedDate(), publish.getProduct(), publish.getPublisher()));

        // when
        ProductDetailResponseDto response = service.findById(1L);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getThumbnailFileUrl()).isEqualTo(URL + "/image1.png");
        assertThat(response.getActualPrice()).isEqualTo(10000L);
        assertThat(response.getSellingPrice()).isEqualTo(9000L);
        assertThat(response.getPointPrice()).isEqualTo(200L);
    }

    @Test
    @DisplayName("상품 전체 사용자용 전체 조회 성공")
    void findAll() {
        // given
        List<Product> products = new ArrayList<>();
        for (long i = 1L; i <= 9L; i++) {
            String isbn = "000000000000" + i;

            File thumbnailFile = DummyFile.dummy(URL + "/image" + i + ".png");
            File ebookFile = DummyFile.dummy(URL + "/ebook" + i + ".pdf");
            SubscribeProduct subscribeProduct = SubscribeProduct.builder().id(1L).ISSN("00000001").build();
            TotalDiscountRate totalDiscountRate = DummyTotalDiscountRate.dummy();

            Product product = DummyProduct.dummy(i, isbn, subscribeProduct, thumbnailFile, ebookFile, totalDiscountRate);
            if (i < 5L) {
                product.deleteProduct();
                continue;
            }
            products.add(product);

            Publish publish = Publish.create(product, Publisher.builder().id(1L).name("출판사").build(), LocalDateTime.now(clock).toLocalDate().toString());
            Mockito.when(queryPublishService.findByProduct(any()))
                    .thenReturn(new PublishResponseDto(publish.getPk(), publish.getPublishedDate(), publish.getProduct(), publish.getPublisher()));
        }

        Page<Product> page = new PageImpl<>(
                products,
                PageRequest.of(0, 5),
                products.size()
        );

        Mockito.when(queryProductRepository.findAll(any())).thenReturn(page);

        // when
        Page<ProductsResponseDto> response = service.findAll(PageRequest.of(0, 5), null);

        // then/
        assertThat(response.getTotalElements()).isEqualTo(5);
        assertThat(response.getContent().get(0).getId()).isEqualTo(5L);
        assertThat(response.getContent().get(4).getId()).isEqualTo(9L);
    }

    @Test
    @DisplayName("상품 관리자용 전체 조회 성공")
    void findAllForManager() {
        // given
        List<Product> products = new ArrayList<>();
        for (long i = 1L; i <= 9L; i++) {
            String isbn = "000000000000" + i;

            File thumbnailFile = DummyFile.dummy(URL + "/image" + i + ".png");
            File ebookFile = DummyFile.dummy(URL + "/ebook" + i + ".pdf");
            SubscribeProduct subscribeProduct = SubscribeProduct.builder().id(1L).ISSN("00000001").build();
            TotalDiscountRate totalDiscountRate = DummyTotalDiscountRate.dummy();

            Product product = DummyProduct.dummy(i, isbn, subscribeProduct, thumbnailFile, ebookFile, totalDiscountRate);
            if (i < 5L) {
                product.deleteProduct();
            }
            products.add(product);

            Publish publish = Publish.create(product, Publisher.builder().id(1L).name("출판사").build(), LocalDateTime.now(clock).toLocalDate().toString());
            Mockito.when(queryPublishService.findByProduct(any()))
                    .thenReturn(new PublishResponseDto(publish.getPk(), publish.getPublishedDate(), publish.getProduct(), publish.getPublisher()));
        }

        Page<Product> page = new PageImpl<>(
                products,
                PageRequest.of(0, 5),
                products.size()
        );

        Mockito.when(queryProductRepository.findAllForManager(any())).thenReturn(page);

        // when
        Page<ProductsResponseDto> response = service.findAllForManager(PageRequest.of(0, 5), null);

        // then
        assertThat(response.getTotalElements()).isEqualTo(9);
        assertThat(response.getContent().get(0).getId()).isEqualTo(1L);
        assertThat(response.getContent().get(8).getId()).isEqualTo(9L);
    }
}