package shop.yesaladin.shop.publish.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.dummy.*;
import shop.yesaladin.shop.publish.domain.model.Publish;
import shop.yesaladin.shop.publish.domain.model.Publisher;
import shop.yesaladin.shop.publish.domain.repository.CommandPublishRepository;
import shop.yesaladin.shop.publish.domain.repository.QueryPublishRepository;
import shop.yesaladin.shop.publish.dto.PublishResponseDto;
import shop.yesaladin.shop.publish.exception.PublishNotFoundException;
import shop.yesaladin.shop.publish.service.inter.CommandPublishService;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommandPublishServiceImplTest {

    private final String ISBN = "0000000000001";
    private final String URL = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type";

    private CommandPublishService service;
    private CommandPublishRepository commandPublishRepository;
    private QueryPublishRepository queryPublishRepository;

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
        queryPublishRepository = mock(QueryPublishRepository.class);

        service = new CommandPublishServiceImpl(
                commandPublishRepository,
                queryPublishRepository
        );
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
        assertThat(response.getProduct().getIsbn()).isEqualTo(ISBN);
        assertThat(response.getPublisher()).isNotNull();
        assertThat(response.getPublisher().getName()).isEqualTo(publisher.getName());
    }

    @Test
    @DisplayName("출판 삭제 성공")
    void deleteByProduct_success() {
        // given
        when(queryPublishRepository.existsByProduct(any())).thenReturn(true);

        // when then
        assertThatCode(() -> service.deleteByProduct(product))
                .doesNotThrowAnyException();

        verify(commandPublishRepository, times(1)).deleteByProduct(product);
    }

    @Test
    @DisplayName("출판 삭제 실패_존재하지 않는 출판을 삭제하려 할 때 예외 발생")
    void deleteByProduct_notExistsByProduct_throwPublishNotFoundException() {
        // given
        when(queryPublishRepository.existsByProduct(any())).thenReturn(false);

        // when then
        assertThatCode(() -> service.deleteByProduct(product))
                .isInstanceOf(ClientException.class);

        verify(commandPublishRepository, never()).deleteByProduct(product);
    }
}
