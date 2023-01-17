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

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PointHistoryRequestDto {

    @NotNull
    private Long amount;

    public PointHistory toEntity(PointCode pointCode, Member member) {
        return PointHistory.builder()
                .amount(amount)
                .createDateTime(LocalDateTime.now())
                .pointCode(pointCode)
                .member(member).build();
    }
}
