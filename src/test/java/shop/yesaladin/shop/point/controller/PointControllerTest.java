package shop.yesaladin.shop.point.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.point.dto.PointHistoryRequestDto;
import shop.yesaladin.shop.point.service.inter.CommandPointHistoryService;

@WebMvcTest(PointController.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@AutoConfigureRestDocs
class PointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommandPointHistoryService commandPointHistoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("존재하지 않는 회원 아이디인 경우")
    void usePoint_fail_NotFoundMember() throws Exception {
        //given
        long memberId = 1L;
        Long amount = 1000L;
        PointHistoryRequestDto request = ReflectionUtils.newInstance(
                PointHistoryRequestDto.class,
                amount
        );

        Mockito.when(commandPointHistoryService.use(eq(memberId), any())).thenThrow(
                MemberNotFoundException.class);

        //when
        ResultActions result = mockMvc.perform(post("/v1/points/{memberId}/use", memberId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result.andExpect(status().isNotFound());

        ArgumentCaptor<PointHistoryRequestDto> captor = ArgumentCaptor.forClass(
                PointHistoryRequestDto.class);
        Mockito.verify(commandPointHistoryService, Mockito.times(1))
                .use(anyLong(), captor.capture());
        Assertions.assertThat(captor.getValue().getAmount()).isEqualTo(request.getAmount());

        result.andDo(document(
                "use-point-fail-member-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("memberId").description("회원의 Pk")),
                requestFields(
                        fieldWithPath("amount").type(JsonFieldType.NUMBER)
                                .description("사용한 포인트 양")
                )
        ));
    }

    @Test
    @DisplayName("회원이 소유한 포인트보다 많이 사용하고자한 경우")
    void usePoint_fail_OverPoint() throws Exception {
        //given
        long memberId = 1L;
        Long amount = 100000L;

        PointHistoryRequestDto request = ReflectionUtils.newInstance(
                PointHistoryRequestDto.class,
                amount
        );

        Mockito.when(commandPointHistoryService.use(eq(memberId), any())).thenThrow(
                MemberNotFoundException.class);

        //when
        ResultActions result = mockMvc.perform(post("/v1/points/{memberId}/use", memberId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result.andExpect(status().isNotFound());

        ArgumentCaptor<PointHistoryRequestDto> captor = ArgumentCaptor.forClass(
                PointHistoryRequestDto.class);
        Mockito.verify(commandPointHistoryService, Mockito.times(1))
                .use(anyLong(), captor.capture());
        Assertions.assertThat(captor.getValue().getAmount()).isEqualTo(request.getAmount());

        result.andDo(document(
                "use-point-fail-over-point",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("memberId").description("회원의 Pk")),
                requestFields(
                        fieldWithPath("amount").type(JsonFieldType.NUMBER)
                                .description("사용한 포인트 양")
                )
        ));
    }

    @Test
    @DisplayName("포인트 사용 성공")
    void usePoint() throws Exception {
        //given
        long memberId = 1L;
        Long amount = 1000L;
        PointHistoryRequestDto request = ReflectionUtils.newInstance(
                PointHistoryRequestDto.class,
                amount
        );

        //when
        ResultActions result = mockMvc.perform(post("/v1/points/{memberId}/use", memberId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result.andExpect(status().isOk());

        ArgumentCaptor<PointHistoryRequestDto> captor = ArgumentCaptor.forClass(
                PointHistoryRequestDto.class);
        Mockito.verify(commandPointHistoryService, Mockito.times(1))
                .use(anyLong(), captor.capture());
        Assertions.assertThat(captor.getValue().getAmount()).isEqualTo(request.getAmount());

        result.andDo(document(
                "use-point-success",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("memberId").description("회원의 Pk")),
                requestFields(
                        fieldWithPath("amount").type(JsonFieldType.NUMBER)
                                .description("사용한 포인트 양")
                )
        ));
    }
//    result.andDo(document(
//                "use-point-not-found-member",
//                getDocumentRequest(),
//                getDocumentResponse(),
//                requestParameters(
//                        parameterWithName("amount").description("사용한 포인트 양")
//                ),
//                requestFields(
//                        fieldWithPath("amount").type(JsonFieldType.NUMBER)
//                                .description("사용한 포인트 양")
//                ),
//                responseFields(
//                        fieldWithPath("")
//                )
//        ))

    @Test
    void savePoint() {
    }
}