package shop.yesaladin.shop.member.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberGrade;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRoleRepository;
import shop.yesaladin.shop.member.dto.MemberDto;
import shop.yesaladin.shop.member.dto.MemberGradeQueryResponseDto;
import shop.yesaladin.shop.member.dto.MemberLoginResponseDto;
import shop.yesaladin.shop.member.dto.MemberManagerResponseDto;
import shop.yesaladin.shop.member.dto.MemberQueryResponseDto;
import shop.yesaladin.shop.member.dto.MemberStatisticsResponseDto;
import shop.yesaladin.shop.member.dummy.MemberDummy;

class QueryMemberServiceImplTest {

    private QueryMemberServiceImpl service;
    private QueryMemberRepository queryMemberRepository;
    private QueryMemberRoleRepository queryMemberRoleRepository;

    private Member expectedMember;

    @BeforeEach
    void setUp() {
        queryMemberRepository = Mockito.mock(QueryMemberRepository.class);
        queryMemberRoleRepository = Mockito.mock(QueryMemberRoleRepository.class);
        service = new QueryMemberServiceImpl(
                queryMemberRepository,
                queryMemberRoleRepository
        );

        expectedMember = Mockito.mock(Member.class);
    }

    @Test
    void findMemberById_failed_whenMemberNotFound() throws Exception {
        //given
        long id = 1L;

        Mockito.when(queryMemberRepository.findById(id))
                .thenReturn(Optional.empty());

        //when then
        assertThatThrownBy(() -> service.findMemberById(id))
                .isInstanceOf(ClientException.class);
    }

    @Test
    void findMemberById() throws Exception {
        //given
        long id = 1L;

        Mockito.when(queryMemberRepository.findById(id))
                .thenReturn(Optional.of(expectedMember));
        Mockito.when(expectedMember.getId()).thenReturn(id);

        //when
        MemberDto actualMember = service.findMemberById(id);

        //then
        assertThat(actualMember.getId()).isEqualTo(expectedMember.getId());
    }

    @Test
    void findMemberByNickname_failed_whenMemberNotFound() throws Exception {
        //given
        String nickname = "Ramos";

        Mockito.when(queryMemberRepository.findMemberByNickname(nickname))
                .thenReturn(Optional.empty());

        //when then
        assertThatThrownBy(() -> service.findMemberByNickname(nickname))
                .isInstanceOf(ClientException.class);
    }

    @Test
    void findMemberByNickname() throws Exception {
        //given
        String nickname = "Ramos";

        Mockito.when(queryMemberRepository.findMemberByNickname(nickname))
                .thenReturn(Optional.of(expectedMember));
        Mockito.when(expectedMember.getNickname()).thenReturn(nickname);

        //when
        MemberDto actualMember = service.findMemberByNickname(nickname);

        //then
        assertThat(actualMember.getNickname()).isEqualTo(expectedMember.getNickname());
    }

    @Test
    void findMemberByLoginId_failed_whenMemberNotFound() throws Exception {
        //given
        String loginId = "test1234";

        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenReturn(Optional.empty());

        //when then
        assertThatThrownBy(() -> service.findMemberByLoginId(loginId))
                .isInstanceOf(ClientException.class);
    }

    @Test
    void findMemberByLoginId() throws Exception {
        //given
        String loginId = "test1234";

        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenReturn(Optional.of(expectedMember));
        Mockito.when(expectedMember.getLoginId()).thenReturn(loginId);

        //when
        MemberDto actualMember = service.findMemberByLoginId(loginId);

        //then
        assertThat(actualMember.getLoginId()).isEqualTo(expectedMember.getLoginId());
    }

