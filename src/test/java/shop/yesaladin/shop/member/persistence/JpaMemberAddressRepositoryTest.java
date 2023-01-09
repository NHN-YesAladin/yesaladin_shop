package shop.yesaladin.shop.member.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.member.domain.model.MemberGrade;
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;
import shop.yesaladin.shop.order.persistence.dummy.DummyMemberGrade;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaMemberAddressRepositoryTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    JpaMemberAddressRepository memberAddressRepository;

    private String address = "Gwang-ju buk-gu yongbong-dong";
    private boolean isDefault = false;
    private Member member;
    private MemberAddress memberAddress;

    @BeforeEach
    void setUp() {
        MemberGrade memberGrade = DummyMemberGrade.memberGrade;
        member = DummyMember.member(memberGrade);

        entityManager.persist(memberGrade);
        entityManager.persist(member);

        memberAddress = createMemberAddress();
    }

    @Test
    void save() throws Exception {
        //when
        MemberAddress savedMemberAddress = memberAddressRepository.save(memberAddress);

        //then
        assertThat(savedMemberAddress.getAddress()).isEqualTo(address);
        assertThat(savedMemberAddress.isDefault()).isEqualTo(isDefault);
        assertThat(savedMemberAddress.getMember()).isEqualTo(member);
    }

    @Test
    void deleteById() throws Exception {
        //given
        entityManager.persist(memberAddress);
        Long id = memberAddress.getId();

        //when
        memberAddressRepository.deleteById(id);

        //then
        MemberAddress actual = entityManager.find(MemberAddress.class, id);
        assertThat(actual).isNull();
    }

    @Test
    void findByMemberId() throws Exception {
        //given
        Long memberId = member.getId();

        //when
        List<MemberAddress> memberAddressList = memberAddressRepository.findByMemberId(memberId);

        //then
        assertThat(memberAddressList).hasSize(0);
    }

    @Test
    void findById() throws Exception {
        //given
        entityManager.persist(memberAddress);
        Long id = memberAddress.getId();

        //when
        Optional<MemberAddress> foundMemberAddress = memberAddressRepository.findById(id);

        //then
        assertThat(foundMemberAddress).isPresent();
        assertThat(foundMemberAddress.get().getId()).isEqualTo(id);
        assertThat(foundMemberAddress.get().getAddress()).isEqualTo(address);
        assertThat(foundMemberAddress.get().isDefault()).isEqualTo(isDefault);
        assertThat(foundMemberAddress.get().getMember()).isEqualTo(member);
    }

    private MemberAddress createMemberAddress() {
        return MemberAddress.builder()
                .address(address)
                .isDefault(isDefault)
                .member(member)
                .build();
    }
}