package shop.yesaladin.shop.category.service.inter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.NotThrownAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.repository.CommandCategoryRepository;
import shop.yesaladin.shop.category.domain.repository.QueryCategoryRepository;
import shop.yesaladin.shop.category.dto.CategoryModifyRequestDto;
import shop.yesaladin.shop.category.dto.CategoryOnlyIdDto;
import shop.yesaladin.shop.category.dto.CategoryRequestDto;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.category.dummy.CategoryDummy;
import shop.yesaladin.shop.category.exception.CategoryNotFoundException;
import shop.yesaladin.shop.category.service.impl.CommandCategoryServiceImpl;

class CommandCategoryServiceTest {

    Category parentCategory;
    Category childCategory;
    Long parentId = 10000L;
    Long childId = 10100L;
    private CommandCategoryRepository commandCategoryRepository;
    private QueryCategoryRepository queryCategoryRepository;
    private CommandCategoryService commandCategoryService;

    @BeforeEach
    void setUp() {
        commandCategoryRepository = mock(CommandCategoryRepository.class);
        queryCategoryRepository = mock(QueryCategoryRepository.class);

        commandCategoryService = new CommandCategoryServiceImpl(
                commandCategoryRepository,
                queryCategoryRepository
        );

        parentCategory = CategoryDummy.dummyParent(parentId);
        childCategory = CategoryDummy.dummyChild(parentCategory);
    }

    @Test
    @DisplayName("카테고리 생성 성공 - 1차 카테고리")
    void create_parent() {
        //given
        CategoryRequestDto createDto = new CategoryRequestDto(
                parentCategory.getName(),
                parentCategory.isShown(),
                null,
                null
        );
        CategoryOnlyIdDto idDto = new CategoryOnlyIdDto(
                parentCategory.getId() + Category.TERM_OF_PARENT_ID);
        Category toEntity = createDto.toEntity(
                parentCategory.getId(),
                parentCategory.getDepth(),
                null
        );

        when(commandCategoryRepository.save(any())).thenReturn(toEntity);
        when(queryCategoryRepository.getLatestIdByDepth(Category.DEPTH_PARENT)).thenReturn(idDto);

        //when
        CategoryResponseDto categoryResponseDto = commandCategoryService.create(createDto);

        //then
        assertThat(categoryResponseDto.getName()).isEqualTo(toEntity.getName());
        assertThat(categoryResponseDto.getIsShown()).isEqualTo(toEntity.isShown());

        verify(commandCategoryRepository, times(1)).save(any());
        verify(queryCategoryRepository, times(1)).getLatestIdByDepth(Category.DEPTH_PARENT);
    }

    @Test
    @DisplayName("카테고리 생성 성공 - 2차 카테고리")
    void create_child() {
        //given
        Category childCategory = CategoryDummy.dummyChild(childId, parentCategory);
        CategoryRequestDto createDto = new CategoryRequestDto(
                childCategory.getName(),
                childCategory.isShown(),
                childCategory.getOrder(),
                childCategory.getParent().getId()
        );

        CategoryOnlyIdDto idDto = new CategoryOnlyIdDto(
                childCategory.getId() + Category.TERM_OF_CHILD_ID);

        Category toEntity = createDto.toEntity(
                childCategory.getId(),
                childCategory.getDepth(),
                childCategory.getParent()
        );

        when(commandCategoryRepository.save(any())).thenReturn(toEntity);
        when(queryCategoryRepository.getLatestChildIdByDepthAndParentId(
                Category.DEPTH_CHILD,
                childCategory.getParent().getId()
        )).thenReturn(idDto);
        when(queryCategoryRepository.findById(parentCategory.getId())).thenReturn(
                Optional.of(parentCategory));

        //when
        CategoryResponseDto categoryResponseDto = commandCategoryService.create(createDto);

        //then
        assertThat(categoryResponseDto.getName()).isEqualTo(toEntity.getName());
        assertThat(categoryResponseDto.getIsShown()).isEqualTo(toEntity.isShown());

        verify(commandCategoryRepository, times(1)).save(any());
        verify(
                queryCategoryRepository,
                times(1)
        ).getLatestChildIdByDepthAndParentId(
                Category.DEPTH_CHILD,
                childCategory.getParent().getId()
        );
        verify(queryCategoryRepository, times(1)).findById(parentCategory.getId());

    }

