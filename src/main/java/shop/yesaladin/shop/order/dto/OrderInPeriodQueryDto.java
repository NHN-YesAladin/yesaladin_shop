package shop.yesaladin.shop.order.dto;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderInPeriodQueryDto {

    private LocalDate startDate;
    private LocalDate endDate;
    @Getter
    private int size = 10;
    @Getter
    private int page = 1;

    public LocalDate getStartDateOrDefaultValue(Clock clock) {
        return Objects.isNull(startDate) ? LocalDate.now(clock).minusMonths(1) : startDate;
    }

    public LocalDate getEndDateOrDefaultValue(Clock clock) {
        return Objects.isNull(endDate) ? LocalDate.now(clock) : endDate;
    }

}
