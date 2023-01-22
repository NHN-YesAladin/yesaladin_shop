package shop.yesaladin.shop.publish.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.dummy.*;
import shop.yesaladin.shop.publish.domain.model.Publish;
import shop.yesaladin.shop.publish.domain.model.Publisher;
import shop.yesaladin.shop.publish.domain.repository.CommandPublishRepository;
import shop.yesaladin.shop.publish.dto.PublishResponseDto;
import shop.yesaladin.shop.publish.service.inter.CommandPublishService;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CommandPublishServiceImplTest {

    private final String ISBN = "0000000000001";
    private final String NAME = "행복한";
    private final String URL = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type";

    private CommandPublishService service;
    private CommandPublishRepository commandPublishRepository;

    private final Clock clock = Clock.fixed(
            Instant.parse("2023-01-20T00:00:00.000Z"),
            ZoneId.of("UTC")
    );

    private Product product;
    private Publisher publisher;
    private Publish publish;

    @BeforeEach
    void setUp() {
        product = DummyProduct.dummy(
                ISBN,
                DummySubscribeProduct.dummy(),
                DummyFile.dummy(URL + "/image.png"),
                DummyFile.dummy(URL + "/ebook.pdf"),
                DummyTotalDiscountRate.dummy()
        );
        publisher = DummyPublisher.dummy();

        publish = Publish.create(product, publisher, LocalDateTime.now(clock).toLocalDate().toString());

        commandPublishRepository = mock(CommandPublishRepository.class);
        service = new CommandPublishServiceImpl(commandPublishRepository);
    }

    @Test
    @DisplayName("출판 관계 등록 성공")
    void register() {
        // given
        when(commandPublishRepository.save(any())).thenReturn(publish);

        // when
        PublishResponseDto response = service.register(publish);

        // then
        assertThat(response.getProduct()).isNotNull();
        assertThat(response.getProduct().getISBN()).isEqualTo(ISBN);
        assertThat(response.getPublisher()).isNotNull();
        assertThat(response.getPublisher().getName()).isEqualTo(publisher.getName());
    }
}
