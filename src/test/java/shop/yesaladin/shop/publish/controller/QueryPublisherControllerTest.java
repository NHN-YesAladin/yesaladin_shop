package shop.yesaladin.shop.publish.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
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
    @DisplayName("????????? ???????????? ????????? ?????? ?????? ??????")
    void getPublishersForManager() throws Exception {
        // given
        List<PublisherResponseDto> publishers = new ArrayList<>();
        for (long i = 1L; i <= 10L; i++) {
            publishers.add(new PublisherResponseDto(i, "?????????" + i));
        }

        Page<PublisherResponseDto> page = new PageImpl<>(
                publishers,
                PageRequest.of(0, 5),
                publishers.size()
        );
        PaginatedResponseDto<PublisherResponseDto> paginated = PaginatedResponseDto.<PublisherResponseDto>builder()
                .totalPage(page.getTotalPages())
                .currentPage(page.getNumber())
                .totalDataCount(page.getTotalElements())
                .dataList(publishers)
                .build();
        Mockito.when(service.findAllForManager(PageRequest.of(0, 5))).thenReturn(paginated);

        // when
        ResultActions result = mockMvc.perform(get("/v1/publishers/manager")
                .param("page", "0")
                .param("size", "5")
                .contentType(MediaType.APPLICATION_JSON));

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
                        parameterWithName("size").description("?????????????????? ?????????"),
                        parameterWithName("page").description("?????????????????? ????????? ??????")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("?????? ?????? ??????"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("??????"),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER)
                                .description("???????????????"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER)
                                .description("?????? ?????????"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER)
                                .description("????????? ??????"),
                        fieldWithPath("data.dataList.[].id").type(JsonFieldType.NUMBER)
                                .description("????????? ?????????"),
                        fieldWithPath("data.dataList.[].name").type(JsonFieldType.STRING)
                                .description("????????????"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("?????? ?????????")
                                .optional()
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("???????????? ????????? ???????????? ?????? ??????")
    void getPublishersByNameForManager() throws Exception {
        // given
        List<PublisherResponseDto> publishers = new ArrayList<>();
        for (long i = 1L; i <= 10L; i++) {
            publishers.add(new PublisherResponseDto(i, "?????????" + i));
        }

        Page<PublisherResponseDto> page = new PageImpl<>(
                publishers,
                PageRequest.of(0, 5),
                publishers.size()
        );
        PaginatedResponseDto<PublisherResponseDto> paginated = PaginatedResponseDto.<PublisherResponseDto>builder()
                .totalPage(page.getTotalPages())
                .currentPage(page.getNumber())
                .totalDataCount(page.getTotalElements())
                .dataList(publishers)
                .build();
        Mockito.when(service.findByNameForManager("name", PageRequest.of(0, 5)))
                .thenReturn(paginated);

        // when
        ResultActions result = mockMvc.perform(get("/v1/publishers/manager")
                .param("page", "0")
                .param("size", "5")
                .param("name", "name")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(service, times(1)).findByNameForManager("name", PageRequest.of(0, 5));

        // docs
        result.andDo(document(
                "find-by-name-for-manager-publisher",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("size").description("?????????????????? ?????????"),
                        parameterWithName("page").description("?????????????????? ????????? ??????"),
                        parameterWithName("name").description("????????? ??????")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("?????? ?????? ??????"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("??????"),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER)
                                .description("???????????????"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER)
                                .description("?????? ?????????"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER)
                                .description("????????? ??????"),
                        fieldWithPath("data.dataList.[].id").type(JsonFieldType.NUMBER)
                                .description("????????? ?????????"),
                        fieldWithPath("data.dataList.[].name").type(JsonFieldType.STRING)
                                .description("????????????"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("?????? ?????????")
                                .optional()
                )
        ));
    }
}