package shop.yesaladin.shop.category.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.dto.CategoriesSimpleResponseDto;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.category.dto.CategorySimpleDto;
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
        Category category = Category.builder().id(id).name(name).isShown(true).build();
        CategoryResponseDto responseDto = CategoryResponseDto.fromEntity(category);
        given(queryCategoryService.findCategoryById(id)).willReturn(responseDto);

        // when
        ResultActions perform = mockMvc.perform(get(
                "/v1/categories/" + id).contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(id.intValue())))
                .andExpect(jsonPath("$.name", equalTo(name)));
    }

    @Test
    @DisplayName("id를 통한 카테고리 조회 실패 - 없는 카테고리")
    void getCategoryById_categoryNotFound_fail() throws Exception {
        // given
        given(queryCategoryService.findCategoryById(id)).willThrow(new CategoryNotFoundException(id));

        // when
        ResultActions perform = mockMvc.perform(get(
                "/v1/categories/" + id).contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("페이징을 통한 카테고리 조회 성공")
    void getCategories() throws Exception {
        // given
        List<CategoryResponseDto> dtoList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Category category = Category.builder()
                    .id((long) i)
                    .name(name + i)
                    .isShown(true)
                    .build();
            dtoList.add(CategoryResponseDto.fromEntity(category));
        }
        Page<CategoryResponseDto> dtoPage = new PageImpl<>(dtoList);
        given(queryCategoryService.findCategories(any())).willReturn(dtoPage);

        // when
        ResultActions perform = mockMvc.perform(get(
                "/v1/categories").contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", equalTo(1)))
                .andExpect(jsonPath("$.[0].name", equalTo(name + "1")));
    }

    @Test
    @DisplayName("전체 카테고리 조회 성공")
    void getAllCategories() throws Exception {
        // given
        List<CategorySimpleDto> parentDtos = new ArrayList<>();
        List<CategorySimpleDto> childDtos = new ArrayList<>();
        List<CategoriesSimpleResponseDto> categoriesDtos = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Category parentCategory = CategoryDummy.dummyParent(100L + i);
            parentDtos.add(new CategorySimpleDto(
                    parentCategory.getId(),
                    parentCategory.getName(),
                    parentCategory.isShown(),
                    parentCategory.getOrder()
            ));
        }

        for (CategorySimpleDto parentDto : parentDtos) {
            int count = 3;
            for (int i = 0; i < count; i++) {
                Category childCategory = CategoryDummy.dummyChild(
                        (long) i,
                        CategoryDummy.dummyParent(parentDto.getId())
                );
                childDtos.add(new CategorySimpleDto(
                        childCategory.getId(),
                        childCategory.getName(),
                        childCategory.isShown(),
                        childCategory.getOrder()
                ));
            }
            categoriesDtos.add(new CategoriesSimpleResponseDto(parentDto, childDtos));
        }

        given(queryCategoryService.findAllCategoryResponseDto()).willReturn(categoriesDtos);

        // when
        ResultActions perform = mockMvc.perform(get(
                "/v1/categories/all").contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().stringValues("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[0].parent.id", equalTo(parentDtos.get(0).getId().intValue())))
                .andExpect(jsonPath("$.[0].parent.name", equalTo(parentDtos.get(0).getName())))
                .andExpect(jsonPath(
                        "$.[0].parent.isShown",
                        equalTo(parentDtos.get(0).getIsShown())
                ))
                .andExpect(jsonPath("$.[0].parent.order", equalTo(parentDtos.get(0).getOrder())))
                .andExpect(jsonPath("$.[0].children.[0].id", equalTo(childDtos.get(0).getId().intValue())))
                .andExpect(jsonPath("$.[0].children.[0].name", equalTo(childDtos.get(0).getName())))
                .andExpect(jsonPath(
                        "$.[0].children.[0].isShown",
                        equalTo(childDtos.get(0).getIsShown())
                ))
                .andExpect(jsonPath(
                        "$.[0].children.[0].order",
                        equalTo(childDtos.get(0).getOrder())
                ));
    }
}
