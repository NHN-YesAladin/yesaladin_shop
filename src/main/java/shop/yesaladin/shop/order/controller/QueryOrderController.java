package shop.yesaladin.shop.order.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.order.dto.OrderInPeriodQueryDto;
import shop.yesaladin.shop.order.dto.OrderSummaryDto;
import shop.yesaladin.shop.order.service.inter.QueryOrderService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/orders")
public class QueryOrderController {

    private final QueryOrderService queryOrderService;

    @GetMapping
    public List<OrderSummaryDto> getAllOrders(@RequestBody OrderInPeriodQueryDto queryDto) {
        return queryOrderService.getAllOrderListInPeriod(queryDto);
    }

}