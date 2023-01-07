package shop.yesaladin.shop.member.persistence;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberGrade;
import shop.yesaladin.shop.member.dummy.MemberDummy;
import shop.yesaladin.shop.member.dummy.MemberGradeDummy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaMemberRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    JpaMemberRepository repository;

    private MemberGrade grade;
    private Member member;

    @BeforeEach
    void setUp() {
        grade = MemberGradeDummy.dummy();
        member = MemberDummy.dummy();
    }

    @Test
    void save() throws Exception {
        //given
        entityManager.persist(grade);

        //when
        Member savedMember = repository.save(member);

        //then
        assertThat(savedMember).isNotNull();
        assertThat(savedMember.getMemberGrade().getName()).isEqualTo(grade.getName());
    }

    @Test
    void findById() throws Exception {
        //given
        entityManager.persist(grade);
        Member savedMember = entityManager.persist(member);

        //when
        Optional<Member> optionalMember = repository.findById(savedMember.getId());

        //then
        assertThat(optionalMember).isPresent();
        assertThat(optionalMember.get().getName()).isEqualTo(savedMember.getName());
    }
}
