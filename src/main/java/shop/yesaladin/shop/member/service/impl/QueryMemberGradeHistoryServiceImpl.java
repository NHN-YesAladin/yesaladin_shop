package shop.yesaladin.shop.member.service.impl;

import java.time.Clock;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.common.dto.PeriodQueryRequestDto;
import shop.yesaladin.shop.member.domain.repository.QueryMemberGradeHistoryRepository;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRepository;
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
    private final QueryMemberRepository queryMemberRepository;
    private final Clock clock;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MemberGradeHistoryQueryResponseDto> getByLoginId(
            String loginId,
            PeriodQueryRequestDto request,
            Pageable pageable
    ) {
        request.validate(clock);

        checkLoginIdIsExist(loginId);

        return queryMemberGradeHistoryRepository.findByLoginIdAndPeriod(
                loginId,
                request.getStartDateOrDefaultValue(clock),
                request.getEndDateOrDefaultValue(clock),
                pageable
        );
    }

    private void checkLoginIdIsExist(String loginId) {
        if (!queryMemberRepository.existsMemberByLoginId(loginId)) {
            throw new ClientException(
                    ErrorCode.MEMBER_NOT_FOUND,
                    "Member not found with loginId : " + loginId
            );
        }
    }
}
