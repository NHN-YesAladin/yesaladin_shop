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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.member.dto.MemberAddressResponseDto;
import shop.yesaladin.shop.member.dummy.MemberDummy;

@Transactional
@SpringBootTest
@ActiveProfiles("local-test")
class QueryDslQueryMemberAddressRepositoryTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    QueryDslQueryMemberAddressRepository queryMemberAddressRepository;
    private final String address = "Gwang-ju buk-gu yongbong-dong";
    private final boolean isDefault = false;
    private final String loginId = "user@1";
    private Member member;
    private MemberAddress memberAddress;

    @BeforeEach
    void setUp() {
        member = MemberDummy.dummyWithLoginId(loginId);
        entityManager.persist(member);

        boolean isDeleted = false;
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
    void findById() {
        //given
        long id = 1;

        //when
        Optional<MemberAddress> result = queryMemberAddressRepository.findById(id);

        //then
        assertThat(result).isEmpty();
    }

    @Test
    void getByMember() {
        //when
        List<MemberAddressResponseDto> memberAddressList = queryMemberAddressRepository.getByLoginId(
                loginId);

        //then
        assertThat(memberAddressList).isEmpty();
    }

    @Test
    void getById() {
        //given
        entityManager.persist(memberAddress);
        Long id = memberAddress.getId();

        //when
        Optional<MemberAddressResponseDto> foundMemberAddress = queryMemberAddressRepository.getById(
                id);

        //then
        assertThat(foundMemberAddress).isPresent();
        assertThat(foundMemberAddress.get().getId()).isEqualTo(id);
        assertThat(foundMemberAddress.get().getAddress()).isEqualTo(address);
        assertThat(foundMemberAddress.get().getIsDefault()).isEqualTo(isDefault);
    }

    @Test
    void getByMemberIdAndMemberAddressId() {
        //given
        entityManager.persist(memberAddress);
        String loginId = member.getLoginId();
        long addressId = memberAddress.getId();

        //when
        Optional<MemberAddressResponseDto> result = queryMemberAddressRepository.getByLoginIdAndMemberAddressId(
                loginId,
                addressId
        );

        //then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(addressId);
        assertThat(result.get().getAddress()).isEqualTo(address);
        assertThat(result.get().getIsDefault()).isEqualTo(isDefault);
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

    @Test
    void findByLoginId() {
        //when
        List<MemberAddress> result = queryMemberAddressRepository.findByLoginId("dfw@q");

        //then
        assertThat(result).isEmpty();
    }

    @Test
    void findByLoginIdAndMemberAddressId() {
        //given
        entityManager.persist(memberAddress);

        String loginId = "user@1";
        long memberAddressId = memberAddress.getId();

        //when
        Optional<MemberAddress> result = queryMemberAddressRepository.findByLoginIdAndMemberAddressId(loginId, memberAddressId);

        //then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(memberAddressId);
        assertThat(result.get().getMember().getLoginId()).isEqualTo(loginId);
        assertThat(result.get().isDeleted()).isFalse();
    }
}