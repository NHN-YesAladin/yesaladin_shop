package shop.yesaladin.shop.writing.controller;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.writing.dto.AuthorsResponseDto;
import shop.yesaladin.shop.writing.service.inter.QueryAuthorService;

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
@WebMvcTest(QueryAuthorController.class)
class QueryAuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QueryAuthorService queryAuthorService;

    @Test
    @DisplayName("저자 전체 조회 성공")
    void getAuthors() throws Exception {
        // given
        List<AuthorsResponseDto> authors = List.of(
                new AuthorsResponseDto(1L, "저자1", "happyAuthor"),
                new AuthorsResponseDto(2L, "저자2", "sadAuthor")
        );
        Mockito.when(queryAuthorService.findAll()).thenReturn(authors);

        // when
        ResultActions result = mockMvc.perform(get("/v1/authors")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", equalTo(1)))
                .andExpect(jsonPath("$[1].id", equalTo(2)))
                .andExpect(jsonPath("$[0].name", equalTo("저자1")))
                .andExpect(jsonPath("$[1].name", equalTo("저자2")))
                .andExpect(jsonPath("$[0].loginId", equalTo("happyAuthor")))
                .andExpect(jsonPath("$[1].loginId", equalTo("sadAuthor")));

        // docs
        result.andDo(document(
                "find-all-author",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("저자 아이디"),
                        fieldWithPath("[].name").type(JsonFieldType.STRING).description("저자 이름"),
                        fieldWithPath("[].loginId").type(JsonFieldType.STRING).description("저자 로그인 아이디")
                )
        ));
    }

    @Test
    @DisplayName("저자 관리자용 페이징 전체 조회 성공")
    void getAuthorsForManager() throws Exception {
        // given
        List<AuthorsResponseDto> authors = new ArrayList<>();
        for (long i = 1L; i <= 10L; i++) {
            authors.add(new AuthorsResponseDto(i, "저자" + 1, null));
        }

        Page<AuthorsResponseDto> page = new PageImpl<>(
                authors,
                PageRequest.of(0, 5),
                authors.size()
        );
        Mockito.when(queryAuthorService.findAllForManager(any())).thenReturn(page);

        // when
        ResultActions result = mockMvc.perform(get("/v1/authors/manager")
                .param("page", "0")
                .param("size", "5")
                .contentType(MediaType.APPLICATION_JSON));
        Mockito.when(queryAuthorService.findAllForManager(PageRequest.of(0, 5))).thenReturn(page);

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(queryAuthorService, times(1)).findAllForManager(PageRequest.of(0, 5));

        // docs
        result.andDo(document(
                "find-all-for-manager-author",
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
                        fieldWithPath("dataList.[].id").type(JsonFieldType.NUMBER).description("저자 아이디"),
                        fieldWithPath("dataList.[].name").type(JsonFieldType.STRING).description("저자명"),
                        fieldWithPath("dataList.[].loginId").description("저자 로그인 아이디")
                )
        ));
    }
}