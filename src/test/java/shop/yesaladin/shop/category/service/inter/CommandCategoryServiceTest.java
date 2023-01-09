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

    }

    @Test
    @DisplayName("1차 카테고리 생성 성공 - DB에 ")
    void create_parent() {
        //given
        CategoryRequestDto createDto = new CategoryRequestDto(
                parentCategory.getName(),
                parentCategory.isShown(),
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
    void create_child() {
        //given
        Category childCategory = CategoryDummy.dummyChild(childId, parentCategory);
        CategoryRequestDto createDto = new CategoryRequestDto(
                childCategory.getName(),
                childCategory.isShown(),
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
        verify(queryDslCategoryRepository, times(1)).getLatestChildIdByDepthAndParentId(
                Category.DEPTH_CHILD,
                childCategory.getParent().getId()
        );
    }

    @Test
    void update() {
        // given

        String name = "소설";


        CategoryRequestDto categoryRequestDto = new CategoryRequestDto(name, true, parentCategory.getId());
        Category toEntity = categoryRequestDto.toEntity(parentCategory);

        when(queryCategoryService.findParentCategoryById(parentId)).thenReturn(parentCategory);
        when(commandCategoryRepository.save(any())).thenReturn(toEntity);

        // when
        CategoryResponseDto categoryResponseDto = commandCategoryService.update(toEntity.getId(),
                categoryRequestDto
        );

        // then
        assertThat(categoryResponseDto.getParentId()).isEqualTo(parentCategory.getId());
        assertThat(categoryResponseDto.getName()).isEqualTo(toEntity.getName());

        verify(queryCategoryService, times(1)).findParentCategoryById(parentId);
        verify(commandCategoryRepository, times(1)).save(any());
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
