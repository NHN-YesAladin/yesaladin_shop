package shop.yesaladin.shop.category.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.repository.CommandCategoryRepository;
import shop.yesaladin.shop.category.dto.CategoryCreateDto;
import shop.yesaladin.shop.category.service.inter.CommandCategoryService;

@RequiredArgsConstructor
@Service
public class CommandCategoryServiceImpl implements CommandCategoryService {

    private final CommandCategoryRepository commandCategoryRepository;
    @Override
    public Category create(CategoryCreateDto createDto) {
        return commandCategoryRepository.save(createDto.toEntity());
    }
}
