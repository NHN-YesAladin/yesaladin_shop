package shop.yesaladin.shop.member.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberGradeHistory;
import shop.yesaladin.shop.member.dummy.MemberDummy;
import shop.yesaladin.shop.member.dummy.MemberGradeHistoryDummy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaCommandMemberGradeHistoryRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    JpaCommandMemberGradeHistoryRepository repository;

    private MemberGradeHistory memberGradeHistory;
    private Member member;

    @BeforeEach
    void setUp() {
        member = MemberDummy.dummy();
    }

    @Test
    void save() throws Exception {
        //given
        Member savedMember = entityManager.persist(member);

        memberGradeHistory = MemberGradeHistoryDummy.dummy(savedMember);

        //when
        MemberGradeHistory savedMemberGradeHistory = repository.save(memberGradeHistory);

        //then
        assertThat(savedMemberGradeHistory.getMember()
                .getName()).isEqualTo(savedMemberGradeHistory.getMember().getName());
        assertThat(savedMemberGradeHistory.getMemberGrade().getName()).isEqualTo(
                savedMemberGradeHistory.getMemberGrade().getName());
        assertThat(savedMemberGradeHistory.getPreviousPaidAmount()).isEqualTo(
                savedMemberGradeHistory.getPreviousPaidAmount());
        assertThat(savedMemberGradeHistory.getUpdateDate()).isEqualTo(savedMemberGradeHistory.getUpdateDate());
    }
}