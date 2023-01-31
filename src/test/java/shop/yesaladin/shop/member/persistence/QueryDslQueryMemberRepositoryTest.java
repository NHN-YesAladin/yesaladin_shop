package shop.yesaladin.shop.member.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.dummy.MemberDummy;

@Transactional
@SpringBootTest
class QueryDslQueryMemberRepositoryTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private QueryDslQueryMemberRepository queryMemberRepository;

    Member member;

    @BeforeEach
    void setUp() {
        member = MemberDummy.dummy();
    }

    @Test
    void findById() throws Exception {
        //given
        entityManager.persist(member);

        //when
        Optional<Member> optionalMember = queryMemberRepository.findById(member.getId());

        //then
        assertThat(optionalMember).isPresent();
        assertThat(optionalMember.get().getName()).isEqualTo(member.getName());
    }

    @Test
    void findMemberByNickname() throws Exception {
        //given
        entityManager.persist(member);

        //when
        Optional<Member> optionalMember = queryMemberRepository.findMemberByNickname(member.getNickname());

        //then
        assertThat(optionalMember).isPresent();
        assertThat(optionalMember.get().getNickname()).isEqualTo(member.getNickname());
    }

    @Test
    void findMemberByLoginId() throws Exception {
        //given
        entityManager.persist(member);

        //when
        Optional<Member> optionalMember = queryMemberRepository.findMemberByLoginId(member.getLoginId());

        //then
        assertThat(optionalMember).isPresent();
        assertThat(optionalMember.get().getLoginId()).isEqualTo(member.getLoginId());
    }

    @Test
    void findMemberByEmail() throws Exception {
        //given
        entityManager.persist(member);

        //when
        Optional<Member> optionalMember = queryMemberRepository.findMemberByEmail(member.getEmail());

        //then
        assertThat(optionalMember).isPresent();
        assertThat(optionalMember.get().getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    void existsMemberByLoginId() throws Exception {
        //given
        entityManager.persist(member);

        //when
        boolean result = queryMemberRepository.existsMemberByLoginId(member.getLoginId());

        //then
        assertThat(result).isTrue();
    }

    @Test
    void existsMemberByNickname() throws Exception {
        //given
        entityManager.persist(member);

        //when
        boolean result = queryMemberRepository.existsMemberByNickname(member.getNickname());

        //then
        assertThat(result).isTrue();
    }

    @Test
    void existsMemberByEmail() throws Exception {
        //given
        entityManager.persist(member);

        //when
        boolean result = queryMemberRepository.existsMemberByEmail(member.getEmail());

        //then
        assertThat(result).isTrue();
    }

    @Test
    void existsMemberByPhone() throws Exception {
        //given
        entityManager.persist(member);

        //when
        boolean result = queryMemberRepository.existsMemberByPhone(member.getPhone());

        //then
        assertThat(result).isTrue();
    }
}