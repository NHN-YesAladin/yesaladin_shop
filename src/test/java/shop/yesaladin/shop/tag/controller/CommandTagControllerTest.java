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
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.tag.dto.TagRequestDto;
import shop.yesaladin.shop.tag.dto.TagResponseDto;
import shop.yesaladin.shop.tag.exception.TagAlreadyExistsException;
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
    @DisplayName("태그 등록 성공")
    void registerTag_success() throws Exception {
        // given
        String name = "아름다운";
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
                .andExpect(jsonPath("id", equalTo(1)))
                .andExpect(jsonPath("name", equalTo(name)));

        verify(service, times(1)).create(any());

        // docs
        result.andDo(document(
                "register-tag",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("태그명")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("생성된 태그 아이디"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("태그명")
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("태그 등록 실패_이미 존재하는 태그명을 입력한 경우 예외 발생")
    void registerTag_throwTagAlreadyExistsException() throws Exception {
        // given
        String name = "아름다운";
        TagRequestDto createDto = new TagRequestDto(name);

        Mockito.when(service.create(any())).thenThrow(TagAlreadyExistsException.class);

        // when
        ResultActions result = mockMvc.perform(post("/v1/tags")
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
    @DisplayName("태그 수정 성공")
    void modifyTag_success() throws Exception {
        // given
        Long id = 1L;
        String name1 = "아름다운";
        String name2 = "슬픈";

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
                .andExpect(jsonPath("id", equalTo(1)))
                .andExpect(jsonPath("name", equalTo(name2)));

        verify(service, times(1)).modify(anyLong(), any());

        // docs
        result.andDo(document(
                "modify-tag",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("태그명")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("수정된 태그 아이디"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("태그명")
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("태그 등록 실패_이미 존재하는 태그명을 입력한 경우 예외 발생")
    void modifyTag_throwTagAlreadyExistsException() throws Exception {
        // given
        Long id = 1L;
        String name = "아름다운";
        TagRequestDto modifyDto = new TagRequestDto(name);

        Mockito.when(service.modify(any(), any())).thenThrow(TagAlreadyExistsException.class);

        // when
        ResultActions result = mockMvc.perform(put("/v1/tags/{tagId}", id)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(modifyDto)));

        // then
        result.andDo(print())
                .andExpect(status().isConflict());

        verify(service, times(1)).modify(any(), any());
    }
}