package shop.yesaladin.shop.category.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.category.dummy.CategoryDummy;
import shop.yesaladin.shop.category.exception.CategoryNotFoundException;
import shop.yesaladin.shop.category.service.inter.QueryCategoryService;

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
                "/v1/categories/" + id).contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(id.intValue())))
                .andExpect(jsonPath("$.name", equalTo(name)));

        verify(queryCategoryService, times(1)).findCategoryById(longArgumentCaptor.capture());
        assertThat(longArgumentCaptor.getValue()).isEqualTo(id);
    }

    @Test
    @DisplayName("id를 통한 카테고리 조회 실패 - 없는 카테고리")
    void getCategoryById_categoryNotFound_fail() throws Exception {
        // given
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        when(queryCategoryService.findCategoryById(id)).thenThrow(new CategoryNotFoundException(id));

        // when
        ResultActions perform = mockMvc.perform(get(
                "/v1/categories/" + id).contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print()).andExpect(status().isNotFound());

        verify(queryCategoryService, times(1)).findCategoryById(longArgumentCaptor.capture());
        assertThat(longArgumentCaptor.getValue()).isEqualTo(id);
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
                "/v1/categories/parents").contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", equalTo(dtoList.get(0).getId().intValue())))
                .andExpect(jsonPath("$.[0].name", equalTo(dtoList.get(0).getName())))
                .andExpect(jsonPath("$.[0].isShown", equalTo(dtoList.get(0).getIsShown())));

        verify(queryCategoryService, times(1)).findParentCategories();
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
                "/v1/categories/{parentId}/children",
                parentId
        ).contentType(MediaType.APPLICATION_JSON));

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
    }

}
