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
import org.springframework.context.ApplicationEventPublisher;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberGrade;
import shop.yesaladin.shop.member.domain.model.Role;
import shop.yesaladin.shop.member.domain.repository.CommandMemberRepository;
import shop.yesaladin.shop.member.domain.repository.CommandMemberRoleRepository;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;
import shop.yesaladin.shop.member.domain.repository.QueryRoleRepository;
import shop.yesaladin.shop.member.dto.MemberBlockRequestDto;
import shop.yesaladin.shop.member.dto.MemberBlockResponseDto;
import shop.yesaladin.shop.member.dto.MemberCreateRequestDto;
import shop.yesaladin.shop.member.dto.MemberCreateResponseDto;
import shop.yesaladin.shop.member.dto.MemberEmailUpdateRequestDto;
import shop.yesaladin.shop.member.dto.MemberNameUpdateRequestDto;
import shop.yesaladin.shop.member.dto.MemberNicknameUpdateRequestDto;
import shop.yesaladin.shop.member.dto.MemberPasswordUpdateRequestDto;
import shop.yesaladin.shop.member.dto.MemberPhoneUpdateRequestDto;
import shop.yesaladin.shop.member.dto.MemberUnblockResponseDto;
import shop.yesaladin.shop.member.dto.MemberUpdateResponseDto;
import shop.yesaladin.shop.member.dto.MemberWithdrawResponseDto;
import shop.yesaladin.shop.member.dto.OauthMemberCreateRequestDto;
import shop.yesaladin.shop.member.dummy.MemberDummy;
import shop.yesaladin.shop.member.dummy.MemberRoleDummy;
import shop.yesaladin.shop.member.dummy.RoleDummy;

class CommandMemberServiceImplTest {

    private CommandMemberServiceImpl service;
    private CommandMemberRepository commandMemberRepository;
    private QueryMemberRepository queryMemberRepository;
    private QueryRoleRepository queryRoleRepository;
    private CommandMemberRoleRepository commandMemberRoleRepository;
    private ApplicationEventPublisher applicationEventPublisher;

    @BeforeEach
    void setUp() {
        commandMemberRepository = Mockito.mock(CommandMemberRepository.class);
        queryMemberRepository = Mockito.mock(QueryMemberRepository.class);
        queryRoleRepository = Mockito.mock(QueryRoleRepository.class);
        commandMemberRoleRepository = Mockito.mock(CommandMemberRoleRepository.class);
        applicationEventPublisher = Mockito.mock(ApplicationEventPublisher.class);

        service = new CommandMemberServiceImpl(
                commandMemberRepository,
                queryMemberRepository,
                queryRoleRepository,
                commandMemberRoleRepository,
                applicationEventPublisher
        );
    }

    @Test
    @DisplayName("회원의 권한이 존재하지 않는 경우 회원가입에 실패한다.")
    void create_fail_whenMemberRoleNotExists() throws Exception {
        //given
        int roleId = 1;

        MemberCreateRequestDto createDto = Mockito.mock(MemberCreateRequestDto.class);

        Mockito.when(queryRoleRepository.findById(roleId)).thenReturn(Optional.empty());

        //when then
        assertThatThrownBy(() -> service.create(createDto))
                .isInstanceOf(ClientException.class)
                .hasMessageContaining("Member Role not found : ");

        verify(commandMemberRoleRepository, never()).save(any());
    }

    @Test
    @DisplayName("입력 받은 loginId이 기존에 있는 경우 예외가 발생한다.")
    void create_fail_whenMemberProfileDuplicated() {
        //given
        String loginId = "loginId";
        String nickname = "nickname";
        String email = "test@test.com";
        String phone = "01011112222";
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
        Mockito.when(queryMemberRepository.existsMemberByPhone(phone))
                .thenReturn(false);

        //when then
        assertThatThrownBy(() -> service.create(createDto))
                .isInstanceOf(ClientException.class)
                .hasMessageContaining("Member Login Id already exist : ");

        verify(commandMemberRoleRepository, never()).save(any());
    }

