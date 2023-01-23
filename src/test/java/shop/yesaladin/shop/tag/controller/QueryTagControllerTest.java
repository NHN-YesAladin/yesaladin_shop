package shop.yesaladin.shop.tag.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.tag.dto.TagsResponseDto;
import shop.yesaladin.shop.tag.service.inter.QueryTagService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;

@AutoConfigureRestDocs
@WebMvcTest(QueryTagController.class)
class QueryTagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QueryTagService queryTagService;

    private List<TagsResponseDto> tags;

    @BeforeEach
    void setUp() {
        tags = Arrays.asList(
                new TagsResponseDto(1L, "행복한"),
                new TagsResponseDto(2L, "슬픈")
        );
    }

    @Test
    @DisplayName("태그 전체 조회 성공")
    void getTags() throws Exception {
        // given
        Mockito.when(queryTagService.findAll()).thenReturn(tags);

        // when
        ResultActions result = mockMvc.perform(get("/v1/tags")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", equalTo(1)))
                .andExpect(jsonPath("$[1].id", equalTo(2)))
                .andExpect(jsonPath("$[0].name", equalTo("행복한")))
                .andExpect(jsonPath("$[1].name", equalTo("슬픈")));

        // docs
        result.andDo(document(
                "find-all-tag",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("태그 아이디"),
                        fieldWithPath("[].name").type(JsonFieldType.STRING).description("태그 이름")
                )
        ));
    }
}