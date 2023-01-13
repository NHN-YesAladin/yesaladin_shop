package shop.yesaladin.shop.member.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shop.yesaladin.shop.member.domain.model.MemberGenderCode;
import shop.yesaladin.shop.member.domain.model.SearchedMember;
import shop.yesaladin.shop.member.domain.repository.SearchMemberRepository;

@SpringBootTest
class ElasticMemberRepositoryTest {

    @Autowired
    private SearchMemberRepository searchMemberRepository;
    private SearchedMember dummySearchedMember;

    @BeforeEach
    void setUp() {
        dummySearchedMember = SearchedMember.builder()
                .id(1L)
                .nickname("nickname")
                .name("name")
                .loginId("loginId")
                .phone("00000000000")
                .birthYear(2001)
                .birthMonth(1)
                .birthDay(21)
                .email("nhn@gmail.com")
                .signUpDate(LocalDate.of(2010, 12, 2))
                .withdrawalDate(null)
                .isWithdrawal(false)
                .isBlocked(true)
                .grade("화이트")
                .point(1000L)
                .gender(MemberGenderCode.MALE)
                .build();
    }

    @Test
    void saveNewMember() {
        SearchedMember result = searchMemberRepository.save(dummySearchedMember);

        assertThat(result.getId()).isEqualTo(dummySearchedMember.getId());
        assertThat(result.getNickname()).isEqualTo(dummySearchedMember.getNickname());
        assertThat(result.getName()).isEqualTo(dummySearchedMember.getName());
        assertThat(result.getLoginId()).isEqualTo(dummySearchedMember.getLoginId());
        assertThat(result.getPhone()).isEqualTo(dummySearchedMember.getPhone());
        assertThat(result.getBirthYear()).isEqualTo(dummySearchedMember.getBirthYear());
        assertThat(result.getBirthMonth()).isEqualTo(dummySearchedMember.getBirthMonth());
        assertThat(result.getBirthDay()).isEqualTo(dummySearchedMember.getBirthDay());
        assertThat(result.getEmail()).isEqualTo(dummySearchedMember.getEmail());
        assertThat(result.getSignUpDate()).isEqualTo(dummySearchedMember.getSignUpDate());
        assertThat(result.getPoint()).isEqualTo(dummySearchedMember.getPoint());
        assertThat(result.getGrade()).isEqualTo(dummySearchedMember.getGrade());
        assertThat(result.getGender()).isEqualTo(dummySearchedMember.getGender());

        searchMemberRepository.deleteByLoginId(result.getLoginId());
    }

    @Test
    void updateMember() {
        String newNickname = "new";
        SearchedMember savedDummy = searchMemberRepository.save(dummySearchedMember);
        SearchedMember updatedDummy = SearchedMember.builder()
                .id(savedDummy.getId())
                .nickname(newNickname)
                .name(savedDummy.getName())
                .loginId(savedDummy.getLoginId())
                .phone(savedDummy.getPhone())
                .birthYear(savedDummy.getBirthYear())
                .birthMonth(savedDummy.getBirthMonth())
                .birthDay(savedDummy.getBirthDay())
                .email(savedDummy.getEmail())
                .signUpDate(savedDummy.getSignUpDate())
                .isWithdrawal(savedDummy.isWithdrawal())
                .isBlocked(savedDummy.isBlocked())
                .point(savedDummy.getPoint())
                .grade(savedDummy.getGrade())
                .gender(savedDummy.getGender())
                .build();
        SearchedMember result = searchMemberRepository.save(updatedDummy);

        assertThat(result.getId()).isEqualTo(updatedDummy.getId());
        assertThat(result.getNickname()).isEqualTo(updatedDummy.getNickname());
        assertThat(result.getName()).isEqualTo(updatedDummy.getName());
        assertThat(result.getLoginId()).isEqualTo(updatedDummy.getLoginId());
        assertThat(result.getPhone()).isEqualTo(updatedDummy.getPhone());
        assertThat(result.getBirthYear()).isEqualTo(updatedDummy.getBirthYear());
        assertThat(result.getBirthMonth()).isEqualTo(updatedDummy.getBirthMonth());
        assertThat(result.getBirthDay()).isEqualTo(updatedDummy.getBirthDay());
        assertThat(result.getEmail()).isEqualTo(updatedDummy.getEmail());
        assertThat(result.getSignUpDate()).isEqualTo(updatedDummy.getSignUpDate());
        assertThat(result.getPoint()).isEqualTo(updatedDummy.getPoint());
        assertThat(result.getGrade()).isEqualTo(updatedDummy.getGrade());
        assertThat(result.getGender()).isEqualTo(updatedDummy.getGender());

        searchMemberRepository.deleteByLoginId(result.getLoginId());
    }

    @Test
    void testDeleteByLoginId() {
        SearchedMember savedMember = searchMemberRepository.save(dummySearchedMember);
        searchMemberRepository.deleteByLoginId(savedMember.getLoginId());
        Optional<SearchedMember> optional = searchMemberRepository.searchByLoginId(savedMember.getLoginId());
        assertThat(optional).isNotPresent();
    }

    @Test
    void testSearchByPhone() {
        searchMemberRepository.save(dummySearchedMember);

        Optional<SearchedMember> result = searchMemberRepository.searchByPhone(dummySearchedMember.getPhone());

        assertThat(result).isPresent();
        assertThat(result.get().getPhone()).isEqualTo(dummySearchedMember.getPhone());

        searchMemberRepository.deleteByLoginId(result.get().getLoginId());
    }

    @Test
    void testSearchByNickname() {
        searchMemberRepository.save(dummySearchedMember);

        Optional<SearchedMember> result = searchMemberRepository.searchByNickname(dummySearchedMember.getNickname());

        assertThat(result).isPresent();
        assertThat(result.get().getNickname()).isEqualTo(dummySearchedMember.getNickname());

        searchMemberRepository.deleteByLoginId(result.get().getLoginId());
    }
    @Test
    void testSearchAllByName() {
        searchMemberRepository.save(dummySearchedMember);

        System.out.println(dummySearchedMember.getName());
        List<SearchedMember> results = searchMemberRepository.searchAllByName(dummySearchedMember.getName());
        assertThat(results).hasSize(1);

        SearchedMember result = results.get(0);
        assertThat(result.getName()).isEqualTo(dummySearchedMember.getName());

        searchMemberRepository.deleteByLoginId(result.getLoginId());
    }

    @Test
    void testSearchBySignUpDate() {
        searchMemberRepository.save(dummySearchedMember);

        List<SearchedMember> results = searchMemberRepository.searchBySignUpDate(dummySearchedMember.getSignUpDate());
        assertThat(results).hasSize(1);

        SearchedMember result = results.get(0);
        assertThat(result.getSignUpDate()).isEqualTo(dummySearchedMember.getSignUpDate());

        searchMemberRepository.deleteByLoginId(result.getLoginId());
    }
}