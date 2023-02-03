package shop.yesaladin.shop.member.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
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
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.member.dto.MemberIdDto;
import shop.yesaladin.shop.member.dummy.MemberDummy;
import shop.yesaladin.shop.order.dto.OrderSheetResponseDto;

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
        entityManager.persist(member);

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
        //when
        Optional<Member> optionalMember = queryMemberRepository.findById(member.getId());

        //then
        assertThat(optionalMember).isPresent();
        assertThat(optionalMember.get().getName()).isEqualTo(member.getName());
        assertThat(optionalMember.get().isWithdrawal()).isFalse();
    }

    @Test
    void findMemberByNickname() throws Exception {
        //when
        Optional<Member> optionalMember = queryMemberRepository.findMemberByNickname(member.getNickname());

        //then
        assertThat(optionalMember).isPresent();
        assertThat(optionalMember.get().getNickname()).isEqualTo(member.getNickname());
        assertThat(optionalMember.get().isWithdrawal()).isFalse();
    }

    @Test
    void findMemberByLoginId() throws Exception {
        //when
        Optional<Member> optionalMember = queryMemberRepository.findMemberByLoginId(member.getLoginId());

        //then
        assertThat(optionalMember).isPresent();
        assertThat(optionalMember.get().getLoginId()).isEqualTo(member.getLoginId());
        assertThat(optionalMember.get().isWithdrawal()).isFalse();
    }

    @Test
    void findMemberByEmail() throws Exception {
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
        assertThat(memberList.getContent()
                .get(0)
                .getSignUpDate()).isEqualTo(member.getSignUpDate());
        assertThat(memberList.getContent().get(0).isWithdrawal()).isFalse();
    }

    @Test
    void findMembersByBirthday() {
        //given
        LocalDate now = LocalDate.now();
        Member dummyMember = MemberDummy.dummyWithBirthday(
                now.getMonthValue(),
                now.getDayOfMonth()
        );
        entityManager.persist(dummyMember);

        //when
        List<MemberIdDto> members = queryMemberRepository.findMemberIdsByBirthday(
                now.getMonthValue(),
                now.getDayOfMonth()
        );

        //then
        assertThat(members).hasSize(1);
    }

    @Test
    void existsMemberByLoginId() throws Exception {
        //when
        boolean result = queryMemberRepository.existsMemberByLoginId(member.getLoginId());

        //then
        assertThat(result).isTrue();
    }

    @Test
    void existsMemberByNickname() throws Exception {
        //when
        boolean result = queryMemberRepository.existsMemberByNickname(member.getNickname());

        //then
        assertThat(result).isTrue();
    }

    @Test
    void existsMemberByEmail() throws Exception {
        //when
        boolean result = queryMemberRepository.existsMemberByEmail(member.getEmail());

        //then
        assertThat(result).isTrue();
    }

    @Test
    void existsMemberByPhone() throws Exception {
        //when
        boolean result = queryMemberRepository.existsMemberByPhone(member.getPhone());

        //then
        assertThat(result).isTrue();
    }

    @Test
    void getMemberOrderData() {
        //when
        Optional<OrderSheetResponseDto> response = queryMemberRepository.getMemberOrderData(loginId);

        //then
        assertThat(response).isPresent();
        assertThat(response.get().getName()).isEqualTo(memberWithLoginId.getName());
        assertThat(response.get().getPhoneNumber()).isEqualTo(memberWithLoginId.getPhone());
        assertThat(response.get().getAddress()).isEqualTo(defaultMemberAddress.getAddress());
        assertThat(response.get().getPoint()).isNull();
        assertThat(response.get().getOrderProducts()).isNull();
    }
}