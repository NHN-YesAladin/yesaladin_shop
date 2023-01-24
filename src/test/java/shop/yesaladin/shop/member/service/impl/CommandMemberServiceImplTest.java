package shop.yesaladin.shop.member.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mockito;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberGrade;
import shop.yesaladin.shop.member.domain.model.Role;
import shop.yesaladin.shop.member.domain.repository.CommandMemberRepository;
import shop.yesaladin.shop.member.domain.repository.CommandMemberRoleRepository;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;
import shop.yesaladin.shop.member.domain.repository.QueryRoleRepository;
import shop.yesaladin.shop.member.dto.MemberBlockResponseDto;
import shop.yesaladin.shop.member.dto.MemberCreateRequestDto;
import shop.yesaladin.shop.member.dto.MemberCreateResponseDto;
import shop.yesaladin.shop.member.dto.MemberUpdateRequestDto;
import shop.yesaladin.shop.member.dto.MemberUpdateResponseDto;
import shop.yesaladin.shop.member.dto.MemberWithdrawResponseDto;
import shop.yesaladin.shop.member.dummy.MemberRoleDummy;
import shop.yesaladin.shop.member.dummy.RoleDummy;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.member.exception.MemberProfileAlreadyExistException;

class CommandMemberServiceImplTest {

    private CommandMemberServiceImpl service;
    private CommandMemberRepository commandMemberRepository;
    private QueryMemberRepository queryMemberRepository;
    private QueryRoleRepository queryRoleRepository;
    private CommandMemberRoleRepository commandMemberRoleRepository;

    @BeforeEach
    void setUp() {
        commandMemberRepository = Mockito.mock(CommandMemberRepository.class);
        queryMemberRepository = Mockito.mock(QueryMemberRepository.class);
        queryRoleRepository = Mockito.mock(QueryRoleRepository.class);
        commandMemberRoleRepository = Mockito.mock(CommandMemberRoleRepository.class);
        service = new CommandMemberServiceImpl(
                commandMemberRepository,
                queryMemberRepository,
                queryRoleRepository,
                commandMemberRoleRepository
        );
    }

    @Test
    @DisplayName("입력 받은 loginId이 기존에 있는 경우 예외가 발생한다.")
    void create_fail_whenMemberProfileDuplicated() throws Exception {
        //given
        String loginId = "loginId";
        String nickname = "nickname";
        String email = "test@test.com";
        int roleId = 1;
        Role role = RoleDummy.dummyWithId();

        MemberCreateRequestDto createDto = Mockito.mock(MemberCreateRequestDto.class);

        Mockito.when(createDto.getLoginId()).thenReturn(loginId);

        Mockito.when(queryRoleRepository.findById(roleId)).thenReturn(Optional.of(role));
        Mockito.when(queryMemberRepository.existsMemberByLoginId(loginId))
                .thenReturn(true);
        Mockito.when(queryMemberRepository.existsMemberByNickname(nickname))
                .thenReturn(false);
        Mockito.when(queryMemberRepository.existsMemberByEmail(email))
                .thenReturn(false);

        //when then
        assertThatThrownBy(() -> service.create(createDto))
                .isInstanceOf(MemberProfileAlreadyExistException.class);

        verify(commandMemberRoleRepository, never()).save(any());
    }

    @Test
    @DisplayName("입력 받은 nickname이 기존에 있는 경우 예외가 발생한다.")
    void create_fail_whenNicknameDuplicated() throws Exception {
        //given
        String loginId = "loginId";
        String nickname = "nickname";
        String email = "test@test.com";
        int roleId = 1;
        Role role = RoleDummy.dummyWithId();

        MemberCreateRequestDto createDto = Mockito.mock(MemberCreateRequestDto.class);

        Mockito.when(createDto.getNickname()).thenReturn(nickname);

        Mockito.when(queryRoleRepository.findById(roleId)).thenReturn(Optional.of(role));
        Mockito.when(queryMemberRepository.existsMemberByLoginId(loginId))
                .thenReturn(false);
        Mockito.when(queryMemberRepository.existsMemberByNickname(nickname))
                .thenReturn(true);
        Mockito.when(queryMemberRepository.existsMemberByEmail(email))
                .thenReturn(false);

        //when then
        assertThatThrownBy(() -> service.create(createDto))
                .isInstanceOf(MemberProfileAlreadyExistException.class);

        verify(commandMemberRoleRepository, never()).save(any());
    }

    @Test
    @DisplayName("입력 받은 email이 기존에 있는 경우 예외가 발생한다.")
    void create_fail_whenEmailDuplicated() throws Exception {
        //given
        String loginId = "loginId";
        String nickname = "nickname";
        String email = "test@test.com";
        int roleId = 1;
        Role role = RoleDummy.dummyWithId();

        MemberCreateRequestDto createDto = Mockito.mock(MemberCreateRequestDto.class);

        Mockito.when(createDto.getEmail()).thenReturn(email);

        Mockito.when(queryRoleRepository.findById(roleId)).thenReturn(Optional.of(role));
        Mockito.when(queryMemberRepository.existsMemberByLoginId(loginId))
                .thenReturn(false);
        Mockito.when(queryMemberRepository.existsMemberByNickname(nickname))
                .thenReturn(false);
        Mockito.when(queryMemberRepository.existsMemberByEmail(email))
                .thenReturn(true);

        //when then
        assertThatThrownBy(() -> service.create(createDto))
                .isInstanceOf(MemberProfileAlreadyExistException.class);

        verify(commandMemberRoleRepository, never()).save(any());
    }

