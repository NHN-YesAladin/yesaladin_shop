package shop.yesaladin.shop.tag.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.yesaladin.shop.tag.domain.model.Tag;
import shop.yesaladin.shop.tag.domain.repository.CommandTagRepository;
import shop.yesaladin.shop.tag.dto.TagResponseDto;
import shop.yesaladin.shop.tag.service.inter.CommandTagService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommandTagServiceImplTest {

    private final String NAME = "아름다운";

    private CommandTagService service;
    private CommandTagRepository commandTagRepository;

    @BeforeEach
    void setUp() {
        commandTagRepository = mock(CommandTagRepository.class);
        service = new CommandTagServiceImpl(commandTagRepository);
    }

    @Test
    @DisplayName("태그 등록 성공")
    void register() {
        // given
        Tag tag = Tag.builder().name(NAME).build();

        when(commandTagRepository.save(any())).thenReturn(tag);

        // when
        TagResponseDto response = service.register(tag);

        // then
        assertThat(response.getName()).isEqualTo(NAME);

        verify(commandTagRepository, times(1)).save(tag);
    }
}
