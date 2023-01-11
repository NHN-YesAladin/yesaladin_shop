package shop.yesaladin.shop.member.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import shop.yesaladin.shop.common.dto.PeriodQueryRequestDto;
import shop.yesaladin.shop.common.exception.InvalidPeriodConditionException;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.repository.QueryMemberGradeHistoryRepository;
import shop.yesaladin.shop.member.dto.MemberGradeHistoryQueryResponseDto;
import shop.yesaladin.shop.member.dummy.MemberDummy;
import shop.yesaladin.shop.member.service.inter.QueryMemberGradeHistoryService;


class QueryMemberGradeHistoryServiceImplTest {

    QueryMemberGradeHistoryService queryMemberGradeHistoryService;

    Clock clock = Clock.fixed(
            Instant.parse("2023-01-01T00:00:00.000Z"),
            ZoneId.of("UTC")
    );
    QueryMemberGradeHistoryRepository queryMemberGradeHistoryRepository;

    Member member = MemberDummy.dummyWithId(1L);

    @BeforeEach
    void setUp() {
        queryMemberGradeHistoryRepository = Mockito.mock(QueryMemberGradeHistoryRepository.class);
        queryMemberGradeHistoryService = new QueryMemberGradeHistoryServiceImpl(
                queryMemberGradeHistoryRepository,
                clock
        );
    }

    @ParameterizedTest(name = "{1}")
    @MethodSource(value = "getPeriodQueryRequestData")
    void findByMemberId_failedByInvalidPeriodCondition(PeriodQueryRequestDto request, String info) {
        Long memberId = member.getId();

        assertThatThrownBy(() -> queryMemberGradeHistoryService.findByMemberId(
                memberId,
                request
        )).isInstanceOf(InvalidPeriodConditionException.class);

        verify(queryMemberGradeHistoryRepository, never()).findByMemberIdAndPeriod(anyLong(), any(), any());
    }

    public static Stream<Arguments> getPeriodQueryRequestData() {
        LocalDate date1 = LocalDate.of(2022, 1, 1);
        LocalDate date2 = LocalDate.of(2023, 1, 1);
        LocalDate date3 = LocalDate.of(2054, 1, 1);

        return Stream.of(
                Arguments.of(
                        ReflectionUtils.newInstance(PeriodQueryRequestDto.class, date2, date1),
                        "[INVALID] 시작날짜가 끝날짜보다 뒤에 있는 경우"
                ),
                Arguments.of(
                        ReflectionUtils.newInstance(PeriodQueryRequestDto.class, date1, date3),
                        "[FUTURE] 끝날짜가 오늘보다 미래인 경우"
                )
        );
    }

    @Test
    void findByMemberId() {
        Long memberId = member.getId();
        LocalDate startDate = LocalDate.of(2022, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 1);

        PeriodQueryRequestDto periodQueryRequestDto = ReflectionUtils.newInstance(
                PeriodQueryRequestDto.class,
                startDate,
                endDate
        );

        Mockito.when(queryMemberGradeHistoryRepository.findByMemberIdAndPeriod(
                eq(memberId),
                any(),
                any()
        )).thenReturn(new ArrayList<>(Collections.emptyList()));

        List<MemberGradeHistoryQueryResponseDto> actual = queryMemberGradeHistoryService.findByMemberId(
                memberId,
                periodQueryRequestDto
        );

        assertThat(actual).isEmpty();

        ArgumentCaptor<LocalDate> firstCaptor = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> secondCaptor = ArgumentCaptor.forClass(LocalDate.class);

        verify(queryMemberGradeHistoryRepository, times(1)).findByMemberIdAndPeriod(
                anyLong(),
                firstCaptor.capture(),
                secondCaptor.capture()
        );

        assertThat(firstCaptor.getValue().getYear()).isEqualTo(startDate.getYear());
        assertThat(firstCaptor.getValue().getMonthValue()).isEqualTo(startDate.getMonthValue());
        assertThat(firstCaptor.getValue().getDayOfMonth()).isEqualTo(startDate.getDayOfMonth());
        assertThat(secondCaptor.getValue().getYear()).isEqualTo(endDate.getYear());
        assertThat(secondCaptor.getValue().getMonthValue()).isEqualTo(endDate.getMonthValue());
        assertThat(secondCaptor.getValue().getDayOfMonth()).isEqualTo(endDate.getDayOfMonth());
    }
}