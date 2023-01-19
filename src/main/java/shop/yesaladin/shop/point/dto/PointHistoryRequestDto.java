package shop.yesaladin.shop.point.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.point.domain.model.PointCode;
import shop.yesaladin.shop.point.domain.model.PointHistory;

/**
 * 포인트내역 등록 후 요청을 위한 dto 클래스 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PointHistoryRequestDto {

    @NotNull
    private Long amount;

    /**
     * dto 클래스를 엔티티 클래스로 변환시켜줍니다.
     *
     * @param pointCode 포인트 코드(사용/적립)
     * @param member 회원 정보
     * @author 최예린
     * @since 1.0
     */
    public PointHistory toEntity(PointCode pointCode, Member member) {
        return PointHistory.builder()
                .amount(amount)
                .createDateTime(LocalDateTime.now())
                .pointCode(pointCode)
                .member(member).build();
    }
}
