package shop.yesaladin.shop.point.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.point.domain.model.PointCode;
import shop.yesaladin.shop.point.domain.model.PointReasonCode;
import shop.yesaladin.shop.point.dto.PointHistoryRequestDto;
import shop.yesaladin.shop.point.dto.PointHistoryResponseDto;
import shop.yesaladin.shop.point.exception.OverPointUseException;
import shop.yesaladin.shop.point.service.inter.CommandPointHistoryService;

@AutoConfigureRestDocs
@WebMvcTest(CommandPointHistoryController.class)
class CommandPointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommandPointHistoryService commandPointHistoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("포인트 사용 실패 - 잘못된 파라미터를 요청한 경우")
    void createPointHistory_fail_InvalidCodeParameter() throws Exception {
        //given
        String loginId = "user@1";
        Long amount = 1000L;
        PointReasonCode pointReasonCode = PointReasonCode.USE_ORDER;

        PointHistoryRequestDto request = ReflectionUtils.newInstance(
                PointHistoryRequestDto.class,
                loginId,
                amount,
                pointReasonCode
        );

        //when
        ResultActions result = mockMvc.perform(post("/v1/points")
                .param("code", "asel")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

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
                "create-point-history-fail-invalid-point-parameter",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(parameterWithName("code").description("포인트 사용/적립 구분")),
                requestFields(
                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("회원의 아이디"),
                        fieldWithPath("amount").type(JsonFieldType.NUMBER).description("사용한 포인트 양"),
                        fieldWithPath("pointReasonCode").type(JsonFieldType.STRING)
                                .description("포인트 사용 사유")
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
    @DisplayName("포인트 사용 실패 - 존재하지 않는 회원 아이디인 경우")
    void createPointHistory_use_fail_NotFoundMember() throws Exception {
        //given
        String loginId = "user@1";
        Long amount = 1000L;
        PointReasonCode pointReasonCode = PointReasonCode.USE_ORDER;

        PointHistoryRequestDto request = ReflectionUtils.newInstance(
                PointHistoryRequestDto.class,
                loginId,
                amount,
                pointReasonCode
        );

        Mockito.when(commandPointHistoryService.use(any())).thenThrow(
                new MemberNotFoundException("Member Id: " + loginId));

        //when
        ResultActions result = mockMvc.perform(post("/v1/points")
                .param("code", "use")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result.andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", startsWith("Member not found")));

        ArgumentCaptor<PointHistoryRequestDto> captor = ArgumentCaptor.forClass(
                PointHistoryRequestDto.class);
        Mockito.verify(commandPointHistoryService, Mockito.times(1))
                .use(captor.capture());
        Assertions.assertThat(captor.getValue().getAmount()).isEqualTo(amount);
        Assertions.assertThat(captor.getValue().getLoginId()).isEqualTo(loginId);
        Assertions.assertThat(captor.getValue().getPointReasonCode()).isEqualTo(pointReasonCode);

        //docs
        result.andDo(document(
                "create-point-history-use-fail-member-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(parameterWithName("code").description("포인트 사용/적립 구분")),
                requestFields(
                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("회원의 아이디"),
                        fieldWithPath("amount").type(JsonFieldType.NUMBER).description("사용한 포인트 양"),
                        fieldWithPath("pointReasonCode").type(JsonFieldType.STRING)
                                .description("포인트 사용 사유")
                ),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                )
        ));
    }

    @Test
    @DisplayName("포인트 사용 실패 - 회원이 소유한 포인트보다 많이 사용하고자한 경우")
    void createPointHistory_use_fail_OverPoint() throws Exception {
        //given
        String loginId = "user@1";
        Long amount = 100000L;
        PointReasonCode pointReasonCode = PointReasonCode.USE_ORDER;

        PointHistoryRequestDto request = ReflectionUtils.newInstance(
                PointHistoryRequestDto.class,
                loginId,
                amount,
                pointReasonCode
        );

        Mockito.when(commandPointHistoryService.use(any())).thenThrow(new OverPointUseException());

        //when
        ResultActions result = mockMvc.perform(post("/v1/points")
                .param("code", "use")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        ErrorCode code = ErrorCode.POINT_OVER_USE;
        result.andDo(print()).andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(false)))
                .andExpect(jsonPath("$.status", equalTo(code.getResponseStatus().value())))
                .andExpect(jsonPath("$.data", equalTo(null)))
                .andExpect(jsonPath("$.errorMessages[0]", equalTo(code.getDisplayName())));

        ArgumentCaptor<PointHistoryRequestDto> captor = ArgumentCaptor.forClass(
                PointHistoryRequestDto.class);
        Mockito.verify(commandPointHistoryService, Mockito.times(1))
                .use(captor.capture());
        Assertions.assertThat(captor.getValue().getAmount()).isEqualTo(amount);
        Assertions.assertThat(captor.getValue().getLoginId()).isEqualTo(loginId);
        Assertions.assertThat(captor.getValue().getPointReasonCode()).isEqualTo(pointReasonCode);

        //docs
        result.andDo(document(
                "create-point-history-use-fail-over-point",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(parameterWithName("code").description("포인트 사용/적립 구분")),
                requestFields(
                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("회원의 아이디"),
                        fieldWithPath("amount").type(JsonFieldType.NUMBER).description("사용한 포인트 양"),
                        fieldWithPath("pointReasonCode").type(JsonFieldType.STRING)
                                .description("포인트 사용 사유")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공유무"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data").type(JsonFieldType.NUMBER)
                                .description("데이")
                                .optional(),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("에러 메세지")
                )
        ));
    }

    @Test
    @DisplayName("포인트 사용 성공")
    void createPointHistory_use_success() throws Exception {
        //given
        String loginId = "user@1";
        Long amount = 1000L;
        PointReasonCode pointReasonCode = PointReasonCode.USE_ORDER;

        PointHistoryRequestDto request = ReflectionUtils.newInstance(
                PointHistoryRequestDto.class,
                loginId,
                amount,
                pointReasonCode
        );
        long pointHistoryId = 1;
        long curAmount = 1000;
        PointHistoryResponseDto response = ReflectionUtils.newInstance(
                PointHistoryResponseDto.class,
                pointHistoryId,
                curAmount,
                LocalDateTime.now(),
                PointCode.USE,
                pointReasonCode
        );
        Mockito.when(commandPointHistoryService.use(any())).thenReturn(response);
        //when
        ResultActions result = mockMvc.perform(post("/v1/points")
                .param("code", "use")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then

        result.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.CREATED.value())))
                .andExpect(jsonPath("$.data.id", equalTo((int) pointHistoryId)))
                .andExpect(jsonPath("$.data.amount", equalTo((int) curAmount)))
                .andExpect(jsonPath("$.data.pointCode", equalTo("USE")))
                .andExpect(jsonPath("$.data.pointReasonCode", equalTo(pointReasonCode.name())));

        ArgumentCaptor<PointHistoryRequestDto> captor = ArgumentCaptor.forClass(
                PointHistoryRequestDto.class);
        Mockito.verify(commandPointHistoryService, Mockito.times(1))
                .use(captor.capture());
        Assertions.assertThat(captor.getValue().getAmount()).isEqualTo(amount);
        Assertions.assertThat(captor.getValue().getLoginId()).isEqualTo(loginId);
        Assertions.assertThat(captor.getValue().getPointReasonCode()).isEqualTo(pointReasonCode);

        //docs
        result.andDo(document(
                "create-point-history-use-success",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(parameterWithName("code").description("포인트 사용/적립 구분")),
                requestFields(
                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("회원의 아이디"),
                        fieldWithPath("amount").type(JsonFieldType.NUMBER).description("사용한 포인트 양"),
                        fieldWithPath("pointReasonCode").type(JsonFieldType.STRING)
                                .description("포인트 사용 사유")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공유무"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                .description("포인트 등록 내역 pk"),
                        fieldWithPath("data.amount").type(JsonFieldType.NUMBER)
                                .description("포인트 양"),
                        fieldWithPath("data.createDateTime").type(JsonFieldType.STRING)
                                .description("포인트내역 등록 일시"),
                        fieldWithPath("data.pointCode").type(JsonFieldType.STRING)
                                .description("포인트내역 등록 코드: 사용"),
                        fieldWithPath("data.pointReasonCode").type(JsonFieldType.STRING)
                                .description("포인트 사유 코드"),
                        fieldWithPath("errorMessages").type(JsonFieldType.STRING)
                                .description("에러 메세지")
                                .optional()
                )
        ));
    }

    @Test
    @DisplayName("포인트 적립 실패 - 존재하지 않는 회원 아이디인 경우")
    void createPointHistory_save_fail_MemberNotFound() throws Exception {
        //given
        String loginId = "user@1";
        Long amount = 1000L;
        PointReasonCode pointReasonCode = PointReasonCode.SAVE_ORDER;

        PointHistoryRequestDto request = ReflectionUtils.newInstance(
                PointHistoryRequestDto.class,
                loginId,
                amount,
                pointReasonCode
        );

        Mockito.when(commandPointHistoryService.save(any())).thenThrow(
                new MemberNotFoundException("Member Id: " + loginId));

        //when
        ResultActions result = mockMvc.perform(post("/v1/points")
                .param("code", "save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result.andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", startsWith("Member not found")));

        ArgumentCaptor<PointHistoryRequestDto> captor = ArgumentCaptor.forClass(
                PointHistoryRequestDto.class);
        Mockito.verify(commandPointHistoryService, Mockito.times(1))
                .save(captor.capture());
        Assertions.assertThat(captor.getValue().getAmount()).isEqualTo(amount);
        Assertions.assertThat(captor.getValue().getLoginId()).isEqualTo(loginId);
        Assertions.assertThat(captor.getValue().getPointReasonCode()).isEqualTo(pointReasonCode);

        //docs
        result.andDo(document(
                "create-point-history-save-fail-member-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(parameterWithName("code").description("포인트 사용/적립 구분")),
                requestFields(
                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("회원의 아이디"),
                        fieldWithPath("amount").type(JsonFieldType.NUMBER).description("적립한 포인트 양"),
                        fieldWithPath("pointReasonCode").type(JsonFieldType.STRING)
                                .description("포인트 적립 사유")
                ),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                )

        ));
    }

    @Test
    @DisplayName("포인트 적립 성공")
    void createPointHistory_save_success() throws Exception {
        //given
        String loginId = "user@1";
        Long amount = 1000L;
        PointReasonCode pointReasonCode = PointReasonCode.SAVE_ORDER;

        PointHistoryRequestDto request = ReflectionUtils.newInstance(
                PointHistoryRequestDto.class,
                loginId,
                amount,
                pointReasonCode
        );
        long pointHistoryId = 1;
        long curAmount = 1000;
        PointHistoryResponseDto response = ReflectionUtils.newInstance(
                PointHistoryResponseDto.class,
                pointHistoryId,
                curAmount,
                LocalDateTime.now(),
                PointCode.SAVE,
                PointReasonCode.SAVE_ORDER
        );
        Mockito.when(commandPointHistoryService.save(any())).thenReturn(response);

        //when
        ResultActions result = mockMvc.perform(post("/v1/points")
                .param("code", "save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.CREATED.value())))
                .andExpect(jsonPath("$.data.id", equalTo((int) pointHistoryId)))
                .andExpect(jsonPath("$.data.amount", equalTo((int) curAmount)))
                .andExpect(jsonPath("$.data.pointCode", equalTo("SAVE")))
                .andExpect(jsonPath("$.data.pointReasonCode", equalTo(pointReasonCode.name())));

        ArgumentCaptor<PointHistoryRequestDto> captor = ArgumentCaptor.forClass(
                PointHistoryRequestDto.class);
        Mockito.verify(commandPointHistoryService, Mockito.times(1))
                .save(captor.capture());
        Assertions.assertThat(captor.getValue().getAmount()).isEqualTo(amount);
        Assertions.assertThat(captor.getValue().getLoginId()).isEqualTo(loginId);
        Assertions.assertThat(captor.getValue().getPointReasonCode()).isEqualTo(pointReasonCode);

        //docs
        result.andDo(document(
                "create-point-history-save-success",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(parameterWithName("code").description("포인트 사용/적립 구분")),
                requestFields(
                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("회원의 아이디"),
                        fieldWithPath("amount").type(JsonFieldType.NUMBER).description("적립한 포인트 양"),
                        fieldWithPath("pointReasonCode").type(JsonFieldType.STRING)
                                .description("포인트 적립 사유")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공유무"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                .description("포인트 등록 내역 pk"),
                        fieldWithPath("data.amount").type(JsonFieldType.NUMBER)
                                .description("포인트 양"),
                        fieldWithPath("data.createDateTime").type(JsonFieldType.STRING)
                                .description("포인트내역 등록 일시"),
                        fieldWithPath("data.pointCode").type(JsonFieldType.STRING)
                                .description("포인트내역 등록 코드: 적립"),
                        fieldWithPath("data.pointReasonCode").type(JsonFieldType.STRING)
                                .description("포인트 사유 코드"),
                        fieldWithPath("errorMessages").type(JsonFieldType.STRING)
                                .description("에러 메세지")
                                .optional()
                )
        ));
    }
}