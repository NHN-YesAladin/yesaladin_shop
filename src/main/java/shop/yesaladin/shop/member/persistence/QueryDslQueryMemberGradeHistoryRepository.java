package shop.yesaladin.shop.member.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.member.domain.model.querydsl.QMemberGradeHistory;
import shop.yesaladin.shop.member.domain.repository.QueryMemberGradeHistoryRepository;
import shop.yesaladin.shop.member.dto.MemberGradeHistoryQueryResponseDto;

/**
 * 회원등급변경내역 조회 관련 QueryDsl repository 구현체입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@RequiredArgsConstructor
@Repository
public class QueryDslQueryMemberGradeHistoryRepository implements
        QueryMemberGradeHistoryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<MemberGradeHistoryQueryResponseDto> findById(long id) {
        QMemberGradeHistory memberGradeHistory = QMemberGradeHistory.memberGradeHistory;

        return Optional.ofNullable(queryFactory.select(Projections.constructor(
                        MemberGradeHistoryQueryResponseDto.class,
                        memberGradeHistory.id,
                        memberGradeHistory.updateDate,
                        memberGradeHistory.previousPaidAmount,
                        memberGradeHistory.memberGrade,
                        memberGradeHistory.member.loginId
                ))
                .from(memberGradeHistory)
                .where(memberGradeHistory.id.eq(id))
                .fetchFirst());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberGradeHistoryQueryResponseDto> findByLoginIdAndPeriod(
            String loginId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        QMemberGradeHistory memberGradeHistory = QMemberGradeHistory.memberGradeHistory;

        return queryFactory.select(Projections.constructor(
                        MemberGradeHistoryQueryResponseDto.class,
                        memberGradeHistory.id,
                        memberGradeHistory.updateDate,
                        memberGradeHistory.previousPaidAmount,
                        memberGradeHistory.memberGrade,
                        memberGradeHistory.member.loginId
                ))
                .from(memberGradeHistory)
                .where(memberGradeHistory.member.loginId.eq(loginId)
                        .and(memberGradeHistory.updateDate.between(startDate, endDate)))
                .fetch();
    }
}
