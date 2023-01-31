package shop.yesaladin.shop.point.controller;

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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.point.domain.model.PointCode;
import shop.yesaladin.shop.point.domain.model.PointReasonCode;
import shop.yesaladin.shop.point.dto.PointHistoryResponseDto;
import shop.yesaladin.shop.point.service.inter.QueryPointHistoryService;

@AutoConfigureRestDocs
@WebMvcTest(QueryPointHistoryController.class)
class QueryPointHistoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    QueryPointHistoryService pointHistoryService;

    @Autowired
    ObjectMapper objectMapper;

    String loginId = "user@1";
    PointCode pointCode = PointCode.USE;
    PointReasonCode pointReasonCode = PointReasonCode.USE_ORDER;
    Page<PointHistoryResponseDto> response = getPageableData(5, pointCode, pointReasonCode);

    @Test
    @DisplayName("회원의 포인트내역 조회 실패 - 유효하지 않는 파라미터 값")
    void getPointHistoriesByLoginId_fail_InvalidCodeParameter() throws Exception {
        //when
        ResultActions result = mockMvc.perform(get("/v1/points/{loginId}/histories", loginId)
                .param("code", "invalidCode"));

        //then
        ErrorCode code = ErrorCode.POINT_INVALID_PARAMETER;
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(false)))
                .andExpect(jsonPath("$.status", equalTo(code.getResponseStatus().value())))
                .andExpect(jsonPath("$.data", equalTo(null)))
                .andExpect(jsonPath("$.errorMessages[0]", equalTo(code.getDisplayName())));

        //docs
        result.andDo(document(
                "get-point-histories-by-loginId-fail-invalid-code-parameter",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("loginId").description("회원의 아이디")),
                requestParameters(
                        parameterWithName("code").description("포인트 사용/적립 구분"),
                        parameterWithName("page").description("페이지 번호")
                                .optional()
                                .attributes(defaultValue(10)),
                        parameterWithName("size").description("페이지 요소 개수")
                                .optional()
                                .attributes(defaultValue(0))
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공유무"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data").type(JsonFieldType.NUMBER)
                                .description("반환값")
                                .optional(),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지")

                )
        ));
    }

    @Test
    @DisplayName("회원의 전체 포인트내역 조회-성공")
    void getPointHistoriesByLoginId_all() throws Exception {
        //given
        Mockito.when(pointHistoryService.getPointHistoriesWithLoginId(eq(loginId), any()))
                .thenReturn(response);

        //when
        ResultActions result = mockMvc.perform(get("/v1/points/{loginId}/histories", loginId)
                .param("size", "5")
                .param("page", "0"));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.data.totalPage", equalTo(1)))
                .andExpect(jsonPath("$.data.currentPage", equalTo(0)))
                .andExpect(jsonPath("$.data.totalDataCount", equalTo(5)))
                .andExpect(jsonPath("$.data.dataList.[0].id", equalTo(0)))
                .andExpect(jsonPath("$.data.dataList.[0].amount", equalTo(1000)))
                .andExpect(jsonPath("$.data.dataList.[0].pointCode", equalTo("USE")))
                .andExpect(jsonPath("$.data.dataList.[0].pointReasonCode", equalTo("USE_ORDER")));

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(pointHistoryService, times(1)).getPointHistoriesWithLoginId(
                anyString(),
                captor.capture()
        );

        assertThat(captor.getValue().getPageNumber()).isEqualTo(0);
        assertThat(captor.getValue().getPageSize()).isEqualTo(5);
        //docs
        result.andDo(document(
                "get-point-histories-by-loginId-all",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("loginId").description("회원의 아이디")),
                requestParameters(
                        parameterWithName("page").description("페이지 번호")
                                .optional()
                                .attributes(defaultValue(10)),
                        parameterWithName("size").description("페이지 요소 개수")
                                .optional()
                                .attributes(defaultValue(0))
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공유무"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER)
                                .description("전체 페이지"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER)
                                .description("현재 페이지"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER)
                                .description("총 데이터 개수"),
                        fieldWithPath("data.dataList.[].id").type(JsonFieldType.NUMBER)
                                .description("포인트 내역 Pk"),
                        fieldWithPath("data.dataList.[].amount").type(JsonFieldType.NUMBER)
                                .description("포인트 사용/적립 값"),
                        fieldWithPath("data.dataList.[].createDateTime").type(JsonFieldType.STRING)
                                .description("포인트 사용/적립 일시"),
                        fieldWithPath("data.dataList.[].pointCode").type(JsonFieldType.STRING)
                                .description("포인트 구분"),
                        fieldWithPath("data.dataList.[].pointReasonCode").type(JsonFieldType.STRING)
                                .description("포인트 사유 구분"),
                        fieldWithPath("errorMessages").type(JsonFieldType.STRING)
                                .description("에러 메세지")
                                .optional()

                )
        ));
    }

    @Test
    @DisplayName("회원의 사용/적립 포인트내역 조회-성공")
    void getPointHistoriesByLoginId() throws Exception {
        //given
        Mockito.when(pointHistoryService.getPointHistoriesWithLoginIdAndCode(
                eq(loginId),
                eq(pointCode),
                any()
        )).thenReturn(response);

        ResultActions result = mockMvc.perform(get("/v1/points/{loginId}/histories", loginId)
                .param("code", "USE")
                .param("page", "0")
                .param("size", "5"));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.data.totalPage", equalTo(1)))
                .andExpect(jsonPath("$.data.currentPage", equalTo(0)))
                .andExpect(jsonPath("$.data.totalDataCount", equalTo(5)))
                .andExpect(jsonPath("$.data.dataList.[0].id", equalTo(0)))
                .andExpect(jsonPath("$.data.dataList.[0].amount", equalTo(1000)))
                .andExpect(jsonPath("$.data.dataList.[0].pointCode", equalTo("USE")))
                .andExpect(jsonPath("$.data.dataList.[0].pointReasonCode", equalTo("USE_ORDER")));

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(pointHistoryService, times(1)).getPointHistoriesWithLoginIdAndCode(
                anyString(),
                any(),
                captor.capture()
        );

        assertThat(captor.getValue().getPageNumber()).isEqualTo(0);
        assertThat(captor.getValue().getPageSize()).isEqualTo(5);
        //docs
        result.andDo(document(
                "get-point-histories-by-loginId-and-code",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("loginId").description("회원의 아이디")),
                requestParameters(
                        parameterWithName("code").description("포인트 사용/적립 구분"),
                        parameterWithName("page").description("페이지 번호")
                                .optional()
                                .attributes(defaultValue(10)),
                        parameterWithName("size").description("페이지 요소 개수")
                                .optional()
                                .attributes(defaultValue(0))
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공유무"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER)
                                .description("전체 페이지"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER)
                                .description("현재 페이지"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER)
                                .description("총 데이터 개수"),
                        fieldWithPath("data.dataList.[].id").type(JsonFieldType.NUMBER)
                                .description("포인트 내역 Pk"),
                        fieldWithPath("data.dataList.[].amount").type(JsonFieldType.NUMBER)
                                .description("포인트 사용/적립 값"),
                        fieldWithPath("data.dataList.[].createDateTime").type(JsonFieldType.STRING)
                                .description("포인트 사용/적립 일시"),
                        fieldWithPath("data.dataList.[].pointCode").type(JsonFieldType.STRING)
                                .description("포인트 구분"),
                        fieldWithPath("data.dataList.[].pointReasonCode").type(JsonFieldType.STRING)
                                .description("포인트 사유 구분"),
                        fieldWithPath("errorMessages").type(JsonFieldType.STRING)
                                .description("에러 메세지")
                                .optional()

                )
        ));
    }


    @Test
    void getPointHistories() {
    }

    @Test
    @DisplayName("회원의 포인트 조회 실패 - 존재하지 않는 회원")
    void getMemberPoint_fail_memberNotFound() throws Exception {
        //given
        Mockito.when(pointHistoryService.getMemberPoint(loginId))
                .thenThrow(new MemberNotFoundException("Member loginId : " + loginId));

        //when
        ResultActions result = mockMvc.perform(get("/v1/points/{loginId}", loginId));

        //then
        result.andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", startsWith("Member not found")));

        //docs
        result.andDo(document(
                "get-member-point-fail-member-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("loginId").description("회원의 아이디")),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                )
        ));
    }

    @Test
    @DisplayName("회원의 포인트 조회 성공")
    void getMemberPoint_success() throws Exception {
        //given
        long amount = 1000;

        Mockito.when(pointHistoryService.getMemberPoint(loginId)).thenReturn(amount);

        //when
        ResultActions result = mockMvc.perform(get("/v1/points/{loginId}", loginId));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.data", equalTo(1000)));

        //docs
        result.andDo(document(
                "get-member-point-success",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("loginId").description("회원의 아이디")),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공유무"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data").type(JsonFieldType.NUMBER)
                                .description("회원의 포인트 값"),
                        fieldWithPath("errorMessages").type(JsonFieldType.STRING)
                                .description("에러 메세지")
                                .optional()
                )
        ));
    }


    Page<PointHistoryResponseDto> getPageableData(
            int size,
            PointCode pointCode,
            PointReasonCode pointReasonCode
    ) {
        List<PointHistoryResponseDto> content = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            content.add(ReflectionUtils.newInstance(
                            PointHistoryResponseDto.class,
                            (long) i,
                            1000L,
                            LocalDateTime.now(),
                            pointCode,
                            pointReasonCode
                    )
            );
        }
        Pageable pageable = Pageable.ofSize(size);
        return PageableExecutionUtils.getPage(content, pageable, () -> size);
    }
}