package shop.yesaladin.shop.member.persistence;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import shop.yesaladin.shop.member.domain.model.MemberGenderCode;
import shop.yesaladin.shop.member.domain.model.SearchedMember;
import shop.yesaladin.shop.member.domain.repository.SearchMemberRepository;
import shop.yesaladin.shop.member.dto.MemberInfoRequest;

@TestConfiguration
@SpringBootTest
class ElasticMemberRepositoryTest {

    @Autowired
    SearchMemberRepository elasticMemberRepository;

    private static MemberInfoRequest dummy;

    @BeforeAll
    static void setUp() {
        dummy = MemberInfoRequest.builder()
                .id(1L)
                .loginId("id1")
                .nickname("nick1")
                .name("김선홍")
                .phone("0100000000")
                .birthYear(2017)
                .birthMonth(1)
                .birthDay(29)
                .signUpDate(LocalDate.of(2019, 3, 28))
                .isWithdrawal(false)
                .isBlocked(false)
                .email("nhn@acadyme.com")
                .point(1000L)
                .grade("브론즈")
                .gender(MemberGenderCode.MALE.name())
                .build();
    }

    @Test
    @DisplayName("save 테스트")
    void testSave() {
        SearchedMember searchedMember = elasticMemberRepository.save(dummy.toSearchedMember());

        assertThat(searchedMember.getId()).isEqualTo(dummy.getId());
        assertThat(searchedMember.getLoginId()).isEqualTo(dummy.getLoginId());
        assertThat(searchedMember.getNickname()).isEqualTo(dummy.getNickname());
        assertThat(searchedMember.getPhone()).isEqualTo(dummy.getPhone());
        assertThat(searchedMember.getBirthYear()).isEqualTo(dummy.getBirthYear());
        assertThat(searchedMember.getBirthMonth()).isEqualTo(dummy.getBirthMonth());
        assertThat(searchedMember.getBirthDay()).isEqualTo(dummy.getBirthDay());
        assertThat(searchedMember.getSignUpDate()).isEqualTo(dummy.getSignUpDate());
        assertThat(searchedMember.getWithdrawalDate()).isEqualTo(dummy.getWithdrawalDate());
        assertThat(searchedMember.isWithdrawal()).isEqualTo(dummy.isWithdrawal());
        assertThat(searchedMember.isBlocked()).isEqualTo(dummy.isBlocked());
        assertThat(searchedMember.getEmail()).isEqualTo(dummy.getEmail());
        assertThat(searchedMember.getPoint()).isEqualTo(dummy.getPoint());
        assertThat(searchedMember.getGrade()).isEqualTo(dummy.getGrade());
        assertThat(searchedMember.getGender().name()).isEqualTo(dummy.getGender());

        elasticMemberRepository.deleteByLoginId(searchedMember.getLoginId());
    }

    @Test
    @DisplayName("delete 테스트")
    void testDelete() {
        elasticMemberRepository.save(dummy.toSearchedMember());
        elasticMemberRepository.deleteByLoginId(dummy.getLoginId());
        Optional<SearchedMember> result = elasticMemberRepository.findByLoginId(dummy.getLoginId());

        assertThat(result).isNotPresent();
    }

    @Test
    @DisplayName("LoginId를 이용해 유저의 유무를 확인한다.(없는 경우)")
    void testExistByLoginIdNonExist() {
        assertThat(elasticMemberRepository.existsByLoginId("test")).isFalse();
    }

    @Test
    @DisplayName("LoginId를 이용해 유저의 유무를 확인한다.(있는 경우)")
    void testExistByLoginIdExist() {
        elasticMemberRepository.save(dummy.toSearchedMember());
        assertThat(elasticMemberRepository.existsByLoginId(dummy.getLoginId())).isTrue();
        elasticMemberRepository.deleteByLoginId(dummy.getLoginId());
    }

    @Test
    @DisplayName("Nickname을 이용해 유저의 유무를 확인한다.(없는 경우)")
    void testExistsByNicknameNonExist() {
        assertThat(elasticMemberRepository.existsByNickname(dummy.getNickname())).isFalse();
    }

