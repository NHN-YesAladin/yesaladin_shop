package shop.yesaladin.shop.member.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberGrade;
import shop.yesaladin.shop.member.domain.model.MemberGradeHistory;
import shop.yesaladin.shop.member.dummy.MemberDummy;
import shop.yesaladin.shop.member.dummy.MemberGradeDummy;
import shop.yesaladin.shop.member.dummy.MemberGradeHistoryDummy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaMemberGradeHistoryRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    JpaMemberGradeHistoryRepository repository;

    private MemberGradeHistory memberGradeHistory;
    private MemberGrade memberGrade;
    private Member member;

    @BeforeEach
    void setUp() {
        memberGrade = MemberGradeDummy.dummy();
    }

    @Test
    void save() throws Exception {
        //given
        MemberGrade savedMemberGrade = entityManager.persist(memberGrade);
        member = MemberDummy.dummy(savedMemberGrade);
        Member savedMember = entityManager.persist(member);

        memberGradeHistory = MemberGradeHistoryDummy.dummy(savedMemberGrade, savedMember);

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

    @Test
    void findById() throws Exception {
        //given
        entityManager.persist(memberGrade);
        member = MemberDummy.dummy();
        entityManager.persist(member);
        memberGradeHistory = MemberGradeHistoryDummy.dummy();
        MemberGradeHistory savedMemberGradeHistory = entityManager.persist(memberGradeHistory);

        //when
        Optional<MemberGradeHistory> optionalMemberGradeHistory = repository.findById(
                savedMemberGradeHistory.getId());

        //then
        assertThat(optionalMemberGradeHistory).isPresent();
        assertThat(optionalMemberGradeHistory.get().getMemberGrade().getName()).isEqualTo(
                savedMemberGradeHistory.getMemberGrade().getName());
        assertThat(optionalMemberGradeHistory.get().getMember().getName()).isEqualTo(
                savedMemberGradeHistory.getMember().getName());
        assertThat(optionalMemberGradeHistory.get().getPreviousPaidAmount()).isEqualTo(
                savedMemberGradeHistory.getPreviousPaidAmount());
        assertThat(optionalMemberGradeHistory.get().getUpdateDate()).isEqualTo(
                savedMemberGradeHistory.getUpdateDate());
    }
}