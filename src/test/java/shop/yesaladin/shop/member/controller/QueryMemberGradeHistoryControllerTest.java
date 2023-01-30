package shop.yesaladin.shop.member.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;
import static shop.yesaladin.shop.docs.DocumentFormatGenerator.defaultValue;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.common.dto.PeriodQueryRequestDto;
import shop.yesaladin.shop.common.exception.InvalidPeriodConditionException;
import shop.yesaladin.shop.common.exception.type.InvalidPeriodConditionType;
import shop.yesaladin.shop.member.domain.model.MemberGrade;
import shop.yesaladin.shop.member.dto.MemberGradeHistoryQueryResponseDto;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.member.service.inter.QueryMemberGradeHistoryService;

@AutoConfigureRestDocs
@WebMvcTest(QueryMemberGradeHistoryController.class)
class QueryMemberGradeHistoryControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    QueryMemberGradeHistoryService queryMemberGradeHistoryService;

    LocalDate updateDate = LocalDate.now();
    long previousPaidAmount = 100000;
    MemberGrade memberGrade = MemberGrade.BRONZE;

    @Test
    @DisplayName("회원 등급내역 조회 실패 - 유효하지 않은 조회 기간")
    void getMemberGrades_fail_invalidPeriodCondition() throws Exception {
        String loginId = "user@1";
        int page = 0;
        int size = 10;

        Map<String, Object> request = Map.of(
                "startDate",
                LocalDate.of(2020, 12, 1),
                "endDate",
                LocalDate.of(2023, 1, 2)
        );
        Mockito.when(queryMemberGradeHistoryService.getByLoginId(eq(loginId), any(), any()))
                .thenThrow(new InvalidPeriodConditionException(InvalidPeriodConditionType.TOO_PAST));

        ResultActions result = mockMvc.perform(get("/v1/members/{loginId}/grade-histories", loginId)
                .param("page", page + "")
                .param("size", size + "")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", startsWith("Cannot query with")));

        ArgumentCaptor<PeriodQueryRequestDto> captor = ArgumentCaptor.forClass(PeriodQueryRequestDto.class);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(queryMemberGradeHistoryService, times(1)).getByLoginId(
                anyString(),
                captor.capture(),
                pageableCaptor.capture()
        );

        assertThat(ReflectionUtils.tryToReadFieldValue(
                PeriodQueryRequestDto.class,
                "startDate",
                captor.getValue()
        ).get()).isEqualTo(request.get("startDate"));
        assertThat(ReflectionUtils.tryToReadFieldValue(
                PeriodQueryRequestDto.class,
                "endDate",
                captor.getValue()
        ).get()).isEqualTo(request.get("endDate"));
        assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(size);
        assertThat(pageableCaptor.getValue().getPageNumber()).isEqualTo(page);

        result.andDo(document(
                "get-member-grade-fail-invalid-period-condition",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("loginId").description("회원의 아이디")),
                requestParameters(
                        parameterWithName("page").description("페이지 번호")
                                .optional()
                                .attributes(defaultValue(0)),
                        parameterWithName("size").description("데이터 개수")
                                .optional()
                                .attributes(defaultValue(0))
                ),
                requestFields(
                        fieldWithPath("startDate").type(JsonFieldType.STRING)
                                .description("회원등급 내역 조회 시작일"),
                        fieldWithPath("endDate").type(JsonFieldType.STRING)
                                .description("회원등급 내역 조회 끝일")
                ),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                )
        ));
    }

    @Test
    @DisplayName("회원 등급내역 조회 실패- 존재하지 않는 회원")
    void getMemberGrades_fail_memberNotFound() throws Exception {
        String loginId = "user@1";
        int page = 0;
        int size = 10;

        Map<String, Object> request = Map.of(
                "startDate",
                LocalDate.of(2023, 1, 1),
                "endDate",
                LocalDate.of(2023, 1, 10)
        );
        Page<MemberGradeHistoryQueryResponseDto> response = getMemberGradeHistoryQueryResponseData(
                loginId, size);
        Mockito.when(queryMemberGradeHistoryService.getByLoginId(eq(loginId), any(), any()))
                .thenThrow(new MemberNotFoundException("Member loginId: " + loginId));

        ResultActions result = mockMvc.perform(get("/v1/members/{loginId}/grade-histories", loginId)
                        .param("page", page + "")
                        .param("size", size + "")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", startsWith("Member not found")));

        ArgumentCaptor<PeriodQueryRequestDto> captor = ArgumentCaptor.forClass(PeriodQueryRequestDto.class);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(queryMemberGradeHistoryService, times(1)).getByLoginId(
                anyString(),
                captor.capture(),
                pageableCaptor.capture()
        );

        assertThat(ReflectionUtils.tryToReadFieldValue(
                PeriodQueryRequestDto.class,
                "startDate",
                captor.getValue()
        ).get()).isEqualTo(request.get("startDate"));
        assertThat(ReflectionUtils.tryToReadFieldValue(
                PeriodQueryRequestDto.class,
                "endDate",
                captor.getValue()
        ).get()).isEqualTo(request.get("endDate"));
        assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(size);
        assertThat(pageableCaptor.getValue().getPageNumber()).isEqualTo(page);

        result.andDo(document(
                "get-member-grade-fail-member-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("loginId").description("회원의 아이디")),
                requestParameters(
                        parameterWithName("page").description("페이지 번호")
                                .optional()
                                .attributes(defaultValue(0)),
                        parameterWithName("size").description("데이터 개수")
                                .optional()
                                .attributes(defaultValue(0))
                ),
                requestFields(
                        fieldWithPath("startDate").type(JsonFieldType.STRING)
                                .description("회원등급 내역 조회 시작일"),
                        fieldWithPath("endDate").type(JsonFieldType.STRING)
                                .description("회원등급 내역 조회 끝일")
                ),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                )
        ));
    }

    @Test
    @DisplayName("회원 등급내역 조회 성공")
    void getMemberGrades() throws Exception {
        String loginId = "user@1";
        int page = 0;
        int size = 10;

        Map<String, Object> request = Map.of(
                "startDate",
                LocalDate.of(2023, 1, 1),
                "endDate",
                LocalDate.of(2023, 1, 2)
        );
        Page<MemberGradeHistoryQueryResponseDto> response = getMemberGradeHistoryQueryResponseData(
                loginId, size);
        Mockito.when(queryMemberGradeHistoryService.getByLoginId(eq(loginId), any(), any()))
                .thenReturn(response);

        ResultActions result = mockMvc.perform(get("/v1/members/{loginId}/grade-histories", loginId)
                        .param("page", page + "")
                        .param("size", size + "")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.dataList.[0].id", equalTo(0)))
                .andExpect(jsonPath("$.dataList.[0].updateDate", equalTo(updateDate.toString())))
                .andExpect(jsonPath(
                        "$.dataList.[0].previousPaidAmount",
                        equalTo((int) previousPaidAmount)
                ))
                .andExpect(jsonPath("$.dataList.[0].memberGrade", equalTo(memberGrade.name())))
                .andExpect(jsonPath("$.dataList.[0].loginId", equalTo(loginId)))
                .andExpect(jsonPath("$.totalPage", equalTo(1)))
                .andExpect(jsonPath("$.currentPage", equalTo(0)))
                .andExpect(jsonPath("$.totalDataCount", equalTo(10)));

        ArgumentCaptor<PeriodQueryRequestDto> captor = ArgumentCaptor.forClass(PeriodQueryRequestDto.class);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(queryMemberGradeHistoryService, times(1)).getByLoginId(
                anyString(),
                captor.capture(),
                pageableCaptor.capture()
        );

        assertThat(ReflectionUtils.tryToReadFieldValue(
                PeriodQueryRequestDto.class,
                "startDate",
                captor.getValue()
        ).get()).isEqualTo(request.get("startDate"));
        assertThat(ReflectionUtils.tryToReadFieldValue(
                PeriodQueryRequestDto.class,
                "endDate",
                captor.getValue()
        ).get()).isEqualTo(request.get("endDate"));
        assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(size);
        assertThat(pageableCaptor.getValue().getPageNumber()).isEqualTo(page);

        result.andDo(document(
                "get-member-grade-success",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("loginId").description("회원의 아이디")),
                requestParameters(
                        parameterWithName("page").description("페이지 번호")
                                .optional()
                                .attributes(defaultValue(0)),
                        parameterWithName("size").description("데이터 개수")
                                .optional()
                                .attributes(defaultValue(0))
                ),
                requestFields(
                        fieldWithPath("startDate").type(JsonFieldType.STRING)
                                .description("회원등급 내역 조회 시작일"),
                        fieldWithPath("endDate").type(JsonFieldType.STRING)
                                .description("회원등급 내역 조회 끝일")
                ),
                responseFields(
                        fieldWithPath("dataList.[].id").type(JsonFieldType.NUMBER)
                                .description("회원등급 변경 내역 Pk"),
                        fieldWithPath("dataList.[].updateDate").type(JsonFieldType.STRING)
                                .description("회원등급 변경 내역일"),
                        fieldWithPath("dataList.[].previousPaidAmount").type(JsonFieldType.NUMBER)
                                .description("회원의 전달 구매 금액"),
                        fieldWithPath("dataList.[].memberGrade").type(JsonFieldType.STRING)
                                .description("회원의 등급"),
                        fieldWithPath("dataList.[].loginId").type(JsonFieldType.STRING)
                                .description("회원의 아이디"),
                        fieldWithPath("totalPage").type(JsonFieldType.NUMBER).description("전체 페이지"),
                        fieldWithPath("currentPage").type(JsonFieldType.NUMBER)
                                .description("현재 페이지"),
                        fieldWithPath("totalDataCount").type(JsonFieldType.NUMBER)
                                .description("총 데이터 개수")
                        )
        ));
    }

    private Page<MemberGradeHistoryQueryResponseDto> getMemberGradeHistoryQueryResponseData(
            String loginId,
            int size
    ) {
        List<MemberGradeHistoryQueryResponseDto> list = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            list.add(ReflectionUtils.newInstance(
                    MemberGradeHistoryQueryResponseDto.class,
                    (long) i,
                    updateDate,
                    previousPaidAmount,
                    memberGrade,
                    loginId
            ));
        }
        Pageable pageable = Pageable.ofSize(size);
        return PageableExecutionUtils.getPage(list, pageable, () -> size);
    }
}