    @Test
    @DisplayName("Nickname을 이용해 유저의 유무를 확인한다.(없는 경우)")
    void testExistsByNicknameExist() {
        elasticMemberRepository.save(dummy.toSearchedMember());
        assertThat(elasticMemberRepository.existsByNickname(dummy.getNickname())).isTrue();
        elasticMemberRepository.deleteByLoginId(dummy.getLoginId());
    }

    @Test
    @DisplayName("해당 핸드폰 번호를 사용하고 있는 유저가 없으면 성공")
    void testExistsByPhoneNotExist() {
        assertThat(elasticMemberRepository.existsByNickname(dummy.getPhone())).isFalse();
    }

    @Test
    @DisplayName("해당 핸드폰 번호를 사용하고 있는 유저가 있으면 성공")
    void testExistsByPhoneYesExist() {
        elasticMemberRepository.save(dummy.toSearchedMember());
        assertThat(elasticMemberRepository.existsByPhone(dummy.getPhone())).isTrue();
        elasticMemberRepository.deleteByLoginId(dummy.getLoginId());
    }

    @Test
    @DisplayName("해당 이메일을 사용하고 없는 유저가 없으면 성공")
    void testExistsByEmailNotExist() {
        assertThat(elasticMemberRepository.existsByEmail(dummy.getEmail())).isFalse();
    }

    @Test
    @DisplayName("해당 핸드폰 번호를 사용하고 있는 유저가 없으면 성공")
    void testExistsByEmailYesExist() {
        elasticMemberRepository.save(dummy.toSearchedMember());
        assertThat(elasticMemberRepository.existsByEmail(dummy.getEmail())).isTrue();
        elasticMemberRepository.deleteByLoginId(dummy.getLoginId());
    }

    @Test
    @DisplayName("개인키인 id가 없을 경우")
    void testExistsByIdNotExist() {
        elasticMemberRepository.existsById(dummy.getId());
    }

    @Test
    @DisplayName("개인키인 id가 존재할 경우")
    void testExistsByIdYesExist() {
        elasticMemberRepository.save(dummy.toSearchedMember());
        assertThat(elasticMemberRepository.existsById(dummy.getId())).isTrue();
        elasticMemberRepository.deleteByLoginId(dummy.getLoginId());
    }

    @Test
    @DisplayName("loginId로 검색 테스트")
    void testFindByLoginId() {
        elasticMemberRepository.save(dummy.toSearchedMember());

        Optional<SearchedMember> result = elasticMemberRepository.findByLoginId(dummy.getLoginId());
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(dummy.getId());
        assertThat(result.get().getLoginId()).isEqualTo(dummy.getLoginId());
        assertThat(result.get().getNickname()).isEqualTo(dummy.getNickname());
        assertThat(result.get().getPhone()).isEqualTo(dummy.getPhone());
        assertThat(result.get().getBirthYear()).isEqualTo(dummy.getBirthYear());
        assertThat(result.get().getBirthMonth()).isEqualTo(dummy.getBirthMonth());
        assertThat(result.get().getBirthDay()).isEqualTo(dummy.getBirthDay());
        assertThat(result.get().getSignUpDate()).isEqualTo(dummy.getSignUpDate());
        assertThat(result.get().getWithdrawalDate()).isEqualTo(dummy.getWithdrawalDate());
        assertThat(result.get().isWithdrawal()).isEqualTo(dummy.isWithdrawal());
        assertThat(result.get().isBlocked()).isEqualTo(dummy.isBlocked());
        assertThat(result.get().getEmail()).isEqualTo(dummy.getEmail());
        assertThat(result.get().getPoint()).isEqualTo(dummy.getPoint());
        assertThat(result.get().getGrade()).isEqualTo(dummy.getGrade());
        assertThat(result.get().getGender().name()).isEqualTo(dummy.getGender());

        elasticMemberRepository.deleteByLoginId(result.get().getLoginId());
    }

