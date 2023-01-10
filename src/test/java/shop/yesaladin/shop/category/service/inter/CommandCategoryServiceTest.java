package shop.yesaladin.shop.category.service.inter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.repository.CommandCategoryRepository;
import shop.yesaladin.shop.category.domain.repository.QueryDslCategoryRepository;
import shop.yesaladin.shop.category.dto.CategoryOnlyIdDto;
import shop.yesaladin.shop.category.dto.CategoryRequestDto;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.category.dummy.CategoryDummy;
import shop.yesaladin.shop.category.service.impl.CommandCategoryServiceImpl;

class CommandCategoryServiceTest {

    private CommandCategoryRepository commandCategoryRepository;
    private QueryDslCategoryRepository queryDslCategoryRepository;
    private QueryCategoryService queryCategoryService;
    private CommandCategoryService commandCategoryService;

    Category parentCategory;
    Category childCategory;
    Long parentId = 10000L;
    Long childId = 10100L;

    @BeforeEach
    void setUp() {
        commandCategoryRepository = mock(CommandCategoryRepository.class);
        queryCategoryService = mock(QueryCategoryService.class);
        queryDslCategoryRepository = mock(QueryDslCategoryRepository.class);

        commandCategoryService = new CommandCategoryServiceImpl(
                commandCategoryRepository,
                queryDslCategoryRepository,
                queryCategoryService
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
        Category toEntity = createDto.toEntity(parentCategory.getId(),
                parentCategory.getDepth(),
                null);

        when(commandCategoryRepository.save(any())).thenReturn(toEntity);
        when(queryDslCategoryRepository.getLatestIdByDepth(Category.DEPTH_PARENT)).thenReturn(idDto);

        //when
        CategoryResponseDto categoryResponseDto = commandCategoryService.create(createDto);

        //then
        assertThat(categoryResponseDto.getName()).isEqualTo(toEntity.getName());
        assertThat(categoryResponseDto.getIsShown()).isEqualTo(toEntity.isShown());

        verify(commandCategoryRepository, times(1)).save(any());
        verify(queryDslCategoryRepository, times(1)).getLatestIdByDepth(Category.DEPTH_PARENT);
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

        Category toEntity = createDto.toEntity(childCategory.getId(),
                childCategory.getDepth(),
                childCategory.getParent());

        when(commandCategoryRepository.save(any())).thenReturn(toEntity);
        when(queryDslCategoryRepository.getLatestChildIdByDepthAndParentId(Category.DEPTH_CHILD,
                childCategory.getParent().getId())).thenReturn(idDto);

        //when
        CategoryResponseDto categoryResponseDto = commandCategoryService.create(createDto);

        //then
        assertThat(categoryResponseDto.getName()).isEqualTo(toEntity.getName());
        assertThat(categoryResponseDto.getIsShown()).isEqualTo(toEntity.isShown());

        verify(commandCategoryRepository, times(1)).save(any());
        verify(
                queryDslCategoryRepository,
                times(1)
        ).getLatestChildIdByDepthAndParentId(
                Category.DEPTH_CHILD,
                childCategory.getParent().getId()
        );
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

        when(queryCategoryService.findInnerCategoryById(parentCategory.getId())).thenReturn(parentCategory);

        // when
        CategoryResponseDto responseDto = commandCategoryService.update(
                parentCategory.getId(),
                categoryRequestDto
        );

        // then
        assertThat(responseDto.getId()).isEqualTo(parentCategory.getId());
        assertThat(responseDto.getName()).isEqualTo(categoryRequestDto.getName());
        assertThat(parentCategory.getDepth()).isEqualTo(Category.DEPTH_PARENT);

        verify(queryCategoryService, times(1)).findInnerCategoryById(parentCategory.getId());
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

        when(queryCategoryService.findInnerCategoryById(childCategory.getId())).thenReturn(childCategory);

        // when
        CategoryResponseDto responseDto = commandCategoryService.update(
                childCategory.getId(),
                categoryRequestDto
        );

        // then
        assertThat(responseDto.getId()).isEqualTo(childCategory.getId());
        assertThat(responseDto.getName()).isEqualTo(categoryRequestDto.getName());
        assertThat(childCategory.getDepth()).isEqualTo(Category.DEPTH_CHILD);

        verify(queryCategoryService, times(1)).findInnerCategoryById(childCategory.getId());
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

        when(queryCategoryService.findInnerCategoryById(childCategory.getId())).thenReturn(childCategory);
        when(commandCategoryRepository.save(any())).thenReturn(toEntity);
        when(queryDslCategoryRepository.getLatestIdByDepth(Category.DEPTH_PARENT)).thenReturn(idDto);


        // when
        CategoryResponseDto responseDto = commandCategoryService.update(
                childCategory.getId(),
                categoryRequestDto
        );

        // then
        assertThat(responseDto.getId()).isEqualTo(addedParentId);
        assertThat(responseDto.getName()).isEqualTo(categoryRequestDto.getName());
        assertThat(childCategory.getDepth()).isEqualTo(Category.DEPTH_DISABLE);

        verify(queryCategoryService, times(1)).findInnerCategoryById(childCategory.getId());
        verify(commandCategoryRepository, times(1)).save(any());
        verify(queryDslCategoryRepository, times(1)).getLatestIdByDepth(Category.DEPTH_PARENT);
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

        when(queryCategoryService.findInnerCategoryById(childCategory.getId())).thenReturn(childCategory);
        when(commandCategoryRepository.save(any())).thenReturn(toEntity);
        when(queryDslCategoryRepository.getLatestChildIdByDepthAndParentId(
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
        assertThat(childCategory.getDepth()).isEqualTo(Category.DEPTH_DISABLE);

        verify(queryCategoryService, times(1)).findInnerCategoryById(childCategory.getId());
        verify(queryCategoryService, times(1)).findInnerCategoryById(categoryRequestDto.getParentId());
        verify(commandCategoryRepository, times(1)).save(any());
        verify(
                queryDslCategoryRepository,
                times(1)
        ).getLatestChildIdByDepthAndParentId(
                Category.DEPTH_CHILD,
                categoryRequestDto.getParentId()
        );
    }

    @Test
    void delete() {
        // given
        long id = 1L;
        doNothing().when(commandCategoryRepository).deleteById(id);

        // then
        assertThatCode(() -> commandCategoryService.delete(id)).doesNotThrowAnyException();

    }
}
