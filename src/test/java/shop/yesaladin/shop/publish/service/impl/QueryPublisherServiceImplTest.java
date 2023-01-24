package shop.yesaladin.shop.publish.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.shop.product.dummy.DummyPublisher;
import shop.yesaladin.shop.publish.domain.model.Publisher;
import shop.yesaladin.shop.publish.domain.repository.QueryPublisherRepository;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.publish.exception.PublisherNotFoundException;
import shop.yesaladin.shop.publish.service.inter.QueryPublisherService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class QueryPublisherServiceImplTest {

    private QueryPublisherService service;
    private QueryPublisherRepository queryPublisherRepository;

    @BeforeEach
    void setUp() {
        queryPublisherRepository = mock(QueryPublisherRepository.class);
        service = new QueryPublisherServiceImpl(queryPublisherRepository);
    }

    @Test
    @DisplayName("ID로 출판사 조회 성공")
    void findById_success() {
        // given
        Long id = 1L;
        Publisher publisher = DummyPublisher.dummy();

        Mockito.when(queryPublisherRepository.findById(any())).thenReturn(Optional.ofNullable(publisher));

        // when
        PublisherResponseDto response = service.findById(id);

        // then
        assertThat(response.getName()).isEqualTo(publisher.getName());
    }

    @Test
    @DisplayName("ID로 출판사 조회 실패_존재하지 않는 ID로 출판사를 조회하려 하는 경우 예외 발생")
    void findById_notFoundId_throwPublisherNotFoundException() {
        // given
        Long id = 1L;
        Mockito.when(queryPublisherRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        // when then
        assertThatThrownBy(() -> service.findById(id)).isInstanceOf(PublisherNotFoundException.class);
    }

    @Test
    @DisplayName("출판사 이름으로 출판사 조회_존재하는 이름인 경우")
    void findByName_success() {
        // given
        Publisher publisher = DummyPublisher.dummy();

        when(queryPublisherRepository.findByName(anyString())).thenReturn(Optional.of(publisher));

        // when
        PublisherResponseDto foundPublisher = service.findByName(publisher.getName());

        // then
        assertThat(foundPublisher).isNotNull();
        assertThat(foundPublisher.getName()).isEqualTo(publisher.getName());
    }

    @Test
    @DisplayName("출판사 이름으로 출판사 조회_존재하지 않는 경우")
    void findByName_notExists() {
        // given
        when(queryPublisherRepository.findByName(anyString())).thenReturn(Optional.ofNullable(null));

        // when
        PublisherResponseDto foundPublisher = service.findByName("출판사");

        // then
        assertThat(foundPublisher).isNull();
    }

    @Test
    @DisplayName("출판사 전체 조회 성공")
    void findAll() {
        // given
        String name1 = "출판사1";
        String name2 = "출판사2";

        List<Publisher> publishers = List.of(
                Publisher.builder().name(name1).build(),
                Publisher.builder().name(name2).build()
        );

        Mockito.when(queryPublisherRepository.findAll()).thenReturn(publishers);

        // when
        List<PublisherResponseDto> response = service.findAll();

        // then
        assertThat(response.size()).isEqualTo(2);
        assertThat(response.get(0).getName()).isEqualTo(name1);
        assertThat(response.get(1).getName()).isEqualTo(name2);
    }
}
