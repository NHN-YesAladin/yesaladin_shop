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
import org.junit.jupiter.api.Test;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.repository.CommandCategoryRepository;
import shop.yesaladin.shop.category.dto.CategoryCreateDto;
import shop.yesaladin.shop.category.dto.CategoryDeleteDto;
import shop.yesaladin.shop.category.dto.CategoryUpdateDto;
import shop.yesaladin.shop.category.service.impl.CommandCategoryServiceImpl;

class CommandCategoryServiceTest {

    private CommandCategoryRepository commandCategoryRepository;
    private QueryCategoryService queryCategoryService;
    private CommandCategoryService commandCategoryService;

    @BeforeEach
    void setUp() {
        commandCategoryRepository = mock(CommandCategoryRepository.class);
        queryCategoryService = mock(QueryCategoryService.class);

        commandCategoryService = new CommandCategoryServiceImpl(
                commandCategoryRepository,
                queryCategoryService
        );
    }

    @Test
    void create() {
        //given
        String name = "국내도서";
        CategoryCreateDto createDto = new CategoryCreateDto(name);
        Category toEntity = createDto.toEntity();
        when(commandCategoryRepository.save(any())).thenReturn(toEntity);

        //when
        Category category = commandCategoryService.create(createDto);

        //then
        assertThat(category.getName()).isEqualTo(toEntity.getName());

        verify(commandCategoryRepository, times(1)).save(any());
    }

    @Test
    void update() {
        // given
        Long parentId = 1L;
        String parentName = "국내도서";

        Long id = 2L;
        String name = "소설";
        boolean isShown = true;
        Category parent = Category.builder()
                .id(parentId)
                .name(parentName)
                .order(null)
                .isShown(true)
                .parent(null)
                .build();

        CategoryUpdateDto updateDto = new CategoryUpdateDto(
                id,
                name,
                isShown,
                null,
                parent.getId()
        );
        Category toEntity = updateDto.toEntity(parent);

        when(queryCategoryService.findCategoryById(updateDto.getParentId())).thenReturn(parent);
        when(commandCategoryRepository.save(any())).thenReturn(toEntity);

        // when
        Category update = commandCategoryService.update(updateDto);

        // then
        assertThat(update.getParent()).isEqualTo(parent);
        assertThat(update.getId()).isEqualTo(toEntity.getId());

        verify(queryCategoryService, times(1)).findCategoryById(updateDto.getParentId());
        verify(commandCategoryRepository, times(1)).save(any());
    }

    @Test
    void delete() {
        // given
        long id = 1L;
        CategoryDeleteDto deleteDto = new CategoryDeleteDto(id);
        doNothing().when(commandCategoryRepository).deleteById(deleteDto.getId());

        // then
        assertThatCode(() -> commandCategoryService.delete(deleteDto)).doesNotThrowAnyException();

    }
}
