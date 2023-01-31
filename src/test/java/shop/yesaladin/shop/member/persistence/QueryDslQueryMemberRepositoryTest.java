package shop.yesaladin.shop.member.persistence;

import static org.assertj.core.api.Assertions.*;

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
    MemberAddress defaultMemberAddress;
    MemberAddress memberAddress;
    @BeforeEach
    void setUp() {
        member = MemberDummy.dummyWithLoginId(loginId);
        defaultMemberAddress = MemberAddress.builder().member(member)
                .isDeleted(false)
                .address("First")
                .isDefault(true).build();
        memberAddress = MemberAddress.builder().member(member)
                .isDeleted(false)
                .address("Second")
                .isDefault(false).build();

        entityManager.persist(member);
        entityManager.persist(defaultMemberAddress);
        entityManager.persist(memberAddress);
    }

    @Test
    void getMemberOrderData() {
        //when
        MemberOrderResponseDto response = queryMemberRepository.getMemberOrderData(loginId);

        //then
        assertThat(response.getName()).isEqualTo(member.getName());
        assertThat(response.getPhoneNumber()).isEqualTo(member.getPhone());
        assertThat(response.getAddress()).isEqualTo(defaultMemberAddress.getAddress());
        assertThat(response.getPoint()).isNull();
        assertThat(response.getOrderProducts()).isNull();
    }
}