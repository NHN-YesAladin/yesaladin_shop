package shop.yesaladin.shop.category.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
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
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
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
}
