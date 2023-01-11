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

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaMemberRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    JpaMemberRepository repository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = MemberDummy.dummy();
    }

    @Test
    void save() throws Exception {
        //when
        Member savedMember = repository.save(member);

        //then
        assertThat(savedMember).isNotNull();
        assertThat(savedMember.getMemberGrade().getName()).isEqualTo(MemberGrade.WHITE.getName());
    }

    @Test
    void findById() throws Exception {
        //given
        Member savedMember = entityManager.persist(member);

        //when
        Optional<Member> optionalMember = repository.findById(savedMember.getId());

        //then
        assertThat(optionalMember).isPresent();
        assertThat(optionalMember.get().getName()).isEqualTo(savedMember.getName());
    }

    @Test
    void findMemberByLoginId() throws Exception {
        //given
        Member savedMember = entityManager.persist(member);

        //when
        Optional<Member> optionalMember = repository.findMemberByLoginId(savedMember.getLoginId());

        //then
        assertThat(optionalMember).isPresent();
        assertThat(optionalMember.get().getLoginId()).isEqualTo(savedMember.getLoginId());
    }

    @Test
    void findMemberByNickname() throws Exception {
        //given
        Member savedMember = entityManager.persist(member);

        //when
        Optional<Member> optionalMember = repository.findMemberByNickname(savedMember.getNickname());

        //then
        assertThat(optionalMember).isPresent();
        assertThat(optionalMember.get().getNickname()).isEqualTo(savedMember.getNickname());
    }

    @Test
    void findMemberByEmail() throws Exception {
        //given
        Member savedMember = entityManager.persist(member);

        //when
        Optional<Member> optionalMember = repository.findMemberByEmail(savedMember.getEmail());

        //then
        assertThat(optionalMember).isPresent();
        assertThat(optionalMember.get().getEmail()).isEqualTo(savedMember.getEmail());
    }
}
