package shop.yesaladin.shop.category.service.impl;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.repository.CommandCategoryRepository;
import shop.yesaladin.shop.category.domain.repository.QueryDslCategoryRepository;
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
    private final QueryDslCategoryRepository queryDslCategoryRepository;
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
        CategoryOnlyIdDto onlyParentId = queryDslCategoryRepository.getLatestIdByDepth(Category.DEPTH_PARENT);

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
        CategoryOnlyIdDto onlyChildId = queryDslCategoryRepository.getLatestChildIdByDepthAndParentId(
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
     * 카테고리 수정을 위한 기능 요청 dto에 부모 카테고리의 id가 있는 경우 id를 통한 카테고리 조회 추가 실행
     *
     * @param id            수정하고자 하는 카테고리 id
     * @param createRequest 카테고리의 일부 정보를 담은 request Dto
     * @return CategoryResponse 카테고리의 일부 정보를 담은 response Dto
     */
//    @Transactional
//    @Override
//    public CategoryResponseDto update(Long id, CategoryRequestDto createRequest) {
//        Category parentCategory = null;
//        if (Objects.nonNull(createRequest.getParentId())) {
//            parentCategory = queryCategoryService.findInnerCategoryById(createRequest.getParentId());
//        }
//        Category category = commandCategoryRepository.save(createRequest.toEntity(
//                id,
//                0,
//                parentCategory
//        ));
//        return CategoryResponseDto.fromEntity(category);
//    }

    @Transactional
    @Override
    public CategoryResponseDto update(Long id, CategoryRequestDto createRequest) {
        Category categoryById = queryCategoryService.findInnerCategoryById(id);
        categoryById.verifyChange(
                createRequest.getName(),
                createRequest.getIsShown(),
                createRequest.getOrder()
        );

        // 부모 카테고리의 id를 변경해야할 경우
        //  새롭게 insert 후 기존 데이터 delete (fk로 걸려있어서 힘듦)
        //TODO FK 연결로 인한 삭제 불가 -> disable 상태로 만들어야함
        Long requestParentId = createRequest.getParentId();

        if (Objects.nonNull(categoryById.getParent())) {
            if (Objects.isNull(requestParentId)) {
                return this.saveCategoryByAddingId(createRequest);
            }else if (!categoryById.getParent().getId().equals(requestParentId)) {
                return this.saveCategoryByAddingChildId(createRequest);
            }
        }else{
            if (Objects.nonNull(requestParentId)) {
                return this.saveCategoryByAddingChildId(createRequest);
            }
        }

        return CategoryResponseDto.fromEntity(categoryById);
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
