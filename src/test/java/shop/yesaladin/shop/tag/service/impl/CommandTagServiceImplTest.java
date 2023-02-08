package shop.yesaladin.shop.tag.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.tag.domain.model.Tag;
import shop.yesaladin.shop.tag.domain.repository.CommandTagRepository;
import shop.yesaladin.shop.tag.domain.repository.QueryTagRepository;
import shop.yesaladin.shop.tag.dto.TagRequestDto;
import shop.yesaladin.shop.tag.dto.TagResponseDto;
import shop.yesaladin.shop.tag.exception.TagAlreadyExistsException;
import shop.yesaladin.shop.tag.exception.TagNotFoundException;
import shop.yesaladin.shop.tag.service.inter.CommandTagService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommandTagServiceImplTest {

    private CommandTagService service;
    private CommandTagRepository commandTagRepository;
    private QueryTagRepository queryTagRepository;

    @BeforeEach
    void setUp() {
        commandTagRepository = mock(CommandTagRepository.class);
        queryTagRepository = mock(QueryTagRepository.class);

        service = new CommandTagServiceImpl(
                commandTagRepository,
                queryTagRepository
        );
    }

    @Test
    @DisplayName("태그 생성 후 등록 성공")
    void create_success() {
        // given
        String name = "아름다운";
        Tag tag = Tag.builder().name(name).build();
        TagRequestDto createDto = new TagRequestDto(name);

        Mockito.when(commandTagRepository.save(any())).thenReturn(tag);

        // when
        TagResponseDto response = service.create(createDto);

        // then
        assertThat(response.getName()).isEqualTo(name);

        verify(commandTagRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("태그 생성 후 등록 실패_이미 존재하는 태그명인 경우 예외 발생")
    void create_throwTagAlreadyExistsException() {
        // given
        String name = "아름다운";
        TagRequestDto createDto = new TagRequestDto(name);

        Mockito.when(queryTagRepository.existsByName(anyString())).thenReturn(true);

        // when
        assertThatThrownBy(() -> service.create(createDto)).isInstanceOf(ClientException.class);

        verify(queryTagRepository, times(1)).existsByName(anyString());
    }

    @Test
    @DisplayName("태그 수정 성공")
    void modify_success() {
        // given
        Long id = 1L;
        String name = "아름다운";
        String modifiedName = "슬픈";

        TagRequestDto modifyDto = new TagRequestDto(modifiedName);
        Tag tag = Tag.builder().id(id).name(name).build();
        Tag modifiedTag = Tag.builder().id(id).name(modifiedName).build();

        Mockito.when(queryTagRepository.findById(id)).thenReturn(Optional.ofNullable(tag));
        Mockito.when(commandTagRepository.save(any())).thenReturn(modifiedTag);

        // when
        TagResponseDto response = service.modify(id, modifyDto);

        // then
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getName()).isEqualTo(modifiedName);

        verify(queryTagRepository, times(1)).findById(id);
        verify(commandTagRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("태그 수정 실패_이미 존재하는 태그명인 경우 예외 발생")
    void modify_throwTagAlreadyExistsException() {
        // given
        Long id = 1L;
        String name = "아름다운";
        String modifiedName = "슬픈";

        TagRequestDto modifyDto = new TagRequestDto(modifiedName);
        Tag tag = Tag.builder().id(id).name(name).build();

        Mockito.when(queryTagRepository.findById(id)).thenReturn(Optional.ofNullable(tag));
        Mockito.when(queryTagRepository.existsByName(anyString())).thenReturn(true);

        // when
        assertThatThrownBy(() -> service.modify(id, modifyDto)).isInstanceOf(ClientException.class);

        verify(queryTagRepository, times(1)).findById(anyLong());
        verify(queryTagRepository, times(1)).existsByName(anyString());
    }

    @Test
    @DisplayName("태그 수정 실패_ID에 해당하는 태그를 찾지 못한 경우 예외 발생")
    void modify_throwTagNotFoundException() {
        // given
        Long id = 1L;
        String name = "아름다운";
        String modifiedName = "슬픈";
        TagRequestDto modifyDto = new TagRequestDto(modifiedName);

        Mockito.when(queryTagRepository.findById(id)).thenThrow(TagNotFoundException.class);

        // when
        assertThatThrownBy(() -> service.modify(id, modifyDto)).isInstanceOf(TagNotFoundException.class);

        verify(queryTagRepository, times(1)).findById(anyLong());

    }
}
