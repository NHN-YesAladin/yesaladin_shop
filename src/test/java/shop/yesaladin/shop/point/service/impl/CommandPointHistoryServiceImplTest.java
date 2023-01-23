package shop.yesaladin.shop.point.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.dto.MemberDto;
import shop.yesaladin.shop.member.dummy.MemberDummy;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;
import shop.yesaladin.shop.point.domain.model.PointCode;
import shop.yesaladin.shop.point.domain.model.PointHistory;
import shop.yesaladin.shop.point.domain.repository.CommandPointHistoryRepository;
import shop.yesaladin.shop.point.domain.repository.QueryPointHistoryRepository;
import shop.yesaladin.shop.point.dto.PointHistoryRequestDto;
import shop.yesaladin.shop.point.dto.PointHistoryResponseDto;
import shop.yesaladin.shop.point.exception.OverPointUseException;
import shop.yesaladin.shop.point.service.inter.CommandPointHistoryService;

class CommandPointHistoryServiceImplTest {

    CommandPointHistoryService commandPointHistoryService;

    CommandPointHistoryRepository commandPointHistoryRepository;
    QueryPointHistoryRepository queryPointHistoryRepository;
    QueryMemberService queryMemberService;

    @BeforeEach
    void setUp() {
        commandPointHistoryRepository = Mockito.mock(CommandPointHistoryRepository.class);
        queryPointHistoryRepository = Mockito.mock(QueryPointHistoryRepository.class);
        queryMemberService = Mockito.mock(QueryMemberService.class);

        commandPointHistoryService = new CommandPointHistoryServiceImpl(
                commandPointHistoryRepository,
                queryPointHistoryRepository,
                queryMemberService
        );
    }

    @Test
    void use_fail_MemberNotFound() {
        //given
        String loginId = "user@1";
        long amount = 1000;
        PointHistoryRequestDto request = ReflectionUtils.newInstance(
                PointHistoryRequestDto.class,
                loginId,
                amount
        );

        Mockito.when(queryMemberService.findMemberByLoginId(loginId))
                .thenThrow(MemberNotFoundException.class);

        //when, then
        assertThatThrownBy(() -> commandPointHistoryService.use(request)).isInstanceOf(
                MemberNotFoundException.class);

        verify(queryMemberService, times(1)).findMemberByLoginId(anyString());
    }

    @Test
    void use_fail_OverPointUse() {
        //given
        String loginId = "user@1";
        long amount = 1000;
        PointHistoryRequestDto request = ReflectionUtils.newInstance(
                PointHistoryRequestDto.class,
                loginId,
                amount
        );

        Member member = MemberDummy.dummyWithLoginId(loginId);

        Mockito.when(queryMemberService.findMemberByLoginId(loginId))
                .thenReturn(MemberDto.fromEntity(member));
        Mockito.when(queryPointHistoryRepository.getMemberPointByLoginId(loginId)).thenReturn(500L);

        //when, then
        assertThatThrownBy(() -> commandPointHistoryService.use(request)).isInstanceOf(
                OverPointUseException.class);

        verify(queryMemberService, times(1)).findMemberByLoginId(anyString());
        verify(queryPointHistoryRepository, times(1)).getMemberPointByLoginId(anyString());
    }

    @Test
    void use() {
        //given
        String loginId = "user@1";
        long amount = 1000;
        PointCode pointCode = PointCode.USE;

        PointHistoryRequestDto request = ReflectionUtils.newInstance(
                PointHistoryRequestDto.class,
                loginId,
                amount
        );

        Member member = MemberDummy.dummyWithLoginId(loginId);
        PointHistory response = PointHistory.builder()
                .id(1L)
                .amount(amount)
                .pointCode(pointCode)
                .createDateTime(LocalDateTime.now())
                .member(member).build();

        Mockito.when(queryMemberService.findMemberByLoginId(loginId))
                .thenReturn(MemberDto.fromEntity(member));
        Mockito.when(queryPointHistoryRepository.getMemberPointByLoginId(loginId))
                .thenReturn(2000L);
        Mockito.when(commandPointHistoryRepository.save(any())).thenReturn(response);

        //when
        PointHistoryResponseDto result = commandPointHistoryService.use(request);

        //then
        assertThat(result.getAmount()).isEqualTo(amount);
        assertThat(result.getLoginId()).isEqualTo(loginId);
        assertThat(result.getPointCode()).isEqualTo(pointCode);

        ArgumentCaptor<PointHistory> captor = ArgumentCaptor.forClass(PointHistory.class);
        verify(queryMemberService, times(1)).findMemberByLoginId(loginId);
        verify(queryPointHistoryRepository, times(1)).getMemberPointByLoginId(loginId);
        verify(commandPointHistoryRepository, times(1)).save(captor.capture());

        assertThat(captor.getValue().getAmount()).isEqualTo(amount);
        assertThat(captor.getValue().getMember().getId()).isEqualTo(member.getId());
        assertThat(captor.getValue().getPointCode()).isEqualTo(pointCode);
    }

    @Test
    void save_fail_MemberNotFound() {
        //given
        String loginId = "user@1";
        long amount = 1000;
        PointHistoryRequestDto request = ReflectionUtils.newInstance(
                PointHistoryRequestDto.class,
                loginId,
                amount
        );

        Mockito.when(queryMemberService.findMemberByLoginId(loginId))
                .thenThrow(MemberNotFoundException.class);

        //when, then
        assertThatThrownBy(() -> commandPointHistoryService.save(request)).isInstanceOf(
                MemberNotFoundException.class);

        verify(queryMemberService, times(1)).findMemberByLoginId(anyString());

    }

    @Test
    void save() {
        //given
        String loginId = "user@1";
        long amount = 1000;
        PointCode pointCode = PointCode.SAVE;

        PointHistoryRequestDto request = ReflectionUtils.newInstance(
                PointHistoryRequestDto.class,
                loginId,
                amount
        );

        Member member = MemberDummy.dummyWithLoginId(loginId);
        PointHistory response = PointHistory.builder()
                .id(1L)
                .amount(amount)
                .pointCode(pointCode)
                .createDateTime(LocalDateTime.now())
                .member(member).build();

        Mockito.when(queryMemberService.findMemberByLoginId(loginId))
                .thenReturn(MemberDto.fromEntity(member));
        Mockito.when(commandPointHistoryRepository.save(any()))
                .thenReturn(response);

        //when
        PointHistoryResponseDto result = commandPointHistoryService.save(request);

        //then
        assertThat(result.getAmount()).isEqualTo(amount);
        assertThat(result.getLoginId()).isEqualTo(loginId);
        assertThat(result.getPointCode()).isEqualTo(pointCode);

        ArgumentCaptor<PointHistory> captor = ArgumentCaptor.forClass(PointHistory.class);
        verify(queryMemberService, times(1)).findMemberByLoginId(anyString());
        verify(commandPointHistoryRepository, times(1)).save(captor.capture());

        assertThat(captor.getValue().getAmount()).isEqualTo(amount);
        assertThat(captor.getValue().getMember().getId()).isEqualTo(member.getId());
        assertThat(captor.getValue().getPointCode()).isEqualTo(pointCode);
    }
}