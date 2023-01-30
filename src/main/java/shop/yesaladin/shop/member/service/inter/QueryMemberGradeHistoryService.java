package shop.yesaladin.shop.member.service.inter;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.common.dto.PeriodQueryRequestDto;
import shop.yesaladin.shop.member.dto.MemberGradeHistoryQueryResponseDto;

/**
 * 회원 등급변경내역 조회관련 서비스 인터페이스입니다.
 *
 * @author 최예린
 * @since 1.0
 */
public interface QueryMemberGradeHistoryService {

    /**
     * 회원의 등급내역을 조회 하기 위한 메서드 입니다.
     *
     * @param loginId 조회할 회원 id
     * @param request  조회할 기간
     * @param pageable 페이지 번호와 사이즈
     * @return 회원의 등급 내역
     * @author 최예린
     * @since 1.0
     */
    Page<MemberGradeHistoryQueryResponseDto> getByLoginId(
            String loginId,
            PeriodQueryRequestDto request,
            Pageable pageable
    );
}
