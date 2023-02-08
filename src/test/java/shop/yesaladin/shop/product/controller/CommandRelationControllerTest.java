package shop.yesaladin.shop.product.controller;

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
import shop.yesaladin.shop.product.dto.ProductOnlyIdDto;
import shop.yesaladin.shop.product.dto.RelationCreateDto;
import shop.yesaladin.shop.product.exception.RelationAlreadyExistsException;
import shop.yesaladin.shop.product.service.inter.CommandRelationService;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;

@AutoConfigureRestDocs
@WebMvcTest(CommandRelationController.class)
class CommandRelationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommandRelationService service;

    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser
    @Test
    @DisplayName("연관관계 등록 성공")
    void registerRelation_success() throws Exception {
        // given
        Long mainId = 1L;
        Long subId = 2L;
        RelationCreateDto createDto = new RelationCreateDto(subId);
        ProductOnlyIdDto onlyIdDto = new ProductOnlyIdDto(mainId);

        Mockito.when(service.create(mainId, subId)).thenReturn(onlyIdDto);

        // when
        ResultActions result = mockMvc.perform(post("/v1/products/{productMainId}/relations", mainId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(createDto))
        );

        // then
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.CREATED.value())))
                .andExpect(jsonPath("$.data.id", equalTo(1)));

        verify(service, times(1)).create(mainId, subId);

        // docs
        result.andDo(document(
                "register-relation",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("productSubId").type(JsonFieldType.NUMBER).description("연관관계를 맺어줄 Sub 상품의 아이디")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("연관관계를 맺은 Main 상품의 아이디"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY).optional().description("에러 메세지")
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("연관관계 등록 실패_스스로 연관관계를 맺으려 할 때 예외 발생")
    void registerRelation_trySelfRelate_throwSelfRelateException() throws Exception {
        // given
        Long mainId = 1L;
        RelationCreateDto createDto = new RelationCreateDto(mainId);

        // when
        ResultActions result = mockMvc.perform(post("/v1/products/{productMainId}/relations", mainId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(createDto))
        );

        // then
        result.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @WithMockUser
    @Test
    @DisplayName("연관관계 등록 실패_연관관계가 이미 양쪽으로 존재한 경우 예외 발생")
    void registerRelation_throwRelationAlreadyExistsException() throws Exception {
        // given
        Long mainId = 1L;
        Long subId = 2L;
        RelationCreateDto createDto = new RelationCreateDto(subId);

        Mockito.when(service.create(mainId, subId)).thenThrow(RelationAlreadyExistsException.class);

        // when
        ResultActions result = mockMvc.perform(post("/v1/products/{productMainId}/relations", mainId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(createDto))
        );

        // then
        result.andDo(print())
                .andExpect(status().isConflict());

        verify(service, times(1)).create(mainId, subId);
    }

    @WithMockUser
    @Test
    @DisplayName("연관관계 삭제 성공")
    void deleteRelation_success() throws Exception {
        // given
        Long mainId = 1L;
        Long subId = 2L;

        // when
        ResultActions result = mockMvc.perform(delete("/v1/products/{productMainId}/relations/{productSubId}", mainId, subId)
                .with(csrf())
        );

        // then
        result.andDo(print())
                .andExpect(status().isOk());

        verify(service, times(1)).delete(mainId, subId);

        // docs
        result.andDo(document(
                "delete-relation",
                getDocumentRequest(),
                getDocumentResponse()
        ));
    }
}