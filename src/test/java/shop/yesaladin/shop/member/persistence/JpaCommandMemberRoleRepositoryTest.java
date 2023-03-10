package shop.yesaladin.shop.member.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberRole;
import shop.yesaladin.shop.member.domain.model.Role;
import shop.yesaladin.shop.member.dummy.MemberDummy;
import shop.yesaladin.shop.member.dummy.MemberRoleDummy;
import shop.yesaladin.shop.member.dummy.RoleDummy;

@DataJpaTest
@ActiveProfiles("local-test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaCommandMemberRoleRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    JpaCommandMemberRoleRepository memberRoleRepository;

    private Member member;
    private Role role;
    private MemberRole memberRole;

    @BeforeEach
    void setUp() {
        role = RoleDummy.dummy();
    }

    @Test
    @Disabled
    void save() throws Exception {
        //given
        member = MemberDummy.dummy();

        Member savedMember = entityManager.persist(member);
        Role savedRole = entityManager.persist(role);
        memberRole = MemberRoleDummy.dummy(savedMember, savedRole);

        //when
        MemberRole savedMemberRole = memberRoleRepository.save(memberRole);

        //then
        assertThat(savedMemberRole.getRole().getName()).isEqualTo(role.getName());
        assertThat(savedMemberRole.getMember().getName()).isEqualTo(member.getName());
    }
}