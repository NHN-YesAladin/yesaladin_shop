package shop.yesaladin.shop.publish.controller;

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
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.publish.service.inter.QueryPublisherService;

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
@WebMvcTest(QueryPublisherController.class)
class QueryPublisherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QueryPublisherService service;

    private List<PublisherResponseDto> publishers;

    @BeforeEach
    void setUp() {
        publishers = List.of(
                new PublisherResponseDto(1L, "출판사1"),
                new PublisherResponseDto(2L, "출판사2")
        );
    }

    @Test
    @DisplayName("출판사 전체 조회 성공")
    void getPublishers() throws Exception {
        // given
        Mockito.when(service.findAll()).thenReturn(publishers);

        // when
        ResultActions result = mockMvc.perform(get("/v1/publishers")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", equalTo(1)))
                .andExpect(jsonPath("$[1].id", equalTo(2)))
                .andExpect(jsonPath("$[0].name", equalTo("출판사1")))
                .andExpect(jsonPath("$[1].name", equalTo("출판사2")));

        // docs
        result.andDo(document(
                "find-all-publisher",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("출판사 아이디"),
                        fieldWithPath("[].name").type(JsonFieldType.STRING).description("출판사 이름")
                )
        ));
    }
}