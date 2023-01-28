package shop.yesaladin.shop.member.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
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
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;

@Transactional
@SpringBootTest
class QueryDslQueryMemberAddressRepositoryTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    QueryDslQueryMemberAddressRepository queryMemberAddressRepository;
    private String address = "Gwang-ju buk-gu yongbong-dong";
    private boolean isDefault = false;
    private boolean isDeleted = false;
    private String loginId = "user@1";
    private Member member;
    private MemberAddress memberAddress;

    @BeforeEach
    void setUp() {
        member = MemberDummy.dummyWithLoginId(loginId);
        entityManager.persist(member);

        memberAddress = MemberAddress.builder()
                .address(address)
                .isDefault(isDefault)
                .isDeleted(isDeleted)
                .member(member)
                .build();
    }

    @Test
    void countByLoginId() {
        //given
        entityManager.persist(memberAddress);

        //when
        long result = queryMemberAddressRepository.countByLoginId(loginId);

        //then
        assertThat(result).isEqualTo(1);
    }
    @Test
    void findByMember() {
        //when
        List<MemberAddress> memberAddressList = queryMemberAddressRepository.findByMember(member);

        //then
        assertThat(memberAddressList).isEmpty();
    }

    @Test
    void findById() {
        //given
        entityManager.persist(memberAddress);
        Long id = memberAddress.getId();

        //when
        Optional<MemberAddress> foundMemberAddress = queryMemberAddressRepository.findById(id);

        //then
        assertThat(foundMemberAddress).isPresent();
        assertThat(foundMemberAddress.get().getId()).isEqualTo(id);
        assertThat(foundMemberAddress.get().getAddress()).isEqualTo(address);
        assertThat(foundMemberAddress.get().isDefault()).isEqualTo(isDefault);
        assertThat(foundMemberAddress.get().getMember()).isEqualTo(member);
    }

    @Test
    void getByMemberIdAndMemberAddressId() {
        //given
        entityManager.persist(memberAddress);
        String loginId = member.getLoginId();
        long addressId = memberAddress.getId();

        //when
        Optional<MemberAddress> result = queryMemberAddressRepository.getByLoginIdAndMemberAddressId(
                loginId,
                addressId
        );

        //then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(addressId);
        assertThat(result.get().getAddress()).isEqualTo(address);
        assertThat(result.get().isDeleted()).isEqualTo(isDeleted);
        assertThat(result.get().isDefault()).isEqualTo(isDefault);
        assertThat(result.get().getMember()).isEqualTo(member);

    }

    @Test
    void existByMemberIdAndMemberAddressId() {
        //given
        entityManager.persist(memberAddress);
        String loginId = member.getLoginId();
        long addressId = memberAddress.getId();

        //when
        boolean result = queryMemberAddressRepository.existByLoginIdAndMemberAddressId(
                loginId,
                addressId
        );

        //then
        assertThat(result).isTrue();
    }
}