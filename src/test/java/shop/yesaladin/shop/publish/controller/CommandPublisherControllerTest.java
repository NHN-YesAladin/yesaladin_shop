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
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.publish.dto.PublisherRequestDto;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
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
    @DisplayName("????????? ?????? ??????")
    void registerPublisher_success() throws Exception {
        // given
        String name = "?????????1";
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
                        fieldWithPath("name").type(JsonFieldType.STRING).description("????????????")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("?????? ?????? ??????"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("??????"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                .description("????????? ????????? ?????????"),
                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("????????????"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("?????? ?????????")
                                .optional()
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("????????? ?????? ??????_?????? ???????????? ??????????????? ????????? ?????? ?????? ??????")
    void registerPublisher_throwPublisherAlreadyExistsException() throws Exception {
        // given
        String name = "?????????1";
        PublisherRequestDto createDto = new PublisherRequestDto(name);

        Mockito.when(service.create(any())).thenThrow(
                new ClientException(
                        ErrorCode.PUBLISH_ALREADY_EXIST,
                        "Publisher you are trying to create already exists => publisher name : "
                                + createDto.getName()
                )
        );

        // when
        ResultActions result = mockMvc.perform(post("/v1/publishers")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(createDto)));

        // then
        result.andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success", equalTo(false)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.CONFLICT.value())))
                .andExpect(jsonPath(
                        "$.errorMessages[0]",
                        equalTo(ErrorCode.PUBLISH_ALREADY_EXIST.getDisplayName())
                ));

        verify(service, times(1)).create(any());
    }

    @WithMockUser
    @Test
    @DisplayName("????????? ?????? ??????")
    void modifyPublisher_success() throws Exception {
        // given
        Long id = 1L;
        String name1 = "?????????1";
        String name2 = "?????????2";

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
                        fieldWithPath("name").type(JsonFieldType.STRING).description("????????????")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("?????? ?????? ??????"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("HTTP ?????? ??????"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                .description("????????? ????????? ?????????"),
                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("????????????"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("?????? ?????????")
                                .optional()
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("????????? ?????? ??????_?????? ???????????? ??????????????? ????????? ?????? ?????? ??????")
    void modifyPublisher_throwPublisherAlreadyExistsException() throws Exception {
        // given
        Long id = 1L;
        String name = "?????????1";
        PublisherRequestDto modifyDto = new PublisherRequestDto(name);

        Mockito.when(service.modify(any(), any())).thenThrow(
                new ClientException(
                        ErrorCode.PUBLISH_ALREADY_EXIST,
                        "Publisher you are trying to modify already exists => publisher name : "
                                + modifyDto.getName()
                )
        );

        // when
        ResultActions result = mockMvc.perform(put("/v1/publishers/{publisherId}", id)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(modifyDto)));

        // then
        result.andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success", equalTo(false)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.CONFLICT.value())))
                .andExpect(jsonPath(
                        "$.errorMessages[0]",
                        equalTo(ErrorCode.PUBLISH_ALREADY_EXIST.getDisplayName())
                ));

        verify(service, times(1)).modify(any(), any());
    }
}