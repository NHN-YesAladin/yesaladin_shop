package shop.yesaladin.shop.publish.controller;

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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;

import java.util.ArrayList;
import java.util.List;
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
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.publish.dto.PublishersResponseDto;
import shop.yesaladin.shop.publish.service.inter.QueryPublisherService;

@AutoConfigureRestDocs
@WebMvcTest(QueryPublisherController.class)
class QueryPublisherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QueryPublisherService service;

    @WithMockUser
    @Test
    @DisplayName("출판사 전체 조회 성공")
    void getPublishers() throws Exception {
        // given
        List<PublisherResponseDto> publishers = List.of(
                new PublisherResponseDto(1L, "출판사1"),
                new PublisherResponseDto(2L, "출판사2")
        );

        Mockito.when(service.findAll()).thenReturn(publishers);

        // when
        ResultActions result = mockMvc.perform(get("/v1/publishers")
                .with(csrf())
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

    @WithMockUser
    @Test
    @DisplayName("출판사 관리자용 페이징 전체 조회 성공")
    void getPublishersForManager() throws Exception {
        // given
        List<PublishersResponseDto> publishers = new ArrayList<>();
        for (long i = 1L; i <= 10L; i++) {
            publishers.add(new PublishersResponseDto(i, "출판사" + i));
        }

        Page<PublishersResponseDto> page = new PageImpl<>(
                publishers,
                PageRequest.of(0, 5),
                publishers.size()
        );
        Mockito.when(service.findAllForManager(any())).thenReturn(page);

        // when
        ResultActions result = mockMvc.perform(get("/v1/publishers/manager")
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
                "find-all-for-manager-publisher",
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
                        fieldWithPath("dataList.[].id").type(JsonFieldType.NUMBER).description("출판사 아이디"),
                        fieldWithPath("dataList.[].name").type(JsonFieldType.STRING).description("출판사명")
                )
        ));
    }
}