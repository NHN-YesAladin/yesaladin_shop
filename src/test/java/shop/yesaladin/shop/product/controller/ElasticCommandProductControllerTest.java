package shop.yesaladin.shop.product.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.product.domain.model.SearchedProduct;
import shop.yesaladin.shop.product.domain.model.SearchedProductAuthor;
import shop.yesaladin.shop.product.domain.model.SearchedProductCategory;
import shop.yesaladin.shop.product.domain.model.SearchedProductPublisher;
import shop.yesaladin.shop.product.domain.model.SearchedProductTag;
import shop.yesaladin.shop.product.domain.model.SearchedProductTotalDiscountRate;
import shop.yesaladin.shop.product.dto.SearchedProductUpdateDto;
import shop.yesaladin.shop.product.service.inter.ElasticCommandProductService;

@AutoConfigureRestDocs
@WebMvcTest(ElasticCommandProductController.class)
class ElasticCommandProductControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    private ElasticCommandProductService elasticCommandProductService;
    @Autowired
    ObjectMapper objectMapper;
    private SearchedProduct searchedProduct;
    private SearchedProductUpdateDto searchedProductUpdateDto;

    @BeforeEach
    void setUp() {
        searchedProductUpdateDto = SearchedProductUpdateDto.builder()
                .title("title")
                .contents("content")
                .description("description")
                .isbn("isbn")
                .actualPrice(1000L)
                .isSeparatelyDiscount(false)
                .preferentialShowRanking(1000)
                .thumbnailFileUrl("file")
                .isSale(true)
                .isForcedOutOfStock(false)
                .publishedDate(LocalDate.of(2000, 10, 10))
                .publisherId(1L)
                .authors(List.of(1L))
                .tags(List.of(1L))
                .categories(List.of(1L))
                .build();

        searchedProduct = SearchedProduct
                .builder()
                .id(1L)
                .isbn("isbn")
                .title("title")
                .contents("content")
                .description("description")
                .actualPrice(1000)
                .discountRate(10)
                .isSeparatelyDiscount(false)
                .isSale(true)
                .quantity(1000L)
                .isForcedOutOfStock(false)
                .preferentialShowRanking(1000)
                .searchedTotalDiscountRate(new SearchedProductTotalDiscountRate(1, 10))
                .thumbnailFile("file")
                .publisher(new SearchedProductPublisher(1L, "name"))
                .publishedDate(LocalDate.of(2000, 10, 10))
                .isDeleted(false)
                .categories(List.of(new SearchedProductCategory(
                        1L,
                        2L,
                        "name",
                        true,
                        false
                )))
                .authors(List.of(new SearchedProductAuthor(1L, "name")))
                .tags(List.of(new SearchedProductTag(1L, "name")))
                .build();
    }

    @WithMockUser
    @Test
    @DisplayName("상품 수정 성공")
    void update() throws Exception {
        //given
        Mockito.when(elasticCommandProductService.update(any(), any()))
                .thenReturn(searchedProduct);
        //when
        ResultActions resultActions = mockMvc.perform(put("/v1/search/products/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(searchedProductUpdateDto))
                .with(csrf()));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.data.id", equalTo(1)))
                .andExpect(jsonPath("$.data.title", equalTo(searchedProduct.getTitle())))
                .andExpect(jsonPath("$.data.contents", equalTo(searchedProduct.getContents())))
                .andExpect(jsonPath("$.data.isbn", equalTo(searchedProduct.getIsbn())))
                .andExpect(jsonPath("$.data.contents", equalTo(searchedProduct.getContents())))
                .andExpect(jsonPath(
                        "$.data.description",
                        equalTo(searchedProduct.getDescription())
                ))
                .andExpect(jsonPath("$.data.actualPrice", equalTo(1000)))
                .andExpect(jsonPath(
                        "$.data.discountRate",
                        equalTo(searchedProduct.getDiscountRate())
                ))
                .andExpect(jsonPath(
                        "$.data.separatelyDiscount",
                        equalTo(searchedProduct.isSeparatelyDiscount())
                ))
                .andExpect(jsonPath("$.data.sale", equalTo(searchedProduct.isSale())))
                .andExpect(jsonPath("$.data.quantity", equalTo(1000)))
                .andExpect(jsonPath("$.data.preferentialShowRanking", equalTo(1000)))
                .andExpect(jsonPath(
                        "$.data.thumbnailFile",
                        equalTo(searchedProduct.getThumbnailFile())
                ))
                .andExpect(jsonPath(
                        "$.data.publishedDate",
                        equalTo(searchedProduct.getPublishedDate().toString())
                ))
                .andExpect(jsonPath("$.data.isDeleted", equalTo(searchedProduct.getIsDeleted())))
                .andExpect(jsonPath("$.data.searchedTotalDiscountRate.id", equalTo(1)))
                .andExpect(jsonPath(
                        "$.data.searchedTotalDiscountRate.discountRate",
                        equalTo(searchedProduct.getSearchedTotalDiscountRate().getDiscountRate())
                ))
                .andExpect(jsonPath("$.data.categories[0].id", equalTo(1)))
                .andExpect(jsonPath(
                        "$.data.categories[0].name",
                        equalTo(searchedProduct.getCategories().get(0).getName())
                ))
                .andExpect(jsonPath("$.data.publisher.id", equalTo(1)))
                .andExpect(jsonPath(
                        "$.data.publisher.name",
                        equalTo(searchedProduct.getPublisher().getName())
                ))
                .andExpect(jsonPath("$.data.authors[0].id", equalTo(1)))
                .andExpect(jsonPath(
                        "$.data.authors[0].name",
                        equalTo(searchedProduct.getAuthors().get(0).getName())
                ))
                .andExpect(jsonPath("$.data.tags[0].id", equalTo(1)))
                .andExpect(jsonPath(
                        "$.data.tags[0].name",
                        equalTo(searchedProduct.getTags().get(0).getName())
                ));
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