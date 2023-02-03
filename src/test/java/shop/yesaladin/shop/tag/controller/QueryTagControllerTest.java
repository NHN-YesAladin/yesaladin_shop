package shop.yesaladin.shop.tag.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.tag.dto.TagsResponseDto;
import shop.yesaladin.shop.tag.service.inter.QueryTagService;

import java.util.ArrayList;
import java.util.List;

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
    private QueryTagService service;

    @WithMockUser
    @Test
    @DisplayName("태그 전체 조회 성공")
    void getTags() throws Exception {
        // given
        List<TagsResponseDto> tags = List.of(
                new TagsResponseDto(1L, "행복한"),
                new TagsResponseDto(2L, "슬픈")
        );

        Mockito.when(service.findAll()).thenReturn(tags);

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

    @WithMockUser
    @Test
    @DisplayName("태그 관리자용 페이징 전체 조회 성공")
    void getTagsForManager() throws Exception {
        // given
        List<TagsResponseDto> tags = new ArrayList<>();
        for (long i = 1L; i <= 10L; i++) {
            tags.add(new TagsResponseDto(i, "태그" + i));
        }

        Page<TagsResponseDto> page = new PageImpl<>(
                tags,
                PageRequest.of(0, 5),
                tags.size()
        );
        Mockito.when(service.findAllForManager(any())).thenReturn(page);

        // when
        ResultActions result = mockMvc.perform(get("/v1/tags/manager")
                .param("page", "0")
                .param("size", "5")
                .contentType(MediaType.APPLICATION_JSON));
        Mockito.when(service.findAllForManager(PageRequest.of(0, 5))).thenReturn(page);

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(service, times(1)).findAllForManager(PageRequest.of(0, 5));

        // docs
        result.andDo(document(
                "find-all-for-manager-tag",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("size").description("페이지네이션 사이즈"),
                        parameterWithName("page").description("페이지네이션 페이지 번호")
                ),
                responseFields(
                        fieldWithPath("totalPage").type(JsonFieldType.NUMBER).description("전체 페이지"),
                        fieldWithPath("currentPage").type(JsonFieldType.NUMBER).description("현재 페이지"),
                        fieldWithPath("totalDataCount").type(JsonFieldType.NUMBER).description("데이터 개수"),
                        fieldWithPath("dataList.[].id").type(JsonFieldType.NUMBER).description("태그 아이디"),
                        fieldWithPath("dataList.[].name").type(JsonFieldType.STRING).description("태그명")
                )
        ));
    }
}