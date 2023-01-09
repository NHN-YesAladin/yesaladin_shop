package shop.yesaladin.shop.category.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import shop.yesaladin.shop.category.dto.CategoryRequest;
import shop.yesaladin.shop.category.dto.CategoryOnlyId;
import shop.yesaladin.shop.category.dto.CategoryResponse;
import shop.yesaladin.shop.category.service.inter.CommandCategoryService;


@WebMvcTest(CommandCategoryController.class)
class CommandCategoryControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommandCategoryService commandCategoryService;

    private Long id;
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
        // given
        CategoryRequest createDto = new CategoryRequest(name, true, null);
        given(commandCategoryService.create(any())).willReturn(CategoryResponse.fromEntity(category));

        // when
        ResultActions perform = mockMvc.perform(post("/v1/categories").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));

        // then
        perform.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().longValue("Location", id));

        verify(commandCategoryService, times(1)).create(any());
    }

    @Test
    @DisplayName("카테고리 생성 실패 - name이 null 인 경우")
    void createCategory_invalidatedData_fail() throws Exception {
        // given
        CategoryRequest createDto = new CategoryRequest(null, true, null);

        //when
        ResultActions perform = mockMvc.perform(post("/v1/categories").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));

        // then
        perform.andDo(print()).andExpect(status().is5xxServerError());

        verify(commandCategoryService, never()).create(any());
    }

    @Test
    @DisplayName("카테고리 수정 성공")
    void updateCategory() throws Exception {
        // given
        String changeName = "중고서적";
        CategoryRequest categoryRequest = new CategoryRequest(name, true, null);
        Category toEntity = categoryRequest.toEntity(id, null);
        given(commandCategoryService.update(any(), any())).willReturn(CategoryResponse.fromEntity(
                toEntity));

        // when
        ResultActions perform = mockMvc.perform(put(
                "/v1/categories/" + toEntity.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequest)));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(toEntity.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(toEntity.getName())))
                .andExpect(jsonPath("$.isShown", equalTo(toEntity.isShown())));

        verify(commandCategoryService, times(1)).update(any(), any());
    }

    @Test
    @DisplayName("카테고리 수정 실패 - name이 null 인 경우")
    void updateCategory_invalidatedData_fail() throws Exception {
        // given
        String nullName = null;
        CategoryRequest categoryRequest = new CategoryRequest(nullName, true, null);
        Category toEntity = categoryRequest.toEntity(id, null);
        given(commandCategoryService.update(any(), any())).willReturn(CategoryResponse.fromEntity(
                toEntity));

        // when
        ResultActions perform = mockMvc.perform(put(
                "/v1/categories/" + toEntity.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequest)));

        // then
        perform.andDo(print()).andExpect(status().is5xxServerError());

        verify(commandCategoryService, never()).update(any(), any());
    }

    @Test
    @DisplayName("카테고리 삭제 성공")
    void deleteCategory() throws Exception {
        // given
        willDoNothing().given(commandCategoryService).delete(category.getId());
        // when
        ResultActions perform = mockMvc.perform(delete(
                "/v1/categories/" + category.getId()));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", equalTo("Success")));
    }

}