    @Test
    @DisplayName("입력 받은 nickname이 기존에 있는 경우 예외가 발생한다.")
    void create_fail_whenNicknameDuplicated() {
        //given
        String loginId = "loginId";
        String nickname = "nickname";
        String email = "test@test.com";
        String phone = "01011112222";
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
        Mockito.when(queryMemberRepository.existsMemberByPhone(phone))
                .thenReturn(false);

        //when then
        assertThatThrownBy(() -> service.create(createDto))
                .isInstanceOf(ClientException.class)
                .hasMessageContaining("Member Nickname already exist : ");

        verify(commandMemberRoleRepository, never()).save(any());
    }

    @Test
    @DisplayName("입력 받은 phone이 기존에 있는 경우 예외가 발생한다.")
    void create_fail_whenPhoneDuplicated() {
        //given
        String loginId = "loginId";
        String nickname = "nickname";
        String email = "test@test.com";
        String phone = "01011112222";
        int roleId = 1;
        Role role = RoleDummy.dummyWithId();

        MemberCreateRequestDto createDto = Mockito.mock(MemberCreateRequestDto.class);

        Mockito.when(createDto.getPhone()).thenReturn(phone);

        Mockito.when(queryRoleRepository.findById(roleId)).thenReturn(Optional.of(role));
        Mockito.when(queryMemberRepository.existsMemberByLoginId(loginId))
                .thenReturn(false);
        Mockito.when(queryMemberRepository.existsMemberByNickname(nickname))
                .thenReturn(false);
        Mockito.when(queryMemberRepository.existsMemberByEmail(email))
                .thenReturn(false);
        Mockito.when(queryMemberRepository.existsMemberByPhone(phone))
                .thenReturn(true);

        //when then
        assertThatThrownBy(() -> service.create(createDto))
                .isInstanceOf(ClientException.class)
                .hasMessageContaining("Member Phone already exist : ");

        verify(commandMemberRoleRepository, never()).save(any());
    }

    @Test
    @DisplayName("입력 받은 email이 기존에 있는 경우 예외가 발생한다.")
    void create_fail_whenEmailDuplicated() {
        //given
        String loginId = "loginId";
        String nickname = "nickname";
        String email = "test@test.com";
        String phone = "01011112222";
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
        Mockito.when(queryMemberRepository.existsMemberByPhone(phone))
                .thenReturn(false);

        //when then
        assertThatThrownBy(() -> service.create(createDto))
                .isInstanceOf(ClientException.class)
                .hasMessageContaining("Member Email already exist : ");

        verify(commandMemberRoleRepository, never()).save(any());
    }

