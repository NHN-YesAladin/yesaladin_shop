package shop.yesaladin.shop.point.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.dummy.MemberDummy;
import shop.yesaladin.shop.point.domain.model.PointCode;
import shop.yesaladin.shop.point.domain.model.PointHistory;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaPointHistoryRepositoryTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    JpaCommandPointHistoryRepository jpaPointHistoryRepository;

    long amount = 1000;
    LocalDateTime createDateTime = LocalDateTime.of(2023, 1, 1, 12, 0, 0);
    PointCode pointCode = PointCode.USE;
    Member member;

    @BeforeEach
    void setUp() {
        member = MemberDummy.dummy();

        entityManager.persist(member);
    }

    @Test
    void save() {
        PointHistory pointHistory = createPointHistory();

        PointHistory actual = jpaPointHistoryRepository.save(pointHistory);

        assertThat(actual.getAmount()).isEqualTo(pointHistory.getAmount());
        assertThat(actual.getCreateDateTime()).isEqualTo(pointHistory.getCreateDateTime());
        assertThat(actual.getPointCode()).isEqualTo(pointHistory.getPointCode());
        assertThat(actual.getMember()).isSameAs(pointHistory.getMember());
    }


    private PointHistory createPointHistory() {
        return PointHistory.builder()
                .amount(amount)
                .createDateTime(createDateTime)
                .pointCode(pointCode)
                .member(member)
                .build();
    }
}