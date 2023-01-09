package shop.yesaladin.shop.category.domain.repository;

import shop.yesaladin.shop.category.domain.model.Category;

/**
 * 카테고리 CUD용 레포지토리 인터페이스
 *
 * @author 배수한
 * @since 1.0
 */

public interface CommandCategoryRepository {


    /**
     * 카테고리를 생성 및 수정
     *
     * @param category
     * @return 등록된 카테고리
     */
    Category save(Category category);

    /**
     * id를 통해 조회하여 카테고리를 삭제
     *
     * @param id 카테고리 id
     */
    void deleteById(Long id);
}
