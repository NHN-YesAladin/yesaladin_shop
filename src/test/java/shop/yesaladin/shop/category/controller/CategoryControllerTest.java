package shop.yesaladin.shop.category.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.dto.CategoryCreateDto;
import shop.yesaladin.shop.category.dto.CategoryUpdateDto;
import shop.yesaladin.shop.category.service.inter.CommandCategoryService;


@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private CommandCategoryService commandCategoryService;

    private long id;
    private String name;
    private Category category;


    @BeforeEach
    void setUp() {
        id = 1L;
        name = "국외도서";

        category = Category.builder().id(id).name(name).isShown(true).build();

    }

    @Test
    @DisplayName("카테고리 생성 성공")
    void createCategory() throws Exception {
        //given
        CategoryCreateDto createDto = new CategoryCreateDto(name);
        given(commandCategoryService.create(any())).willReturn(category);

        //when
        ResultActions perform = mockMvc.perform(post("/v1/categories").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));

        //then
        perform.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().longValue("Location", id));

        verify(commandCategoryService, times(1)).create(any());
    }

    @Test
    @DisplayName("카테고리 생성 실패 - name이 null 인 경우")
    void createCategory_invalidatedData_fail() throws Exception {
        //given
        CategoryCreateDto createDto = new CategoryCreateDto(null);

        //when
        ResultActions perform = mockMvc.perform(post("/v1/categories").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));

        //then
        perform.andDo(print()).andExpect(status().is5xxServerError());

        verify(commandCategoryService, never()).create(any());
    }

    @Test
    @DisplayName("카테고리 수정 성공")
    void updateCategory() throws Exception {
        // given
        String changeName = "중고서적";
        CategoryUpdateDto updateDto = new CategoryUpdateDto(
                category.getId(),
                changeName,
                category.isShown(),
                category.getOrder(),
                null
        );
        Category toEntity = updateDto.toEntity(null);
        given(commandCategoryService.update(any())).willReturn(toEntity);

        // when
        ResultActions perform = mockMvc.perform(put(
                "/v1/categories/" + toEntity.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(toEntity.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(toEntity.getName())))
                .andExpect(jsonPath("$.isShown", equalTo(toEntity.isShown())));

        verify(commandCategoryService, times(1)).update(any());
    }

    @Test
    @DisplayName("카테고리 수정 실패 - name이 null 인 경우")
    void updateCategory_invalidatedData_fail() throws Exception {
        // given
        String nullName = null;
        CategoryUpdateDto updateDto = new CategoryUpdateDto(
                category.getId(),
                nullName,
                category.isShown(),
                category.getOrder(),
                null
        );
        Category toEntity = updateDto.toEntity(null);
        given(commandCategoryService.update(any())).willReturn(toEntity);

        // when
        ResultActions perform = mockMvc.perform(put(
                "/v1/categories/" + toEntity.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)));

        // then
        perform.andDo(print()).andExpect(status().is5xxServerError());

        verify(commandCategoryService, never()).update(any());
    }

}