    @Test
    @DisplayName("회원 등록 성공")
    void create() {
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
    @DisplayName("회원의 권한이 존재하지 않는 경우 OAuth 회원가입에 실패한다.")
    void createOauth_fail_whenMemberRoleNotExists() throws Exception {
        //given
        int roleId = 1;

        OauthMemberCreateRequestDto createDto = Mockito.mock(OauthMemberCreateRequestDto.class);

        Mockito.when(queryRoleRepository.findById(roleId)).thenReturn(Optional.empty());
        //when then
        assertThatThrownBy(() -> service.createOauth(createDto))
                .isInstanceOf(ClientException.class)
                .hasMessageContaining("Member Role not found : ");

        verify(commandMemberRoleRepository, never()).save(any());
    }

    @Test
    @DisplayName("OAuth2 회원 등록 성공")
    void createOauth() throws Exception {
        //given
        String loginId = "loginId";
        String nickname = "nickname";
        String email = "test@test.com";

        int roleId = 1;

        OauthMemberCreateRequestDto createDto = Mockito.mock(OauthMemberCreateRequestDto.class);
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
        MemberCreateResponseDto actualMember = service.createOauth(createDto);

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
    @DisplayName("회원 닉네임 수정 실패 - 존재하지않는 회원")
    void updateNickname_fail_NotFoundMember() {
        //given
        String loginId = "loginId";
        String nickname = "nickname";

        MemberNicknameUpdateRequestDto request = ReflectionUtils.newInstance(
                MemberNicknameUpdateRequestDto.class,
                nickname
        );
        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenThrow(new ClientException(
                        ErrorCode.MEMBER_NOT_FOUND,
                        "Member loginId " + loginId
                ));

        //when, then
        assertThatThrownBy(() -> service.updateNickname(loginId, request)).isInstanceOf(
                ClientException.class);

        verify(queryMemberRepository, times(1)).findMemberByLoginId(loginId);
        verify(queryMemberRepository, never()).findMemberByNickname(nickname);
    }

    @Test
    @DisplayName("회원 닉네임 수정 실패 - 중복된 닉네임")
    void updateNickname_fail_AlreadyExistNickname() {
        //given
        String loginId = "loginId";
        String nickname = "nickname";

        Member member = MemberDummy.dummyWithLoginIdAndId(loginId);
        MemberNicknameUpdateRequestDto request = ReflectionUtils.newInstance(
                MemberNicknameUpdateRequestDto.class,
                nickname
        );
        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenReturn(Optional.of(member));
        Mockito.when(queryMemberRepository.findMemberByNickname(nickname))
                .thenReturn(Optional.of(member));

        //when, then
        assertThatThrownBy(() -> service.updateNickname(loginId, request)).isInstanceOf(
                ClientException.class);

        verify(queryMemberRepository, times(1)).findMemberByLoginId(loginId);
        verify(queryMemberRepository, times(1)).findMemberByNickname(nickname);
    }

    @Test
    @DisplayName("회원 닉네임 수정 성공")
    void updateNickname() {
        //given
        String loginId = "user@1";
        String nickname = "nickname";

        Member member = MemberDummy.dummyWithLoginIdAndId(loginId);
        MemberNicknameUpdateRequestDto request = ReflectionUtils.newInstance(
                MemberNicknameUpdateRequestDto.class,
                nickname
        );

        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenReturn(Optional.of(member));
        Mockito.when(queryMemberRepository.findMemberByNickname(nickname))
                .thenReturn(Optional.empty());

        //when
        MemberUpdateResponseDto actualMember = service.updateNickname(loginId, request);

        //then
        assertThat(actualMember.getLoginId()).isEqualTo(loginId);
        assertThat(actualMember.getNickname()).isEqualTo(nickname);

        verify(queryMemberRepository, times(1)).findMemberByLoginId(loginId);
        verify(queryMemberRepository, times(1)).findMemberByNickname(nickname);
    }

    @Test
    @DisplayName("회원 이름 수정 실패 - 존재하지않는 회원")
    void updateName_fail_NotFoundMember() {
        //given
        String loginId = "loginId";
        String name = "name";

        MemberNameUpdateRequestDto request = ReflectionUtils.newInstance(
                MemberNameUpdateRequestDto.class,
                name
        );
        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenThrow(new ClientException(
                        ErrorCode.MEMBER_NOT_FOUND,
                        "Member loginId " + loginId
                ));

        //when, then
        assertThatThrownBy(() -> service.updateName(loginId, request)).isInstanceOf(
                ClientException.class);

        verify(queryMemberRepository, times(1)).findMemberByLoginId(loginId);
    }

    @Test
    @DisplayName("회원 이름 수정 성공")
    void updateName() {
        //given
        String loginId = "user@1";
        String name = "name";

        Member member = MemberDummy.dummyWithLoginIdAndId(loginId);
        MemberNameUpdateRequestDto request = ReflectionUtils.newInstance(
                MemberNameUpdateRequestDto.class,
                name
        );

        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenReturn(Optional.of(member));

        //when
        MemberUpdateResponseDto actualMember = service.updateName(loginId, request);

        //then
        assertThat(actualMember.getLoginId()).isEqualTo(loginId);
        assertThat(actualMember.getName()).isEqualTo(name);

        verify(queryMemberRepository, times(1)).findMemberByLoginId(loginId);
    }

    @Test
    @DisplayName("회원 이메일 수정 실패 - 존재하지않는 회원")
    void updateEmail_fail_NotFoundMember() {
        //given
        String loginId = "loginId";
        String email = "email@test.com";

        MemberEmailUpdateRequestDto request = ReflectionUtils.newInstance(
                MemberEmailUpdateRequestDto.class,
                email
        );
        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenThrow(new ClientException(
                        ErrorCode.MEMBER_NOT_FOUND,
                        "Member loginId " + loginId
                ));

        //when, then
        assertThatThrownBy(() -> service.updateEmail(loginId, request)).isInstanceOf(
                ClientException.class);

        verify(queryMemberRepository, times(1)).findMemberByLoginId(loginId);
        verify(queryMemberRepository, never()).findMemberByEmail(email);
    }

    @Test
    @DisplayName("회원 이메일 수정 실패 - 중복된 이메일")
    void updateEmail_fail_AlreadyExistNickname() {
        //given
        String loginId = "loginId";
        String email = "email@test.com";

        Member member = MemberDummy.dummyWithLoginIdAndId(loginId);
        MemberEmailUpdateRequestDto request = ReflectionUtils.newInstance(
                MemberEmailUpdateRequestDto.class,
                email
        );
        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenReturn(Optional.of(member));
        Mockito.when(queryMemberRepository.findMemberByEmail(email))
                .thenReturn(Optional.of(member));

        //when, then
        assertThatThrownBy(() -> service.updateEmail(loginId, request)).isInstanceOf(
                ClientException.class);

        verify(queryMemberRepository, times(1)).findMemberByLoginId(loginId);
        verify(queryMemberRepository, times(1)).findMemberByEmail(email);
    }

    @Test
    @DisplayName("회원 이메일 수정 성공")
    void updateEmail() {
        //given
        String loginId = "user@1";
        String email = "email@test.com";

        Member member = MemberDummy.dummyWithLoginIdAndId(loginId);
        MemberEmailUpdateRequestDto request = ReflectionUtils.newInstance(
                MemberEmailUpdateRequestDto.class,
                email
        );

        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenReturn(Optional.of(member));
        Mockito.when(queryMemberRepository.findMemberByEmail(email))
                .thenReturn(Optional.empty());

        //when
        MemberUpdateResponseDto actualMember = service.updateEmail(loginId, request);

        //then
        assertThat(actualMember.getLoginId()).isEqualTo(loginId);
        assertThat(actualMember.getEmail()).isEqualTo(email);

        verify(queryMemberRepository, times(1)).findMemberByLoginId(loginId);
        verify(queryMemberRepository, times(1)).findMemberByEmail(email);
    }

    @Test
    @DisplayName("회원 전화번호 수정 실패 - 존재하지않는 회원")
    void updatePhone_fail_NotFoundMember() {
        //given
        String loginId = "loginId";
        String phone = "01011112222";

        MemberPhoneUpdateRequestDto request = ReflectionUtils.newInstance(
                MemberPhoneUpdateRequestDto.class,
                phone
        );
        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenThrow(new ClientException(
                        ErrorCode.MEMBER_NOT_FOUND,
                        "Member loginId " + loginId
                ));

        //when, then
        assertThatThrownBy(() -> service.updatePhone(loginId, request)).isInstanceOf(
                ClientException.class);

        verify(queryMemberRepository, times(1)).findMemberByLoginId(loginId);
        verify(queryMemberRepository, never()).findMemberByPhone(phone);
    }

    @Test
    @DisplayName("회원 전화번호 수정 실패 - 중복된 전화번호")
    void updatePhone_fail_AlreadyExistNickname() {
        //given
        String loginId = "loginId";
        String phone = "01011112222";

        Member member = MemberDummy.dummyWithLoginIdAndId(loginId);
        MemberPhoneUpdateRequestDto request = ReflectionUtils.newInstance(
                MemberPhoneUpdateRequestDto.class,
                phone
        );
        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenReturn(Optional.of(member));
        Mockito.when(queryMemberRepository.findMemberByPhone(phone))
                .thenReturn(Optional.of(member));

        //when, then
        assertThatThrownBy(() -> service.updatePhone(loginId, request)).isInstanceOf(
                ClientException.class);

        verify(queryMemberRepository, times(1)).findMemberByLoginId(loginId);
        verify(queryMemberRepository, times(1)).findMemberByPhone(phone);
    }

    @Test
    @DisplayName("회원 전화번호 수정 성공")
    void updatePhone() {
        //given
        String loginId = "user@1";
        String phone = "01011112222";

        Member member = MemberDummy.dummyWithLoginIdAndId(loginId);
        MemberPhoneUpdateRequestDto request = ReflectionUtils.newInstance(
                MemberPhoneUpdateRequestDto.class,
                phone
        );

        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenReturn(Optional.of(member));
        Mockito.when(queryMemberRepository.findMemberByPhone(phone))
                .thenReturn(Optional.empty());

        //when
        MemberUpdateResponseDto actualMember = service.updatePhone(loginId, request);

        //then
        assertThat(actualMember.getLoginId()).isEqualTo(loginId);
        assertThat(actualMember.getPhone()).isEqualTo(phone);

        verify(queryMemberRepository, times(1)).findMemberByLoginId(loginId);
        verify(queryMemberRepository, times(1)).findMemberByPhone(phone);
    }

    @Test
    @DisplayName("회원 차단 실패-존재하지않는 회원")
    void block_fail_NotFoundMember() {
        //given
        String loginId = "loginId";

        String blockedReason = "You are bad guy";
        MemberBlockRequestDto request = ReflectionUtils.newInstance(
                MemberBlockRequestDto.class,
                blockedReason
        );
        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenThrow(new ClientException(
                        ErrorCode.MEMBER_NOT_FOUND,
                        "Member loginId " + loginId
                ));

        //when, then
        assertThatThrownBy(() -> service.block(loginId, request)).isInstanceOf(
                ClientException.class);

        verify(queryMemberRepository, times(1)).findMemberByLoginId(loginId);
    }

    @Test
    @DisplayName("회원 차단 실패-이미 차단되어있는 경우")
    void block_fail_AlreadyBlockedMember() {
        //given
        String loginId = "loginId";

        String blockedReason = "You are bad guy";
        MemberBlockRequestDto request = ReflectionUtils.newInstance(
                MemberBlockRequestDto.class,
                blockedReason
        );

        Member member = Member.builder()
                .loginId(loginId)
                .isBlocked(true).build();

        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenReturn(Optional.of(member));

        //when, then
        assertThatThrownBy(() -> service.block(
                loginId,
                request
        )).isInstanceOf(ClientException.class);

        verify(queryMemberRepository, times(1)).findMemberByLoginId(loginId);
    }

    @Test
    @DisplayName("회원 차단 성공")
    void block() {
        //given
        String loginId = "loginId";

        String blockedReason = "You are bad guy";
        MemberBlockRequestDto request = ReflectionUtils.newInstance(
                MemberBlockRequestDto.class,
                blockedReason
        );

        Member member = Member.builder()
                .loginId(loginId)
                .isBlocked(false).build();

        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenReturn(Optional.of(member));

        //when
        MemberBlockResponseDto actualMember = service.block(loginId, request);

        //then
        assertThat(actualMember.getLoginId()).isEqualTo(loginId);
        assertThat(actualMember.getIsBlocked()).isTrue();

        verify(queryMemberRepository, times(1)).findMemberByLoginId(loginId);
    }

    @Test
    @DisplayName("회원 차단 해지 실패-존재하지않는 회원")
    void unblock_fail_NotFoundMember() {
        //given
        String loginId = "loginId";

        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenThrow(new ClientException(
                        ErrorCode.MEMBER_NOT_FOUND,
                        "Member loginId " + loginId
                ));

        //when, then
        assertThatThrownBy(() -> service.unblock(loginId)).isInstanceOf(ClientException.class);

        verify(queryMemberRepository, times(1)).findMemberByLoginId(loginId);
    }

    @Test
    @DisplayName("회원 차단 해지 실패-이미 차단되어있는 경우")
    void unblock_fail_AlreadyBlockedMember() {
        //given
        String loginId = "loginId";

        Member member = Member.builder()
                .loginId(loginId)
                .isBlocked(false).build();

        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenReturn(Optional.of(member));

        //when, then
        assertThatThrownBy(() -> service.unblock(loginId)).isInstanceOf(ClientException.class);

        verify(queryMemberRepository, times(1)).findMemberByLoginId(loginId);
    }

    @Test
    @DisplayName("회원 차단 해지 성공")
    void unblock() {
        //given
        String loginId = "loginId";

        Member member = Member.builder()
                .loginId(loginId)
                .isBlocked(true).build();

        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenReturn(Optional.of(member));

        //when
        MemberUnblockResponseDto actualMember = service.unblock(loginId);

        //then
        assertThat(actualMember.getLoginId()).isEqualTo(loginId);
        assertThat(actualMember.getIsBlocked()).isFalse();

        verify(queryMemberRepository, times(1)).findMemberByLoginId(loginId);
    }

    @Test
    @DisplayName("존재하지 않는 회원을 삭제 시 예외가 발생한다.")
    void withdrawMember_fail_whenMemberNotExist() {
        //given
        String loginId = "loginId";

        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> service.withDraw(loginId))
                .isInstanceOf(ClientException.class);

        //then
        verify(queryMemberRepository, times(1)).findMemberByLoginId(loginId);
    }

    @Test
    @DisplayName("회원 삭제(soft delete) 성공")
    void withdrawMember() {
        //given
        long id = 1L;
        String name = "testName";
        String loginId = "loginId";

        String deletedField = "" + id;

        Member member = Member.builder()
                .id(id)
                .name(name)
                .loginId(loginId)
                .build();

        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenReturn(Optional.of(member));

        //when
        MemberWithdrawResponseDto response = service.withDraw(loginId);

        //then
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getName()).isEqualTo(deletedField);
        assertThat(response.isWithdrawal()).isTrue();

        verify(queryMemberRepository, times(1)).findMemberByLoginId(loginId);
    }

