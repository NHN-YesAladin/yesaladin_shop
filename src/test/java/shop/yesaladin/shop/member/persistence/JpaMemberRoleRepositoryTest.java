package shop.yesaladin.shop.member.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberRole;
import shop.yesaladin.shop.member.domain.model.Role;
import shop.yesaladin.shop.member.dummy.MemberDummy;
import shop.yesaladin.shop.member.dummy.MemberRoleDummy;
import shop.yesaladin.shop.member.dummy.RoleDummy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaMemberRoleRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    JpaMemberRoleRepository memberRoleRepository;

    private Member member;
    private Role role;
    private MemberRole memberRole;

    @BeforeEach
    void setUp() {
        role = RoleDummy.dummy();
    }

    @Test
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

    @Test
    void findById() throws Exception {
        //given
        member = MemberDummy.dummy();
        Member savedMember = entityManager.persist(member);
        Role savedRole = entityManager.persist(role);

        memberRole = MemberRoleDummy.dummy(savedMember, savedRole);

        //when
        memberRoleRepository.save(memberRole);

        //when
        Optional<MemberRole> optionalMemberRole = memberRoleRepository.findById(memberRole.getId());

        //then
        assertThat(optionalMemberRole).isPresent();
        assertThat(optionalMemberRole.get().getMember().getName()).isEqualTo(memberRole.getMember()
                .getName());
        assertThat(optionalMemberRole.get().getRole().getName()).isEqualTo(memberRole.getRole()
                .getName());
    }
}