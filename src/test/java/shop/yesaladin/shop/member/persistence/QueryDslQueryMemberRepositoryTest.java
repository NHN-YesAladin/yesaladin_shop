package shop.yesaladin.shop.member.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.coupon.domain.model.MemberCoupon;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.member.dto.MemberIdDto;
import shop.yesaladin.shop.member.dto.MemberManagerResponseDto;
import shop.yesaladin.shop.member.dto.MemberOrderSheetResponseDto;
import shop.yesaladin.shop.member.dummy.MemberDummy;

@Transactional
@SpringBootTest
@ActiveProfiles("local-test")
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
    void findMemberByPhone() throws Exception {
        //when
        Optional<Member> optionalMember = queryMemberRepository.findMemberByPhone(member.getPhone());

        //then
        assertThat(optionalMember).isPresent();
        assertThat(optionalMember.get().getEmail()).isEqualTo(member.getEmail());
        assertThat(optionalMember.get().isWithdrawal()).isFalse();
    }

    @Test
    void findMemberManagers() {
        //when
        Page<MemberManagerResponseDto> memberList = queryMemberRepository.findMemberManagers(
                PageRequest.of(0, 1)
        );
        //then
        assertThat(memberList.getTotalElements()).isEqualTo(2);
        assertThat(memberList.getContent()).hasSize(1);
        assertThat(memberList.getContent().get(0).getLoginId().contains(member.getLoginId())).isTrue();
    }

    @Test
    void findMemberManagersByLoginId() {
        //when
        Page<MemberManagerResponseDto> memberList = queryMemberRepository.findMemberManagersByLoginId(
                member.getLoginId().substring(0, 1),
                PageRequest.of(0, 1)
        );
        //then
        assertThat(memberList.getTotalElements()).isEqualTo(1);
        assertThat(memberList.getContent()).hasSize(1);
        assertThat(memberList.getContent().get(0).getLoginId().contains(member.getLoginId())).isTrue();
    }

    @Test
    void findMemberManagersByNickname() {
        //when
        Page<MemberManagerResponseDto> memberList = queryMemberRepository.findMemberManagersByNickname(
                member.getNickname().substring(0, 1),
                PageRequest.of(0, 1)
        );
        //then
        assertThat(memberList.getTotalElements()).isEqualTo(1);
        assertThat(memberList.getContent()).hasSize(1);
        assertThat(memberList.getContent().get(0).getNickname().contains(member.getNickname())).isTrue();
    }

    @Test
    void findMemberManagersByPhone() {
        //when
        Page<MemberManagerResponseDto> memberList = queryMemberRepository.findMemberManagersByPhone(
                member.getPhone().substring(0, 1),
                PageRequest.of(0, 1)
        );
        //then
        assertThat(memberList.getTotalElements()).isEqualTo(2);
        assertThat(memberList.getContent()).hasSize(1);
        assertThat(memberList.getContent().get(0).getPhone().contains(member.getPhone())).isTrue();
    }

    @Test
    void findMemberManagersByName() {
        //when
        Page<MemberManagerResponseDto> memberList = queryMemberRepository.findMemberManagersByName(
                member.getName().substring(0, 1),
                PageRequest.of(0, 1)
                );
        //then
        assertThat(memberList.getTotalElements()).isEqualTo(1);
        assertThat(memberList.getContent()).hasSize(1);
        assertThat(memberList.getContent().get(0).getName().contains(member.getName())).isTrue();
    }

    @Test
    void findMemberManagersBySignUpDate() {
        //when
        Page<MemberManagerResponseDto> memberList = queryMemberRepository.findMemberManagersBySignUpDate(
                member.getSignUpDate(),
                PageRequest.of(0, 1)
        );

        //then
        assertThat(memberList.getTotalElements()).isEqualTo(2);
        assertThat(memberList.getContent()).hasSize(1);
        assertThat(memberList.getContent().get(0).getSignUpDate()).isEqualTo(
                member.getSignUpDate());
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
        for (int i = 0; i < 5; i++) {
            String uuid = UUID.randomUUID().toString();
            String guuid = UUID.randomUUID().toString();
            MemberCoupon memberCoupon = MemberCoupon.builder()
                    .member(memberWithLoginId)
                    .couponCode(uuid)
                    .couponGroupCode(guuid)
                    .expirationDate(LocalDate.now().plusMonths(1))
                    .build();
            entityManager.persist(memberCoupon);
        }
        //when
        Optional<MemberOrderSheetResponseDto> response = queryMemberRepository.getMemberOrderData(
                loginId);

        //then
        assertThat(response).isPresent();
        assertThat(response.get().getName()).isEqualTo(memberWithLoginId.getName());
        assertThat(response.get().getPhoneNumber()).isEqualTo(memberWithLoginId.getPhone());
        assertThat(response.get().getCouponCount()).isEqualTo(5);
    }

    @Test
    void countTotalMembers() throws Exception {
        //when
        Long count = queryMemberRepository.countTotalMembers();

        //then
        assertThat(count).isEqualTo(2L);
    }

    @Test
    void countBlockedMembers() throws Exception {
        //when
        Long count = queryMemberRepository.countBlockedMembers();

        //then
        assertThat(count).isZero();
    }

    @Test
    void countWithdrawMembers() throws Exception {
        //when
        Long count = queryMemberRepository.countWithdrawMembers();

        //then
        assertThat(count).isZero();
    }

    @Test
    void countWhiteMembers() throws Exception {
        //when
        Long count = queryMemberRepository.countWhiteMembers();

        //then
        assertThat(count).isEqualTo(2L);
    }

    @Test
    void countBronzeMembers() throws Exception {
        //when
        Long count = queryMemberRepository.countBronzeMembers();

        //then
        assertThat(count).isZero();
    }

    @Test
    void countSilverMembers() throws Exception {
        //when
        Long count = queryMemberRepository.countSilverMembers();

        //then
        assertThat(count).isZero();
    }

    @Test
    void countGoldMembers() throws Exception {
        //when
        Long count = queryMemberRepository.countGoldMembers();

        //then
        assertThat(count).isZero();
    }

    @Test
    void countPlatinumMembers() throws Exception {
        //when
        Long count = queryMemberRepository.countPlatinumMembers();

        //then
        assertThat(count).isZero();
    }
}