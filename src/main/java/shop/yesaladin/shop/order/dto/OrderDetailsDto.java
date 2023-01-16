package shop.yesaladin.shop.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import shop.yesaladin.shop.order.domain.model.OrderCode;

@Getter
@AllArgsConstructor
public class OrderDetailsDto {
    private final String orderNumber;
    private final String name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private final LocalDateTime orderDateTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate expectedTransportDate;
    private final long usedPoint;
    private final long productTotalPrice;
    private final int shippingFee;
    private final int wrappingFee;
    private final OrdererSummaryDto ordererSummary;
    private final OrderCode orderCode;
    private final List<OrderProductSummaryDto> orderProductList;
    private final SubscribeSummaryDto subscribeSummary;
}
