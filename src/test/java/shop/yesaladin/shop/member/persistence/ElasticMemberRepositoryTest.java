package shop.yesaladin.shop.member.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shop.yesaladin.shop.member.domain.model.MemberGenderCode;
import shop.yesaladin.shop.member.domain.model.SearchedMember;
import shop.yesaladin.shop.member.domain.repository.SearchMemberRepository;
import shop.yesaladin.shop.member.dto.SearchMemberManagerRequestDto;

@SpringBootTest
class ElasticMemberRepositoryTest {

    @Autowired
    private SearchMemberRepository searchMemberRepository;
    private SearchMemberManagerRequestDto dummyDto;

    @BeforeEach
    void setUp() {
        dummyDto = SearchMemberManagerRequestDto.builder()
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
        //when
        SearchMemberManagerRequestDto result = searchMemberRepository.save(dummyDto.toSearchedMember())
                .toDto();

        //then
        assertThat(result.getId()).isEqualTo(dummyDto.getId());
        assertThat(result.getNickname()).isEqualTo(dummyDto.getNickname());
        assertThat(result.getName()).isEqualTo(dummyDto.getName());
        assertThat(result.getLoginId()).isEqualTo(dummyDto.getLoginId());
        assertThat(result.getPhone()).isEqualTo(dummyDto.getPhone());
        assertThat(result.getBirthYear()).isEqualTo(dummyDto.getBirthYear());
        assertThat(result.getBirthMonth()).isEqualTo(dummyDto.getBirthMonth());
        assertThat(result.getBirthDay()).isEqualTo(dummyDto.getBirthDay());
        assertThat(result.getEmail()).isEqualTo(dummyDto.getEmail());
        assertThat(result.getSignUpDate()).isEqualTo(dummyDto.getSignUpDate());
        assertThat(result.getPoint()).isEqualTo(dummyDto.getPoint());
        assertThat(result.getGrade()).isEqualTo(dummyDto.getGrade());
        assertThat(result.getGender()).isEqualTo(dummyDto.getGender());

        searchMemberRepository.deleteByLoginId(result.getLoginId());
    }

    @Test
    void updateMember() {
        //given
        String newNickname = "new";

        //when
        SearchMemberManagerRequestDto savedDummy = searchMemberRepository.save(dummyDto.toSearchedMember())
                .toDto();
        SearchMemberManagerRequestDto updatedDummy = SearchMemberManagerRequestDto.builder()
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
        SearchMemberManagerRequestDto result = searchMemberRepository.save(updatedDummy.toSearchedMember())
                .toDto();

        //then
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
        //given
        SearchMemberManagerRequestDto savedMember = searchMemberRepository.save(dummyDto.toSearchedMember())
                .toDto();
        //when
        searchMemberRepository.deleteByLoginId(savedMember.getLoginId());

        //then
        Optional<SearchedMember> optional = searchMemberRepository.searchByLoginId(
                savedMember.getLoginId());
        assertThat(optional).isNotPresent();
    }

    @Test
    void testSearchByPhone() {
        //given
        searchMemberRepository.save(dummyDto.toSearchedMember());

        //when
        Optional<SearchMemberManagerRequestDto> result = Optional.of(searchMemberRepository.searchByPhone(
                dummyDto.getPhone()).get().toDto());

        //then
        assertThat(result).isPresent();
        assertThat(result.get().getPhone()).isEqualTo(dummyDto.getPhone());

        searchMemberRepository.deleteByLoginId(result.get().getLoginId());
    }

    @Test
    void testSearchByNickname() {
        //given
        searchMemberRepository.save(dummyDto.toSearchedMember());

        //when
        Optional<SearchMemberManagerRequestDto> result = Optional.of(searchMemberRepository.searchByNickname(
                dummyDto.getNickname()).get().toDto());

        //then
        assertThat(result).isPresent();
        assertThat(result.get().getNickname()).isEqualTo(dummyDto.getNickname());

        searchMemberRepository.deleteByLoginId(result.get().getLoginId());
    }

    @Test
    void testSearchAllByName() {
        //given
        searchMemberRepository.save(dummyDto.toSearchedMember());

        //when
        List<SearchMemberManagerRequestDto> results = searchMemberRepository.searchAllByName(
                dummyDto.getName()).stream().map(SearchedMember::toDto).collect(
                Collectors.toList());

        //then
        assertThat(results).hasSize(1);
        SearchMemberManagerRequestDto result = results.get(0);
        assertThat(result.getName()).isEqualTo(dummyDto.getName());

        searchMemberRepository.deleteByLoginId(result.getLoginId());
    }

    @Test
    void testSearchBySignUpDate() {
        //given
        searchMemberRepository.save(dummyDto.toSearchedMember());

        //wheb
        List<SearchMemberManagerRequestDto> results = searchMemberRepository.searchBySignUpDate(
                        dummyDto.getSignUpDate())
                .stream()
                .map(SearchedMember::toDto)
                .collect(Collectors.toList());

        //then
        assertThat(results).hasSize(1);
        SearchMemberManagerRequestDto result = results.get(0);
        assertThat(result.getSignUpDate()).isEqualTo(dummyDto.getSignUpDate());

        searchMemberRepository.deleteByLoginId(result.getLoginId());
    }
}