    @Test
    @DisplayName("회원 이름 수정 실패 - 존재하지않는 회원")
    void updatePassword_fail_NotFoundMember() {
        //given
        String loginId = "loginId";
        String password = "password";

        MemberPasswordUpdateRequestDto request = ReflectionUtils.newInstance(
                MemberPasswordUpdateRequestDto.class,
                password
        );
        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenThrow(new ClientException(
                        ErrorCode.MEMBER_NOT_FOUND,
                        "Member loginId " + loginId
                ));

        //when, then
        assertThatThrownBy(() -> service.updatePassword(loginId, request)).isInstanceOf(
                ClientException.class);

        verify(queryMemberRepository, times(1)).findMemberByLoginId(loginId);
    }

    @Test
    @DisplayName("회원 이름 수정 성공")
    void updatePassword() {
        //given
        String loginId = "user@1";
        String password = "password";

        Member member = MemberDummy.dummyWithLoginIdAndId(loginId);
        MemberPasswordUpdateRequestDto request = ReflectionUtils.newInstance(
                MemberPasswordUpdateRequestDto.class,
                password
        );

        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenReturn(Optional.of(member));

        //when
        MemberUpdateResponseDto actualMember = service.updatePassword(loginId, request);

        //then
        assertThat(actualMember.getLoginId()).isEqualTo(loginId);

        verify(queryMemberRepository, times(1)).findMemberByLoginId(loginId);
    }
}