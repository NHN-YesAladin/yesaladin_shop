package shop.yesaladin.shop.publish.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.product.dummy.DummyPublisher;
import shop.yesaladin.shop.publish.domain.model.Publisher;
import shop.yesaladin.shop.publish.domain.repository.CommandPublisherRepository;
import shop.yesaladin.shop.publish.domain.repository.QueryPublisherRepository;
import shop.yesaladin.shop.publish.dto.PublisherRequestDto;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.publish.exception.PublisherNotFoundException;
import shop.yesaladin.shop.publish.service.inter.CommandPublisherService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommandPublisherServiceImplTest {

    private final String NAME = "출판사1";

    private CommandPublisherService service;
    private CommandPublisherRepository commandPublisherRepository;
    private QueryPublisherRepository queryPublisherRepository;

    @BeforeEach
    void setUp() {
        commandPublisherRepository = mock(CommandPublisherRepository.class);
        queryPublisherRepository = mock(QueryPublisherRepository.class);

        service = new CommandPublisherServiceImpl(
                commandPublisherRepository,
                queryPublisherRepository
        );
    }

    @Test
    @DisplayName("출판사 생성 후 등록 성공")
    void create_success() {
        // given
        PublisherRequestDto createDto = new PublisherRequestDto(NAME);
        Publisher publisher = DummyPublisher.dummy(NAME);

        Mockito.when(commandPublisherRepository.save(any())).thenReturn(publisher);

        // when
        PublisherResponseDto response = service.create(createDto);

        // then
        assertThat(response.getName()).isEqualTo(NAME);

        verify(commandPublisherRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("출판사 생성 후 등록 실패_이미 존재하는 출판사명인 경우 예외 발생")
    void create_throwPublisherAlreadyExistsException() {
        // given
        PublisherRequestDto createDto = new PublisherRequestDto(NAME);
        Mockito.when(queryPublisherRepository.existsByName(anyString())).thenReturn(true);

        // when
        assertThatThrownBy(() -> service.create(createDto)).isInstanceOf(ClientException.class);

        verify(queryPublisherRepository, times(1)).existsByName(anyString());
    }

    @Test
    @DisplayName("출판사 수정 성공")
    void modify_success() {
        // given
        Long id = 1L;
        String modifiedName = "출판사2";

        PublisherRequestDto modifyDto = new PublisherRequestDto(modifiedName);
        Publisher publisher = Publisher.builder().id(id).name(NAME).build();

        Publisher modifiedPublisher = Publisher.builder().id(id).name(modifiedName).build();

        Mockito.when(queryPublisherRepository.findById(id)).thenReturn(Optional.ofNullable(publisher));
        Mockito.when(commandPublisherRepository.save(any())).thenReturn(modifiedPublisher);

        // when
        PublisherResponseDto response = service.modify(id, modifyDto);

        // then
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getName()).isEqualTo(modifiedName);

        verify(queryPublisherRepository, times(1)).findById(id);
        verify(commandPublisherRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("출판사 수정 실패_이미 존재하는 출판사명인 경우 예외 발생")
    void modify_throwPublisherAlreadyExistsException() {
        // given
        Long id = 1L;
        String modifiedName = "출판사";

        PublisherRequestDto modifyDto = new PublisherRequestDto(modifiedName);
        Publisher publisher = Publisher.builder().id(id).name(NAME).build();

        Mockito.when(queryPublisherRepository.findById(id)).thenReturn(Optional.ofNullable(publisher));
        Mockito.when(queryPublisherRepository.existsByName(anyString())).thenReturn(true);

        // when
        assertThatThrownBy(() -> service.modify(id, modifyDto)).isInstanceOf(ClientException.class);

        verify(queryPublisherRepository, times(1)).findById(anyLong());
        verify(queryPublisherRepository, times(1)).existsByName(anyString());
    }

    @Test
    @DisplayName("출판사 수정 실패_ID에 해당하는 출판사를 찾지 못한 경우 예외 발생")
    void modify_throwPublisherNotFoundException() {
        // given
        Long id = 1L;
        String modifiedName = "출판사";
        PublisherRequestDto modifyDto = new PublisherRequestDto(modifiedName);

        Mockito.when(queryPublisherRepository.findById(id)).thenThrow(PublisherNotFoundException.class);

        // when
        assertThatThrownBy(() -> service.modify(id, modifyDto)).isInstanceOf(PublisherNotFoundException.class);

        verify(queryPublisherRepository, times(1)).findById(anyLong());
    }
}
