package shop.yesaladin.shop.member.persistence;

import static org.assertj.core.api.Assertions.assertThat;

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
import shop.yesaladin.shop.member.domain.repository.QueryMemberAddressRepository;
import shop.yesaladin.shop.order.persistence.dummy.DummyMember;

@Transactional
@SpringBootTest
@ActiveProfiles("local-test")
class QueryDslCommandMemberAddressRepositoryTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    QueryDslCommandMemberAddressRepository commandMemberAddressRepository;
    @Autowired
    QueryMemberAddressRepository queryMemberAddressRepository;

    String address = "Gwangju";
    boolean isDefault = false;
    Member member;

    MemberAddress memberAddress;

    @BeforeEach
    void setUp() {
        member = DummyMember.member();

        entityManager.persist(member);

        memberAddress = MemberAddress.builder()
                .address(address)
                .isDefault(isDefault)
                .member(member)
                .build();
    }

    @Test
    void save() {
        //when
        MemberAddress savedMemberAddress = commandMemberAddressRepository.save(memberAddress);

        //then
        assertThat(savedMemberAddress.getAddress()).isEqualTo(address);
        assertThat(savedMemberAddress.isDefault()).isEqualTo(isDefault);
        assertThat(savedMemberAddress.getMember()).isEqualTo(member);
    }

    @Test
    void deleteById() {
        //given
        entityManager.persist(memberAddress);
        Long id = memberAddress.getId();

        assertThat(queryMemberAddressRepository.getById(id)).isPresent();
        //when
        commandMemberAddressRepository.deleteById(id);
        //then
        assertThat(queryMemberAddressRepository.getById(id)).isEmpty();
    }
}