package shop.yesaladin.shop.point.persistence;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.dummy.MemberDummy;
import shop.yesaladin.shop.point.domain.model.PointCode;
import shop.yesaladin.shop.point.domain.model.PointHistory;
import shop.yesaladin.shop.point.domain.repository.QueryPointHistoryRepository;
import shop.yesaladin.shop.point.dto.PointHistoryResponseDto;

@Transactional
@SpringBootTest
class QuerydslQueryPointHistoryRepositoryTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    QueryPointHistoryRepository queryPointHistoryRepository;

    Member member;
    String loginId = "user@1";
    Pageable pageable = Pageable.ofSize(5);

    long amount = 1000;
    LocalDateTime createDateTime = LocalDateTime.now();
    PointCode pointCode = PointCode.USE;

    @BeforeEach
    void setUp() {
        member = MemberDummy.dummyWithLoginId(loginId);
        entityManager.persist(member);
    }

    @Test
    void findById() {
        //given
        PointHistory pointHistory = createPointHistory(member, pointCode);
        entityManager.persist(pointHistory);

        Long id = pointHistory.getId();
        //when
        Optional<PointHistory> foundPointHistory = queryPointHistoryRepository.findById(id);

        //then
        assertThat(foundPointHistory).isPresent();
        assertThat(foundPointHistory.get().getId()).isEqualTo(id);
        assertThat(foundPointHistory.get().getPointCode()).isEqualTo(pointCode);
        assertThat(foundPointHistory.get().getAmount()).isEqualTo(amount);
        assertThat(foundPointHistory.get().getCreateDateTime()).isEqualTo(createDateTime);
        assertThat(foundPointHistory.get().getMember()).isSameAs(member);
    }

    @Test
    void findByLoginId() {
        //given
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 2, 1);

        //when
        List<PointHistory> result = queryPointHistoryRepository.findByLoginId(
                loginId,
                startDate,
                endDate
        );

        //then
        assertThat(result).isEmpty();
    }

    @Test
    void findByPointCode() {
        //given
        PointCode pointCode = PointCode.USE;
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 2, 1);

        //when
        List<PointHistory> result = queryPointHistoryRepository.findByPointCode(
                pointCode,
                startDate,
                endDate
        );

        //then
        assertThat(result).isEmpty();
    }

    @Test
    void getMemberPointByLoginId() {
        setPointHistory(3, 5);

        String loginId = member.getLoginId();

        long point = queryPointHistoryRepository.getMemberPointByLoginId(loginId);

        assertThat(point).isEqualTo(-1000);

    }

    @Test
    void getByLoginIdAndPointCode() {
        //given
        setPointHistory(5, 5);

        //when
        Page<PointHistoryResponseDto> result = queryPointHistoryRepository.getByLoginIdAndPointCode(
                loginId,
                pointCode,
                pageable
        );

        //then
        assertThat(result.getPageable()).isEqualTo(pageable);
        assertThat(result.getContent()).hasSize(5);
    }

    @Test
    void getByLoginId() {
        //given
        setPointHistory(5, 5);

        //when
        Page<PointHistoryResponseDto> result = queryPointHistoryRepository.getByLoginId(
                loginId,
                pageable
        );

        //then
        assertThat(result.getPageable()).isEqualTo(pageable);
        assertThat(result.getContent()).hasSize(5);
        result.get().forEach(System.out::println);
        assertThat(result.getTotalElements()).isEqualTo(10);
    }

    @Test
    void getByPointCode() {
        //given
        setPointHistory(5, 5);

        //when
        Page<PointHistoryResponseDto> result = queryPointHistoryRepository.getByPointCode(
                pointCode,
                pageable
        );

        //then
        assertThat(result.getPageable()).isEqualTo(pageable);
        assertThat(result.getContent()).hasSize(5);
    }

    @Test
    void getBy() {
        //given
        setPointHistory(5, 5);

        //when
        Page<PointHistoryResponseDto> result = queryPointHistoryRepository.getBy(
                pageable
        );

        //then
        assertThat(result.getPageable()).isEqualTo(pageable);
        assertThat(result.getContent()).hasSize(5);
        assertThat(result.getTotalElements()).isEqualTo(10);
    }

    PointHistory createPointHistory(Member member, PointCode pointCode) {
        return PointHistory.builder()
                .amount(amount)
                .pointCode(pointCode)
                .member(member)
                .createDateTime(createDateTime)
                .build();
    }

    void setPointHistory(int save, int use) {
        entityManager.persist(createPointHistory(member, PointCode.SUM));
        for (int i = 0; i < save; i++) {
            entityManager.persist(createPointHistory(member, PointCode.SAVE));
        }
        for (int i = 0; i < use; i++) {
            entityManager.persist(createPointHistory(member, PointCode.USE));
        }
    }

}