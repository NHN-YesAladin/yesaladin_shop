package shop.yesaladin.shop.category.service.impl;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.repository.CommandCategoryRepository;
import shop.yesaladin.shop.category.domain.repository.QueryCategoryRepository;
import shop.yesaladin.shop.category.dto.CategoryOnlyIdDto;
import shop.yesaladin.shop.category.dto.CategoryRequestDto;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
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
    private final QueryCategoryRepository queryCategoryRepository;
    private final QueryCategoryService queryCategoryService;


    /**
     * 카테고리 생성을 위한 기능 요청 dto에 부모 카테고리의 id가 있는 경우 id를 통한 카테고리 조회 추가 실행 부모 id가 null이 아닌 경우 : 동일한
     * parentId를 가지는 카테고리중 id에 100L을 더하여 엔티티 생성 부모 id가 null인 경우 : 카테고리 id에 10000L 더하여 엔티티 생성
     * <p>
     * *  해당 save() 메서드는 기본키 생성이 데이터베이스에 없기 때문에 select문이 한 번 실행되어 *   해당하는 pk 값이 없는지 확인 후 insert
     * 한다.
     *
     * @param createRequest 카테고리의 일부 정보를 담은 request Dto
     * @return CategoryResponse 카테고리의 일부 정보를 담은 response Dto
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
        CategoryOnlyIdDto onlyChildId = queryCategoryRepository.getLatestChildIdByDepthAndParentId(
                Category.DEPTH_CHILD,
                createRequest.getParentId()
        );
        Category parentCategory = queryCategoryService.findInnerCategoryById(createRequest.getParentId());

        Category category = commandCategoryRepository.save(createRequest.toEntity(
                onlyChildId.getId() + Category.TERM_OF_CHILD_ID,
                Category.DEPTH_CHILD,
                parentCategory
        ));
        return CategoryResponseDto.fromEntity(category);
    }

    /**
     * 카테고리 수정을 위한 기능
     *   1. id를 통해 해당하는 카테고리를 찾고 변경된 값이 있을 경우, 해당 트랜잭션이 변경 되면 변경 감지를 통해 변경
     *   2. parentId에 수정이 필요한 경우 3가지 케이스에 대처한다
     *   @see CommandCategoryServiceImpl#getResponseDtoByUpdateCase(CategoryRequestDto, Category, String)
     *   3. CategoryResponseDto 반환
     *
     * @param id            수정하고자 하는 카테고리 id
     * @param createRequest 카테고리의 일부 정보를 담은 request Dto
     * @return CategoryResponse 카테고리의 일부 정보를 담은 response Dto
     */
    @Transactional
    @Override
    public CategoryResponseDto update(Long id, CategoryRequestDto createRequest) {
        Category categoryById = queryCategoryService.findInnerCategoryById(id);
        String nameBeforeChanging = categoryById.getName();
        categoryById.verifyChange(
                createRequest.getName(),
                createRequest.getIsShown(),
                createRequest.getOrder()
        );

        return getResponseDtoByUpdateCase(createRequest, categoryById, nameBeforeChanging);
    }

    /**
     *   parentId에 수정이 필요한 경우 3가지 케이스에 대처한다
     *     CASE 1) 기존 parentId가 null이거나 달라지지 않았을 경우 , 이름 등 다른 필드만 변경
     *     CASE 2) 2차 카테고리이고 변경하고자하는 parentId가 null 인 경우, 1차 카테고리로 새로 생성
     *     CASE 3) 2차 카테고리이고 변경하고자 하는 parentId가 다른 1차 카테고리의 아이디 인경우,
     *             다른 1차 카테고리 id를 부모 id로 가지는 2차 카테고리로 새로 생성
     *
     *   categoryById.disableCategory(nameBeforeChanging)
     *             : 새로 카테고리가 생성되고 기존의 카테고리의 이름을 다시 복구하고,
     *               depth를 -1로 변경하여 해당 카테고리를 disable 한 것으로 활용한다.
     *
     * @param createRequest 요청 dto
     * @param categoryById 변경될 카테고리 객체
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
