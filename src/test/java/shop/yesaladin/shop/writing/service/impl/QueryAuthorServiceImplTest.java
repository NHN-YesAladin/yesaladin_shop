package shop.yesaladin.shop.writing.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.member.dummy.MemberDummy;
import shop.yesaladin.shop.writing.domain.model.Author;
import shop.yesaladin.shop.writing.domain.repository.QueryAuthorRepository;
import shop.yesaladin.shop.writing.dto.AuthorResponseDto;
import shop.yesaladin.shop.writing.dto.AuthorsResponseDto;
import shop.yesaladin.shop.writing.dummy.DummyAuthor;
import shop.yesaladin.shop.writing.exception.AuthorNotFoundException;
import shop.yesaladin.shop.writing.service.inter.QueryAuthorService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

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
    @DisplayName("ID로 저자 조회 실패_존재하지 않는 ID로 저자를 조회하려 하는 경우 예외 발생")
    void findById_notFoundId_throwAuthorNotFoundException() {
        // given
        Long id = 1L;
        Mockito.when(queryAuthorRepository.findById(id)).thenReturn(Optional.ofNullable(null));

        // when then
        assertThatThrownBy(() -> service.findById(id)).isInstanceOf(ClientException.class);
    }

    @Test
    @DisplayName("저자 관리자용 전체 조회 성공")
    void findAllForManager() {
        // given
        List<Author> authors = new ArrayList<>();
        for (long i = 1L; i <= 10L; i++) {
            Author author = Author.builder().id(i).name("저자" + i).member(null).build();
            authors.add(author);
        }

        Page<Author> page = new PageImpl<>(
                authors,
                PageRequest.of(0, 5),
                authors.size()
        );

        Mockito.when(queryAuthorRepository.findAllForManager(any())).thenReturn(page);

        // when
        PaginatedResponseDto<AuthorsResponseDto> response = service.findAllForManager(PageRequest.of(0, 5));

        // then
        assertThat(response.getTotalDataCount()).isEqualTo(10);
        assertThat(response.getDataList().get(0).getId()).isEqualTo(1L);
        assertThat(response.getDataList().get(9).getId()).isEqualTo(10L);
    }
}