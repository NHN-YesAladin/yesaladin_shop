package shop.yesaladin.shop.publish.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.yesaladin.shop.product.dummy.DummyPublisher;
import shop.yesaladin.shop.publish.domain.model.Publisher;
import shop.yesaladin.shop.publish.domain.repository.CommandPublisherRepository;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.publish.service.inter.CommandPublisherService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommandPublisherServiceImplTest {

    private CommandPublisherService service;
    private CommandPublisherRepository commandPublisherRepository;

    @BeforeEach
    void setUp() {
        commandPublisherRepository = mock(CommandPublisherRepository.class);
        service = new CommandPublisherServiceImpl(commandPublisherRepository);
    }

    @Test
    @DisplayName("출판사 등록 성공")
    void register() {
        // given
        Publisher publisher = DummyPublisher.dummy();

        when(commandPublisherRepository.save(any())).thenReturn(publisher);

        // when
        PublisherResponseDto response = service.register(publisher);

        // then
        assertThat(response.getName()).isEqualTo(publisher.getName());

        verify(commandPublisherRepository, times(1)).save(publisher);
    }
}
