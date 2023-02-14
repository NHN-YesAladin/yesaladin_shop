package shop.yesaladin.shop.product.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.Relation;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.domain.repository.QueryRelationRepository;
import shop.yesaladin.shop.product.dto.RelationsResponseDto;
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummyPublisher;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;
import shop.yesaladin.shop.product.service.inter.QueryRelationService;
import shop.yesaladin.shop.publish.domain.model.Publish;
import shop.yesaladin.shop.publish.domain.model.Publisher;
import shop.yesaladin.shop.publish.dto.PublishResponseDto;
import shop.yesaladin.shop.publish.service.inter.QueryPublishService;
import shop.yesaladin.shop.writing.service.inter.QueryWritingService;

class QueryRelationServiceImplTest {

    private final Long PRODUCT_ID = 1L;
    private final String ISBN = "000000000000";
    private final String URL = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type";

    private QueryRelationService service;

    private QueryRelationRepository queryRelationRepository;
    private QueryWritingService queryWritingService;
    private QueryPublishService queryPublishService;

    private TotalDiscountRate totalDiscountRate;

    Clock clock = Clock.fixed(
            Instant.parse("2023-03-10T00:00:00.000Z"),
            ZoneId.of("UTC")
    );

    private final List<Product> products = new ArrayList<>();

    @BeforeEach
    void setUp() {
        queryRelationRepository = mock(QueryRelationRepository.class);
        queryWritingService = mock(QueryWritingService.class);
        queryPublishService = mock(QueryPublishService.class);

        service = new QueryRelationServiceImpl(
                queryRelationRepository,
                queryWritingService,
                queryPublishService
        );

        totalDiscountRate = DummyTotalDiscountRate.dummy();

        for (int i = 0; i < 10; i++) {
            File thumbnailFile = DummyFile.dummy(URL + i);

            boolean isDeleted = i % 2 != 0;
            Product product = DummyProduct.dummy(
                    ISBN + i,
                    null,
                    thumbnailFile,
                    null,
                    totalDiscountRate,
                    isDeleted
            );
            products.add(product);
        }
    }

    @Test
    @DisplayName("관리자용 연관관계 전체 조회 성공")
    void findAllForManager() {
        // given
        List<Relation> relations = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            relations.add(Relation.create(products.get(0), products.get(i)));
        }

        Page<Relation> page = new PageImpl<>(
                relations,
                PageRequest.of(0, 3),
                relations.size()
        );

        Mockito.when(queryRelationRepository.findAllForManager(any(), any())).thenReturn(page);

        Publisher publisher = DummyPublisher.dummy();
        Mockito.when(queryPublishService.findByProduct(any()))
                .thenReturn(new PublishResponseDto(
                        Publish.Pk.builder().productId(PRODUCT_ID).publisherId(publisher.getId()).build(),
                        LocalDateTime.now(clock).toLocalDate(),
                        products.get(0),
                        publisher
                ));

        // when
        PaginatedResponseDto<RelationsResponseDto> response = service.findAllForManager(PRODUCT_ID, PageRequest.of(0, 3));

        // then
        assertThat(response.getTotalDataCount()).isEqualTo(9);
        assertThat(response.getTotalPage()).isEqualTo(3);
    }

    @Test
    @DisplayName("일반 사용자용 연관관계 전체 조회")
    void findAll() {
        // given
        List<Relation> relations = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            relations.add(Relation.create(products.get(0), products.get(i)));
        }

        Page<Relation> page = new PageImpl<>(
                relations,
                PageRequest.of(0, 3),
                relations.size()
        );
        Mockito.when(queryRelationRepository.findAll(PRODUCT_ID, PageRequest.of(0, 3))).thenReturn(page);

        Publisher publisher = DummyPublisher.dummy();
        Mockito.when(queryPublishService.findByProduct(any()))
                .thenReturn(new PublishResponseDto(
                        Publish.Pk.builder().productId(PRODUCT_ID).publisherId(publisher.getId()).build(),
                        LocalDateTime.now(clock).toLocalDate(),
                        products.get(0),
                        publisher
                ));

        // when
        PaginatedResponseDto<RelationsResponseDto> response = service.findAll(PRODUCT_ID, PageRequest.of(0, 3));

        // then
        assertThat(response.getTotalDataCount()).isEqualTo(4);
        assertThat(response.getTotalPage()).isEqualTo(2);
    }
}