    @Test
    @DisplayName("1차 카테고리 수정 성공 - 부모 id 제외 다른 필드 변경")
    void update_parent_otherFieldChange() {
        // given
        String name = "구독상품";
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto(
                name,
                parentCategory.isShown(),
                parentCategory.getOrder(),
                null
        );

        when(queryCategoryRepository.findById(parentCategory.getId())).thenReturn(
                Optional.of(parentCategory));

        // when
        CategoryResponseDto responseDto = commandCategoryService.update(
                parentCategory.getId(),
                categoryRequestDto
        );

        // then
        assertThat(responseDto.getId()).isEqualTo(parentCategory.getId());
        assertThat(responseDto.getName()).isEqualTo(categoryRequestDto.getName());
        assertThat(parentCategory.getDepth()).isEqualTo(Category.DEPTH_PARENT);

        verify(queryCategoryRepository, times(1)).findById(parentCategory.getId());
    }


    @Test
    @DisplayName("2차 카테고리 수정 성공 - 부모 id 제외 다른 필드 변경")
    void update_child_otherFieldChange() {
        // given
        String name = "SF영화";
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto(
                name,
                childCategory.isShown(),
                childCategory.getOrder(),
                childCategory.getParent().getId()
        );

        when(queryCategoryRepository.findById(childCategory.getId())).thenReturn(
                Optional.of(childCategory));

        // when
        CategoryResponseDto responseDto = commandCategoryService.update(
                childCategory.getId(),
                categoryRequestDto
        );

        // then
        assertThat(responseDto.getId()).isEqualTo(childCategory.getId());
        assertThat(responseDto.getName()).isEqualTo(categoryRequestDto.getName());
        assertThat(childCategory.getDepth()).isEqualTo(Category.DEPTH_CHILD);

        verify(queryCategoryRepository, times(1)).findById(childCategory.getId());
    }

    @Test
    @DisplayName("2차 카테고리 수정 성공 - 부모 id가 null이 되어 1차 카테고리로 변환")
    void update_child_parentIdToNull() {
        // given
        String name = "SF영화";
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto(
                name,
                childCategory.isShown(),
                childCategory.getOrder(),
                null
        );
        CategoryOnlyIdDto idDto = new CategoryOnlyIdDto(parentCategory.getId());
        long addedParentId = parentCategory.getId() + Category.TERM_OF_PARENT_ID;
        Category toEntity = categoryRequestDto.toEntity(
                addedParentId,
                parentCategory.getDepth(),
                null
        );

        when(queryCategoryRepository.findById(childCategory.getId())).thenReturn(
                Optional.of(childCategory));
        when(commandCategoryRepository.save(any())).thenReturn(toEntity);
        when(queryCategoryRepository.getLatestIdByDepth(Category.DEPTH_PARENT)).thenReturn(idDto);

        // when
        CategoryResponseDto responseDto = commandCategoryService.update(
                childCategory.getId(),
                categoryRequestDto
        );

        // then
        assertThat(responseDto.getId()).isEqualTo(addedParentId);
        assertThat(responseDto.getName()).isEqualTo(categoryRequestDto.getName());
        assertThat(childCategory.isDisable()).isEqualTo(true);

        verify(queryCategoryRepository, times(1)).findById(childCategory.getId());
        verify(commandCategoryRepository, times(1)).save(any());
        verify(queryCategoryRepository, times(1)).getLatestIdByDepth(Category.DEPTH_PARENT);
    }


    @Test
    @DisplayName("2차 카테고리 수정 성공 - 다른 부모 id로 변경")
    void update_child_parentToOtherParent() {
        // given
        String name = "SF영화";
        long otherParentId = 20000L;
        Category otherParentCategory = CategoryDummy.dummyParent(otherParentId);
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto(
                name,
                childCategory.isShown(),
                childCategory.getOrder(),
                otherParentCategory.getId()
        );

        long addedChildId = otherParentCategory.getId() + Category.TERM_OF_CHILD_ID;
        CategoryOnlyIdDto idDto = new CategoryOnlyIdDto(otherParentCategory.getId());
        Category toEntity = categoryRequestDto.toEntity(
                addedChildId,
                childCategory.getDepth(),
                otherParentCategory
        );

        when(queryCategoryRepository.findById(childCategory.getId())).thenReturn(
                Optional.of(childCategory));
        when(queryCategoryRepository.findById(otherParentCategory.getId())).thenReturn(
                Optional.of(otherParentCategory));
        when(commandCategoryRepository.save(any())).thenReturn(toEntity);
        when(queryCategoryRepository.getLatestChildIdByDepthAndParentId(
                Category.DEPTH_CHILD,
                categoryRequestDto.getParentId()
        )).thenReturn(idDto);


        // when
        CategoryResponseDto responseDto = commandCategoryService.update(
                childCategory.getId(),
                categoryRequestDto
        );

        // then
        assertThat(responseDto.getId()).isEqualTo(addedChildId);
        assertThat(responseDto.getName()).isEqualTo(categoryRequestDto.getName());
        assertThat(childCategory.isDisable()).isEqualTo(true);

        verify(queryCategoryRepository, times(1)).findById(childCategory.getId());
        verify(queryCategoryRepository, times(1)).findById(otherParentCategory.getId());


        verify(commandCategoryRepository, times(1)).save(any());
        verify(
                queryCategoryRepository,
                times(1)
        ).getLatestChildIdByDepthAndParentId(
                Category.DEPTH_CHILD,
                categoryRequestDto.getParentId()
        );
    }