    @Test
    @DisplayName("nickname으로 검색 테스트")
    void testFindByNickname() {
        elasticMemberRepository.save(dummy.toSearchedMember());

        Optional<SearchedMember> result = elasticMemberRepository.findByNickname(dummy.getNickname());
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(dummy.getId());
        assertThat(result.get().getLoginId()).isEqualTo(dummy.getLoginId());
        assertThat(result.get().getNickname()).isEqualTo(dummy.getNickname());
        assertThat(result.get().getPhone()).isEqualTo(dummy.getPhone());
        assertThat(result.get().getBirthYear()).isEqualTo(dummy.getBirthYear());
        assertThat(result.get().getBirthMonth()).isEqualTo(dummy.getBirthMonth());
        assertThat(result.get().getBirthDay()).isEqualTo(dummy.getBirthDay());
        assertThat(result.get().getSignUpDate()).isEqualTo(dummy.getSignUpDate());
        assertThat(result.get().getWithdrawalDate()).isEqualTo(dummy.getWithdrawalDate());
        assertThat(result.get().isWithdrawal()).isEqualTo(dummy.isWithdrawal());
        assertThat(result.get().isBlocked()).isEqualTo(dummy.isBlocked());
        assertThat(result.get().getEmail()).isEqualTo(dummy.getEmail());
        assertThat(result.get().getPoint()).isEqualTo(dummy.getPoint());
        assertThat(result.get().getGrade()).isEqualTo(dummy.getGrade());
        assertThat(result.get().getGender().name()).isEqualTo(dummy.getGender());

        elasticMemberRepository.deleteByLoginId(result.get().getLoginId());

    }

    @Test
    @DisplayName("핸드폰으로 검색 테스트")
    void testFindByPhone() {
        elasticMemberRepository.save(dummy.toSearchedMember());

        Optional<SearchedMember> result = elasticMemberRepository.findByPhone(dummy.getPhone());
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(dummy.getId());
        assertThat(result.get().getLoginId()).isEqualTo(dummy.getLoginId());
        assertThat(result.get().getNickname()).isEqualTo(dummy.getNickname());
        assertThat(result.get().getPhone()).isEqualTo(dummy.getPhone());
        assertThat(result.get().getBirthYear()).isEqualTo(dummy.getBirthYear());
        assertThat(result.get().getBirthMonth()).isEqualTo(dummy.getBirthMonth());
        assertThat(result.get().getBirthDay()).isEqualTo(dummy.getBirthDay());
        assertThat(result.get().getSignUpDate()).isEqualTo(dummy.getSignUpDate());
        assertThat(result.get().getWithdrawalDate()).isEqualTo(dummy.getWithdrawalDate());
        assertThat(result.get().isWithdrawal()).isEqualTo(dummy.isWithdrawal());
        assertThat(result.get().isBlocked()).isEqualTo(dummy.isBlocked());
        assertThat(result.get().getEmail()).isEqualTo(dummy.getEmail());
        assertThat(result.get().getPoint()).isEqualTo(dummy.getPoint());
        assertThat(result.get().getGrade()).isEqualTo(dummy.getGrade());
        assertThat(result.get().getGender().name()).isEqualTo(dummy.getGender());

        elasticMemberRepository.deleteByLoginId(result.get().getLoginId());
    }

    @Test
    @DisplayName("이름으로 검색 테스트")
    void testFindAllByName() {
        elasticMemberRepository.save(dummy.toSearchedMember());

        List<SearchedMember> results = elasticMemberRepository.findAllByName(dummy.getName());
        assertThat(results.size()).isEqualTo(1);

        elasticMemberRepository.deleteByLoginId(dummy.getLoginId());
    }

    @Test
    @DisplayName("가입날로 검색 테스트")
    void testFindBySignUpDate() {
        elasticMemberRepository.save(dummy.toSearchedMember());

        List<SearchedMember> results = elasticMemberRepository.findBySignUpDate(dummy.getSignUpDate());
        assertThat(results.size()).isEqualTo(1);

        elasticMemberRepository.deleteByLoginId(dummy.getLoginId());
    }
}