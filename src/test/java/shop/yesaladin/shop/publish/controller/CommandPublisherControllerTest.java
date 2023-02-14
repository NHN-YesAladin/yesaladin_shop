package shop.yesaladin.shop.publish.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.publish.dto.PublisherRequestDto;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.publish.exception.PublisherAlreadyExistsException;
import shop.yesaladin.shop.publish.service.inter.CommandPublisherService;

@AutoConfigureRestDocs
@WebMvcTest(CommandPublisherController.class)
class CommandPublisherControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommandPublisherService service;

    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser
    @Test
    @DisplayName("출판사 등록 성공")
    void registerPublisher_success() throws Exception {
        // given
        String name = "출판사1";
        PublisherRequestDto createDto = new PublisherRequestDto(name);
        PublisherResponseDto responseDto = new PublisherResponseDto(1L, name);
        Mockito.when(service.create(any())).thenReturn(responseDto);

        // when
        ResultActions result = mockMvc.perform(post("/v1/publishers")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(createDto)));

        // then
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.CREATED.value())))
                .andExpect(jsonPath("$.data.id", equalTo(1)))
                .andExpect(jsonPath("$.data.name", equalTo(name)));

        verify(service, times(1)).create(any());

        // docs
        result.andDo(document(
                "register-publisher",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("출판사명")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("생성된 출판사 아이디"),
                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("출판사명"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY).description("에러 메세지").optional()
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("출판사 등록 실패_이미 존재하는 출판사명을 입력한 경우 예외 발생")
    void registerPublisher_throwPublisherAlreadyExistsException() throws Exception {
        // given
        String name = "출판사1";
        PublisherRequestDto createDto = new PublisherRequestDto(name);

        Mockito.when(service.create(any())).thenThrow(PublisherAlreadyExistsException.class);

        // when
        ResultActions result = mockMvc.perform(post("/v1/publishers")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(createDto)));

        // then
        result.andDo(print())
                .andExpect(status().isConflict());

        verify(service, times(1)).create(any());
    }

    @WithMockUser
    @Test
    @DisplayName("출판사 수정 성공")
    void modifyPublisher_success() throws Exception {
        // given
        Long id = 1L;
        String name1 = "출판사1";
        String name2 = "출판사2";

        PublisherRequestDto modifyDto = new PublisherRequestDto(name1);
        PublisherResponseDto responseDto = new PublisherResponseDto(id, name2);
        Mockito.when(service.modify(anyLong(), any())).thenReturn(responseDto);

        // when
        ResultActions result = mockMvc.perform(put("/v1/publishers/{publisherId}", id)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(modifyDto)));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.data.id", equalTo(1)))
                .andExpect(jsonPath("$.data.name", equalTo(name2)));

        verify(service, times(1)).modify(anyLong(), any());

        // docs
        result.andDo(document(
                "modify-publisher",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("출판사명")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("수정된 출판사 아이디"),
                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("출판사명"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY).description("에러 메세지").optional()
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("출판사 등록 실패_이미 존재하는 출판사명을 입력한 경우 예외 발생")
    void modifyPublisher_throwPublisherAlreadyExistsException() throws Exception {
        // given
        Long id = 1L;
        String name = "출판사1";
        PublisherRequestDto modifyDto = new PublisherRequestDto(name);

        Mockito.when(service.modify(any(), any())).thenThrow(PublisherAlreadyExistsException.class);

        // when
        ResultActions result = mockMvc.perform(put("/v1/publishers/{publisherId}", id)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(modifyDto)));

        // then
        result.andDo(print())
                .andExpect(status().isConflict());

        verify(service, times(1)).modify(any(), any());
    }
}