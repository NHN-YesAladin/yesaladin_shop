package shop.yesaladin.shop.category.service.inter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.repository.CommandCategoryRepository;
import shop.yesaladin.shop.category.dto.CategoryCreateDto;
import shop.yesaladin.shop.category.service.impl.CommandCategoryServiceImpl;

class CommandCategoryServiceTest {

    private CommandCategoryRepository commandCategoryRepository;
    private CommandCategoryService commandCategoryService;

    @BeforeEach
    void setUp() {
        commandCategoryRepository = mock(CommandCategoryRepository.class);

        commandCategoryService = new CommandCategoryServiceImpl(commandCategoryRepository);
    }

    @Test
    void create() {
        //given
        String name = "국내도서";
        CategoryCreateDto createDto = new CategoryCreateDto(name);
        Category toEntity = createDto.toEntity();

        //when
        when(commandCategoryRepository.save(any())).thenReturn(toEntity);

        //then
        Category category = commandCategoryService.create(createDto);

        assertThat(category.getName()).isEqualTo(toEntity.getName());

        verify(commandCategoryRepository, times(1)).save(any());
    }
}
