package shop.yesaladin.shop.member.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import shop.yesaladin.shop.common.dto.PeriodQueryRequestDto;
import shop.yesaladin.shop.member.service.inter.QueryMemberGradeHistoryService;

@WebMvcTest(QueryMemberGradeHistoryController.class)
class QueryMemberGradeHistoryControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    QueryMemberGradeHistoryService queryMemberGradeHistoryService;

    @Test
    void getMemberGrades() throws Exception {
        Long memberId = 1L;
        Map<String, Object> request = Map.of(
                "startDate",
                LocalDate.of(2022, 12, 1),
                "endDate",
                LocalDate.of(2023, 1, 2)
        );

        Mockito.when(queryMemberGradeHistoryService.findByMemberId(eq(memberId), any()))
                .thenReturn(new ArrayList<>(Collections.emptyList()));

        mockMvc.perform(get("/v1/members/{memberId}/grade-histories", memberId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        ArgumentCaptor<PeriodQueryRequestDto> captor = ArgumentCaptor.forClass(PeriodQueryRequestDto.class);

        verify(queryMemberGradeHistoryService, times(1)).findByMemberId(
                anyLong(),
                captor.capture()
        );

        Assertions.assertThat(ReflectionUtils.tryToReadFieldValue(
                PeriodQueryRequestDto.class,
                "startDate",
                captor.getValue()
        ).get()).isEqualTo(request.get("startDate"));
        Assertions.assertThat(ReflectionUtils.tryToReadFieldValue(
                PeriodQueryRequestDto.class,
                "endDate",
                captor.getValue()
        ).get()).isEqualTo(request.get("endDate"));
    }
}