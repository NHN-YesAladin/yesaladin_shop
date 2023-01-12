package shop.yesaladin.shop.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

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

}
