package shop.yesaladin.shop.point.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;
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
        long memberId = 1L;
        long amount = 1000;
        PointHistoryRequestDto request = ReflectionUtils.newInstance(
                PointHistoryRequestDto.class,
                amount
        );

        Mockito.when(queryMemberService.findMemberById(memberId))
                .thenThrow(MemberNotFoundException.class);

        //when, then
        assertThatThrownBy(() -> commandPointHistoryService.use(memberId, request)).isInstanceOf(
                MemberNotFoundException.class);

        verify(queryMemberService, times(1)).findMemberById(anyLong());
    }

    @Test
    void use_fail_OverPointUse() {
        //given
        long memberId = 1L;
        long amount = 1000;
        PointHistoryRequestDto request = ReflectionUtils.newInstance(
                PointHistoryRequestDto.class,
                amount
        );

        Member member = DummyMember.member();

        Mockito.when(queryMemberService.findMemberById(memberId))
                .thenReturn(MemberDto.fromEntity(member));
        Mockito.when(queryPointHistoryRepository.getMemberPointByMemberId(memberId))
                .thenReturn(500L);

        //when, then
        assertThatThrownBy(() -> commandPointHistoryService.use(memberId, request)).isInstanceOf(
                OverPointUseException.class);

        verify(queryMemberService, times(1)).findMemberById(anyLong());
        verify(queryPointHistoryRepository, times(1)).getMemberPointByMemberId(anyLong());
    }

    @Test
    void use() {
        //given
        long memberId = 1L;
        long amount = 1000;
        PointCode pointCode = PointCode.USE;

        PointHistoryRequestDto request = ReflectionUtils.newInstance(
                PointHistoryRequestDto.class,
                amount
        );

        Member member = MemberDummy.dummyWithId(memberId);
        PointHistory response = PointHistory.builder()
                .id(1L)
                .amount(amount)
                .pointCode(pointCode)
                .createDateTime(LocalDateTime.now())
                .member(member).build();

        Mockito.when(queryMemberService.findMemberById(memberId))
                .thenReturn(MemberDto.fromEntity(member));
        Mockito.when(queryPointHistoryRepository.getMemberPointByMemberId(memberId))
                .thenReturn(2000L);
        Mockito.when(commandPointHistoryRepository.save(any()))
                .thenReturn(response);

        //when
        PointHistoryResponseDto result = commandPointHistoryService.use(memberId, request);

        //then
        assertThat(result.getAmount()).isEqualTo(amount);
        assertThat(result.getMember().getId()).isEqualTo(member.getId());
        assertThat(result.getPointCode()).isEqualTo(pointCode);

        ArgumentCaptor<PointHistory> captor = ArgumentCaptor.forClass(PointHistory.class);
        verify(queryMemberService, times(1)).findMemberById(anyLong());
        verify(queryPointHistoryRepository, times(1)).getMemberPointByMemberId(anyLong());
        verify(commandPointHistoryRepository, times(1)).save(captor.capture());

        assertThat(captor.getValue().getAmount()).isEqualTo(amount);
        assertThat(captor.getValue().getMember().getId()).isEqualTo(member.getId());
        assertThat(captor.getValue().getPointCode()).isEqualTo(pointCode);
    }

    @Test
    void save_fail_MemberNotFound() {
        //given
        long memberId = 1L;
        long amount = 1000;
        PointHistoryRequestDto request = ReflectionUtils.newInstance(
                PointHistoryRequestDto.class,
                amount
        );

        Mockito.when(queryMemberService.findMemberById(memberId))
                .thenThrow(MemberNotFoundException.class);

        //when, then
        assertThatThrownBy(() -> commandPointHistoryService.save(memberId, request)).isInstanceOf(
                MemberNotFoundException.class);

        verify(queryMemberService, times(1)).findMemberById(anyLong());

    }

    @Test
    void save() {
        //given
        long memberId = 1L;
        long amount = 1000;
        PointCode pointCode = PointCode.SAVE;

        PointHistoryRequestDto request = ReflectionUtils.newInstance(
                PointHistoryRequestDto.class,
                amount
        );

        Member member = MemberDummy.dummyWithId(memberId);
        PointHistory response = PointHistory.builder()
                .id(1L)
                .amount(amount)
                .pointCode(pointCode)
                .createDateTime(LocalDateTime.now())
                .member(member).build();

        Mockito.when(queryMemberService.findMemberById(memberId))
                .thenReturn(MemberDto.fromEntity(member));
        Mockito.when(commandPointHistoryRepository.save(any()))
                .thenReturn(response);

        //when
        PointHistoryResponseDto result = commandPointHistoryService.save(memberId, request);

        //then
        assertThat(result.getAmount()).isEqualTo(amount);
        assertThat(result.getMember().getId()).isEqualTo(member.getId());
        assertThat(result.getPointCode()).isEqualTo(pointCode);

        ArgumentCaptor<PointHistory> captor = ArgumentCaptor.forClass(PointHistory.class);
        verify(queryMemberService, times(1)).findMemberById(anyLong());
        verify(commandPointHistoryRepository, times(1)).save(captor.capture());

        assertThat(captor.getValue().getAmount()).isEqualTo(amount);
        assertThat(captor.getValue().getMember().getId()).isEqualTo(member.getId());
        assertThat(captor.getValue().getPointCode()).isEqualTo(pointCode);
    }
}