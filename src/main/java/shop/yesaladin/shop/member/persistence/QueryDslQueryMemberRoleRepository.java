package shop.yesaladin.shop.member.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.member.domain.model.MemberRole;
import shop.yesaladin.shop.member.domain.model.MemberRole.Pk;
import shop.yesaladin.shop.member.domain.model.querydsl.QMemberRole;
import shop.yesaladin.shop.member.domain.repository.QueryMemberRoleRepository;

@RequiredArgsConstructor
@Repository
public class QueryDslQueryMemberRoleRepository implements QueryMemberRoleRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<MemberRole> findById(Pk pk) {
        QMemberRole memberRole = QMemberRole.memberRole;
        return Optional.ofNullable(queryFactory.selectFrom(memberRole)
                .where(memberRole.id.eq(pk))
                .fetchFirst());
    }

    @Override
    public List<String> findMemberRolesByMemberId(Long memberId) {
        QMemberRole memberRole = QMemberRole.memberRole;

        return queryFactory.select(memberRole.role.name)
                .from(memberRole)
                .where(memberRole.member.id.eq(memberId))
                .fetch();
    }
}
