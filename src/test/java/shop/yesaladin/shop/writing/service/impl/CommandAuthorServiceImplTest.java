package shop.yesaladin.shop.writing.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.dto.MemberDto;
import shop.yesaladin.shop.member.dummy.MemberDummy;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;
import shop.yesaladin.shop.writing.domain.model.Author;
import shop.yesaladin.shop.writing.domain.repository.CommandAuthorRepository;
import shop.yesaladin.shop.writing.domain.repository.QueryAuthorRepository;
import shop.yesaladin.shop.writing.dto.AuthorRequestDto;
import shop.yesaladin.shop.writing.dto.AuthorResponseDto;
import shop.yesaladin.shop.writing.dummy.DummyAuthor;
import shop.yesaladin.shop.writing.service.inter.CommandAuthorService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommandAuthorServiceImplTest {

    private final String NAME = "저자1";

    private CommandAuthorService service;

    private CommandAuthorRepository commandAuthorRepository;
    private QueryAuthorRepository queryAuthorRepository;
    private QueryMemberService queryMemberService;

    @BeforeEach
    void setUp() {
        commandAuthorRepository = mock(CommandAuthorRepository.class);
        queryAuthorRepository = mock(QueryAuthorRepository.class);
        queryMemberService = mock(QueryMemberService.class);

        service = new CommandAuthorServiceImpl(
                commandAuthorRepository,
                queryAuthorRepository,
                queryMemberService
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

    @Test
    @DisplayName("저자 생성 후 등록 성공")
    void create_success() {
        // given
        AuthorRequestDto createDto = new AuthorRequestDto(NAME, "loginID");
        Author author = DummyAuthor.dummy(NAME, null);

        Member member = DummyMember.member();
        MemberDto dto = MemberDto.fromEntity(member);

        Mockito.when(queryMemberService.existsLoginId(anyString())).thenReturn(true);
        Mockito.when(queryMemberService.findMemberByLoginId(anyString())).thenReturn(dto);
        Mockito.when(commandAuthorRepository.save(any())).thenReturn(author);

        // when
        AuthorResponseDto response = service.create(createDto);

        // then
        assertThat(response.getName()).isEqualTo(NAME);
        assertThat(response.getMember()).isNull();

        verify(queryMemberService, times(1)).existsLoginId(createDto.getLoginId());
        verify(queryMemberService, times(1)).findMemberByLoginId(createDto.getLoginId());
        verify(commandAuthorRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("저자 생성 후 등록 실패_존재하지 않는 멤버 로그인 아이디를 입력한 경우 예외 발생")
    void create_notExistsLoginId_throwMemberNotFoundException() {
        // given
        AuthorRequestDto createDto = new AuthorRequestDto(NAME, "loginID");

        // when then
        assertThatThrownBy(() -> service.create(createDto)).isInstanceOf(MemberNotFoundException.class);

        verify(queryMemberService, times(1)).existsLoginId(createDto.getLoginId());
    }

    @Test
    @DisplayName("저자 수정 성공")
    void modify_success() {
        // given
        Long id = 1L;
        String modifiedName = "저자2";
        String modifiedLoginId = "loginID";

        AuthorRequestDto modifyDto = new AuthorRequestDto(modifiedName, modifiedLoginId);
        Author author = DummyAuthor.dummy(NAME, null);

        Member member = MemberDummy.dummyWithLoginId(modifiedLoginId);
        MemberDto dto = MemberDto.fromEntity(member);
        Author modifiedAuthor = DummyAuthor.dummy(id, modifiedName, member);

        Mockito.when(queryAuthorRepository.findById(id)).thenReturn(Optional.ofNullable(author));
        Mockito.when(queryMemberService.existsLoginId(anyString())).thenReturn(true);
        Mockito.when(queryMemberService.findMemberByLoginId(anyString())).thenReturn(dto);
        Mockito.when(commandAuthorRepository.save(any())).thenReturn(modifiedAuthor);

        // when
        AuthorResponseDto response = service.modify(id, modifyDto);

        // then
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getName()).isEqualTo(modifiedName);
        assertThat(response.getMember()).isEqualTo(member);

        verify(queryAuthorRepository, times(1)).findById(id);
        verify(queryMemberService, times(1)).findMemberByLoginId(modifiedLoginId);
        verify(commandAuthorRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("저자 수정 실패_존재하지 않는 멤버 로그인 아이디를 입력한 경우 예외 발생")
    void modify_notExistsLoginId_throwMemberNotFoundException() {
        // given
        Long id = 1L;
        AuthorRequestDto modifyDto = new AuthorRequestDto(NAME, "loginID");
        Author author = DummyAuthor.dummy(NAME, null);

        Mockito.when(queryAuthorRepository.findById(id)).thenReturn(Optional.ofNullable(author));

        // when then
        assertThatThrownBy(() -> service.modify(id, modifyDto)).isInstanceOf(MemberNotFoundException.class);

        verify(queryMemberService, times(1)).existsLoginId(modifyDto.getLoginId());
    }
}