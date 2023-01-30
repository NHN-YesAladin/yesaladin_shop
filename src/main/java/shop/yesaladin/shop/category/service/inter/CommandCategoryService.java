package shop.yesaladin.shop.category.service.inter;

import java.util.List;
import shop.yesaladin.shop.category.dto.CategoryModifyRequestDto;
import shop.yesaladin.shop.category.dto.CategoryRequestDto;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;

/**
 * Create, Update, Delete 를 controller 단에서 사용하기 위해 서비스 인터페이스
 *
 * @author 배수한
 * @since 1.0
 */

public interface CommandCategoryService {

    /**
     * 카테고리 생성을 위한 기능 요청 dto에 부모 카테고리의 id가 있는 경우 id를 통한 카테고리 조회 추가 실행
     * 부모 id가 null이 아닌 경우 : 동일한 parentId를 가지는 카테고리중 id에 100L을 더하여 엔티티 생성
     * 부모 id가 null인 경우 : 카테고리 id에 10000L 더하여 엔티티 생성
     *
     * @param createRequest 카테고리의 일부 정보를 담은 request Dto
     * @return CategoryResponse 카테고리의 일부 정보를 담은 response Dto
     */
    CategoryResponseDto create(CategoryRequestDto createRequest);

    /**
     * 카테고리 수정을 위한 기능
     * 1. id를 통해 해당하는 카테고리를 찾고 변경된 값이 있을 경우, 해당 트랜잭션이 변경 되면 변경 감지를 통해 변경
     * 2. parentId에 수정이 필요한 경우 3가지 케이스에 대처한다
     *
     * @param id            수정하고자 하는 카테고리 id
     * @param createRequest 카테고리의 일부 정보를 담은 request Dto
     * @return CategoryResponse 카테고리의 일부 정보를 담은 response Dto
     */
    CategoryResponseDto update(Long id, CategoryRequestDto createRequest);


    /**
     * 카테고리 삭제를 위한 기능
     * soft delete를 위해 commandCategoryRepository.deleteById(id) 대신 disabled
     *
     * @param id 삭제하고자 하는 카테고리 id
     */
    void delete(Long id);


    /**
     * 카테고리의 순서를 변경시 최악의 경우 모든 카테고리의 순서를 바꿔야하기 때문에 List를 인자로 가지는 메서드
     *
     * @param requestList 변경한 카테고리의 순서를
     */
    void updateOrder(List<CategoryModifyRequestDto> requestList);
}
