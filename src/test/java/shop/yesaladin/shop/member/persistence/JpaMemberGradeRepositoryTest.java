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
import shop.yesaladin.shop.member.domain.model.MemberGrade;
import shop.yesaladin.shop.member.dummy.MemberGradeDummy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaMemberGradeRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    JpaMemberGradeRepository repository;

    private MemberGrade memberGrade;

    @BeforeEach
    void setUp() {
        memberGrade = MemberGradeDummy.dummy();
    }

    @Test
    void save() throws Exception {
        //when
        MemberGrade savedMemberGrade = repository.save(memberGrade);

        //then
        assertThat(savedMemberGrade).isNotNull();
    }

    @Test
    void findById() throws Exception {
        //given
        int id = 1;
        entityManager.persist(memberGrade);

        //when
        Optional<MemberGrade> optionalMemberGrade = repository.findById(id);

        //then
        assertThat(optionalMemberGrade).isPresent();
    }
}