package shop.yesaladin.shop.writing.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.shop.writing.domain.model.Author;
import shop.yesaladin.shop.writing.domain.repository.CommandAuthorRepository;
import shop.yesaladin.shop.writing.dto.AuthorResponseDto;
import shop.yesaladin.shop.writing.dummy.DummyAuthor;
import shop.yesaladin.shop.writing.service.inter.CommandAuthorService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CommandAuthorServiceImplTest {

    private final String NAME = "저자1";

    private CommandAuthorService service;

    private CommandAuthorRepository commandAuthorRepository;

    @BeforeEach
    void setUp() {
        commandAuthorRepository = Mockito.mock(CommandAuthorRepository.class);

        service = new CommandAuthorServiceImpl(
                commandAuthorRepository
        );
    }

    @Test
    @DisplayName("저자 등록 성공")
    void register() {
        // given
        Author author = DummyAuthor.dummy(NAME, null);

        Mockito.when(commandAuthorRepository.save(any())).thenReturn(author);

        // when
        AuthorResponseDto response = service.register(author);

        // then
        assertThat(response.getName()).isEqualTo(NAME);
        assertThat(response.getMember()).isNull();

        verify(commandAuthorRepository, times(1)).save(author);
    }
}