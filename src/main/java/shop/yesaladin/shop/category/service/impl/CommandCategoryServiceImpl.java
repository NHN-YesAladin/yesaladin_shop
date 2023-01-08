package shop.yesaladin.shop.category.service.impl;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.repository.CommandCategoryRepository;
import shop.yesaladin.shop.category.dto.CategoryCreateDto;
import shop.yesaladin.shop.category.dto.CategoryDeleteDto;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.category.dto.CategoryUpdateDto;
import shop.yesaladin.shop.category.service.inter.CommandCategoryService;
import shop.yesaladin.shop.category.service.inter.QueryCategoryService;

/**
 * 카테고리 CUD용 카테고리 서비스 구현체
 *
 * @author 배수한
 * @since 1.0
 */

@RequiredArgsConstructor
@Service
public class CommandCategoryServiceImpl implements CommandCategoryService {

    private final CommandCategoryRepository commandCategoryRepository;
    private final QueryCategoryService queryCategoryService;

    @Transactional
    @Override
    public Category create(CategoryCreateDto createDto) {
        return commandCategoryRepository.save(createDto.toEntity());
    }

    @Transactional
    @Override
    public Category update(CategoryUpdateDto updateDto) {
        CategoryResponseDto responseParentDto = null;
        if (Objects.nonNull(updateDto.getParentId())) {
            responseParentDto = queryCategoryService.findCategoryById(updateDto.getParentId());
        }

        //TODO service 단의 return 값을 모두 dto로 바꿀때 변경 -> null 임시 값
        return commandCategoryRepository.save(updateDto.toEntity(null));
    }

    @Transactional
    @Override
    public void delete(CategoryDeleteDto deleteDto) {
        commandCategoryRepository.deleteById(deleteDto.getId());
    }
}
