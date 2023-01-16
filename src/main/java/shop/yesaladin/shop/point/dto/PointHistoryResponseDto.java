package shop.yesaladin.shop.point.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.point.domain.model.PointCode;
import shop.yesaladin.shop.point.domain.model.PointHistory;

/**
 * 포인트내역 등록 후 반환을 위한 dto 클래스 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PointHistoryResponseDto {
    private long id;
    private long amount;
    private LocalDateTime createDateTime;
    private PointCode pointCode;
    private Member member;

    /**
     * 엔티티 클래스를 dto 클래스로 변환시켜줍니다.
     *
     * @param pointHistory pointHistory 엔티티 클래스
     * @return PointHistoryResponseDto 클래스
     * @author 최예린
     * @since 1.0
     */
    public static PointHistoryResponseDto fromEntity(PointHistory pointHistory) {
        return new PointHistoryResponseDto(
                pointHistory.getId(),
                pointHistory.getAmount(),
                pointHistory.getCreateDateTime(),
                pointHistory.getPointCode(),
                pointHistory.getMember()
        );
    }
}
