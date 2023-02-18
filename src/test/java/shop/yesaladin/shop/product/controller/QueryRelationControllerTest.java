package shop.yesaladin.shop.product.controller;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.Relation;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.dto.RelationsResponseDto;
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.product.dummy.DummyProduct;
import shop.yesaladin.shop.product.dummy.DummyTotalDiscountRate;
import shop.yesaladin.shop.product.service.inter.QueryRelationService;

@AutoConfigureRestDocs
@WebMvcTest(QueryRelationController.class)
class QueryRelationControllerTest {

    private final Long PRODUCT_ID = 1L;
    private final String ISBN = "000000000000";
    private final String URL = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QueryRelationService service;

    Clock clock = Clock.fixed(
            Instant.parse("2023-03-10T00:00:00.000Z"),
            ZoneId.of("UTC")
    );
    private TotalDiscountRate totalDiscountRate;
    private final List<Product> products = new ArrayList<>();

    @BeforeEach
    void setUp() {
        totalDiscountRate = DummyTotalDiscountRate.dummy();

        for (int i = 0; i < 10; i++) {
            File thumbnailFile = DummyFile.dummy(URL + i);

            boolean isDeleted = i % 2 != 0;
            Product product = DummyProduct.dummy(
                    ISBN + i,
                    null,
                    thumbnailFile,
                    null,
                    totalDiscountRate,
                    isDeleted
            );
            products.add(product);
        }
    }

    @WithMockUser
    @Test
    @DisplayName("상품 연관관계 관리자용 전체 조회 성공")
    void getRelationsForManager() throws Exception {
        // given
        List<RelationsResponseDto> relations = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            List<String> authors = List.of("저자" + i);

            Relation relation = Relation.create(products.get(0), products.get(i));
            relations.add(new RelationsResponseDto(
                    (long) i,
                    relation.getProductSub().getThumbnailFile().getUrl(),
                    relation.getProductSub().getTitle(),
                    authors,
                    "출판사" + i,
                    LocalDateTime.now(clock).toLocalDate().toString(),
                    10000L,
                    10
            ));
        }

        Page<RelationsResponseDto> page = new PageImpl<>(
                relations,
                PageRequest.of(0, 5),
                relations.size()
        );
        PaginatedResponseDto<RelationsResponseDto> paginated = PaginatedResponseDto.<RelationsResponseDto>builder()
                .totalPage(page.getNumber())
                .currentPage(page.getNumber())
                .totalDataCount(page.getTotalElements())
                .dataList(relations)
                .build();
        Mockito.when(service.findAllForManager(PRODUCT_ID, PageRequest.of(0, 5))).thenReturn(paginated);

        // when
        ResultActions result = mockMvc.perform(get("/v1/products/{productId}/relations/manager", PRODUCT_ID)
                .param("page", "0")
                .param("size", "5")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(service, times(1)).findAllForManager(PRODUCT_ID, PageRequest.of(0, 5));

        // docs
        result.andDo(document(
                "find-all-for-manager-relation",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("size").description("페이지네이션 사이즈"),
                        parameterWithName("page").description("페이지네이션 페이지 번호")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER).description("전체 페이지"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER).description("현재 페이지"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER).description("데이터 개수"),
                        fieldWithPath("data.dataList.[].id").type(JsonFieldType.NUMBER).description("Sub 상품의 아이디"),
                        fieldWithPath("data.dataList.[].thumbnailFileUrl").type(JsonFieldType.STRING).description("Sub 상품의 썸네일"),
                        fieldWithPath("data.dataList.[].title").type(JsonFieldType.STRING).description("Sub 상품의 제목"),
                        fieldWithPath("data.dataList.[].authors").type(JsonFieldType.ARRAY).description("Sub 상품의 저자"),
                        fieldWithPath("data.dataList.[].publisher").type(JsonFieldType.STRING).description("Sub 상품의 출판사"),
                        fieldWithPath("data.dataList.[].publishedDate").type(JsonFieldType.STRING).description("Sub 상품의 출판일"),
                        fieldWithPath("data.dataList.[].sellingPrice").type(JsonFieldType.NUMBER).description("Sub 상품의 판매가"),
                        fieldWithPath("data.dataList.[].rate").type(JsonFieldType.NUMBER).description("Sub 상품의 할인율"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY).description("에러 메세지").optional()
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("상품 연관관계 일반 사용자용 전체 조회 성공")
    void getRelations() throws Exception {
        // given
        List<RelationsResponseDto> relations = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            List<String> authors = List.of("저자" + i);

            Relation relation = Relation.create(products.get(0), products.get(i));
            relations.add(new RelationsResponseDto(
                    (long) i,
                    relation.getProductSub().getThumbnailFile().getUrl(),
                    relation.getProductSub().getTitle(),
                    authors,
                    "출판사" + i,
                    LocalDateTime.now(clock).toLocalDate().toString(),
                    10000L,
                    10
            ));
        }

        Page<RelationsResponseDto> page = new PageImpl<>(
                relations,
                PageRequest.of(0, 5),
                relations.size()
        );
        PaginatedResponseDto<RelationsResponseDto> paginated = PaginatedResponseDto.<RelationsResponseDto>builder()
                .totalPage(page.getNumber())
                .currentPage(page.getNumber())
                .totalDataCount(page.getTotalElements())
                .dataList(relations)
                .build();
        Mockito.when(service.findAll(PRODUCT_ID, PageRequest.of(0, 5))).thenReturn(paginated);

        // when
        ResultActions result = mockMvc.perform(get("/v1/products/{productId}/relations", PRODUCT_ID)
                .param("page", "0")
                .param("size", "5")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(service, times(1)).findAll(PRODUCT_ID, PageRequest.of(0, 5));

        // docs
        result.andDo(document(
                "find-all-relation",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("size").description("페이지네이션 사이즈"),
                        parameterWithName("page").description("페이지네이션 페이지 번호")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("동작 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER).description("전체 페이지"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER).description("현재 페이지"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER).description("데이터 개수"),
                        fieldWithPath("data.dataList.[].id").type(JsonFieldType.NUMBER).description("Sub 상품의 아이디"),
                        fieldWithPath("data.dataList.[].thumbnailFileUrl").type(JsonFieldType.STRING).description("Sub 상품의 썸네일"),
                        fieldWithPath("data.dataList.[].title").type(JsonFieldType.STRING).description("Sub 상품의 제목"),
                        fieldWithPath("data.dataList.[].authors").type(JsonFieldType.ARRAY).description("Sub 상품의 저자"),
                        fieldWithPath("data.dataList.[].publisher").type(JsonFieldType.STRING).description("Sub 상품의 출판사"),
                        fieldWithPath("data.dataList.[].publishedDate").type(JsonFieldType.STRING).description("Sub 상품의 출판일"),
                        fieldWithPath("data.dataList.[].sellingPrice").type(JsonFieldType.NUMBER).description("Sub 상품의 판매가"),
                        fieldWithPath("data.dataList.[].rate").type(JsonFieldType.NUMBER).description("Sub 상품의 할인율"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY).description("에러 메세지").optional()
                )
        ));
    }
}