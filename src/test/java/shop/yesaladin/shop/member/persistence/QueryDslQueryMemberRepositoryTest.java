package shop.yesaladin.shop.member.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
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
        assertThat(optionalMember.get().isWithdrawal()).isFalse();
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
        assertThat(optionalMember.get().isWithdrawal()).isFalse();
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
        assertThat(optionalMember.get().isWithdrawal()).isFalse();
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
        assertThat(optionalMember.get().isWithdrawal()).isFalse();
    }

    @Test
    void findMemberByPhone() {
        //given
        entityManager.persist(member);

        //when
        Optional<Member> optionalMember = queryMemberRepository.findMemberByPhone(member.getPhone());

        //then
        assertThat(optionalMember).isPresent();
        assertThat(optionalMember.get().getPhone()).isEqualTo(member.getPhone());
        assertThat(optionalMember.get().isWithdrawal()).isFalse();
    }

    @Test
    void findMembersByName() {
        //given
        entityManager.persist(member);

        //when
        Page<Member> memberList = queryMemberRepository.findMembersByName(
                member.getName(),
                0,
                10
        );

        //then
        assertThat(memberList.getTotalElements()).isEqualTo(1);
        assertThat(memberList.getContent().get(0).getName()).isEqualTo(member.getName());
        assertThat(memberList.getContent().get(0).isWithdrawal()).isFalse();
    }

    @Test
    void findMembersBySignUpDate() {
        //given
        entityManager.persist(member);

        //when
        Page<Member> memberList = queryMemberRepository.findMembersBySignUpDate(
                member.getSignUpDate(),
                0,
                10
        );

        //then
        assertThat(memberList.getTotalElements()).isEqualTo(1);
        assertThat(memberList.getContent().get(0).getSignUpDate()).isEqualTo(member.getSignUpDate());
        assertThat(memberList.getContent().get(0).isWithdrawal()).isFalse();
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