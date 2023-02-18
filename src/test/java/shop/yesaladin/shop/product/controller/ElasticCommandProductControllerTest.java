package shop.yesaladin.shop.product.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.product.service.inter.ElasticCommandProductService;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@WebMvcTest(ElasticCommandProductController.class)
class ElasticCommandProductControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    private ElasticCommandProductService elasticCommandProductService;

    @WithMockUser
    @Test
    @DisplayName("상품 수정 성공")
    void update() throws Exception {
        //given
        Mockito.when(elasticCommandProductService.update(1L))
                .thenReturn(1L);
        //when
        ResultActions resultActions = mockMvc.perform(put("/v1/search/products/{id}", 1)
                .with(csrf()));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.data.id", equalTo(1)));
    }

    @WithMockUser
    @Test
    @DisplayName("is-sale 수정 성공")
    void changeIsSale() throws Exception {
        //given
        Mockito.when(elasticCommandProductService.changeIsSale(1L))
                .thenReturn(1L);
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products/is-sale/{id}", 1)
                .with(csrf()));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.data.id", equalTo(1)));
    }

    @WithMockUser
    @Test
    @DisplayName("is-sale 수정 성공")
    void changeIsForcedOutOfStock() throws Exception {
        //given
        Mockito.when(elasticCommandProductService.changeIsForcedOutOfStock(1L))
                .thenReturn(1L);
        //when
        ResultActions resultActions = mockMvc.perform(get("/v1/search/products/is-forced-out-of-stock/{id}", 1)
                .with(csrf()));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.data.id", equalTo(1)));
    }

    @WithMockUser
    @Test
    void delete() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/v1/search/products/{id}", 1).with(csrf()));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.success", equalTo(true)));

        verify(elasticCommandProductService, atLeastOnce()).delete(1L);
    }
}