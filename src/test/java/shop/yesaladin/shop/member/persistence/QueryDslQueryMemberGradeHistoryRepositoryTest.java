package shop.yesaladin.shop.member.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberGradeHistory;
import shop.yesaladin.shop.member.domain.repository.QueryMemberGradeHistoryRepository;
import shop.yesaladin.shop.member.dto.MemberGradeHistoryQueryResponseDto;
import shop.yesaladin.shop.member.dummy.MemberDummy;
import shop.yesaladin.shop.member.dummy.MemberGradeHistoryDummy;

@Transactional
@SpringBootTest
class QueryDslQueryMemberGradeHistoryRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    QueryMemberGradeHistoryRepository queryMemberGradeHistoryRepository;

    Member member;
    String loginId = "user@1";
    Pageable pageable = Pageable.ofSize(10);


    @BeforeEach
    void setUp() {
        member = MemberDummy.dummyWithLoginId(loginId);
        entityManager.persist(member);
    }

    @Test
    void findById() {
        //given
        MemberGradeHistory memberGradeHistory = MemberGradeHistoryDummy.dummy(member);
        entityManager.persist(memberGradeHistory);
        Long id = memberGradeHistory.getId();

        //when
        Optional<MemberGradeHistoryQueryResponseDto> actual = queryMemberGradeHistoryRepository.findById(
                id);

        //then
        assertThat(actual).isPresent();
        assertThat(actual.get().getId()).isEqualTo(id);
        assertThat(actual.get().getMemberGrade().getName()).isEqualTo(
                memberGradeHistory.getMemberGrade().getName());
        assertThat(actual.get().getLoginId()).isEqualTo(loginId);
        assertThat(actual.get().getPreviousPaidAmount()).isEqualTo(
                memberGradeHistory.getPreviousPaidAmount());
        assertThat(actual.get().getUpdateDate()).isEqualTo(
                memberGradeHistory.getUpdateDate());
    }

    @Test
    void findByMemberIdAndPeriod() {
        //given
        int expectedCnt = 10;
        LocalDate startDate = LocalDate.of(2022, 12, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 1);

        addMemberGradeHistoryData(expectedCnt);

        //when
        Page<MemberGradeHistoryQueryResponseDto> actual = queryMemberGradeHistoryRepository.findByLoginIdAndPeriod(
                loginId,
                startDate,
                endDate,
                pageable
        );

        //then
        assertThat(actual).hasSize(expectedCnt);
        assertThat(actual.stream()
                .filter(x -> x.getUpdateDate().isAfter(startDate) &&
                        x.getUpdateDate().isBefore(endDate) &&
                        x.getLoginId().equals(loginId))
                .collect(Collectors.toList())).hasSize(expectedCnt);

    }

    private void addMemberGradeHistoryData(int cnt) {
        LocalDate date = LocalDate.of(2022, 12, 15);

        MemberGradeHistory memberGradeHistory;

        for (int i = 0; i < cnt; i++) {
            memberGradeHistory = MemberGradeHistoryDummy.dummyWithDate(member, date);

            entityManager.persist(memberGradeHistory);
        }
    }
}