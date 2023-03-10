package shop.yesaladin.shop.category.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;
import static shop.yesaladin.shop.docs.DocumentFormatGenerator.defaultValue;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.category.dummy.CategoryDummy;
import shop.yesaladin.shop.category.exception.CategoryNotFoundException;
import shop.yesaladin.shop.category.service.inter.QueryCategoryService;

@AutoConfigureRestDocs
@WebMvcTest(QueryCategoryController.class)
class QueryCategoryControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private QueryCategoryService queryCategoryService;


    private Long id;
    private String name;

    @BeforeEach
    void setUp() {
        id = 1L;
        name = "????????????";
    }

    @WithMockUser
    @Test
    @DisplayName("id??? ?????? ???????????? ?????? ??????")
    void getCategoryById() throws Exception {
        // given
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        Category category = Category.builder().id(id).name(name).isShown(true).build();
        CategoryResponseDto responseDto = CategoryResponseDto.fromEntity(category);
        when(queryCategoryService.findCategoryById(id)).thenReturn(responseDto);

        // when
        ResultActions perform = mockMvc.perform(get(
                "/v1/categories/{categoryId}", id)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk())
                .andExpect(header().stringValues("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.data.id", equalTo(id.intValue())))
                .andExpect(jsonPath("$.data.name", equalTo(name)));

        verify(queryCategoryService, times(1)).findCategoryById(longArgumentCaptor.capture());
        assertThat(longArgumentCaptor.getValue()).isEqualTo(id);

        perform.andDo(document(
                "get-category-by-id",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("categoryId").description("??????????????? ??????????????? ?????????")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("?????? ?????? ??????"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("HTTP ?????? ??????"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .optional()
                                .description("?????? ?????????"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("???????????? ?????????"),
                        fieldWithPath("data.name").type(JsonFieldType.STRING)
                                .description("???????????? ??????"),
                        fieldWithPath("data.isShown").type(JsonFieldType.BOOLEAN)
                                .description("???????????? ?????? ??????"),
                        fieldWithPath("data.order").type(JsonFieldType.NUMBER)
                                .optional()
                                .description("???????????? ??????"),
                        fieldWithPath("data.parentId").type(JsonFieldType.NUMBER).optional()
                                .description("?????? ????????????(=1??? ????????????)??? ?????????"),
                        fieldWithPath("data.parentName").type(JsonFieldType.STRING).optional()
                                .description("?????? ????????????(=1??? ????????????)??? ??????")
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("id??? ?????? ???????????? ?????? ?????? - ?????? ????????????")
    void getCategoryById_categoryNotFound_fail() throws Exception {
        // given
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        when(queryCategoryService.findCategoryById(id)).thenThrow(new CategoryNotFoundException(id));

        // when
        ResultActions perform = mockMvc.perform(get(
                "/v1/categories/{categoryId}", id)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print()).andExpect(status().isNotFound());

        verify(queryCategoryService, times(1)).findCategoryById(longArgumentCaptor.capture());
        assertThat(longArgumentCaptor.getValue()).isEqualTo(id);

        perform.andDo(document(
                "get-category-by-id-not-found-fail",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("categoryId").description("??????????????? ??????????????? ?????????")
                ),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ?????????")
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("???????????? ?????? ???????????? ?????? ??????")
    void getCategoriesByParentId() throws Exception {
        // given
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        long parentId = 100L;
        Category parent = CategoryDummy.dummyParent(parentId);
        List<CategoryResponseDto> dtoList = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            Category category = Category.builder()
                    .id((long) i)
                    .name(name + i)
                    .parent(parent)
                    .depth(Category.DEPTH_CHILD)
                    .isShown(true)
                    .build();
            dtoList.add(CategoryResponseDto.fromEntity(category));
        }
        Integer page = 0;
        Integer size = 3;
        int first = page * size;
        Page<CategoryResponseDto> dtoPage = new PageImpl<>(
                dtoList.subList(first, first + size),
                PageRequest.of(page, size),
                dtoList.size()
        );
        when(queryCategoryService.findCategoriesByParentId(any(), any())).thenReturn(dtoPage);

        // when
        ResultActions perform = mockMvc.perform(get("/v1/categories").param(
                        "parentId",
                        parent.getId().toString()
                )
                .param("page", page.toString())
                .param("size", size.toString())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk())
                .andExpect(header().stringValues("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.data.totalPage", equalTo(dtoList.size() / size)))
                .andExpect(jsonPath("$.data.currentPage", equalTo(page)))
                .andExpect(jsonPath("$.data.totalDataCount", equalTo(dtoList.size())))
                .andExpect(jsonPath(
                        "$.data.dataList.[0].id",
                        equalTo(dtoList.get(0).getId().intValue())
                ))
                .andExpect(jsonPath("$.data.dataList.[0].name", equalTo(dtoList.get(0).getName())));

        verify(queryCategoryService, times(1)).findCategoriesByParentId(
                pageableCaptor.capture(),
                longArgumentCaptor.capture()
        );
        assertThat(pageableCaptor.getValue()).isEqualTo(PageRequest.of(page, size));
        assertThat(longArgumentCaptor.getValue()).isEqualTo(parentId);

        perform.andDo(document(
                "get-categories-by-parent-id-paging",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("parentId").description("??????????????? ??????????????? ????????? ??????????????? id"),
                        parameterWithName("size").description("?????????????????? ?????????")
                                .optional()
                                .attributes(defaultValue(20)),
                        parameterWithName("page").description("?????????????????? ????????? ??????")
                                .optional()
                                .attributes(defaultValue(0))
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("?????? ?????? ??????"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("HTTP ?????? ??????"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .optional()
                                .description("?????? ?????????"),
                        fieldWithPath("data.totalPage").type(JsonFieldType.NUMBER)
                                .description("??? ????????? ???"),
                        fieldWithPath("data.currentPage").type(JsonFieldType.NUMBER)
                                .description("?????? ????????? ??????"),
                        fieldWithPath("data.totalDataCount").type(JsonFieldType.NUMBER)
                                .description("?????? ???????????? ???"),
                        fieldWithPath("data.dataList").type(JsonFieldType.ARRAY)
                                .description("????????? ???????????? ?????? ????????? ?????????"),
                        fieldWithPath("data.dataList.[].id").type(JsonFieldType.NUMBER)
                                .description("???????????? ?????????"),
                        fieldWithPath("data.dataList.[].name").type(JsonFieldType.STRING)
                                .description("???????????? ??????"),
                        fieldWithPath("data.dataList.[].isShown").type(JsonFieldType.BOOLEAN)
                                .description("???????????? ?????? ??????"),
                        fieldWithPath("data.dataList.[].order").type(JsonFieldType.NUMBER)
                                .optional()
                                .description("???????????? ??????"),
                        fieldWithPath("data.dataList.[].parentId").type(JsonFieldType.NUMBER)
                                .optional()
                                .description("?????? ????????????(=1??? ????????????)??? ?????????"),
                        fieldWithPath("data.dataList.[].parentName").type(JsonFieldType.STRING)
                                .optional()
                                .description("?????? ????????????(=1??? ????????????)??? ??????")
                )
        ));

    }

    @WithMockUser
    @Test
    @DisplayName("?????? 1??? ???????????? ?????? ??????")
    void getParentCategories() throws Exception {
        // given

        List<CategoryResponseDto> dtoList = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            Category category = Category.builder()
                    .id((long) i)
                    .name(name + i)
                    .depth(Category.DEPTH_PARENT)
                    .isShown(true)
                    .build();
            dtoList.add(CategoryResponseDto.fromEntity(category));
        }
        when(queryCategoryService.findParentCategories()).thenReturn(dtoList);

        // when
        ResultActions perform = mockMvc.perform(get(
                "/v1/categories").queryParam("cate", "parents")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk())
                .andExpect(header().stringValues("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.data.[0].id", equalTo(dtoList.get(0).getId().intValue())))
                .andExpect(jsonPath("$.data.[0].name", equalTo(dtoList.get(0).getName())))
                .andExpect(jsonPath("$.data.[0].isShown", equalTo(dtoList.get(0).getIsShown())));

        verify(queryCategoryService, times(1)).findParentCategories();

        perform.andDo(document(
                "get-parent-categories",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("cate").description("?????? 1??? ??????????????? ?????? ??? ??? ??????")
                                .attributes(defaultValue("parents")),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("?????? ?????? ??????"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("HTTP ?????? ??????"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .optional()
                                .description("?????? ?????????"),
                        fieldWithPath("data.[].id").type(JsonFieldType.NUMBER)
                                .description("1??? ???????????? ?????????"),
                        fieldWithPath("data.[].name").type(JsonFieldType.STRING)
                                .description("1??? ???????????? ??????"),
                        fieldWithPath("data.[].isShown").type(JsonFieldType.BOOLEAN)
                                .description("1??? ???????????? ?????? ??????"),
                        fieldWithPath("data.[].order").type(JsonFieldType.NUMBER)
                                .optional()
                                .description("1??? ???????????? ??????"),
                        fieldWithPath("data.[].parentId").type(JsonFieldType.NUMBER).optional()
                                .description("?????? ????????????(=1??? ????????????)??? ????????? - null"),
                        fieldWithPath("data.[].parentName").type(JsonFieldType.STRING)
                                .optional()
                                .description("?????? ????????????(=1??? ????????????)??? ?????? - null")
                )
        ));
    }

    @WithMockUser
    @Test
    @DisplayName("????????? ?????? 2??? ???????????? ?????? ?????? ")
    void getChildCategoriesByParentId() throws Exception {
        // given
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        long parentId = 100L;
        Category parent = CategoryDummy.dummyParent(parentId);
        List<CategoryResponseDto> dtoList = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            Category category = Category.builder()
                    .id((long) i)
                    .name(name + i)
                    .parent(parent)
                    .depth(Category.DEPTH_CHILD)
                    .isShown(true)
                    .build();
            dtoList.add(CategoryResponseDto.fromEntity(category));
        }
        when(queryCategoryService.findChildCategoriesByParentId(parentId)).thenReturn(dtoList);

        // when
        ResultActions perform = mockMvc.perform(get(
                "/v1/categories/{parentId}",
                parentId
        ).queryParam("cate", "children")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk())
                .andExpect(header().stringValues("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.data.[0].id", equalTo(dtoList.get(0).getId().intValue())))
                .andExpect(jsonPath("$.data.[0].name", equalTo(dtoList.get(0).getName())))
                .andExpect(jsonPath("$.data.[0].isShown", equalTo(dtoList.get(0).getIsShown())));

        verify(
                queryCategoryService,
                times(1)
        ).findChildCategoriesByParentId(longArgumentCaptor.capture());
        assertThat(longArgumentCaptor.getValue()).isEqualTo(parentId);

        perform.andDo(document(
                "get-child-categories-by-parent-id",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("parentId").description("??????????????? ??????????????? ????????? ??????????????? id")
                ),
                requestParameters(
                        parameterWithName("cate").description("???????????? ???????????? 2??? ??????????????? ?????? ???????????? ?????? ??????")
                                .attributes(defaultValue("children")),
                        parameterWithName("_csrf").description("csrf")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("?????? ?????? ??????"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("HTTP ?????? ??????"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .optional()
                                .description("?????? ?????????"),
                        fieldWithPath("data.[].id").type(JsonFieldType.NUMBER)
                                .description("2??? ???????????? ?????????"),
                        fieldWithPath("data.[].name").type(JsonFieldType.STRING)
                                .description("2??? ???????????? ??????"),
                        fieldWithPath("data.[].isShown").type(JsonFieldType.BOOLEAN)
                                .description("2??? ???????????? ?????? ??????"),
                        fieldWithPath("data.[].order").type(JsonFieldType.NUMBER)
                                .optional()
                                .description("2??? ???????????? ??????"),
                        fieldWithPath("data.[].parentId").type(JsonFieldType.NUMBER).optional()
                                .description("?????? ????????????(=1??? ????????????)??? ?????????"),
                        fieldWithPath("data.[].parentName").type(JsonFieldType.STRING)
                                .optional()
                                .description("?????? ????????????(=1??? ????????????)??? ??????")
                )
        ));

    }

}
