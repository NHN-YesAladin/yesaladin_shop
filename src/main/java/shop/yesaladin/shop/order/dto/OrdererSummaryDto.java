package shop.yesaladin.shop.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrdererSummaryDto {
    private final String name;
    private final String phoneNumber;
    private final String address;
}
