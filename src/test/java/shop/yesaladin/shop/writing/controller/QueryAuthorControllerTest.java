package shop.yesaladin.shop.writing.controller;

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
import shop.yesaladin.shop.writing.dto.AuthorsResponseDto;
import shop.yesaladin.shop.writing.service.inter.QueryAuthorService;

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
@WebMvcTest(QueryAuthorController.class)
class QueryAuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QueryAuthorService queryAuthorService;

    private List<AuthorsResponseDto> authors;

    @BeforeEach
    void setUp() {
        authors = List.of(
                new AuthorsResponseDto(1L, "저자1", "happyAuthor"),
                new AuthorsResponseDto(2L, "저자2", "sadAuthor")
        );
    }

    @Test
    @DisplayName("저자 전체 조회 성공")
    void getAuthors() throws Exception {
        // given
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
}