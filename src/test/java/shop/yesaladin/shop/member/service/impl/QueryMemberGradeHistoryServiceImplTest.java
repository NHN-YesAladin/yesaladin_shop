package shop.yesaladin.shop.member.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.common.dto.PeriodQueryRequestDto;
import shop.yesaladin.shop.common.exception.InvalidPeriodConditionException;
import shop.yesaladin.shop.member.domain.repository.QueryMemberGradeHistoryRepository;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;
import shop.yesaladin.shop.member.dto.MemberGradeHistoryQueryResponseDto;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.member.service.inter.QueryMemberGradeHistoryService;


class QueryMemberGradeHistoryServiceImplTest {

    QueryMemberGradeHistoryService queryMemberGradeHistoryService;

    Clock clock = Clock.fixed(
            Instant.parse("2023-03-10T00:00:00.000Z"),
            ZoneId.of("UTC")
    );
    QueryMemberGradeHistoryRepository queryMemberGradeHistoryRepository;
    QueryMemberRepository queryMemberRepository;

    String loginId = "user@1";

    @BeforeEach
    void setUp() {
        queryMemberRepository = Mockito.mock(QueryMemberRepository.class);
        queryMemberGradeHistoryRepository = Mockito.mock(QueryMemberGradeHistoryRepository.class);
        queryMemberGradeHistoryService = new QueryMemberGradeHistoryServiceImpl(
                queryMemberGradeHistoryRepository,
                queryMemberRepository,
                clock
        );
    }

    @ParameterizedTest(name = "{1}")
    @MethodSource(value = "getPeriodQueryRequestData")
    @DisplayName("회원의 등급내역 조회 실패 - 유효하지않은 기간")
    void findByMemberId_failedByInvalidPeriodCondition(PeriodQueryRequestDto request, String info) {
        Pageable pageable = Pageable.ofSize(10);

        assertThatThrownBy(() -> queryMemberGradeHistoryService.getByLoginId(
                loginId,
                request,
                pageable
        )).isInstanceOf(InvalidPeriodConditionException.class);

        verify(queryMemberGradeHistoryRepository, never()).findByLoginIdAndPeriod(
                anyString(),
                any(),
                any(),
                any()
        );
    }

    public static Stream<Arguments> getPeriodQueryRequestData() {
        LocalDate date1 = LocalDate.of(2022, 1, 1);
        LocalDate date2 = LocalDate.of(2023, 1, 1);
        LocalDate date3 = LocalDate.of(2054, 1, 1);

        return Stream.of(
                Arguments.of(
                        ReflectionUtils.newInstance(PeriodQueryRequestDto.class, date2, date1),
                        "[START_OVER_END] 시작날짜가 끝날짜보다 뒤에 있는 경우"
                ),
                Arguments.of(
                        ReflectionUtils.newInstance(PeriodQueryRequestDto.class, date1, date3),
                        "[FUTURE] 끝날짜가 오늘보다 미래인 경우"
                )
        );
    }

    @Test
    @DisplayName("회원의 등급내역 조회 실패 - 존재하지 않는 회원")
    void findByMemberId_fail_MemberNotFound() {
        //given
        LocalDate startDate = LocalDate.of(2023, 1, 10);
        LocalDate endDate = LocalDate.of(2023, 1, 12);
        Pageable pageable = Pageable.ofSize(10);

        PeriodQueryRequestDto request = ReflectionUtils.newInstance(
                PeriodQueryRequestDto.class,
                startDate,
                endDate
        );
        Mockito.when(queryMemberRepository.existsMemberByLoginId(loginId)).thenReturn(false);

        //when, then
        assertThatThrownBy(() -> queryMemberGradeHistoryService.getByLoginId(
                loginId,
                request,
                pageable
        )).isInstanceOf(MemberNotFoundException.class);

        verify(queryMemberRepository, times(1)).existsMemberByLoginId(loginId);
        verify(queryMemberGradeHistoryRepository, never()).findByLoginIdAndPeriod(
                anyString(),
                any(),
                any(),
                any()
        );
    }

    @Test
    @DisplayName("회원의 등급내역 조회 성공")
    void findByMemberId() {
        LocalDate startDate = LocalDate.of(2023, 1, 10);
        LocalDate endDate = LocalDate.of(2023, 1, 12);
        Pageable pageable = Pageable.ofSize(10);

        PeriodQueryRequestDto periodQueryRequestDto = ReflectionUtils.newInstance(
                PeriodQueryRequestDto.class,
                startDate,
                endDate
        );
        Mockito.when(queryMemberRepository.existsMemberByLoginId(loginId)).thenReturn(true);
        Mockito.when(queryMemberGradeHistoryRepository.findByLoginIdAndPeriod(
                eq(loginId),
                any(),
                any(),
                any()
        )).thenReturn(Page.empty());

        Page<MemberGradeHistoryQueryResponseDto> actual = queryMemberGradeHistoryService.getByLoginId(
                loginId,
                periodQueryRequestDto,
                pageable
        );

        assertThat(actual).isEmpty();

        ArgumentCaptor<LocalDate> firstCaptor = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> secondCaptor = ArgumentCaptor.forClass(LocalDate.class);

        verify(queryMemberRepository, times(1)).existsMemberByLoginId(anyString());
        verify(queryMemberGradeHistoryRepository, times(1)).findByLoginIdAndPeriod(
                anyString(),
                firstCaptor.capture(),
                secondCaptor.capture(),
                any()
        );

        assertThat(firstCaptor.getValue().getYear()).isEqualTo(startDate.getYear());
        assertThat(firstCaptor.getValue().getMonthValue()).isEqualTo(startDate.getMonthValue());
        assertThat(firstCaptor.getValue().getDayOfMonth()).isEqualTo(startDate.getDayOfMonth());
        assertThat(secondCaptor.getValue().getYear()).isEqualTo(endDate.getYear());
        assertThat(secondCaptor.getValue().getMonthValue()).isEqualTo(endDate.getMonthValue());
        assertThat(secondCaptor.getValue().getDayOfMonth()).isEqualTo(endDate.getDayOfMonth());
    }
}