    @Test
    void delete() {
        // given
        when(queryCategoryRepository.findById(parentCategory.getId())).thenReturn(Optional.of(
                parentCategory));

        // then
        assertThatCode(() -> commandCategoryService.delete(parentCategory.getId())).doesNotThrowAnyException();

        verify(queryCategoryRepository, times(1)).findById(parentCategory.getId());
    }

    @Test
    void updateOrder_parentCategories() throws Exception {
        // given
        List<CategoryModifyRequestDto> requestList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CategoryModifyRequestDto request = CategoryModifyRequestDto.builder()
                    .id((long) i)
                    .name("name" + i)
                    .order(i + 1)
                    .isShown(true)
                    .build();
            requestList.add(request);
        }

        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Category category = Category.builder()
                    .id((long) i)
                    .name("name" + i)
                    .order(10 - i)
                    .depth(Category.DEPTH_PARENT)
                    .isShown(true)
                    .build();
            categories.add(category);
        }

        when(queryCategoryRepository.findCategories(null, Category.DEPTH_PARENT)).thenReturn(
                categories);

        // when
        assertThatCode(() -> commandCategoryService.updateOrder(requestList)).doesNotThrowAnyException();

        // then
        verify(queryCategoryRepository, times(1)).findCategories(null, Category.DEPTH_PARENT);

    }

    @Test
    void updateOrder_childCategories() throws Exception {
        // given
        List<CategoryModifyRequestDto> requestList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
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

        for (int i = 0; i < 10; i++) {
            Category category = Category.builder()
                    .id((long) i)
                    .name("name" + i)
                    .order(10 - i)
                    .depth(Category.DEPTH_PARENT)
                    .isShown(true)
                    .parent(parentCategory.getParent())
                    .build();
            parentCategory.getChildren().add(category);
        }


        when(queryCategoryRepository.findById(parentCategory.getId())).thenReturn(Optional.of(
                parentCategory));

        // when
        assertThatCode(() -> commandCategoryService.updateOrder(requestList)).doesNotThrowAnyException();

        // then
        verify(queryCategoryRepository, times(1)).findById(parentCategory.getId());

    }

    @Test
    void updateOrder_parentCategories_notFound_fail() throws Exception {
        // given
        List<CategoryModifyRequestDto> requestList = new ArrayList<>();
        for (int i = 10; i < 20; i++) {
            CategoryModifyRequestDto request = CategoryModifyRequestDto.builder()
                    .id((long) i)
                    .name("name" + i)
                    .order(i + 1)
                    .isShown(true)
                    .build();
            requestList.add(request);
        }

        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Category category = Category.builder()
                    .id((long) i)
                    .name("name" + i)
                    .order(10 - i)
                    .depth(Category.DEPTH_PARENT)
                    .isShown(true)
                    .build();
            categories.add(category);
        }

        when(queryCategoryRepository.findCategories(null, Category.DEPTH_PARENT)).thenReturn(
                categories);

        // when
        assertThatCode(() -> commandCategoryService.updateOrder(requestList)).isInstanceOf(
                CategoryNotFoundException.class);

        // then
        verify(queryCategoryRepository, times(1)).findCategories(null, Category.DEPTH_PARENT);

    }

    @Test
    void updateOrder_childCategories_notFound_fail() throws Exception {
        // given
        List<CategoryModifyRequestDto> requestList = new ArrayList<>();
        for (int i = 10; i < 20; i++) {
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

        for (int i = 0; i < 10; i++) {
            Category category = Category.builder()
                    .id((long) i)
                    .name("name" + i)
                    .order(10 - i)
                    .depth(Category.DEPTH_PARENT)
                    .isShown(true)
                    .parent(parentCategory.getParent())
                    .build();
            parentCategory.getChildren().add(category);
        }


        when(queryCategoryRepository.findById(parentCategory.getId())).thenReturn(Optional.of(
                parentCategory));

        // when
        assertThatCode(() -> commandCategoryService.updateOrder(requestList)).isInstanceOf(
                CategoryNotFoundException.class);

        // then
        verify(queryCategoryRepository, times(1)).findById(parentCategory.getId());

    }
}
