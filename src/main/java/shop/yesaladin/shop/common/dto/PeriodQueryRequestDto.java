package shop.yesaladin.shop.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.common.exception.InvalidPeriodConditionException;
import shop.yesaladin.shop.common.exception.type.InvalidPeriodConditionType;

/**
 * 기간조회를 위한 요청 dto 클래스입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PeriodQueryRequestDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    public LocalDate getStartDateOrDefaultValue(Clock clock) {
        return Objects.isNull(startDate) ? LocalDate.now(clock).minusMonths(1) : startDate;
    }

    public LocalDate getEndDateOrDefaultValue(Clock clock) {
        return Objects.isNull(endDate) ? LocalDate.now(clock) : endDate;
    }

    public void validate(Clock clock) {
        LocalDate start = this.getStartDateOrDefaultValue(clock);
        LocalDate end = this.getEndDateOrDefaultValue(clock);

        if (start.isAfter(end)) {
            throw new InvalidPeriodConditionException(InvalidPeriodConditionType.START_OVER_END);
        }

        if (end.isAfter(LocalDate.now(clock))) {
            throw new InvalidPeriodConditionException(InvalidPeriodConditionType.FUTURE);
        }

        // TODO 6개월 까지 늘려야하는 것은 아닌가?
        if (start.isBefore(LocalDate.of(2023, 1, 1))) {
            throw new InvalidPeriodConditionException(InvalidPeriodConditionType.TOO_PAST);
        }

        if (start.isBefore(end.minusYears(1))) {
            throw new InvalidPeriodConditionException(InvalidPeriodConditionType.LONG_PERIOD_OF_TIME);
        }
    }
}
