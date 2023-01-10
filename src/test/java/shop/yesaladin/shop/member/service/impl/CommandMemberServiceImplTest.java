package shop.yesaladin.shop.member.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
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
import shop.yesaladin.shop.member.dummy.MemberRoleDummy;
import shop.yesaladin.shop.member.dummy.RoleDummy;

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
    void create() throws Exception {
        //given
        String loginId = "loginId";
        String nickname = "nickname";
        int roleId = 1;

        MemberCreateRequestDto createDto = Mockito.mock(MemberCreateRequestDto.class);
        Member member = Member.builder()
                .loginId(loginId)
                .nickname(nickname)
                .memberGrade(MemberGrade.WHITE)
                .build();

        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenReturn(Optional.empty());
        Mockito.when(queryMemberRepository.findMemberByNickname(nickname))
                .thenReturn(Optional.empty());
        // 1번 Role 빼오기
        Role role = RoleDummy.dummyWithId();

        Mockito.when(queryRoleRepository.findById(roleId)).thenReturn(Optional.of(role));
        // memberRole 등록
        Mockito.when(commandMemberRoleRepository.save(any())).thenReturn(MemberRoleDummy.dummy(member, role));


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
}