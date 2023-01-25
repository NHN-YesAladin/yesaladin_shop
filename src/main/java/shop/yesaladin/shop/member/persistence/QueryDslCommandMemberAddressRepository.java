package shop.yesaladin.shop.member.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.member.domain.model.querydsl.QMemberAddress;
import shop.yesaladin.shop.member.domain.repository.CommandMemberAddressRepository;

/**
 * 회원 배송지 데이터 수정을 위한 레포지토리의 QueryDsl 구현체입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@RequiredArgsConstructor
@Repository
public class QueryDslCommandMemberAddressRepository implements CommandMemberAddressRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    @Override
    public MemberAddress save(MemberAddress memberAddress) {
        entityManager.persist(memberAddress);
        return memberAddress;
    }

    @Override
    public void deleteById(Long id) {
        QMemberAddress memberAddress = QMemberAddress.memberAddress;
        queryFactory.delete(memberAddress)
                .where(memberAddress.id.eq(id))
                .execute();
    }

    @Override
    public void updateIsDefaultToFalseByLoginId(String loginId) {
        QMemberAddress memberAddress = QMemberAddress.memberAddress;

        queryFactory.update(memberAddress)
                .where(memberAddress.member.loginId.eq(loginId)
                        .and(memberAddress.isDefault)
                        .isTrue())
                .set(memberAddress.isDefault, false);
    }
}
