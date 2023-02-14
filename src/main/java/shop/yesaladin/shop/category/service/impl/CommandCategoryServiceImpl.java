package shop.yesaladin.shop.category.service.impl;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.repository.CommandCategoryRepository;
import shop.yesaladin.shop.category.domain.repository.QueryCategoryRepository;
import shop.yesaladin.shop.category.dto.CategoryModifyRequestDto;
import shop.yesaladin.shop.category.dto.CategoryOnlyIdDto;
import shop.yesaladin.shop.category.dto.CategoryRequestDto;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.category.exception.CategoryNotFoundException;
import shop.yesaladin.shop.category.service.inter.CommandCategoryService;

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
    private final QueryCategoryRepository queryCategoryRepository;


    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public CategoryResponseDto create(CategoryRequestDto createRequest) {

        if (Objects.nonNull(createRequest.getParentId())) {
            return saveCategoryByAddingChildId(createRequest);
        }
        return saveCategoryByAddingId(createRequest);
    }

    /**
     * 1차 카테고리인 경우,카테고리 생성시 id에 10000L씩 더해서 id 배정
     *
     * @param createRequest
     * @return CategoryResponseDto
     */
    private CategoryResponseDto saveCategoryByAddingId(CategoryRequestDto createRequest) {
        CategoryOnlyIdDto onlyParentId = queryCategoryRepository.getLatestIdByDepth(Category.DEPTH_PARENT);

        createRequest.setOrder(
                queryCategoryRepository.getLatestOrderByDepth(Category.DEPTH_PARENT) + 1);
        Category category = commandCategoryRepository.save(createRequest.toEntity(
                onlyParentId.getId() + Category.TERM_OF_PARENT_ID, Category.DEPTH_PARENT, null));
        return CategoryResponseDto.fromEntity(category);
    }

    /**
     * 2차 카테고리 인 경우, 카테고리 생성시 id에 100L씩 더해서 id 배정
     *
     * @param createRequest
     * @return CategoryResponseDto
     */
    private CategoryResponseDto saveCategoryByAddingChildId(CategoryRequestDto createRequest) {
        Long parentId = createRequest.getParentId();
        CategoryOnlyIdDto onlyChildId = queryCategoryRepository.getLatestChildIdByDepthAndParentId(
                Category.DEPTH_CHILD,
                parentId
        );
        Category parentCategory = queryCategoryRepository.findById(parentId)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.CATEGORY_NOT_FOUND,
                        "Category not found with id : " + parentId
                ));

        createRequest.setOrder(queryCategoryRepository.getLatestChildOrderByDepthAndParentId(
                Category.DEPTH_CHILD,
                parentId
        ) + 1);
        Category category = commandCategoryRepository.save(createRequest.toEntity(
                onlyChildId.getId() + Category.TERM_OF_CHILD_ID,
                Category.DEPTH_CHILD,
                parentCategory
        ));
        return CategoryResponseDto.fromEntity(category);
    }


    /**
     *  {@inheritDoc}
     *
     */
    @Transactional
    @Override
    public CategoryResponseDto update(Long id, CategoryRequestDto createRequest) {
        Category categoryById = queryCategoryRepository.findById(id)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.CATEGORY_NOT_FOUND,
                        "Category not found with id : " + id
                ));
        String nameBeforeChanging = categoryById.getName();
        categoryById.verifyChange(
                createRequest.getName(),
                createRequest.getIsShown(),
                createRequest.getOrder()
        );

        return getResponseDtoByUpdateCase(createRequest, categoryById, nameBeforeChanging);
    }

    /**
     * parentId에 수정이 필요한 경우 3가지 케이스에 대처한다
     * CASE 1) 기존 parentId가 null이거나 달라지지 않았을 경우 , 이름 등 다른 필드만 변경
     * CASE 2) 2차 카테고리이고 변경하고자하는 parentId가 null 인 경우, 1차 카테고리로 새로 생성
     * CASE 3) 2차 카테고리이고 변경하고자 하는 parentId가 다른 1차 카테고리의 아이디 인경우, 다른 1차 카테고리 id를 부모 id로 가지는 2차 카테고리로 새로 생성
     * <p>
     * categoryById.disableCategory(nameBeforeChanging) : 새로 카테고리가 생성되고 기존의 카테고리의 이름을 다시 복구하고,
     * depth를 -1로 변경하여 해당 카테고리를 disable 한 것으로 활용한다.
     *
     * @param createRequest      요청 dto
     * @param categoryById       변경될 카테고리 객체
     * @param nameBeforeChanging 변경 이후 객체의 이름을 되돌려 놓기 위한 String 객체
     * @return CategoryResponseDto
     */
    private CategoryResponseDto getResponseDtoByUpdateCase(
            CategoryRequestDto createRequest,
            Category categoryById,
            String nameBeforeChanging
    ) {
        Long requestParentId = createRequest.getParentId();
        if (Objects.isNull(categoryById.getParent()) || categoryById.getParent()
                .getId()
                .equals(requestParentId)) {
            // CASE 1)
            return CategoryResponseDto.fromEntity(categoryById);
        }
        if (Objects.isNull(requestParentId)) {
            // CASE 2)
            categoryById.disableCategory(nameBeforeChanging);
            return this.saveCategoryByAddingId(createRequest);
        }
        // CASE 3)
        categoryById.disableCategory(nameBeforeChanging);
        return this.saveCategoryByAddingChildId(createRequest);
    }


    /**
     *  {@inheritDoc}
     *
     */
    @Transactional
    @Override
    public void delete(Long id) {
        Category category = queryCategoryRepository.findById(id)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.CATEGORY_NOT_FOUND,
                        "Category not found with id : " + id
                ));
        category.disableCategory(category.getName());
    }


    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void updateOrder(List<CategoryModifyRequestDto> requestList) {
        //1차 카테고리 순서 변경
        Long parentId = requestList.get(0).getParentId();
        if (Objects.isNull(parentId)) {
            List<Category> categories = queryCategoryRepository.findCategories(
                    null,
                    Category.DEPTH_PARENT
            );
            for (CategoryModifyRequestDto request : requestList) {
                Long id = request.getId();
                Category category = categories.stream()
                        .filter(it -> it.getId().equals(id))
                        .findFirst()
                        .orElseThrow(() -> new ClientException(
                                ErrorCode.CATEGORY_NOT_FOUND,
                                "Category not found with id : " + id
                        ));
                category.verifyChange(null, null, request.getOrder());
            }
            return;
        }

        //2차 카테고리 순서 변경
        Category parent = queryCategoryRepository.findById(parentId)
                .orElseThrow(() -> new CategoryNotFoundException(parentId));
        for (CategoryModifyRequestDto request : requestList) {
            Category category = parent.getChildren()
                    .stream()
                    .filter(it -> it.getId().equals(request.getId()))
                    .findFirst()
                    .orElseThrow(() -> new ClientException(
                            ErrorCode.CATEGORY_NOT_FOUND,
                            "Category not found with id : " + request.getId()
                    ));
            category.verifyChange(null, null, request.getOrder());
        }
    }
}
