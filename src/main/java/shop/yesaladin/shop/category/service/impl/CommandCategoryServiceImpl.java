package shop.yesaladin.shop.category.service.impl;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.repository.CommandCategoryRepository;
import shop.yesaladin.shop.category.dto.CategoryRequest;
import shop.yesaladin.shop.category.dto.CategoryOnlyId;
import shop.yesaladin.shop.category.dto.CategoryResponse;
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


    /**
     * 카테고리 생성을 위한 기능
     *  요청 dto에 부모 카테고리의 id가 있는 경우 id를 통한 카테고리 조회 추가 실행
     *
     * @param createRequest 카테고리의 일부 정보를 담은 request Dto
     * @return CategoryResponse 카테고리의 일부 정보를 담은 response Dto
     */
    @Transactional
    @Override
    public CategoryResponse create(CategoryRequest createRequest) {
        Category parentCategory = null;
        if (Objects.nonNull(createRequest.getParentId())) {
            parentCategory = queryCategoryService.findParentCategoryById(createRequest.getParentId());
        }
        Category category = commandCategoryRepository.save(createRequest.toEntity(parentCategory));
        return CategoryResponse.fromEntity(category);
    }

    /**
     * 카테고리 수정을 위한 기능
     *  요청 dto에 부모 카테고리의 id가 있는 경우 id를 통한 카테고리 조회 추가 실행
     *
     * @param id 수정하고자 하는 카테고리 id
     * @param createRequest 카테고리의 일부 정보를 담은 request Dto
     * @return CategoryResponse 카테고리의 일부 정보를 담은 response Dto
     */
    @Transactional
    @Override
    public CategoryResponse update(Long id, CategoryRequest createRequest) {
        Category parentCategory = null;
        if (Objects.nonNull(createRequest.getParentId())) {
            parentCategory = queryCategoryService.findParentCategoryById(createRequest.getParentId());
        }
        Category category = commandCategoryRepository.save(createRequest.toEntity(
                id,
                parentCategory
        ));
        return CategoryResponse.fromEntity(category);
    }

    /**
     * 카테고리 삭제를 위한 기능
     *
     * @param id 삭제하고자 하는 카테고리 id
     */
    @Transactional
    @Override
    public void delete(Long id) {
        commandCategoryRepository.deleteById(id);
    }
}
