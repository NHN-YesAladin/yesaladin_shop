package shop.yesaladin.shop.member.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import shop.yesaladin.shop.member.domain.model.Role;
import shop.yesaladin.shop.member.dummy.RoleDummy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class JpaRoleRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    JpaRoleRepository repository;

    private Role role;

    @BeforeEach
    void setUp() {
        role = RoleDummy.dummy();
    }

    @Test
    void save() throws Exception {
        //when
        Role savedRole = repository.save(role);

        //then
        assertThat(savedRole.getName()).isEqualTo(role.getName());
    }
    
    @Test
    void findById() throws Exception {
        //given
        Role savedRole = entityManager.persist(role);

        //when
        Optional<Role> optionalRole = repository.findById(savedRole.getId());

        //then
        assertThat(optionalRole).isPresent();
    }
}