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
import shop.yesaladin.shop.member.domain.model.MemberRole;
import shop.yesaladin.shop.member.domain.model.Role;
import shop.yesaladin.shop.member.dummy.MemberDummy;
import shop.yesaladin.shop.member.dummy.MemberRoleDummy;
import shop.yesaladin.shop.member.dummy.RoleDummy;

@Transactional
@SpringBootTest
@ActiveProfiles("local-test")
class QueryDslQueryMemberRoleRepositoryTest {

    @PersistenceContext
    EntityManager entityManager;
    Member member;
    Role role;
    MemberRole memberRole;
    @Autowired
    private QueryDslQueryMemberRoleRepository queryMemberRoleRepository;

    @BeforeEach
    void setUp() {
        member = MemberDummy.dummy();
        role = RoleDummy.dummy();
    }

    @Test
    void findById() throws Exception {
        //given
        member = MemberDummy.dummy();
        entityManager.persist(member);
        entityManager.persist(role);

        memberRole = MemberRoleDummy.dummy(member, role);

        entityManager.persist(memberRole);

        //when
        Optional<MemberRole> optionalMemberRole = queryMemberRoleRepository.findById(memberRole.getId());

        //then
        assertThat(optionalMemberRole).isPresent();
        assertThat(optionalMemberRole.get().getMember().getName()).isEqualTo(memberRole.getMember()
                .getName());
        assertThat(optionalMemberRole.get().getRole().getName()).isEqualTo(memberRole.getRole()
                .getName());
    }

    @Test
    void findMemberRolesByMemberId() throws Exception {
        //given
        member = MemberDummy.dummy();
        entityManager.persist(member);
        entityManager.persist(role);

        memberRole = MemberRoleDummy.dummy(member, role);

        entityManager.persist(memberRole);

        //when
        List<String> roles = queryMemberRoleRepository.findMemberRolesByMemberId(member.getId());

        //then
        assertThat(roles).hasSize(1);
    }
}