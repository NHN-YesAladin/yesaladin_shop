package shop.yesaladin.shop.tag.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shop.yesaladin.shop.tag.domain.model.Tag;
import shop.yesaladin.shop.tag.domain.repository.CommandTagRepository;
import shop.yesaladin.shop.tag.service.inter.CommandTagService;

class CommandTagServiceImplTest {

    private final String TAG_NAME = "아름다운";

    private CommandTagService commandTagService;
    private CommandTagRepository commandTagRepository;

    @BeforeEach
    void setUp() {
        commandTagRepository = mock(CommandTagRepository.class);
        commandTagService = new CommandTagServiceImpl(commandTagRepository);
    }

    @Test
    void register() {
        // given
        Tag tag = Tag.builder().name(TAG_NAME).build();

        when(commandTagRepository.save(any())).thenReturn(tag);

        // when
        Tag registerdTag = commandTagService.register(tag);

        // then
        assertThat(registerdTag.getName()).isEqualTo(TAG_NAME);
    }
}
