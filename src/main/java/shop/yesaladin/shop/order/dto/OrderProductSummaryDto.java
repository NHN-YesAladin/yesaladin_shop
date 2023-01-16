package shop.yesaladin.shop.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderProductSummaryDto {

    private final long productId;
    private final String title;
    private final long price;
    private final int quantity;
    private final Boolean isCancelled;
}
