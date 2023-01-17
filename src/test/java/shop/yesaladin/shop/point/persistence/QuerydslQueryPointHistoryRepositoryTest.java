package shop.yesaladin.shop.point.persistence;


import java.time.LocalDateTime;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.dummy.MemberDummy;
import shop.yesaladin.shop.point.domain.model.PointCode;
import shop.yesaladin.shop.point.domain.model.PointHistory;
import shop.yesaladin.shop.point.domain.repository.QueryPointHistoryRepository;

@Transactional
@SpringBootTest
class QuerydslQueryPointHistoryRepositoryTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    QueryPointHistoryRepository queryPointHistoryRepository;

    Member member;

    long amount = 1000;
    LocalDateTime createDateTime = LocalDateTime.now();
    PointCode pointCode = PointCode.USE;
    @BeforeEach
    void setUp() {
        member = MemberDummy.dummy();
        entityManager.persist(member);
    }

    @Test
    void findById() {
        //given
        PointHistory pointHistory = createPointHistory(member, pointCode);
        entityManager.persist(pointHistory);

        Long id = pointHistory.getId();
        //when
        Optional<PointHistory> foundPointHistory = queryPointHistoryRepository.findById(id);

        //then
        assertThat(foundPointHistory).isPresent();
        assertThat(foundPointHistory.get().getId()).isEqualTo(id);
        assertThat(foundPointHistory.get().getPointCode()).isEqualTo(pointCode);
        assertThat(foundPointHistory.get().getAmount()).isEqualTo(amount);
        assertThat(foundPointHistory.get().getCreateDateTime()).isEqualTo(createDateTime);
        assertThat(foundPointHistory.get().getMember()).isSameAs(member);
    }

    @Test
    void getMemberPointByMemberId() {

    }

    PointHistory createPointHistory(Member member, PointCode pointCode) {
        return PointHistory.builder().amount(amount)
                .pointCode(pointCode)
                .member(member)
                .createDateTime(createDateTime)
                .build();
    }
    void setPointHistory(int save, int use) {
        for (int i = 0; i < save; i++) {
            entityManager.persist(createPointHistory(member, PointCode.SAVE));
        }
        for (int i = 0; i < use; i++) {
            entityManager.persist(createPointHistory(member, PointCode.USE));
        }
    }

}