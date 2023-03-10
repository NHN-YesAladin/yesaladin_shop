package shop.yesaladin.shop.point.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;
import shop.yesaladin.shop.point.domain.model.PointCode;
import shop.yesaladin.shop.point.domain.repository.QueryPointHistoryRepository;
import shop.yesaladin.shop.point.dto.PointHistoryResponseDto;
import shop.yesaladin.shop.point.service.inter.QueryPointHistoryService;


class QueryPointHistoryServiceImplTest {

    QueryMemberService queryMemberService;
    QueryPointHistoryService queryPointHistoryService;
    QueryPointHistoryRepository queryPointHistoryRepository;

    @BeforeEach
    void setUp() {
        queryMemberService = Mockito.mock(QueryMemberService.class);
        queryPointHistoryRepository = Mockito.mock(QueryPointHistoryRepository.class);

        queryPointHistoryService = new QueryPointHistoryServiceImpl(
                queryPointHistoryRepository,
                queryMemberService
        );
    }

    @Test
    void getPointHistoriesWithLoginIdAndCode_fail_memberNotFound() {
        //given
        String loginId = "user@1";
        PointCode pointCode = PointCode.USE;
        Pageable pageable = Pageable.ofSize(5);

        Page<PointHistoryResponseDto> response = Page.empty(pageable);

        Mockito.when(queryMemberService.existsLoginId(loginId)).thenReturn(false);

        //when, then
        assertThatThrownBy(() -> queryPointHistoryService.getPointHistoriesWithLoginIdAndCode(
                loginId,
                pointCode,
                pageable
        )).isInstanceOf(ClientException.class);
    }

    @Test
    void getPointHistoriesWithLoginIdAndCode() {
        //given
        String loginId = "user@1";
        PointCode pointCode = PointCode.USE;
        Pageable pageable = Pageable.ofSize(5);

        Page<PointHistoryResponseDto> response = Page.empty(pageable);

        Mockito.when(queryMemberService.existsLoginId(loginId)).thenReturn(true);
        Mockito.when(queryPointHistoryRepository.getByLoginIdAndPointCode(
                        eq(loginId),
                        any(),
                        any()
                ))
                .thenReturn(response);

        //when
        Page<PointHistoryResponseDto> result = queryPointHistoryService.getPointHistoriesWithLoginIdAndCode(
                loginId,
                pointCode,
                pageable
        );

        //then
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getPageable()).isEqualTo(pageable);

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(queryPointHistoryRepository, times(1)).getByLoginIdAndPointCode(
                anyString(),
                any(),
                captor.capture()
        );
        assertThat(captor.getValue().getPageSize()).isEqualTo(5);
    }

    @Test
    void getPointHistoriesWithLoginId_fail_memberNotFound() {
        //given
        String loginId = "user@1";
        Pageable pageable = Pageable.ofSize(5);

        Mockito.when(queryMemberService.existsLoginId(loginId)).thenReturn(false);

        //when, then
        assertThatThrownBy(() -> queryPointHistoryService.getPointHistoriesWithLoginId(
                loginId,
                pageable
        )).isInstanceOf(ClientException.class);
    }

    @Test
    void getPointHistoriesWithLoginId() {
        //given
        String loginId = "user@1";
        Pageable pageable = Pageable.ofSize(5);

        Page<PointHistoryResponseDto> response = Page.empty(pageable);

        Mockito.when(queryMemberService.existsLoginId(loginId)).thenReturn(true);
        Mockito.when(queryPointHistoryRepository.getByLoginId(eq(loginId), any()))
                .thenReturn(response);

        //when
        Page<PointHistoryResponseDto> result = queryPointHistoryService.getPointHistoriesWithLoginId(
                loginId,
                pageable
        );

        //then
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getPageable()).isEqualTo(pageable);

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(queryPointHistoryRepository, times(1)).getByLoginId(
                anyString(),
                captor.capture()
        );
        assertThat(captor.getValue().getPageSize()).isEqualTo(5);
    }

    @Test
    void getPointHistoriesWithCode() {
        //given
        PointCode pointCode = PointCode.USE;
        Pageable pageable = Pageable.ofSize(5);

        Page<PointHistoryResponseDto> response = Page.empty(pageable);

        Mockito.when(queryPointHistoryRepository.getByPointCode(any(), any())).thenReturn(response);

        //when
        Page<PointHistoryResponseDto> result = queryPointHistoryService.getPointHistoriesWithCode(
                pointCode,
                pageable
        );

        //then
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getPageable()).isEqualTo(pageable);

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(queryPointHistoryRepository, times(1)).getByPointCode(
                any(),
                captor.capture()
        );
        assertThat(captor.getValue().getPageSize()).isEqualTo(5);
    }

    @Test
    void getPointHistories() {
        //given
        Pageable pageable = Pageable.ofSize(5);

        Page<PointHistoryResponseDto> response = Page.empty(pageable);

        Mockito.when(queryPointHistoryRepository.getBy(any())).thenReturn(response);

        //when
        Page<PointHistoryResponseDto> result = queryPointHistoryService.getPointHistories(pageable);

        //then
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getPageable()).isEqualTo(pageable);

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(queryPointHistoryRepository, times(1)).getBy(captor.capture());
        assertThat(captor.getValue().getPageSize()).isEqualTo(5);
    }

    @Test
    void getMemberPoint_fail_memberNotFound() {
        String loginId = "user@1";

        Mockito.when(queryMemberService.existsLoginId(loginId)).thenReturn(false);

        assertThatThrownBy(() -> queryPointHistoryService.getMemberPoint(loginId))
                .isInstanceOf(ClientException.class);
    }

    @Test
    void getMemberPoint_success() {
        String loginId = "user@1";

        Mockito.when(queryMemberService.existsLoginId(loginId)).thenReturn(true);

        Mockito.when(queryPointHistoryRepository.getMemberPointByLoginId(loginId))
                .thenReturn(1000L);

        long result = queryPointHistoryService.getMemberPoint(loginId);

        assertThat(result).isEqualTo(1000L);
    }
}