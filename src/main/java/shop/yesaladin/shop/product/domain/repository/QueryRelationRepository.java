package shop.yesaladin.shop.product.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.product.domain.model.Relation;
import shop.yesaladin.shop.product.domain.model.Relation.Pk;

/**
 * 상품 연관관계 조회 관련 Repository Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QueryRelationRepository {

    /**
     * 연관관계의 Pk로 존재여부를 확인합니다.
     *
     * @param pk 연관관계의 pk
     * @return 연관관계의 존재여부
     * @author 이수정
     * @since 1.0
     */
    boolean existsByPk(Pk pk);

    /**
     * 대상 상품을 기준으로 관리자용 상품의 연관관계를 페이징 조회합니다.
     *
     * @param productId 연관관계를 조회할 기준 상품
     * @param pageable  페이징 처리를 위한 객체
     * @return 페이징 조회된 연관관계
     * @author 이수정
     * @since 1.0
     */
    Page<Relation> findAllForManager(Long productId, Pageable pageable);

    /**
     * 대상 상품을 기준으로 일반 사용자용 상품의 연관관계를 페이징 조회합니다.
     *
     * @param productId 연관관계를 조회할 기준 상품
     * @param pageable  페이징 처리를 위한 객체
     * @return 페이징 조회된 연관관계
     * @author 이수정
     * @since 1.0
     */
    Page<Relation> findAll(Long productId, Pageable pageable);
}
