package shop.yesaladin.shop.product.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.product.domain.model.Product;

import java.util.Optional;

/**
 * 상품 조회 관련 Repository Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QueryProductRepository {

    Optional<Product> findById(Long id);

    Optional<Product> findByISBN(String ISBN);

    Page<Product> findAllForManager(Pageable pageable);

    Page<Product> findAllByTypeIdForManager(Pageable pageable, Integer typeId);

    Page<Product> findAll(Pageable pageable);

    Page<Product> findAllByTypeId(Pageable pageable, Integer typeId);
}
