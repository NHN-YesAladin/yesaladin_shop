package shop.yesaladin.shop.publish.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shop.yesaladin.shop.product.dummy.DummyPublisher;
import shop.yesaladin.shop.publish.domain.model.Publisher;
import shop.yesaladin.shop.publish.domain.repository.QueryPublisherRepository;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.publish.service.inter.QueryPublisherService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class QueryPublisherServiceImplTest {

    private QueryPublisherService queryPublisherService;
    private QueryPublisherRepository queryPublisherRepository;

    @BeforeEach
    void setUp() {
        queryPublisherRepository = mock(QueryPublisherRepository.class);
        queryPublisherService = new QueryPublisherServiceImpl(queryPublisherRepository);
    }

    @Test
    void findByName() {
        // given
        Publisher publisher = DummyPublisher.dummy();

        when(queryPublisherRepository.findByName(anyString())).thenReturn(Optional.of(publisher));

        // when
        PublisherResponseDto foundPublisher = queryPublisherService.findByName(publisher.getName());

        // then
        assertThat(foundPublisher).isNotNull();
    }
}
