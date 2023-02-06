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
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.dummy.MemberDummy;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;
import shop.yesaladin.shop.point.domain.model.PointCode;
import shop.yesaladin.shop.point.domain.model.PointHistory;
import shop.yesaladin.shop.point.domain.model.PointReasonCode;
import shop.yesaladin.shop.point.domain.repository.CommandPointHistoryRepository;
import shop.yesaladin.shop.point.domain.repository.QueryPointHistoryRepository;
import shop.yesaladin.shop.point.dto.PointHistoryRequestDto;
import shop.yesaladin.shop.point.dto.PointHistoryResponseDto;
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
        PointReasonCode pointReasonCode = PointReasonCode.USE_ORDER;

        PointHistoryRequestDto request = getPointHistoryRequest(loginId, amount, pointReasonCode);

        Mockito.when(queryMemberService.findByLoginId(loginId)).thenThrow(ClientException.class);

        //when, then
        assertThatThrownBy(() -> commandPointHistoryService.use(request))
                .isInstanceOf(ClientException.class);

        verify(queryMemberService, times(1)).findByLoginId(anyString());
    }

    @Test
    void use_fail_OverPointUse() {
        //given
        String loginId = "user@1";
        long amount = 1000;
        PointReasonCode pointReasonCode = PointReasonCode.USE_ORDER;

        PointHistoryRequestDto request = getPointHistoryRequest(loginId, amount, pointReasonCode);

        Member member = MemberDummy.dummyWithLoginIdAndId(loginId);

        Mockito.when(queryMemberService.findByLoginId(loginId)).thenReturn(member);
        Mockito.when(queryPointHistoryRepository.getMemberPointByLoginId(loginId)).thenReturn(500L);

        //when, then
        assertThatThrownBy(() -> commandPointHistoryService.use(request))
                .isInstanceOf(ClientException.class);

        verify(queryMemberService, times(1)).findByLoginId(anyString());
        verify(queryPointHistoryRepository, times(1)).getMemberPointByLoginId(anyString());
    }

    @Test
    void use() {
        //given
        String loginId = "user@1";
        long amount = 1000;
        PointCode pointCode = PointCode.USE;
        PointReasonCode pointReasonCode = PointReasonCode.USE_ORDER;

        PointHistoryRequestDto request = getPointHistoryRequest(loginId, amount, pointReasonCode);

        Member member = MemberDummy.dummyWithLoginIdAndId(loginId);
        PointHistory response = PointHistory.builder()
                .id(1L)
                .amount(amount)
                .pointCode(pointCode)
                .pointReasonCode(pointReasonCode)
                .createDateTime(LocalDateTime.now())
                .member(member).build();

        Mockito.when(queryMemberService.findByLoginId(loginId)).thenReturn(member);
        Mockito.when(queryPointHistoryRepository.getMemberPointByLoginId(loginId))
                .thenReturn(2000L);
        Mockito.when(commandPointHistoryRepository.save(any())).thenReturn(response);

        //when
        PointHistoryResponseDto result = commandPointHistoryService.use(request);

        //then
        assertThat(result.getAmount()).isEqualTo(amount);
        assertThat(result.getPointCode()).isEqualTo(pointCode);
        assertThat(result.getPointReasonCode()).isEqualTo(pointReasonCode);

        ArgumentCaptor<PointHistory> captor = ArgumentCaptor.forClass(PointHistory.class);
        verify(queryMemberService, times(1)).findByLoginId(loginId);
        verify(queryPointHistoryRepository, times(1)).getMemberPointByLoginId(loginId);
        verify(commandPointHistoryRepository, times(1)).save(captor.capture());

        assertThat(captor.getValue().getAmount()).isEqualTo(amount);
        assertThat(captor.getValue().getMember().getId()).isEqualTo(member.getId());
        assertThat(captor.getValue().getPointCode()).isEqualTo(pointCode);
        assertThat(captor.getValue().getPointReasonCode()).isEqualTo(pointReasonCode);
    }

    @Test
    void save_fail_MemberNotFound() {
        //given
        String loginId = "user@1";
        long amount = 1000;
        PointReasonCode pointReasonCode = PointReasonCode.SAVE_ORDER;

        PointHistoryRequestDto request = getPointHistoryRequest(loginId, amount, pointReasonCode);

        Mockito.when(queryMemberService.findByLoginId(loginId)).thenThrow(ClientException.class);

        //when, then
        assertThatThrownBy(() -> commandPointHistoryService.save(request))
                .isInstanceOf(ClientException.class);

        verify(queryMemberService, times(1)).findByLoginId(anyString());
    }

    @Test
    void save() {
        //given
        String loginId = "user@1";
        long amount = 1000;
        PointCode pointCode = PointCode.SAVE;
        PointReasonCode pointReasonCode = PointReasonCode.SAVE_ORDER;

        PointHistoryRequestDto request = getPointHistoryRequest(loginId, amount, pointReasonCode);

        Member member = MemberDummy.dummyWithLoginIdAndId(loginId);
        PointHistory response = PointHistory.builder()
                .id(1L)
                .amount(amount)
                .pointCode(pointCode)
                .pointReasonCode(pointReasonCode)
                .createDateTime(LocalDateTime.now())
                .member(member).build();

        Mockito.when(queryMemberService.findByLoginId(loginId)).thenReturn(member);
        Mockito.when(commandPointHistoryRepository.save(any())).thenReturn(response);

        //when
        PointHistoryResponseDto result = commandPointHistoryService.save(request);

        //then
        assertThat(result.getAmount()).isEqualTo(amount);
        assertThat(result.getPointCode()).isEqualTo(pointCode);
        assertThat(result.getPointReasonCode()).isEqualTo(pointReasonCode);

        ArgumentCaptor<PointHistory> captor = ArgumentCaptor.forClass(PointHistory.class);
        verify(queryMemberService, times(1)).findByLoginId(anyString());
        verify(commandPointHistoryRepository, times(1)).save(captor.capture());

        assertThat(captor.getValue().getAmount()).isEqualTo(amount);
        assertThat(captor.getValue().getMember().getId()).isEqualTo(member.getId());
        assertThat(captor.getValue().getPointCode()).isEqualTo(pointCode);
        assertThat(captor.getValue().getPointReasonCode()).isEqualTo(pointReasonCode);
    }

    @Test
    void sum_fail_MemberNotFound() {
        //given
        String loginId = "user@1";
        long amount = 1000;
        PointReasonCode pointReasonCode = PointReasonCode.SUM;

        PointHistoryRequestDto request = getPointHistoryRequest(loginId, amount, pointReasonCode);

        Mockito.when(queryMemberService.findByLoginId(loginId)).thenThrow(ClientException.class);

        //when, then
        assertThatThrownBy(() -> commandPointHistoryService.sum(request))
                .isInstanceOf(ClientException.class);

        verify(queryMemberService, times(1)).findByLoginId(anyString());
    }

    @Test
    void sum() {
        //given
        String loginId = "user@1";
        long amount = 1000;
        PointCode pointCode = PointCode.SUM;
        PointReasonCode pointReasonCode = PointReasonCode.SUM;

        PointHistoryRequestDto request = getPointHistoryRequest(loginId, amount, pointReasonCode);

        Member member = MemberDummy.dummyWithLoginIdAndId(loginId);
        PointHistory response = PointHistory.builder()
                .id(1L)
                .amount(amount)
                .pointCode(pointCode)
                .pointReasonCode(pointReasonCode)
                .createDateTime(LocalDateTime.now())
                .member(member).build();

        Mockito.when(queryMemberService.findByLoginId(loginId)).thenReturn(member);
        Mockito.when(commandPointHistoryRepository.save(any())).thenReturn(response);

        //when
        PointHistoryResponseDto result = commandPointHistoryService.sum(request);

        //then
        assertThat(result.getAmount()).isEqualTo(amount);
        assertThat(result.getPointCode()).isEqualTo(pointCode);
        assertThat(result.getPointReasonCode()).isEqualTo(pointReasonCode);

        ArgumentCaptor<PointHistory> captor = ArgumentCaptor.forClass(PointHistory.class);
        verify(queryMemberService, times(1)).findByLoginId(anyString());
        verify(commandPointHistoryRepository, times(1)).save(captor.capture());

        assertThat(captor.getValue().getAmount()).isEqualTo(amount);
        assertThat(captor.getValue().getMember().getId()).isEqualTo(member.getId());
        assertThat(captor.getValue().getPointCode()).isEqualTo(pointCode);
        assertThat(captor.getValue().getPointReasonCode()).isEqualTo(pointReasonCode);
    }

    private PointHistoryRequestDto getPointHistoryRequest(
            String loginId,
            long amount,
            PointReasonCode pointReasonCode
    ) {
        return new PointHistoryRequestDto(
                loginId,
                amount,
                pointReasonCode
        );
    }
}