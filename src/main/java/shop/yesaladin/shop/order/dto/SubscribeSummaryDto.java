package shop.yesaladin.shop.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubscribeSummaryDto {

    private final int expectedDay;
    private final int intervalMonth;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate nextRenewalDate;
    private final String ISSN;
}
