package shop.yesaladin.shop.tag.controller;

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
import shop.yesaladin.shop.tag.dto.TagRequestDto;
import shop.yesaladin.shop.tag.dto.TagResponseDto;
import shop.yesaladin.shop.tag.service.inter.CommandTagService;

@AutoConfigureRestDocs
@WebMvcTest(CommandTagController.class)
class CommandTagControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommandTagService service;

    @Autowired
    private ObjectMapper objectMapper;


    @WithMockUser
    @Test
    @DisplayName("?????? ?????? ??????")
    void registerTag_success() throws Exception {
        // given
        String name = "????????????";
        TagRequestDto createDto = new TagRequestDto(name);
        TagResponseDto responseDto = new TagResponseDto(1L, name);
        Mockito.when(service.create(any())).thenReturn(responseDto);

        // when
        ResultActions result = mockMvc.perform(post("/v1/tags")
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
                "register-tag",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("?????????")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("?????? ?????? ??????"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("??????"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                .description("????????? ?????? ?????????"),
                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("?????????"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("?????? ?????????")
                                .optional()
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("?????? ?????? ??????_?????? ???????????? ???????????? ????????? ?????? ?????? ??????")
    void registerTag_throwTagAlreadyExistsException() throws Exception {
        // given
        String name = "????????????";
        TagRequestDto createDto = new TagRequestDto(name);

        Mockito.when(service.create(any())).thenThrow(
                new ClientException(
                        ErrorCode.TAG_ALREADY_EXIST,
                        "Tag name already exists with name : " + createDto.getName()
                )
        );

        // when
        ResultActions result = mockMvc.perform(post("/v1/tags")
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
                        equalTo(ErrorCode.TAG_ALREADY_EXIST.getDisplayName())
                ));

        verify(service, times(1)).create(any());
    }

    @WithMockUser
    @Test
    @DisplayName("?????? ?????? ??????")
    void modifyTag_success() throws Exception {
        // given
        Long id = 1L;
        String name1 = "????????????";
        String name2 = "??????";

        TagRequestDto modifyDto = new TagRequestDto(name1);
        TagResponseDto responseDto = new TagResponseDto(id, name2);
        Mockito.when(service.modify(anyLong(), any())).thenReturn(responseDto);

        // when
        ResultActions result = mockMvc.perform(put("/v1/tags/{tagId}", id)
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
                "modify-tag",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("?????????")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("?????? ?????? ??????"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("??????"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                .description("????????? ?????? ?????????"),
                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("?????????"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("?????? ?????????")
                                .optional()
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("?????? ?????? ??????_?????? ???????????? ???????????? ????????? ?????? ?????? ??????")
    void modifyTag_throwTagAlreadyExistsException() throws Exception {
        // given
        Long id = 1L;
        String name = "????????????";
        TagRequestDto modifyDto = new TagRequestDto(name);

        Mockito.when(service.modify(any(), any())).thenThrow(
                new ClientException(
                        ErrorCode.TAG_ALREADY_EXIST,
                        "Tag name already exists with name : " + modifyDto.getName()
                )
        );

        // when
        ResultActions result = mockMvc.perform(put("/v1/tags/{tagId}", id)
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
                        equalTo(ErrorCode.TAG_ALREADY_EXIST.getDisplayName())
                ));

        verify(service, times(1)).modify(any(), any());
    }
}