package shop.yesaladin.shop.product.domain.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.dto.ProductOrderResponseDto;

/**
 * 상품 조회 관련 Repository Interface 입니다.
 *
 * @author 이수정
 * @author 최예린
 * @since 1.0
 */
public interface QueryProductRepository {

    Optional<Product> findById(Long id);

    Optional<Product> findByIsbn(String isbn);

    Page<Product> findAllForManager(Pageable pageable);

    Page<Product> findAllByTypeIdForManager(Pageable pageable, Integer typeId);

    Page<Product> findAll(Pageable pageable);

    Page<Product> findAllByTypeId(Pageable pageable, Integer typeId);

    /**
     * 주문 상품의 isbn을 통해 주문의 상품 상세정보를 반환합니다.
     *
     * @param isbnList 주문 상품의 Isbn 목록
     * @return 주문 상품의 상세 정보
     * @author 최예린
     * @since 1.0
     */
    List<ProductOrderResponseDto> getByIsbnList(List<String> isbnList);

    /**
     * 주문 상품의 isbn을 통해 주문 상품의 엔티티를 반환합니다.
     *
     * @param isbnList 주문 상품의 isbn 목록
     * @return 주문 상품의 엔티티 목록
     * @author 최예린
     * @since 1.0
     */
    List<Product> findByIsbnList(List<String> isbnList);
}
