package shop.yesaladin.shop.order.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import shop.yesaladin.shop.order.dto.OrderInPeriodQueryDto;
import shop.yesaladin.shop.order.service.inter.QueryOrderService;

@WebMvcTest(QueryOrderController.class)
class QueryOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private QueryOrderService queryOrderService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("기간 내의 모든 주문 내역이 조회된다.")
    void getAllOrdersTest() throws Exception {
        Mockito.when(queryOrderService.getAllOrderListInPeriod(any())).thenReturn(List.of());
        Map<String, Object> request = Map.of(
                "startDate",
                LocalDate.of(2023, 1, 1),
                "endDate",
                LocalDate.of(2023, 1, 2),
                "size",
                20,
                "page",
                1
        );
        ArgumentCaptor<OrderInPeriodQueryDto> captor = ArgumentCaptor.forClass(OrderInPeriodQueryDto.class);

        mockMvc.perform(get("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        Mockito.verify(queryOrderService, Mockito.times(1))
                .getAllOrderListInPeriod(captor.capture());
        OrderInPeriodQueryDto actual = captor.getValue();
        Assertions.assertThat(actual.getSize()).isEqualTo(20);
        Assertions.assertThat(actual.getPage()).isEqualTo(1);
    }
}