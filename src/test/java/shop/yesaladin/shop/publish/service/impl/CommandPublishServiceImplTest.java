package shop.yesaladin.shop.publish.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummyPublisher;
import shop.yesaladin.shop.publish.domain.model.Publish;
import shop.yesaladin.shop.publish.domain.model.Publisher;
import shop.yesaladin.shop.publish.domain.repository.CommandPublishRepository;
import shop.yesaladin.shop.publish.dto.PublishResponseDto;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.publish.service.inter.CommandPublishService;

class CommandPublishServiceImplTest {

    private final String ISBN = "00001-...";

    private CommandPublishService commandPublishService;
    private CommandPublishRepository commandPublishRepository;

    private Publish publish;

    @BeforeEach
    void setUp() {
        Product product = DummyProduct.dummy(ISBN);
        Publisher publisher = DummyPublisher.dummy();
        LocalDateTime now = LocalDateTime.now();

        publish = Publish.create(product, publisher, now.toLocalDate().toString());

        commandPublishRepository = mock(CommandPublishRepository.class);
        commandPublishService = new CommandPublishServiceImpl(commandPublishRepository);
    }

    @Test
    void register() {
        // given
        when(commandPublishRepository.save(any())).thenReturn(publish);

        // when
        PublishResponseDto registerdPublish = commandPublishService.register(publish);

        // then
        assertThat(registerdPublish.getProduct().getISBN()).isEqualTo(ISBN);
    }
}
