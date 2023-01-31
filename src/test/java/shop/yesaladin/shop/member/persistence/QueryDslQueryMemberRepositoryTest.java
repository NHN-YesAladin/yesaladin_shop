package shop.yesaladin.shop.member.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.member.dummy.MemberDummy;
import shop.yesaladin.shop.order.dto.MemberOrderResponseDto;

@Transactional
@SpringBootTest
class QueryDslQueryMemberRepositoryTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    QueryDslQueryMemberRepository queryMemberRepository;

    String loginId = "user@1";
    Member member;
    Member memberWithLoginId;
    MemberAddress defaultMemberAddress;
    MemberAddress memberAddress;

    @BeforeEach
    void setUp() {
        member = MemberDummy.dummy();

        memberWithLoginId = MemberDummy.dummyWithLoginId(loginId);
        defaultMemberAddress = MemberAddress.builder().member(memberWithLoginId)
                .isDeleted(false)
                .address("First")
                .isDefault(true).build();
        memberAddress = MemberAddress.builder().member(memberWithLoginId)
                .isDeleted(false)
                .address("Second")
                .isDefault(false).build();

        entityManager.persist(memberWithLoginId);
        entityManager.persist(defaultMemberAddress);
        entityManager.persist(memberAddress);
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

    @Test
    void getMemberOrderData() {
        //when
        MemberOrderResponseDto response = queryMemberRepository.getMemberOrderData(loginId);

        //then
        assertThat(response.getName()).isEqualTo(memberWithLoginId.getName());
        assertThat(response.getPhoneNumber()).isEqualTo(memberWithLoginId.getPhone());
        assertThat(response.getAddress()).isEqualTo(defaultMemberAddress.getAddress());
        assertThat(response.getPoint()).isNull();
        assertThat(response.getOrderProducts()).isNull();
    }
}