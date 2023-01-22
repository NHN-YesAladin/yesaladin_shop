package shop.yesaladin.shop.publish.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.dummy.*;
import shop.yesaladin.shop.publish.domain.model.Publish;
import shop.yesaladin.shop.publish.domain.model.Publisher;
import shop.yesaladin.shop.publish.domain.repository.QueryPublishRepository;
import shop.yesaladin.shop.publish.dto.PublishResponseDto;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.publish.service.inter.QueryPublishService;
import shop.yesaladin.shop.tag.domain.model.ProductTag;
import shop.yesaladin.shop.tag.domain.model.Tag;
import shop.yesaladin.shop.tag.dto.ProductTagResponseDto;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class QueryPublishServiceImplTest {

    private final String ISBN = "0000000000001";
    private final String URL = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type";

    private QueryPublishService service;
    private QueryPublishRepository queryPublishRepository;

    private final Clock clock = Clock.fixed(
            Instant.parse("2023-01-20T00:00:00.000Z"),
            ZoneId.of("UTC")
    );

    private Product product;

    @BeforeEach
    void setUp() {
        product = DummyProduct.dummy(
                ISBN,
                DummySubscribeProduct.dummy(),
                DummyFile.dummy(URL + "/image.png"),
                DummyFile.dummy(URL + "/ebook.pdf"),
                DummyTotalDiscountRate.dummy()
        );

        queryPublishRepository = mock(QueryPublishRepository.class);

        service = new QueryPublishServiceImpl(
                queryPublishRepository
        );
    }

    @Test
    @DisplayName("상품으로 출판 관계 조회 성공")
    void findByProduct() {
        // given
        Publish publish = Publish.create(product, DummyPublisher.dummy(), LocalDateTime.now(clock).toLocalDate().toString());

        Mockito.when(queryPublishRepository.findByProduct(product)).thenReturn(Optional.ofNullable(publish));

        // when
        PublishResponseDto response = service.findByProduct(product);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getPublisher().getName()).isEqualTo("출판사");
        assertThat(response.getProduct().getISBN()).isEqualTo(ISBN);
    }
}