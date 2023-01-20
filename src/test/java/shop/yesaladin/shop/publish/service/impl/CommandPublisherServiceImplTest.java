package shop.yesaladin.shop.publish.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shop.yesaladin.shop.publish.domain.model.Publisher;
import shop.yesaladin.shop.publish.domain.repository.CommandPublisherRepository;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.publish.service.inter.CommandPublisherService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CommandPublisherServiceImplTest {

    private final String PUBLISHER_NAME = "테스트";

    private CommandPublisherService commandPublisherService;
    private CommandPublisherRepository commandPublisherRepository;

    @BeforeEach
    void setUp() {
        commandPublisherRepository = mock(CommandPublisherRepository.class);
        commandPublisherService = new CommandPublisherServiceImpl(commandPublisherRepository);
    }

    @Test
    void register() {
        // given
        Publisher publisher = Publisher.builder().name(PUBLISHER_NAME).build();

        when(commandPublisherRepository.save(any())).thenReturn(publisher);

        // when
        PublisherResponseDto registerdPublisher = commandPublisherService.register(publisher);

        // then
        assertThat(registerdPublisher.getName()).isEqualTo(PUBLISHER_NAME);
    }
}
