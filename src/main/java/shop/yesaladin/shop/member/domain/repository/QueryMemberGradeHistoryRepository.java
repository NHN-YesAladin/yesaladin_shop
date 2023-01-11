package shop.yesaladin.shop.member.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import shop.yesaladin.shop.member.dto.MemberGradeHistoryQueryResponseDto;

/**
 * 회원 등급 변경 이력 조회 관련 repository interface 입니다.
 *
 * @author : 송학현, 최예린
 * @since : 1.0
 */
public interface QueryMemberGradeHistoryRepository {

    /**
     * primary key를 통해 회원의 등급 변경 이력을 조회 합니다.
     *
     * @param id primary key
     * @return 조회된 회원 등급 변경 이력
     * @author : 송학현, 최예린
     * @since : 1.0
     */
    Optional<MemberGradeHistoryQueryResponseDto> findById(Long id);

    /**
     * 회원의 id를 통해 회원의 등급 변경 이력을 조회 합니다.
     *
     * @param memberId  회원 id
     * @param startDate 조회 시작일
     * @param endDate   조회 종료일
     * @return 회원의 등급변경 내역
     * @author 최예린
     * @since 1.0
     */
    List<MemberGradeHistoryQueryResponseDto> findByMemberIdAndPeriod(
            Long memberId,
            LocalDate startDate,
            LocalDate endDate
    );
}
