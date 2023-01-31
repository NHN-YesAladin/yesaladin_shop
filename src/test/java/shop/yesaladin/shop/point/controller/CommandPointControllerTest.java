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
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.point.domain.model.PointCode;
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
        PointHistoryRequestDto request = ReflectionUtils.newInstance(
                PointHistoryRequestDto.class,
                loginId,
                amount
        );

        Mockito.when(commandPointHistoryService.use(any()))
                .thenThrow(MemberNotFoundException.class);

        //when
        ResultActions result = mockMvc.perform(post("/v1/points")
                .param("code", "asel")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", startsWith("Invalid Code Parameter:")));

        //docs
        result.andDo(document(
                "create-point-history-fail-invalid-point-parameter",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(parameterWithName("code").description("포인트 사용/적립 구분")),
                requestFields(
                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("회원의 아이디"),
                        fieldWithPath("amount").type(JsonFieldType.NUMBER).description("사용한 포인트 양")
                ),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                )
        ));
    }

    @Test
    @DisplayName("포인트 사용 실패 - 존재하지 않는 회원 아이디인 경우")
    void createPointHistory_use_fail_NotFoundMember() throws Exception {
        //given
        String loginId = "user@1";
        Long amount = 1000L;
        PointHistoryRequestDto request = ReflectionUtils.newInstance(
                PointHistoryRequestDto.class,
                loginId,
                amount
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

        //docs
        result.andDo(document(
                "create-point-history-use-fail-member-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(parameterWithName("code").description("포인트 사용/적립 구분")),
                requestFields(
                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("회원의 아이디"),
                        fieldWithPath("amount").type(JsonFieldType.NUMBER).description("사용한 포인트 양")
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

        PointHistoryRequestDto request = ReflectionUtils.newInstance(
                PointHistoryRequestDto.class,
                loginId,
                amount
        );

        Mockito.when(commandPointHistoryService.use(any())).thenThrow(OverPointUseException.class);

        //when
        ResultActions result = mockMvc.perform(post("/v1/points")
                .param("code", "use")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", startsWith("Over Point Use")));

        ArgumentCaptor<PointHistoryRequestDto> captor = ArgumentCaptor.forClass(
                PointHistoryRequestDto.class);
        Mockito.verify(commandPointHistoryService, Mockito.times(1))
                .use(captor.capture());
        Assertions.assertThat(captor.getValue().getAmount()).isEqualTo(amount);
        Assertions.assertThat(captor.getValue().getLoginId()).isEqualTo(loginId);

        //docs
        result.andDo(document(
                "create-point-history-use-fail-over-point",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(parameterWithName("code").description("포인트 사용/적립 구분")),
                requestFields(
                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("회원의 아이디"),
                        fieldWithPath("amount").type(JsonFieldType.NUMBER).description("사용한 포인트 양")
                ),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                )
        ));
    }

    @Test
    @DisplayName("포인트 사용 성공")
    void createPointHistory_use_success() throws Exception {
        //given
        String loginId = "user@1";
        Long amount = 1000L;

        PointHistoryRequestDto request = ReflectionUtils.newInstance(
                PointHistoryRequestDto.class,
                loginId,
                amount
        );
        long pointHistoryId = 1;
        long curAmount = 1000;
        PointHistoryResponseDto response = ReflectionUtils.newInstance(
                PointHistoryResponseDto.class,
                pointHistoryId,
                curAmount,
                LocalDateTime.now(),
                PointCode.USE,
                loginId
        );
        Mockito.when(commandPointHistoryService.use(any())).thenReturn(response);
        //when
        ResultActions result = mockMvc.perform(post("/v1/points")
                .param("code", "use")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id", equalTo((int) pointHistoryId)));
        result.andExpect(jsonPath("$.amount", equalTo((int) curAmount)));
        result.andExpect(jsonPath("$.pointCode", equalTo("USE")));
        result.andExpect(jsonPath("$.loginId", equalTo(loginId)));

        ArgumentCaptor<PointHistoryRequestDto> captor = ArgumentCaptor.forClass(
                PointHistoryRequestDto.class);
        Mockito.verify(commandPointHistoryService, Mockito.times(1))
                .use(captor.capture());
        Assertions.assertThat(captor.getValue().getAmount()).isEqualTo(amount);
        Assertions.assertThat(captor.getValue().getLoginId()).isEqualTo(loginId);

        //docs
        result.andDo(document(
                "create-point-history-use-success",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(parameterWithName("code").description("포인트 사용/적립 구분")),
                requestFields(
                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("회원의 아이디"),
                        fieldWithPath("amount").type(JsonFieldType.NUMBER).description("사용한 포인트 양")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("포인트 등록 내역 pk"),
                        fieldWithPath("amount").type(JsonFieldType.NUMBER).description("포인트 양"),
                        fieldWithPath("createDateTime").type(JsonFieldType.STRING)
                                .description("포인트내역 등록 일시"),
                        fieldWithPath("pointCode").type(JsonFieldType.STRING)
                                .description("포인트내역 등록 코드: 사용"),
                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("회원 아이디")
                )
        ));
    }

    @Test
    @DisplayName("포인트 적립 실패 - 존재하지 않는 회원 아이디인 경우")
    void createPointHistory_save_fail_MemberNotFound() throws Exception {
        //given
        String loginId = "user@1";
        Long amount = 1000L;
        PointHistoryRequestDto request = ReflectionUtils.newInstance(
                PointHistoryRequestDto.class,
                loginId,
                amount
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

        //docs
        result.andDo(document(
                "create-point-history-save-fail-member-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(parameterWithName("code").description("포인트 사용/적립 구분")),
                requestFields(
                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("회원의 아이디"),
                        fieldWithPath("amount").type(JsonFieldType.NUMBER).description("적립한 포인트 양")
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
        PointHistoryRequestDto request = ReflectionUtils.newInstance(
                PointHistoryRequestDto.class,
                loginId,
                amount
        );
        long pointHistoryId = 1;
        long curAmount = 1000;
        PointHistoryResponseDto response = ReflectionUtils.newInstance(
                PointHistoryResponseDto.class,
                pointHistoryId,
                curAmount,
                LocalDateTime.now(),
                PointCode.SAVE,
                loginId
        );
        Mockito.when(commandPointHistoryService.save(any())).thenReturn(response);

        //when
        ResultActions result = mockMvc.perform(post("/v1/points")
                .param("code", "save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result.andExpect(status().isOk());
        result.andExpect(content().contentType(MediaType.APPLICATION_JSON));
        result.andExpect(jsonPath("$.id", equalTo((int) pointHistoryId)));
        result.andExpect(jsonPath("$.amount", equalTo((int) curAmount)));
        result.andExpect(jsonPath("$.pointCode", equalTo("SAVE")));
        result.andExpect(jsonPath("$.loginId", equalTo(loginId)));

        ArgumentCaptor<PointHistoryRequestDto> captor = ArgumentCaptor.forClass(
                PointHistoryRequestDto.class);
        Mockito.verify(commandPointHistoryService, Mockito.times(1))
                .save(captor.capture());
        Assertions.assertThat(captor.getValue().getAmount()).isEqualTo(amount);
        Assertions.assertThat(captor.getValue().getLoginId()).isEqualTo(loginId);

        //docs
        result.andDo(document(
                "create-point-history-save-success",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(parameterWithName("code").description("포인트 사용/적립 구분")),
                requestFields(
                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("회원의 아이디"),
                        fieldWithPath("amount").type(JsonFieldType.NUMBER).description("적립한 포인트 양")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("포인트 등록 내역 pk"),
                        fieldWithPath("amount").type(JsonFieldType.NUMBER).description("포인트 양"),
                        fieldWithPath("createDateTime").type(JsonFieldType.STRING)
                                .description("포인트내역 등록 일시"),
                        fieldWithPath("pointCode").type(JsonFieldType.STRING)
                                .description("포인트내역 등록 코드: 적립"),
                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("회원 아이디")
                )
        ));
    }
}