    @Test
    void findMemberLoginInfoByLoginId_failed_whenMemberNotFound() throws Exception {
        //given
        String loginId = "test1234";

        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> service.findMemberLoginInfoByLoginId(loginId))
                .isInstanceOf(ClientException.class);
    }

    @Test
    void findMemberLoginInfoByLoginId() throws Exception {
        //given
        String loginId = "test1234";
        Long memberId = 1L;

        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenReturn(Optional.of(expectedMember));
        Mockito.when(expectedMember.getLoginId()).thenReturn(loginId);
        Mockito.when(expectedMember.getId()).thenReturn(memberId);

        Mockito.when(queryMemberRoleRepository.findMemberRolesByMemberId(memberId))
                .thenReturn(List.of("ROLE_MEMBER"));

        //when
        MemberLoginResponseDto response = service.findMemberLoginInfoByLoginId(
                loginId);

        //then
        assertThat(response.getRoles()).hasSize(1);
        assertThat(response.getLoginId()).isEqualTo(loginId);
        assertThat(response.getId()).isEqualTo(memberId);
    }

    @Test
    void findMemberManages() {
        //given
        String loginId = "loginId";
        Member member = Member.builder().loginId(loginId).build();
        MemberManagerResponseDto dto = MemberManagerResponseDto.fromEntity(member);

        Mockito.when(queryMemberRepository.findMemberManagers(
                        PageRequest.of(0, 10)
                ))
                .thenReturn(new PageImpl<>(List.of(dto), PageRequest.of(0, 10), 1L));

        //when
        Page<MemberManagerResponseDto> result = service.findMemberManages(
                PageRequest.of(0, 10)
        );

        //then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getNumber()).isZero();
        assertThat(result.getContent().get(0).getLoginId()).isEqualTo(loginId);
        verify(queryMemberRepository, atLeastOnce()).findMemberManagers(PageRequest.of(0, 10));
    }

    @Test
    void findMemberManagesByLoginId() {
        //given
        String loginId = "loginId";
        Member member = Member.builder().loginId(loginId).build();
        MemberManagerResponseDto dto = MemberManagerResponseDto.fromEntity(member);

        Mockito.when(queryMemberRepository.findMemberManagersByLoginId(
                        loginId,
                        PageRequest.of(0, 10)
                ))
                .thenReturn(new PageImpl<>(List.of(dto), PageRequest.of(0, 10), 1L));

        //when
        Page<MemberManagerResponseDto> result = service.findMemberManagesByLoginId(
                loginId,
                PageRequest.of(0, 10)
        );

        //then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getNumber()).isZero();
        assertThat(result.getContent().get(0).getLoginId()).isEqualTo(loginId);
        verify(queryMemberRepository, atLeastOnce()).findMemberManagersByLoginId(loginId, PageRequest.of(0, 10));
    }


    @Test
    void findMemberManagesByNickName() {
        //given
        String nickname = "nickname";
        Member member = Member.builder().nickname(nickname).build();
        MemberManagerResponseDto dto = MemberManagerResponseDto.fromEntity(member);

        Mockito.when(queryMemberRepository.findMemberManagersByNickname(
                nickname,
                PageRequest.of(0, 1)
        )).thenReturn(new PageImpl<>(List.of(dto), PageRequest.of(0, 1), 1L));

        //when
        Page<MemberManagerResponseDto> result = service.findMemberManagesByNickName(
                nickname,
                PageRequest.of(0, 1)
        );

        //then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getNickname()).isEqualTo(nickname);
    }

    @Test
    void findMemberManagesByPhone() {
        //given
        String phone = "phone";
        Member member = Member.builder().phone(phone).build();
        MemberManagerResponseDto dto = MemberManagerResponseDto.fromEntity(member);

        Mockito.when(queryMemberRepository.findMemberManagersByPhone(
                phone,
                PageRequest.of(0, 1)
        )).thenReturn(new PageImpl<>(List.of(dto), PageRequest.of(0, 1), 1L));

        //when
        Page<MemberManagerResponseDto> result = service.findMemberManagesByPhone(
                phone,
                PageRequest.of(0, 1)
        );

        //then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getPhone()).isEqualTo(phone);
    }


    @Test
    void findMemberManagesByName() {
        //given
        String name = "name";
        Member member = Member.builder().name(name).build();
        MemberManagerResponseDto dto = MemberManagerResponseDto.fromEntity(member);

        Mockito.when(queryMemberRepository.findMemberManagersByName(
                name,
                PageRequest.of(0, 1)
        )).thenReturn(new PageImpl<>(List.of(dto), PageRequest.of(0, 1), 1L));

        //when
        Page<MemberManagerResponseDto> result = service.findMemberManagesByName(
                name,
                PageRequest.of(0, 1)
        );

        //then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo(name);
    }


    @Test
    void findMemberManagesBySignUpDate() {
        //given
        LocalDate signUpDate = LocalDate.now();
        Member member = Member.builder().signUpDate(signUpDate).build();
        MemberManagerResponseDto dto = MemberManagerResponseDto.fromEntity(member);

        Mockito.when(queryMemberRepository.findMemberManagersBySignUpDate(
                signUpDate,
                PageRequest.of(0, 1)
        )).thenReturn(new PageImpl<>(List.of(dto), PageRequest.of(0, 1), 1L));

        //when
        Page<MemberManagerResponseDto> result = service.findMemberManagesBySignUpDate(
                signUpDate,
                PageRequest.of(0, 1)
        );

        //then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getSignUpDate()).isEqualTo(signUpDate.toString());
    }

    @Test
    void existsMemberByLoginId_whenNotExists_return_false() throws Exception {
        //given
        String loginId = "test1234";

        //when
        boolean result = service.existsLoginId(loginId);

        //then
        assertThat(result).isFalse();
    }

    @Test
    void existsMemberByLoginId_whenExists_return_true() throws Exception {
        //given
        String loginId = "test1234";

        Mockito.when(queryMemberRepository.existsMemberByLoginId(loginId)).thenReturn(true);

        //when
        boolean result = service.existsLoginId(loginId);

        //then
        assertThat(result).isTrue();
    }

    @Test
    void existsMemberByNickname_whenNotExists_return_false() throws Exception {
        //given
        String nickname = "testNickname";

        //when
        boolean result = service.existsNickname(nickname);

        //then
        assertThat(result).isFalse();
    }

    @Test
    void existsMemberByNickname_whenExists_return_true() throws Exception {
        //given
        String nickname = "testNickname";

        Mockito.when(queryMemberRepository.existsMemberByNickname(nickname)).thenReturn(true);

        //when
        boolean result = service.existsNickname(nickname);

        //then
        assertThat(result).isTrue();
    }

    @Test
    void existsMemberByEmail_whenNotExists_return_false() throws Exception {
        //given
        String email = "test@test.com";

        //when
        boolean result = service.existsEmail(email);

        //then
        assertThat(result).isFalse();
    }

    @Test
    void existsMemberByEmail_whenExists_return_true() throws Exception {
        //given
        String email = "test@test.com";

        Mockito.when(queryMemberRepository.existsMemberByEmail(email)).thenReturn(true);

        //when
        boolean result = service.existsEmail(email);

        //then
        assertThat(result).isTrue();
    }

    @Test
    void existsMemberByPhone_whenNotExists_return_false() throws Exception {
        //given
        String phone = "01011112222";

        //when
        boolean result = service.existsPhone(phone);

        //then
        assertThat(result).isFalse();
    }

    @Test
    void existsMemberByPhone_whenExists_return_true() throws Exception {
        //given
        String phone = "01011112222";

        Mockito.when(queryMemberRepository.existsMemberByPhone(phone)).thenReturn(true);

        //when
        boolean result = service.existsPhone(phone);

        //then
        assertThat(result).isTrue();
    }

    @Test
    void getMemberGrade_fail_memberNotFound() {
        //given
        String loginId = "user@1";

        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenThrow(ClientException.class);

        //when, then
        assertThatThrownBy(() -> service.getMemberGradeByLoginId(loginId)).isInstanceOf(
                ClientException.class);
    }

    @Test
    void getMemberGrade_success() {
        //given
        String loginId = "user@1";
        Member member = MemberDummy.dummyWithLoginIdAndId(loginId);
        MemberGrade memberGrade = member.getMemberGrade();

        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenReturn(Optional.of(member));

        //when
        MemberGradeQueryResponseDto result = service.getMemberGradeByLoginId(loginId);

        //then
        assertThat(result.getGradeEn()).isEqualTo(memberGrade.name());
        assertThat(result.getGradeKo()).isEqualTo(memberGrade.getName());
    }

    @Test
    void getByLoginId_fail_memberNotFound() {
        //given
        String loginId = "user@1";

        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenThrow(ClientException.class);

        //when, then
        assertThatThrownBy(() -> service.getByLoginId(loginId)).isInstanceOf(
                ClientException.class);
    }

    @Test
    void getByLoginId_success() {
        //given
        String loginId = "user@1";
        Member member = MemberDummy.dummyWithLoginIdAndId(loginId);

        Mockito.when(queryMemberRepository.findMemberByLoginId(loginId))
                .thenReturn(Optional.of(member));

        //when
        MemberQueryResponseDto result = service.getByLoginId(loginId);

        //then
        assertThat(result.getId()).isEqualTo(member.getId());
        assertThat(result.getNickname()).isEqualTo(member.getNickname());
        assertThat(result.getName()).isEqualTo(member.getName());
        assertThat(result.getLoginId()).isEqualTo(member.getLoginId());
        assertThat(result.getPassword()).isEqualTo(member.getPassword());
        assertThat(result.getBirthYear()).isEqualTo(member.getBirthYear());
        assertThat(result.getBirthMonth()).isEqualTo(member.getBirthMonth());
        assertThat(result.getBirthDay()).isEqualTo(member.getBirthDay());
        assertThat(result.getEmail()).isEqualTo(member.getEmail());
        assertThat(result.getSignUpDate()).isEqualTo(member.getSignUpDate());
        assertThat(result.getGrade()).isEqualTo(member.getMemberGrade().getName());
        assertThat(result.getGender()).isEqualTo(
                member.getMemberGenderCode().getGender() == 1 ? "남" : "여");
    }

    @Test
    void getMemberStatistics() throws Exception {
        //given
        long totalMembers = 10L;
        long totalBlocked = 1L;
        long totalWithdraws = 3L;
        long totalWhite = 8L;
        long theOthers = 0L;
        long totalGold = 1L;
        long totalPlatinum = 1L;

        Mockito.when(queryMemberRepository.countTotalMembers()).thenReturn(totalMembers);
        Mockito.when(queryMemberRepository.countBlockedMembers()).thenReturn(totalBlocked);
        Mockito.when(queryMemberRepository.countWithdrawMembers()).thenReturn(totalWithdraws);
        Mockito.when(queryMemberRepository.countWhiteMembers()).thenReturn(totalWhite);
        Mockito.when(queryMemberRepository.countBronzeMembers()).thenReturn(theOthers);
        Mockito.when(queryMemberRepository.countSilverMembers()).thenReturn(theOthers);
        Mockito.when(queryMemberRepository.countGoldMembers()).thenReturn(totalGold);
        Mockito.when(queryMemberRepository.countPlatinumMembers()).thenReturn(totalPlatinum);

        //when
        MemberStatisticsResponseDto response = service.getMemberStatistics();

        //then
        assertThat(response.getTotalMembers()).isEqualTo(totalMembers);
        assertThat(response.getTotalBlockedMembers()).isEqualTo(totalBlocked);
        assertThat(response.getTotalWithdrawMembers()).isEqualTo(totalWithdraws);
        assertThat(response.getTotalWhiteGrades()).isEqualTo(totalWhite);
        assertThat(response.getTotalBronzeGrades()).isEqualTo(theOthers);
        assertThat(response.getTotalSilverGrades()).isEqualTo(theOthers);
        assertThat(response.getTotalGoldGrades()).isEqualTo(totalGold);
        assertThat(response.getTotalPlatinumGrades()).isEqualTo(totalPlatinum);
    }
}