    @Test
    @DisplayName("회원 등록 성공")
    void create() throws Exception {
        //given
        String loginId = "loginId";
        String nickname = "nickname";
        String email = "test@test.com";
        int roleId = 1;

        MemberCreateRequestDto createDto = Mockito.mock(MemberCreateRequestDto.class);
        Member member = Member.builder()
                .loginId(loginId)
                .nickname(nickname)
                .email(email)
                .memberGrade(MemberGrade.WHITE)
                .build();

        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenReturn(Optional.empty());
        Mockito.when(queryMemberRepository.findMemberByNickname(nickname))
                .thenReturn(Optional.empty());
        Mockito.when(queryMemberRepository.findMemberByEmail(email))
                .thenReturn(Optional.empty());
        // 1번 Role 빼오기
        Role role = RoleDummy.dummyWithId();

        Mockito.when(queryRoleRepository.findById(roleId)).thenReturn(Optional.of(role));
        // memberRole 등록
        Mockito.when(commandMemberRoleRepository.save(any()))
                .thenReturn(MemberRoleDummy.dummy(member, role));

        Mockito.when(createDto.toEntity()).thenReturn(member);

        Mockito.when(commandMemberRepository.save(member)).thenReturn(member);

        //when
        MemberCreateResponseDto actualMember = service.create(createDto);

        //then
        assertThat(actualMember.getLoginId()).isEqualTo(loginId);
        assertThat(actualMember.getNickname()).isEqualTo(nickname);
        assertThat(actualMember.getMemberGrade()).isEqualTo(MemberGrade.WHITE.getName());
        assertThat(actualMember.getRole()).isEqualTo("ROLE_MEMBER");

        verify(queryRoleRepository, times(1)).findById(roleId);
        verify(commandMemberRoleRepository, times(1)).save(any());
        verify(commandMemberRepository, times(1)).save(member);
    }

    @Test
    @DisplayName("회원 정보 수정 성공")
    void update() {
        //given
        long id = 1;
        String name = "name";
        String nickname = "nickname";
        MemberUpdateRequestDto request = ReflectionUtils.newInstance(
                MemberUpdateRequestDto.class,
                nickname
        );

        Member member = Member.builder()
                .id(id)
                .name(name)
                .nickname(nickname)
                .build();
        Mockito.when(queryMemberRepository.findById(id)).thenReturn(Optional.of(member));
        Mockito.when(queryMemberRepository.findMemberByNickname(nickname))
                .thenReturn(Optional.empty());

        //when
        MemberUpdateResponseDto actualMember = service.update(id, request);

        //then
        assertThat(actualMember.getId()).isEqualTo(id);
        assertThat(actualMember.getNickname()).isEqualTo(nickname);
        assertThat(actualMember.getName()).isEqualTo(name);

        verify(queryMemberRepository, times(1)).findById(id);
        verify(queryMemberRepository, times(1)).findMemberByNickname(nickname);
    }

    @Test
    void block() {
        //given
        long id = 1;
        boolean blocked = false;
        String name = "name";
        String loginId = "loginId";

        Member member = Member.builder()
                .id(id)
                .name(name)
                .loginId(loginId)
                .isBlocked(blocked)
                .build();
        Mockito.when(queryMemberRepository.findById(id)).thenReturn(Optional.of(member));

        //when
        MemberBlockResponseDto actualMember = service.block(id);

        //then
        assertThat(actualMember.getId()).isEqualTo(id);
        assertThat(actualMember.getName()).isEqualTo(name);
        assertThat(actualMember.getLoginId()).isEqualTo(loginId);
        assertThat(actualMember.isBlocked()).isTrue();

        verify(queryMemberRepository, times(1)).findById(id);
    }

    @Test
    void unblock() {
        //given
        long id = 1;
        boolean blocked = true;
        String name = "name";
        String loginId = "loginId";

        Member member = Member.builder()
                .id(id)
                .name(name)
                .loginId(loginId)
                .isBlocked(blocked)
                .build();
        Mockito.when(queryMemberRepository.findById(id)).thenReturn(Optional.of(member));

        //when
        MemberBlockResponseDto actualMember = service.unblock(id);

        //then
        assertThat(actualMember.getId()).isEqualTo(id);
        assertThat(actualMember.getName()).isEqualTo(name);
        assertThat(actualMember.getLoginId()).isEqualTo(loginId);
        assertThat(actualMember.isBlocked()).isFalse();

        verify(queryMemberRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("존재하지 않는 회원을 삭제 시 예외가 발생한다.")
    void withdrawMember_fail_whenMemberNotExist() throws Exception {
        //given
        String loginId = "loginId";

        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId)).thenReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> service.withDraw(loginId))
                .isInstanceOf(MemberNotFoundException.class);

        //then
        verify(queryMemberRepository, times(1)).findMemberByLoginId(loginId);
    }

    @Test
    @DisplayName("회원 삭제(soft delete) 성공")
    void withdrawMember() throws Exception {
        //given
        String name = "testName";
        String loginId = "loginId";

        String deletedField = "deleted-" + 1L;

        Member member = Member.builder()
                .id(1L)
                .name(name)
                .loginId(loginId)
                .build();

        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId)).thenReturn(Optional.of(member));

        //when
        MemberWithdrawResponseDto response = service.withDraw(loginId);

        //then
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo(deletedField);
        assertThat(response.isWithdrawal()).isTrue();

        verify(queryMemberRepository, times(1)).findMemberByLoginId(loginId);
    }
}