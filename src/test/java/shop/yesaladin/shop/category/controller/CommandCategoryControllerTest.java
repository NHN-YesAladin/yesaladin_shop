package shop.yesaladin.shop.category.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.dto.CategoryModifyRequestDto;
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

    private static void documentCreateCategory(ResultActions perform, String identifier)
            throws Exception {
        perform.andDo(document(
                identifier,
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("???????????? ??????"),
                        fieldWithPath("isShown").type(JsonFieldType.BOOLEAN)
                                .description("???????????? ?????? ??????")
                                .attributes(defaultValue(true)),
                        fieldWithPath("order").type(JsonFieldType.NUMBER)
                                .description("???????????? ??????")
                                .optional(),
                        fieldWithPath("parentId").type(JsonFieldType.NUMBER)
                                .description("?????? ????????????(=1??? ????????????)??? ?????????")
                                .optional()
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

    private static void documentModifyCategory(ResultActions perform, String identifier)
            throws Exception {
        perform.andDo(document(
                identifier,
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("id").description("????????? ???????????? ?????????")
                ),
                requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("???????????? ??????"),
                        fieldWithPath("isShown").type(JsonFieldType.BOOLEAN)
                                .description("???????????? ?????? ??????")
                                .attributes(defaultValue(true)),
                        fieldWithPath("order").type(JsonFieldType.NUMBER)
                                .description("???????????? ??????")
                                .optional(),
                        fieldWithPath("parentId").type(JsonFieldType.NUMBER)
                                .description("?????? ????????????(=1??? ????????????)??? ?????????")
                                .optional()
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

    private static void documentModifyCategories(ResultActions perform, String identifier)
            throws Exception {
        perform.andDo(document(
                identifier,
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("???????????? ?????????"),
                        fieldWithPath("[].name").type(JsonFieldType.STRING).description("???????????? ??????"),
                        fieldWithPath("[].isShown").type(JsonFieldType.BOOLEAN)
                                .description("???????????? ?????? ??????"),
                        fieldWithPath("[].order").type(JsonFieldType.NUMBER)
                                .optional()
                                .description("???????????? ??????"),
                        fieldWithPath("[].parentId").type(JsonFieldType.NUMBER).optional()
                                .description("?????? ????????????(=1??? ????????????)??? ?????????"),
                        fieldWithPath("[].parentName").type(JsonFieldType.STRING).optional()
                                .description("?????? ????????????(=1??? ????????????)??? ??????")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("?????? ?????? ??????"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("HTTP status code"),
                        fieldWithPath("data.result").type(JsonFieldType.STRING)
                                .description("?????? ?????? ?????????"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("???????????????")
                                .optional()
                )
        ));
    }

    @BeforeEach
    void setUp() {
        parentCategory = CategoryDummy.dummyParent(parentId);
        childCategory = CategoryDummy.dummyChild(parentCategory);
    }

    @WithMockUser
    @Test
    @DisplayName("1??? ???????????? ?????? ??????")
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
        ResultActions perform = mockMvc.perform(post("/v1/categories")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));

        // then
        perform.andExpect(status().isCreated())
                .andExpect(header().stringValues("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.CREATED.value())))
                .andExpect(jsonPath("$.data.id", equalTo(responseDto.getId().intValue())))
                .andExpect(jsonPath("$.data.name", equalTo(responseDto.getName())))
                .andExpect(jsonPath("$.data.isShown", equalTo(responseDto.getIsShown())));

        verify(commandCategoryService, times(1)).create(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getName()).isEqualTo(createDto.getName());
        assertThat(argumentCaptor.getValue().getOrder()).isEqualTo(createDto.getOrder());
        assertThat(argumentCaptor.getValue().getParentId()).isEqualTo(createDto.getParentId());

        documentCreateCategory(perform, "create-parent-category");
    }

    @WithMockUser
    @Test
    @DisplayName("2??? ???????????? ?????? ??????")
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
        ResultActions perform = mockMvc.perform(post("/v1/categories")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));

        // then
        perform.andExpect(status().isCreated())
                .andExpect(header().stringValues("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.CREATED.value())))
                .andExpect(jsonPath("$.data.id", equalTo(responseDto.getId().intValue())))
                .andExpect(jsonPath("$.data.name", equalTo(responseDto.getName())))
                .andExpect(jsonPath("$.data.isShown", equalTo(responseDto.getIsShown())));

        verify(commandCategoryService, times(1)).create(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getName()).isEqualTo(createDto.getName());
        assertThat(argumentCaptor.getValue().getOrder()).isEqualTo(createDto.getOrder());
        assertThat(argumentCaptor.getValue().getParentId()).isEqualTo(createDto.getParentId());

        documentCreateCategory(perform, "create-child-category");
    }

    @WithMockUser
    @Test
    @DisplayName("???????????? ?????? ?????? - name??? null ??? ??????")
    void createCategory_invalidatedData_fail() throws Exception {
        // given
        CategoryRequestDto createDto = new CategoryRequestDto(null, true, null, null);

        //when
        ResultActions perform = mockMvc.perform(post("/v1/categories")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)));

        // then
        perform.andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(false)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath(
                        "$.errorMessages[0]",
                        equalTo(ErrorCode.BAD_REQUEST.getDisplayName())
                ));

        verify(commandCategoryService, never()).create(any());

    }

    @WithMockUser
    @Test
    @DisplayName("1??? ???????????? ?????? ?????? - ?????? id ?????? ?????? ?????? ??????")
    void updateCategory_parent_otherFieldChange() throws Exception {
        // given
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<CategoryRequestDto> dtoArgumentCaptor = ArgumentCaptor.forClass(
                CategoryRequestDto.class);

        String changeName = "????????????";
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
                "/v1/categories/{id}", toEntity.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequestDto)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(header().stringValues("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.data.id", equalTo(toEntity.getId().intValue())))
                .andExpect(jsonPath("$.data.name", equalTo(toEntity.getName())))
                .andExpect(jsonPath("$.data.isShown", equalTo(toEntity.isShown())));

        verify(commandCategoryService, times(1)).update(
                longArgumentCaptor.capture(),
                dtoArgumentCaptor.capture()
        );
        assertThat(longArgumentCaptor.getValue()).isEqualTo(responseDto.getId());
        assertThat(dtoArgumentCaptor.getValue().getName()).isEqualTo(categoryRequestDto.getName());
        assertThat(dtoArgumentCaptor.getValue()
                .getParentId()).isEqualTo(categoryRequestDto.getParentId());
        assertThat(dtoArgumentCaptor.getValue()
                .getIsShown()).isEqualTo(categoryRequestDto.getIsShown());

        documentModifyCategory(perform, "update-parent-fields-category");
    }

    @WithMockUser
    @Test
    @DisplayName("2??? ???????????? ?????? ?????? - ?????? id ?????? ?????? ?????? ??????")
    void updateCategory_child_otherFieldChange() throws Exception {
        // given
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<CategoryRequestDto> dtoArgumentCaptor = ArgumentCaptor.forClass(
                CategoryRequestDto.class);

        String changeName = "SF??????";
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
                "/v1/categories/" + toEntity.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequestDto)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(header().stringValues("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.data.id", equalTo(toEntity.getId().intValue())))
                .andExpect(jsonPath("$.data.name", equalTo(toEntity.getName())))
                .andExpect(jsonPath("$.data.isShown", equalTo(toEntity.isShown())));

        verify(commandCategoryService, times(1)).update(
                longArgumentCaptor.capture(),
                dtoArgumentCaptor.capture()
        );
        assertThat(longArgumentCaptor.getValue()).isEqualTo(responseDto.getId());
        assertThat(dtoArgumentCaptor.getValue().getName()).isEqualTo(categoryRequestDto.getName());
        assertThat(dtoArgumentCaptor.getValue()
                .getParentId()).isEqualTo(categoryRequestDto.getParentId());
        assertThat(dtoArgumentCaptor.getValue()
                .getIsShown()).isEqualTo(categoryRequestDto.getIsShown());

    }

    @WithMockUser
    @Test
    @DisplayName("2??? ???????????? ?????? ?????? - ?????? id??? null??? ?????? 1??? ??????????????? ??????")
    void updateCategory_parentIdToNull() throws Exception {
        // given
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<CategoryRequestDto> dtoArgumentCaptor = ArgumentCaptor.forClass(
                CategoryRequestDto.class);

        Long id = 10300L;
        String changeName = "?????????";
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
                "/v1/categories/{id}", id)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequestDto)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(header().stringValues("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.data.id", equalTo(toEntity.getId().intValue())))
                .andExpect(jsonPath("$.data.name", equalTo(toEntity.getName())))
                .andExpect(jsonPath("$.data.isShown", equalTo(toEntity.isShown())));

        verify(commandCategoryService, times(1)).update(
                longArgumentCaptor.capture(),
                dtoArgumentCaptor.capture()
        );
        assertThat(longArgumentCaptor.getValue()).isEqualTo(id);
        assertThat(dtoArgumentCaptor.getValue().getName()).isEqualTo(categoryRequestDto.getName());
        assertThat(dtoArgumentCaptor.getValue()
                .getParentId()).isEqualTo(categoryRequestDto.getParentId());
        assertThat(dtoArgumentCaptor.getValue()
                .getIsShown()).isEqualTo(categoryRequestDto.getIsShown());

        documentModifyCategory(perform, "update-child-to-parent-category");
    }

    @WithMockUser
    @Test
    @DisplayName("2??? ???????????? ?????? ?????? - ?????? ?????? id??? ??????")
    void updateCategory_parentToOtherParent() throws Exception {
        // given
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<CategoryRequestDto> dtoArgumentCaptor = ArgumentCaptor.forClass(
                CategoryRequestDto.class);

        String changeName = "?????????";
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
                "/v1/categories/{id}", toEntity.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequestDto)));

        // then
        perform.andExpect(status().isOk())
                .andExpect(header().stringValues("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.data.id", equalTo(toEntity.getId().intValue())))
                .andExpect(jsonPath("$.data.name", equalTo(toEntity.getName())))
                .andExpect(jsonPath("$.data.isShown", equalTo(toEntity.isShown())));

        verify(commandCategoryService, times(1)).update(
                longArgumentCaptor.capture(),
                dtoArgumentCaptor.capture()
        );
        assertThat(longArgumentCaptor.getValue()).isEqualTo(responseDto.getId());
        assertThat(dtoArgumentCaptor.getValue().getName()).isEqualTo(categoryRequestDto.getName());
        assertThat(dtoArgumentCaptor.getValue().getParentId()).isEqualTo(otherParentId);
        assertThat(dtoArgumentCaptor.getValue()
                .getIsShown()).isEqualTo(categoryRequestDto.getIsShown());

        documentModifyCategory(perform, "update-child-other-parent-category");
    }

    @WithMockUser
    @Test
    @DisplayName("???????????? ?????? ?????? - name??? null ??? ??????")
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
                "/v1/categories/" + toEntity.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequestDto)));

        // then
        perform.andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", equalTo(false)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath(
                        "$.errorMessages[0]",
                        equalTo(ErrorCode.BAD_REQUEST.getDisplayName())
                ));

        verify(commandCategoryService, never()).update(any(), any());
    }

    @WithMockUser
    @Test
    @DisplayName("?????? ???????????? ?????? ?????? ??????")
    void modifyParnetCategoriesOrder() throws Exception {
        // given
        List<CategoryModifyRequestDto> requestList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            CategoryModifyRequestDto request = CategoryModifyRequestDto.builder()
                    .id((long) i)
                    .name("name" + i)
                    .order(i + 1)
                    .isShown(true)
                    .build();
            requestList.add(request);
        }
        doNothing().when(commandCategoryService).updateOrder(requestList);

        // when
        ResultActions perform = mockMvc.perform(put(
                "/v1/categories/order")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestList)));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().stringValues(
                        "Vary",
                        "Origin",
                        "Access-Control-Request-Method",
                        "Access-Control-Request-Headers"
                ))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(200)))
                .andExpect(jsonPath("$.data.result", equalTo("Success")))
                .andExpect(jsonPath("$.errorMessages", equalTo(null)));

        documentModifyCategories(perform, "modify-parent-category-order");
    }

    @WithMockUser
    @Test
    @DisplayName("?????? ???????????? ?????? ?????? ??????")
    void modifyChildCategoriesOrder() throws Exception {
        // given
        List<CategoryModifyRequestDto> requestList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            CategoryModifyRequestDto request = CategoryModifyRequestDto.builder()
                    .id((long) i)
                    .name("name" + i)
                    .order(i + 1)
                    .isShown(true)
                    .parentId(parentCategory.getId())
                    .parentName(parentCategory.getName())
                    .build();
            requestList.add(request);
        }
        doNothing().when(commandCategoryService).updateOrder(requestList);

        // when
        ResultActions perform = mockMvc.perform(put(
                "/v1/categories/order")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestList)));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().stringValues(
                        "Vary",
                        "Origin",
                        "Access-Control-Request-Method",
                        "Access-Control-Request-Headers"
                ))
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(200)))
                .andExpect(jsonPath("$.data.result", equalTo("Success")))
                .andExpect(jsonPath("$.errorMessages", equalTo(null)));

        documentModifyCategories(perform, "modify-child-category-order");

    }

    @WithMockUser
    @Test
    @DisplayName("???????????? ?????? ??????")
    void deleteCategory() throws Exception {
        // given
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        doNothing().when(commandCategoryService).delete(parentCategory.getId());
        // when
        ResultActions perform = mockMvc.perform(delete(
                "/v1/categories/{id}", parentCategory.getId())
                .with(csrf()));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", equalTo(true)))
                .andExpect(jsonPath("$.status", equalTo(200)))
                .andExpect(jsonPath("$.data.result", equalTo("Success")))
                .andExpect(jsonPath("$.errorMessages", equalTo(null)));

        verify(commandCategoryService, times(1)).delete(longArgumentCaptor.capture());
        assertThat(longArgumentCaptor.getValue().intValue()).isEqualTo(parentCategory.getId()
                .intValue());

        perform.andDo(document(
                "delete-category",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("id").description("????????? ???????????? ?????????")
                ),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("?????? ?????? ??????"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("HTTP status code"),
                        fieldWithPath("data.result").type(JsonFieldType.STRING)
                                .description("?????? ?????? ?????????"),
                        fieldWithPath("errorMessages").type(JsonFieldType.ARRAY)
                                .description("???????????????")
                                .optional()
                )
        ));

    }

}
