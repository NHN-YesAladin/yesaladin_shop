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
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;
import static shop.yesaladin.shop.docs.DocumentFormatGenerator.defaultValue;
import static shop.yesaladin.shop.docs.DocumentFormatGenerator.getDateFormat;

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
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
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
        name = "국외도서";
    }

    @Test
    @DisplayName("id를 통한 카테고리 조회 성공")
    void getCategoryById() throws Exception {
        // given
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        Category category = Category.builder().id(id).name(name).isShown(true).build();
        CategoryResponseDto responseDto = CategoryResponseDto.fromEntity(category);
        when(queryCategoryService.findCategoryById(id)).thenReturn(responseDto);

        // when
        ResultActions perform = mockMvc.perform(get(
                "/v1/categories/{categoryId}", id).contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(id.intValue())))
                .andExpect(jsonPath("$.name", equalTo(name)));

        verify(queryCategoryService, times(1)).findCategoryById(longArgumentCaptor.capture());
        assertThat(longArgumentCaptor.getValue()).isEqualTo(id);

        perform.andDo(document(
                "get-category-by-id",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("categoryId").description("찾고자하는 카테고리의 아이디")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("카테고리 아이디"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("카테고리 이름"),
                        fieldWithPath("isShown").type(JsonFieldType.BOOLEAN)
                                .description("카테고리 노출 여부"),
                        fieldWithPath("order").type(JsonFieldType.NUMBER)
                                .optional()
                                .description("카테고리 순서"),
                        fieldWithPath("parentId").type(JsonFieldType.NUMBER).optional()
                                .description("부모 카테고리(=1차 카테고리)의 아이디"),
                        fieldWithPath("parentName").type(JsonFieldType.STRING).optional()
                                .description("부모 카테고리(=1차 카테고리)의 이름")
                )
        ));
    }

    @Test
    @DisplayName("id를 통한 카테고리 조회 실패 - 없는 카테고리")
    void getCategoryById_categoryNotFound_fail() throws Exception {
        // given
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        when(queryCategoryService.findCategoryById(id)).thenThrow(new CategoryNotFoundException(id));

        // when
        ResultActions perform = mockMvc.perform(get(
                "/v1/categories/{categoryId}", id).contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print()).andExpect(status().isNotFound());

        verify(queryCategoryService, times(1)).findCategoryById(longArgumentCaptor.capture());
        assertThat(longArgumentCaptor.getValue()).isEqualTo(id);

        perform.andDo(document(
                "get-category-by-id-not-found-fail",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("categoryId").description("찾고자하는 카테고리의 아이디")
                ),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                )
        ));
    }

    @Test
    @DisplayName("페이징을 통한 카테고리 조회 성공")
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
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPage", equalTo(dtoList.size() / size)))
                .andExpect(jsonPath("$.currentPage", equalTo(page)))
                .andExpect(jsonPath("$.totalDataCount", equalTo(dtoList.size())))
                .andExpect(jsonPath(
                        "$.dataList.[0].id",
                        equalTo(dtoList.get(0).getId().intValue())
                ))
                .andExpect(jsonPath("$.dataList.[0].name", equalTo(dtoList.get(0).getName())));

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
                        parameterWithName("parentId").description("찾고자하는 카테고리의 부모인 카테고리의 id"),
                        parameterWithName("size").description("페이지네이션 사이즈")
                                .optional()
                                .attributes(defaultValue(20)),
                        parameterWithName("page").description("페이지네이션 페이지 번호")
                                .optional()
                                .attributes(defaultValue(0))
                ),
                responseFields(
                        fieldWithPath("totalPage").type(JsonFieldType.NUMBER)
                                .description("총 페이지 수"),
                        fieldWithPath("currentPage").type(JsonFieldType.NUMBER)
                                .description("현재 페이지 번호"),
                        fieldWithPath("totalDataCount").type(JsonFieldType.NUMBER)
                                .description("모든 데이터의 수"),
                        fieldWithPath("dataList").type(JsonFieldType.ARRAY)
                                .description("조회된 카테고리 요약 데이터 리스트"),
                        fieldWithPath("dataList.[].id").type(JsonFieldType.NUMBER)
                                .description("카테고리 아이디"),
                        fieldWithPath("dataList.[].name").type(JsonFieldType.STRING)
                                .description("카테고리 이름"),
                        fieldWithPath("dataList.[].isShown").type(JsonFieldType.BOOLEAN)
                                .description("카테고리 노출 여부"),
                        fieldWithPath("dataList.[].order").type(JsonFieldType.NUMBER)
                                .optional()
                                .description("카테고리 순서"),
                        fieldWithPath("dataList.[].parentId").type(JsonFieldType.NUMBER).optional()
                                .description("부모 카테고리(=1차 카테고리)의 아이디"),
                        fieldWithPath("dataList.[].parentName").type(JsonFieldType.STRING)
                                .optional()
                                .description("부모 카테고리(=1차 카테고리)의 이름")
                )
        ));

    }

    @Test
    @DisplayName("모든 1차 카테고리 조회 성공")
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
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", equalTo(dtoList.get(0).getId().intValue())))
                .andExpect(jsonPath("$.[0].name", equalTo(dtoList.get(0).getName())))
                .andExpect(jsonPath("$.[0].isShown", equalTo(dtoList.get(0).getIsShown())));

        verify(queryCategoryService, times(1)).findParentCategories();

        perform.andDo(document(
                "get-parent-categories",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("cate").description("모든 1차 카테고리를 불러 올 때 사용")
                                .attributes(defaultValue("parents"))
                ),
                responseFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER)
                                .description("1차 카테고리 아이디"),
                        fieldWithPath("[].name").type(JsonFieldType.STRING)
                                .description("1차 카테고리 이름"),
                        fieldWithPath("[].isShown").type(JsonFieldType.BOOLEAN)
                                .description("1차 카테고리 노출 여부"),
                        fieldWithPath("[].order").type(JsonFieldType.NUMBER)
                                .optional()
                                .description("1차 카테고리 순서"),
                        fieldWithPath("[].parentId").type(JsonFieldType.NUMBER).optional()
                                .description("부모 카테고리(=1차 카테고리)의 아이디 - null"),
                        fieldWithPath("[].parentName").type(JsonFieldType.STRING)
                                .optional()
                                .description("부모 카테고리(=1차 카테고리)의 이름 - null")
                )
        ));
    }

    @Test
    @DisplayName("페이징 없는 2차 카테고리 조회 성공 ")
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
        ).queryParam("cate", "children").contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", equalTo(dtoList.get(0).getId().intValue())))
                .andExpect(jsonPath("$.[0].name", equalTo(dtoList.get(0).getName())))
                .andExpect(jsonPath("$.[0].isShown", equalTo(dtoList.get(0).getIsShown())));

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
                        parameterWithName("parentId").description("찾고자하는 카테고리의 부모인 카테고리의 id")
                ),
                requestParameters(
                        parameterWithName("cate").description("해당하는 아이디의 2차 카테고리를 모두 조회하는 경우 사용")
                                .attributes(defaultValue("children"))
                ),
                responseFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER)
                                .description("2차 카테고리 아이디"),
                        fieldWithPath("[].name").type(JsonFieldType.STRING)
                                .description("2차 카테고리 이름"),
                        fieldWithPath("[].isShown").type(JsonFieldType.BOOLEAN)
                                .description("2차 카테고리 노출 여부"),
                        fieldWithPath("[].order").type(JsonFieldType.NUMBER)
                                .optional()
                                .description("2차 카테고리 순서"),
                        fieldWithPath("[].parentId").type(JsonFieldType.NUMBER).optional()
                                .description("부모 카테고리(=1차 카테고리)의 아이디"),
                        fieldWithPath("[].parentName").type(JsonFieldType.STRING)
                                .optional()
                                .description("부모 카테고리(=1차 카테고리)의 이름")
                )
        ));

    }

}
