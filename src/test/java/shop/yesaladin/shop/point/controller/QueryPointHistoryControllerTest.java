package shop.yesaladin.shop.point.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.common.aspect.advice.LoginIdAspect;
import shop.yesaladin.shop.point.domain.model.PointCode;
import shop.yesaladin.shop.point.domain.model.PointReasonCode;
import shop.yesaladin.shop.point.dto.PointHistoryResponseDto;
import shop.yesaladin.shop.point.service.inter.QueryPointHistoryService;

@AutoConfigureRestDocs
@Import({AopAutoConfiguration.class, LoginIdAspect.class})
@WebMvcTest(QueryPointHistoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class QueryPointHistoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    QueryPointHistoryService pointHistoryService;

    @Autowired
    ObjectMapper objectMapper;

    PointCode pointCode = PointCode.USE;
    PointReasonCode pointReasonCode = PointReasonCode.USE_ORDER;
    Page<PointHistoryResponseDto> response = getPageableData(5, pointCode, pointReasonCode);

    @Test
    @DisplayName("????????? ??????????????? ?????? ?????? - ???????????? ?????? ??????")
    @WithAnonymousUser
    void getPointHistoriesByLoginId_fail_InvalidAuthority() throws Exception {
        //when
        ResultActions result = mockMvc.perform(get("/v1/point-histories")
                .with(csrf())
                .param("code", "invalidCode"));

        //then
        result.andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(false)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.UNAUTHORIZED.value())))
                .andExpect(jsonPath(
                        "$.errorMessages[0]",
                        equalTo(ErrorCode.UNAUTHORIZED.getDisplayName())
                ));

        //docs
        result.andDo(document(
                "get-point-histories-by-loginId-fail-unauthorized",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("code").description("????????? ??????/?????? ??????"),
                        parameterWithName("page").description("????????? ??????")
                                .optional()
                                .attributes(defaultValue(10)),
                        parameterWithName("size").description("????????? ?????? ??????")
                                .optional()
                                .attributes(defaultValue(0)),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("?????? ?????? ??????"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("HTTP ?????? ??????"),
                        fieldWithPath("data").type(JsonFieldType.NUMBER)
                                .description("null")
                                .optional(),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("?????? ?????????")
                )
        ));
    }

    @Test
    @DisplayName("????????? ??????????????? ?????? ?????? - ???????????? ?????? ???????????? ???")
    @WithMockUser(username = "user@1", authorities = "ROLE_USER")
    void getPointHistoriesByLoginId_fail_InvalidCodeParameter() throws Exception {
        //given

        //when
        ResultActions result = mockMvc.perform(get("/v1/point-histories")
                .with(csrf())
                .param("code", "invalidCode"));

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(false)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath(
                        "$.errorMessages[0]",
                        equalTo(ErrorCode.POINT_INVALID_PARAMETER.getDisplayName())
                ));

        //docs
        result.andDo(document(
                "get-point-histories-by-loginId-fail-invalid-code-parameter",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("code").description("????????? ??????/?????? ??????"),
                        parameterWithName("page").description("????????? ??????")
                                .optional()
                                .attributes(defaultValue(10)),
                        parameterWithName("size").description("????????? ?????? ??????")
                                .optional()
                                .attributes(defaultValue(0)),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("?????? ?????? ??????"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("HTTP ?????? ??????"),
                        fieldWithPath("data").type(JsonFieldType.NUMBER)
                                .description("null")
                                .optional(),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("?????? ?????????")
                )
        ));
    }

    @Test
    @DisplayName("????????? ?????? ??????????????? ??????-??????")
    @WithMockUser(username = "user@1", authorities = "ROLE_USER")
    void getPointHistoriesByLoginId_all() throws Exception {
        //given
        Mockito.when(pointHistoryService.getPointHistoriesWithLoginId(any(), any()))
                .thenReturn(response);

        //when
        ResultActions result = mockMvc.perform(get("/v1/point-histories")
                .with(csrf())
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
                any(),
                captor.capture()
        );

        assertThat(captor.getValue().getPageNumber()).isEqualTo(0);
        assertThat(captor.getValue().getPageSize()).isEqualTo(5);
        //docs
        result.andDo(document(
                "get-point-histories-by-loginId-all",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("page").description("????????? ??????")
                                .optional()
                                .attributes(defaultValue(10)),
                        parameterWithName("size").description("????????? ?????? ??????")
                                .optional()
                                .attributes(defaultValue(0)),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("?????? ?????? ??????"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("HTTP ?????? ??????"),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER)
                                .description("?????? ?????????"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER)
                                .description("?????? ?????????"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER)
                                .description("??? ????????? ??????"),
                        fieldWithPath("data.dataList.[].id").type(JsonFieldType.NUMBER)
                                .description("????????? ?????? Pk"),
                        fieldWithPath("data.dataList.[].amount").type(JsonFieldType.NUMBER)
                                .description("????????? ??????/?????? ???"),
                        fieldWithPath("data.dataList.[].createDateTime").type(JsonFieldType.STRING)
                                .description("????????? ??????/?????? ??????"),
                        fieldWithPath("data.dataList.[].pointCode").type(JsonFieldType.STRING)
                                .description("????????? ??????"),
                        fieldWithPath("data.dataList.[].pointReasonCode").type(JsonFieldType.STRING)
                                .description("????????? ?????? ??????"),
                        fieldWithPath("errorMessages").type(JsonFieldType.STRING)
                                .description("?????? ?????????")
                                .optional()

                )
        ));
    }

    @Test
    @DisplayName("????????? ??????/?????? ??????????????? ??????-??????")
    @WithMockUser(username = "user@1", authorities = "ROLE_USER")
    void getPointHistoriesByLoginId() throws Exception {
        //given
        Mockito.when(pointHistoryService.getPointHistoriesWithLoginIdAndCode(
                any(),
                any(),
                any()
        )).thenReturn(response);

        ResultActions result = mockMvc.perform(get("/v1/point-histories")
                .with(csrf())
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
                any(),
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
                requestParameters(
                        parameterWithName("code").description("????????? ??????/?????? ??????"),
                        parameterWithName("page").description("????????? ??????")
                                .optional()
                                .attributes(defaultValue(10)),
                        parameterWithName("size").description("????????? ?????? ??????")
                                .optional()
                                .attributes(defaultValue(0)),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("?????? ?????? ??????"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("HTTP ?????? ??????"),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER)
                                .description("?????? ?????????"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER)
                                .description("?????? ?????????"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER)
                                .description("??? ????????? ??????"),
                        fieldWithPath("data.dataList.[].id").type(JsonFieldType.NUMBER)
                                .description("????????? ?????? Pk"),
                        fieldWithPath("data.dataList.[].amount").type(JsonFieldType.NUMBER)
                                .description("????????? ??????/?????? ???"),
                        fieldWithPath("data.dataList.[].createDateTime").type(JsonFieldType.STRING)
                                .description("????????? ??????/?????? ??????"),
                        fieldWithPath("data.dataList.[].pointCode").type(JsonFieldType.STRING)
                                .description("????????? ??????"),
                        fieldWithPath("data.dataList.[].pointReasonCode").type(JsonFieldType.STRING)
                                .description("????????? ?????? ??????"),
                        fieldWithPath("errorMessages").type(JsonFieldType.STRING)
                                .description("?????? ?????????")
                                .optional()

                )
        ));

    }

    @Test
    @DisplayName("????????? ????????? ?????? ?????? - ????????? ??????")
    @WithMockUser(username = "user@1", authorities = "ROLE_ADMIN")
    void getMemberPoint_fail_memberNotFound() throws Exception {
        //given
        Mockito.when(pointHistoryService.getMemberPoint(any()))
                .thenThrow(new ClientException(ErrorCode.MEMBER_NOT_FOUND, ""));

        //when
        ResultActions result = mockMvc.perform(get("/v1/points").with(csrf()));

        //then
        result.andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(false)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath(
                        "$.errorMessages[0]",
                        equalTo(ErrorCode.MEMBER_NOT_FOUND.getDisplayName())
                ));

        //docs
        result.andDo(document(
                "get-member-point-fail-member-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("?????? ?????? ??????"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("HTTP ?????? ??????"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT)
                                .description("null")
                                .optional(),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("?????? ?????????")
                )
        ));
    }

    @Test
    @DisplayName("????????? ????????? ?????? ??????")
    @WithMockUser(username = "user@1", authorities = "ROLE_ADMIN")
    void getMemberPoint_success() throws Exception {
        //given
        long amount = 1000;

        Mockito.when(pointHistoryService.getMemberPoint(any())).thenReturn(amount);

        //when
        ResultActions result = mockMvc.perform(get("/v1/points").with(csrf()));

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
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("?????? ?????? ??????"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("HTTP ?????? ??????"),
                        fieldWithPath("data").type(JsonFieldType.NUMBER)
                                .description("????????? ????????? ???"),
                        fieldWithPath("errorMessages").type(JsonFieldType.STRING)
                                .description("?????? ?????????")
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
        return new PageImpl<>(content, pageable, size);
    }
}
