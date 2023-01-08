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
import shop.yesaladin.shop.category.dto.CategoryRequest;
import shop.yesaladin.shop.category.dto.CategoryDeleteDto;
import shop.yesaladin.shop.category.dto.CategoryResponse;
import shop.yesaladin.shop.category.dummy.CategoryDummy;
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
        CategoryRequest createDto = new CategoryRequest(name, true, null);
        Category toEntity = createDto.toEntity(null);
        when(commandCategoryRepository.save(any())).thenReturn(toEntity);

        //when
        CategoryResponse categoryResponse = commandCategoryService.create(createDto);

        //then
        assertThat(categoryResponse.getName()).isEqualTo(toEntity.getName());

        verify(commandCategoryRepository, times(1)).save(any());
    }

    @Test
    void update() {
        // given
        Long parentId = 10000L;
        String name = "소설";
        Category parent = CategoryDummy.dummyParent(parentId);

        CategoryRequest categoryRequest = new CategoryRequest(name, true, parent.getId());
        Category toEntity = categoryRequest.toEntity(parent);

        when(queryCategoryService.findParentCategoryById(parentId)).thenReturn(parent);
        when(commandCategoryRepository.save(any())).thenReturn(toEntity);

        // when
        CategoryResponse categoryResponse = commandCategoryService.update(toEntity.getId(), categoryRequest);

        // then
        assertThat(categoryResponse.getParentId()).isEqualTo(parent.getId());
        assertThat(categoryResponse.getName()).isEqualTo(toEntity.getName());

        verify(queryCategoryService, times(1)).findParentCategoryById(parentId);
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
