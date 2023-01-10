package shop.yesaladin.shop.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderInPeriodQueryDto {

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

}
