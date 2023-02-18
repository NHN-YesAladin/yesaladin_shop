package shop.yesaladin.shop.publish.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.product.dummy.DummyPublisher;
import shop.yesaladin.shop.publish.domain.model.Publisher;
import shop.yesaladin.shop.publish.domain.repository.QueryPublisherRepository;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.publish.service.inter.QueryPublisherService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

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
        assertThatThrownBy(() -> service.findById(id)).isInstanceOf(ClientException.class);
    }

    @Test
    @DisplayName("출판사 관리자용 전체 조회 성공")
    void findAllForManager() {
        // given
        List<Publisher> publishers = new ArrayList<>();
        for (long i = 1L; i <= 10L; i++) {
            Publisher publisher = Publisher.builder().id(i).name("출판사" + i).build();
            publishers.add(publisher);
        }

        Page<Publisher> page = new PageImpl<>(
                publishers,
                PageRequest.of(0, 5),
                publishers.size()
        );

        Mockito.when(queryPublisherRepository.findAllForManager(any())).thenReturn(page);

        // when
        PaginatedResponseDto<PublisherResponseDto> response = service.findAllForManager(PageRequest.of(0, 5));

        // then
        assertThat(response.getTotalDataCount()).isEqualTo(10);
        assertThat(response.getDataList().get(0).getId()).isEqualTo(1L);
        assertThat(response.getDataList().get(9).getId()).isEqualTo(10L);
    }
}
