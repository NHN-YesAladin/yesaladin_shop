package shop.yesaladin.shop.member.service.impl;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.common.dto.PeriodQueryRequestDto;
import shop.yesaladin.shop.common.exception.InvalidPeriodConditionException;
import shop.yesaladin.shop.common.exception.type.InvalidPeriodConditionType;
import shop.yesaladin.shop.member.domain.repository.QueryMemberGradeHistoryRepository;
import shop.yesaladin.shop.member.dto.MemberGradeHistoryQueryResponseDto;
import shop.yesaladin.shop.member.service.inter.QueryMemberGradeHistoryService;

/**
 * 회원등급 변경내역 조회 관련 service 구현체입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class QueryMemberGradeHistoryServiceImpl implements QueryMemberGradeHistoryService {

    private final QueryMemberGradeHistoryRepository queryMemberGradeHistoryRepository;
    private final Clock clock;

    @Override
    @Transactional(readOnly = true)
    public List<MemberGradeHistoryQueryResponseDto> findByMemberId(
            long memberId,
            PeriodQueryRequestDto request
    ) {
        checkValidQueryCondition(request);
        return queryMemberGradeHistoryRepository.findByMemberIdAndPeriod(
                memberId,
                request.getStartDateOrDefaultValue(clock),
                request.getEndDateOrDefaultValue(clock)
        );
    }

    private void checkValidQueryCondition(PeriodQueryRequestDto queryDto) {
        if (queryDto.getStartDateOrDefaultValue(clock)
                .isAfter(queryDto.getEndDateOrDefaultValue(clock))) {
            throw new InvalidPeriodConditionException(InvalidPeriodConditionType.START_OVER_END);
        }
        if (queryDto.getEndDateOrDefaultValue(clock).isAfter(LocalDate.now(clock))) {
            throw new InvalidPeriodConditionException(InvalidPeriodConditionType.FUTURE);
        }
    }
}
