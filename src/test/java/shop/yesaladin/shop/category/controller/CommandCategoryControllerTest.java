package shop.yesaladin.shop.category.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;
import static shop.yesaladin.shop.docs.DocumentFormatGenerator.defaultValue;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.dto.CategoryRequestDto;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.category.dummy.CategoryDummy;
import shop.yesaladin.shop.category.service.inter.CommandCategoryService;

@AutoConfigureRestDocs
@WebMvcTest(CommandCategoryController.class)
class CommandCategoryControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    Category parentCategory;
    Category childCategory;
    Long parentId = 10000L;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommandCategoryService commandCategoryService;

    @BeforeEach
    void setUp() {
        parentCategory = CategoryDummy.dummyParent(parentId);
        childCategory = CategoryDummy.dummyChild(parentCategory);
    }

    @Test
    @DisplayName("1차 카테고리 생성 성공")
    void createCategory_parent() throws Exception {
        // given
        ArgumentCaptor<CategoryRequestDto> argumentCaptor = ArgumentCaptor.forClass(
                CategoryRequestDto.class);

        CategoryRequestDto createDto = new CategoryRequestDto(
                parentCategory.getName(),
                parentCategory.isShown(),
                parentCategory.getOrder(),
                null
        );

        CategoryResponseDto responseDto = CategoryResponseDto.fromEntity(
                parentCategory);
        when(commandCategoryService.create(any())).thenReturn(responseDto);

        // when
        ResultActions perform = mockMvc.perform(post("/v1/categories").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().stringValues("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(responseDto.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(responseDto.getName())))
                .andExpect(jsonPath("$.isShown", equalTo(responseDto.getIsShown())));

        verify(commandCategoryService, times(1)).create(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getName()).isEqualTo(createDto.getName());
        assertThat(argumentCaptor.getValue().getOrder()).isEqualTo(createDto.getOrder());
        assertThat(argumentCaptor.getValue().getParentId()).isEqualTo(createDto.getParentId());

        documentCreateCategory(perform,"create-parent-category");
    }

    @Test
    @DisplayName("2차 카테고리 생성 성공")
    void createCategory_child() throws Exception {
        // given
        ArgumentCaptor<CategoryRequestDto> argumentCaptor = ArgumentCaptor.forClass(
                CategoryRequestDto.class);

        CategoryRequestDto createDto = new CategoryRequestDto(
                childCategory.getName(),
                childCategory.isShown(),
                childCategory.getOrder(),
                childCategory.getParent().getId()
        );

        CategoryResponseDto responseDto = CategoryResponseDto.fromEntity(childCategory);
        when(commandCategoryService.create(any())).thenReturn(responseDto);

        // when
        ResultActions perform = mockMvc.perform(post("/v1/categories").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().stringValues("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(responseDto.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(responseDto.getName())))
                .andExpect(jsonPath("$.isShown", equalTo(responseDto.getIsShown())));

        verify(commandCategoryService, times(1)).create(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getName()).isEqualTo(createDto.getName());
        assertThat(argumentCaptor.getValue().getOrder()).isEqualTo(createDto.getOrder());
        assertThat(argumentCaptor.getValue().getParentId()).isEqualTo(createDto.getParentId());

        documentCreateCategory(perform, "create-child-category");
    }

    private static void documentCreateCategory(ResultActions perform,String identifier) throws Exception {
        perform.andDo(document(
                identifier,
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("카테고리 이름"),
                        fieldWithPath("isShown").type(JsonFieldType.BOOLEAN)
                                .description("카테고리 노출 여부")
                                .attributes(defaultValue(true)),
                        fieldWithPath("order").type(JsonFieldType.NUMBER)
                                .description("카테고리 순서")
                                .optional(),
                        fieldWithPath("parentId").type(JsonFieldType.NUMBER)
                                .description("부모 카테고리(=1차 카테고리)의 아이디")
                                .optional()
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
    @DisplayName("카테고리 생성 실패 - name이 null 인 경우")
    void createCategory_invalidatedData_fail() throws Exception {
        // given
        CategoryRequestDto createDto = new CategoryRequestDto(null, true, null, null);

        //when
        ResultActions perform = mockMvc.perform(post("/v1/categories").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));

        // then
        perform.andDo(print()).andExpect(status().isBadRequest());

        verify(commandCategoryService, never()).create(any());

    }

    @Test
    @DisplayName("1차 카테고리 수정 성공 - 부모 id 제외 다른 필드 변경")
    void updateCategory_parent_otherFieldChange() throws Exception {
        // given
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<CategoryRequestDto> dtoArgumentCaptor = ArgumentCaptor.forClass(
                CategoryRequestDto.class);

        String changeName = "중고서적";
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto(
                changeName,
                parentCategory.isShown(),
                parentCategory.getOrder(),
                null
        );
        Category toEntity = categoryRequestDto.toEntity(
                parentCategory.getId(),
                parentCategory.getDepth(),
                null
        );

        CategoryResponseDto responseDto = CategoryResponseDto.fromEntity(toEntity);
        when(commandCategoryService.update(any(), any())).thenReturn(responseDto);

        // when
        ResultActions perform = mockMvc.perform(put(
                "/v1/categories/{id}", toEntity.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequestDto)));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(toEntity.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(toEntity.getName())))
                .andExpect(jsonPath("$.isShown", equalTo(toEntity.isShown())));

        verify(commandCategoryService, times(1)).update(
                longArgumentCaptor.capture(),
                dtoArgumentCaptor.capture()
        );
        assertThat(longArgumentCaptor.getValue()).isEqualTo(responseDto.getId());
        assertThat(dtoArgumentCaptor.getValue().getName()).isEqualTo(categoryRequestDto.getName());
        assertThat(dtoArgumentCaptor.getValue().getParentId()).isEqualTo(categoryRequestDto.getParentId());
        assertThat(dtoArgumentCaptor.getValue().getIsShown()).isEqualTo(categoryRequestDto.getIsShown());

        documentModifyCategory(perform,"update-parent-fields-category");
    }

    @Test
    @DisplayName("2차 카테고리 수정 성공 - 부모 id 제외 다른 필드 변경")
    void updateCategory_child_otherFieldChange() throws Exception {
        // given
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<CategoryRequestDto> dtoArgumentCaptor = ArgumentCaptor.forClass(
                CategoryRequestDto.class);

        String changeName = "SF소설";
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto(
                changeName,
                childCategory.isShown(),
                childCategory.getOrder(),
                childCategory.getParent().getId()
        );
        Category toEntity = categoryRequestDto.toEntity(
                childCategory.getId(),
                childCategory.getDepth(),
                childCategory.getParent()
        );
        CategoryResponseDto responseDto = CategoryResponseDto.fromEntity(toEntity);
        when(commandCategoryService.update(any(), any())).thenReturn(responseDto);

        // when
        ResultActions perform = mockMvc.perform(put(
                "/v1/categories/" + toEntity.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequestDto)));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(toEntity.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(toEntity.getName())))
                .andExpect(jsonPath("$.isShown", equalTo(toEntity.isShown())));

        verify(commandCategoryService, times(1)).update(
                longArgumentCaptor.capture(),
                dtoArgumentCaptor.capture()
        );
        assertThat(longArgumentCaptor.getValue()).isEqualTo(responseDto.getId());
        assertThat(dtoArgumentCaptor.getValue().getName()).isEqualTo(categoryRequestDto.getName());
        assertThat(dtoArgumentCaptor.getValue().getParentId()).isEqualTo(categoryRequestDto.getParentId());
        assertThat(dtoArgumentCaptor.getValue().getIsShown()).isEqualTo(categoryRequestDto.getIsShown());

    }

    @Test
    @DisplayName("2차 카테고리 수정 성공 - 부모 id가 null이 되어 1차 카테고리로 변환")
    void updateCategory_parentIdToNull() throws Exception {
        // given
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<CategoryRequestDto> dtoArgumentCaptor = ArgumentCaptor.forClass(
                CategoryRequestDto.class);

        Long id = 10300L;
        String changeName = "참고서";
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto(
                changeName,
                childCategory.isShown(),
                childCategory.getOrder(),
                null
        );
        Category toEntity = categoryRequestDto.toEntity(
                parentCategory.getId() + Category.TERM_OF_PARENT_ID,
                Category.DEPTH_PARENT,
                null
        );

        CategoryResponseDto responseDto = CategoryResponseDto.fromEntity(toEntity);
        when(commandCategoryService.update(any(), any())).thenReturn(responseDto);

        // when
        ResultActions perform = mockMvc.perform(put(
                "/v1/categories/{id}", id).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequestDto)));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(responseDto.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(toEntity.getName())))
                .andExpect(jsonPath("$.isShown", equalTo(toEntity.isShown())));

        verify(commandCategoryService, times(1)).update(
                longArgumentCaptor.capture(),
                dtoArgumentCaptor.capture()
        );
        assertThat(longArgumentCaptor.getValue()).isEqualTo(id);
        assertThat(dtoArgumentCaptor.getValue().getName()).isEqualTo(categoryRequestDto.getName());
        assertThat(dtoArgumentCaptor.getValue().getParentId()).isEqualTo(categoryRequestDto.getParentId());
        assertThat(dtoArgumentCaptor.getValue().getIsShown()).isEqualTo(categoryRequestDto.getIsShown());

        documentModifyCategory(perform,"update-child-to-parent-category");
    }

    @Test
    @DisplayName("2차 카테고리 수정 성공 - 다른 부모 id로 변경")
    void updateCategory_parentToOtherParent() throws Exception {
        // given
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<CategoryRequestDto> dtoArgumentCaptor = ArgumentCaptor.forClass(
                CategoryRequestDto.class);

        String changeName = "참고서";
        long otherParentId = 20000L;
        Category otherParentCategory = CategoryDummy.dummyParent(otherParentId);

        CategoryRequestDto categoryRequestDto = new CategoryRequestDto(
                changeName,
                childCategory.isShown(),
                childCategory.getOrder(),
                otherParentCategory.getId()
        );
        Category toEntity = categoryRequestDto.toEntity(
                otherParentCategory.getId() + Category.TERM_OF_CHILD_ID,
                childCategory.getDepth(),
                otherParentCategory
        );

        CategoryResponseDto responseDto = CategoryResponseDto.fromEntity(toEntity);
        when(commandCategoryService.update(any(), any())).thenReturn(responseDto);

        // when
        ResultActions perform = mockMvc.perform(put(
                "/v1/categories/{id}", toEntity.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequestDto)));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(toEntity.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo(toEntity.getName())))
                .andExpect(jsonPath("$.isShown", equalTo(toEntity.isShown())));

        verify(commandCategoryService, times(1)).update(
                longArgumentCaptor.capture(),
                dtoArgumentCaptor.capture()
        );
        assertThat(longArgumentCaptor.getValue()).isEqualTo(responseDto.getId());
        assertThat(dtoArgumentCaptor.getValue().getName()).isEqualTo(categoryRequestDto.getName());
        assertThat(dtoArgumentCaptor.getValue().getParentId()).isEqualTo(otherParentId);
        assertThat(dtoArgumentCaptor.getValue().getIsShown()).isEqualTo(categoryRequestDto.getIsShown());

        documentModifyCategory(perform,"update-child-other-parent-category");
    }

    private static void documentModifyCategory(ResultActions perform,String identifier) throws Exception {
        perform.andDo(document(
                identifier,
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("id").description("수정할 카테고리 아이디")
                ),
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("카테고리 이름"),
                        fieldWithPath("isShown").type(JsonFieldType.BOOLEAN)
                                .description("카테고리 노출 여부")
                                .attributes(defaultValue(true)),
                        fieldWithPath("order").type(JsonFieldType.NUMBER)
                                .description("카테고리 순서")
                                .optional(),
                        fieldWithPath("parentId").type(JsonFieldType.NUMBER)
                                .description("부모 카테고리(=1차 카테고리)의 아이디")
                                .optional()
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
    @DisplayName("카테고리 수정 실패 - name이 null 인 경우")
    void updateCategory_invalidatedData_fail() throws Exception {
        // given
        String nullName = null;
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto(nullName, true, null, null);
        Category toEntity = categoryRequestDto.toEntity(
                parentCategory.getId(),
                parentCategory.getDepth(),
                null
        );
        when(commandCategoryService.update(any(), any())).thenReturn(CategoryResponseDto.fromEntity(
                toEntity));

        // when
        ResultActions perform = mockMvc.perform(put(
                "/v1/categories/" + toEntity.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequestDto)));

        // then
        perform.andDo(print()).andExpect(status().isBadRequest());

        verify(commandCategoryService, never()).update(any(), any());
    }

    @Test
    @DisplayName("카테고리 삭제 성공")
    void deleteCategory() throws Exception {
        // given
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        doNothing().when(commandCategoryService).delete(parentCategory.getId());
        // when
        ResultActions perform = mockMvc.perform(delete(
                "/v1/categories/{id}", parentCategory.getId()));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", equalTo("Success")));

        verify(commandCategoryService, times(1)).delete(longArgumentCaptor.capture());
        assertThat(longArgumentCaptor.getValue().intValue()).isEqualTo(parentCategory.getId()
                .intValue());

        perform.andDo(document(
                "delete-category",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("id").description("삭제할 카테고리 아이디")
                ),
                responseFields(
                        fieldWithPath("result").type(JsonFieldType.STRING).description("삭제 성공 메시지")
                )
        ));

    }

}
