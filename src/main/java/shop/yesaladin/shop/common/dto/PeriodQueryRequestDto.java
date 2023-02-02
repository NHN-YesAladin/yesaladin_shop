package shop.yesaladin.shop.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import shop.yesaladin.shop.common.exception.InvalidPeriodConditionException;
import shop.yesaladin.shop.common.exception.type.InvalidPeriodConditionType;

/**
 * 기간조회를 위한 요청 dto 클래스입니다.
 *
 * @DateTimeFormat : @ModelAttribute를 사용해 query param으로 매칭 하기 위해 사용
 * @JsonFormat : @RequestBody를 사용해 http body로 매칭 하기 위해 사용
 *
 * @author 최예린
 * @author 배수한
 * @since 1.0
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PeriodQueryRequestDto {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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

        if (start.isBefore(start.minusYears(1))) {
            throw new InvalidPeriodConditionException(InvalidPeriodConditionType.TOO_PAST);
        }

        if (start.isBefore(end.minusYears(1))) {
            throw new InvalidPeriodConditionException(InvalidPeriodConditionType.LONG_PERIOD_OF_TIME);
        }
    }
}
