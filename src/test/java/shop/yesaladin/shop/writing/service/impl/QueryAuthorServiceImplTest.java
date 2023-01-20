package shop.yesaladin.shop.writing.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.shop.member.dummy.MemberDummy;
import shop.yesaladin.shop.writing.domain.model.Author;
import shop.yesaladin.shop.writing.domain.repository.QueryAuthorRepository;
import shop.yesaladin.shop.writing.dto.AuthorResponseDto;
import shop.yesaladin.shop.writing.dto.AuthorsResponseDto;
import shop.yesaladin.shop.writing.dummy.DummyAuthor;
import shop.yesaladin.shop.writing.exception.AuthorNotFoundException;
import shop.yesaladin.shop.writing.service.inter.QueryAuthorService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QueryAuthorServiceImplTest {

    private QueryAuthorService service;

    private QueryAuthorRepository queryAuthorRepository;

    @BeforeEach
    void setUp() {
        queryAuthorRepository = Mockito.mock(QueryAuthorRepository.class);

        service = new QueryAuthorServiceImpl(
                queryAuthorRepository
        );
    }

    @Test
    @DisplayName("ID로 저자 조회 성공")
    void findById_success() {
        // given
        Long id = 1L;
        String name = "저자1";
        Author author = DummyAuthor.dummy(name, null);

        Mockito.when(queryAuthorRepository.findById(id)).thenReturn(Optional.ofNullable(author));

        // when
        AuthorResponseDto response = service.findById(id);

        // then
        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getMember()).isNull();
    }

    @Test
    @DisplayName("Id로 저자 조회 실패_존재하지 않는 Id로 저자를 조회하려 하는 경우 예외 발생")
    void findById_notFoundId_throwAuthorNotFoundException() {
        // given
        Long id = 1L;
        Mockito.when(queryAuthorRepository.findById(id)).thenReturn(Optional.ofNullable(null));

        // when then
        assertThatThrownBy(() -> service.findById(id)).isInstanceOf(AuthorNotFoundException.class);
    }

    @Test
    @DisplayName("저자 전체 조회 성공")
    void findAll() {
        // given
        List<Author> authors = Arrays.asList(
                DummyAuthor.dummy("저자1", MemberDummy.dummyWithId(1L)),
                DummyAuthor.dummy("저자2", null)
        );

        Mockito.when(queryAuthorRepository.findAll()).thenReturn(authors);

        // when
        List<AuthorsResponseDto> response = service.findAll();

        // then
        assertThat(response.size()).isEqualTo(2);
        assertThat(response.get(0).getName()).isEqualTo("저자1");
        assertThat(response.get(1).getName()).isEqualTo("저자2");
    }
}