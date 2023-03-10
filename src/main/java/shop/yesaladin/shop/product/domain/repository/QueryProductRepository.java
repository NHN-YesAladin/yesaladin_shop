package shop.yesaladin.shop.product.domain.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.dto.ProductOnlyTitleDto;
import shop.yesaladin.shop.product.dto.ProductOrderSheetResponseDto;
import shop.yesaladin.shop.product.dto.ProductWithCategoryResponseDto;

/**
 * 상품 조회 관련 Repository Interface 입니다.
 *
 * @author 이수정
 * @author 최예린
 * @since 1.0
 */
public interface QueryProductRepository {

    /**
     * ISBN(Unique) 기준으로 상품의 제목을 조회합니다.
     *
     * @param isbn 상품의 isbn (Unique)
     * @return 조회된 상품의 제목을 담은 Dto
     * @author 이수정
     * @since 1.0
     */
    ProductOnlyTitleDto findTitleByIsbn(String isbn);

    /**
     * ISBN의 존재여부를 조회합니다.
     *
     * @param isbn 조회할 isbn (Unique)
     * @return 조회된 상품의 존재여부
     * @author 이수정
     * @since 1.0
     */
    Boolean existsByIsbn(String isbn);

    /**
     * id(PK)기준으로 상품의 수량을 조회합니다.
     *
     * @param id 상품의 id (PK)
     * @return 조회된 상품의 수량
     * @author 이수정
     * @since 1.0
     */
    Long findQuantityById(Long id);

    /**
     * Id를 기준으로 상품을 조회합니다.
     *
     * @author 이수정
     * @since 1.0
     */
    Optional<Product> findProductById(long id);

    /**
     * ISSN(Unique)기준으로 상품을 조회합니다.
     *
     * @param isbn 상품의 isbn (Unique)
     * @return 조회된 상품 엔터티
     * @author 이수정
     * @since 1.0
     */
    Optional<Product> findByIsbn(String isbn);

    /**
     * isbn으로 구매가능한 상품을 조회합니다.
     *
     * @param isbn 상품의 isbn
     * @return 상품의 정보
     * @author 최예린
     * @since 1.0
     */
    Optional<ProductWithCategoryResponseDto> getByIsbn(String isbn);

    /**
     * 상품을 Paging하여 전체 사용자용 전체 조회합니다.
     *
     * @param pageable page, size 정보를 담은 Pagination을 위한 객체
     * @return 조회된 상품 엔터티 Page
     * @author 이수정
     * @since 1.0
     */
    Page<Product> findAll(Pageable pageable);

    /**
     * 상품을 상품 유형별로 Paging하여 전체 사용자용 전체 조회합니다.
     *
     * @param pageable page, size 정보를 담은 Pagination을 위한 객체
     * @param typeId   조회할 상품 유형 Id
     * @return 조회된 상품 엔터티 Page
     * @author 이수정
     * @since 1.0
     */
    Page<Product> findAllByTypeId(Pageable pageable, Integer typeId);

    /**
     * 상품을 Paging하여 관리자용 전체 조회합니다.
     *
     * @param pageable page, size 정보를 담은 Pagination을 위한 객체
     * @return 조회된 상품 엔터티 Page
     * @author 이수정
     * @since 1.0
     */
    Page<Product> findAllForManager(Pageable pageable);

    /**
     * 상품을 상품 유형별로 Paging하여 관리자용 전체 조회합니다.
     *
     * @param pageable page, size 정보를 담은 Pagination을 위한 객체
     * @param typeId   조회할 상품 유형 Id
     * @return 조회된 상품 엔터티 Page
     * @author 이수정
     * @since 1.0
     */
    Page<Product> findAllByTypeIdForManager(Pageable pageable, Integer typeId);

    /**
     * 관리자용 상품 제목 검색 메서드
     *
     * @param title    검색할 제목
     * @param pageable 페이지 정보
     * @return 검색 결과
     * @author 김선홍
     * @since 1, 0
     */
    Page<Product> findByTitleForManager(String title, Pageable pageable);

    /**
     * 관리자용 상품 isbn 검색 메서드
     *
     * @param isbn     검색할 isbn
     * @param pageable 페이지 정보
     * @return 검색 결과
     * @author 김선홍
     * @since 1, 0
     */
    Page<Product> findByISBNForManager(String isbn, Pageable pageable);

    /**
     * 관리자용 상품 내용 검색 메서드
     *
     * @param content  검색할 제목
     * @param pageable 페이지 정보
     * @return 검색 결과
     * @author 김선홍
     * @since 1, 0
     */
    Page<Product> findByContentForManager(String content, Pageable pageable);

    /**
     * 관리자용 상품 출판사 검색 메서드
     *
     * @param publisher 검색할 제목
     * @param pageable  페이지 정보
     * @return 검색 결과
     * @author 김선홍
     * @since 1, 0
     */
    Page<Product> findByPublisherForManager(String publisher, Pageable pageable);

    /**
     * 관리자용 상품 저자 검색 메서드
     *
     * @param author   검색할 제목
     * @param pageable 페이지 정보
     * @return 검색 결과
     * @author 김선홍
     * @since 1, 0
     */
    Page<Product> findByAuthorForManager(String author, Pageable pageable);

    /**
     * 주문 상품의 isbn 과 수량으로 구매 가능한 상품을 조회 합니다.
     *
     * @param isbn     상품의 isbn
     * @param quantity 주문 수량
     * @return 주문 가능한 주문 상품
     * @author 최예린
     * @since 1.0
     */
    Optional<Product> findOrderProductByIsbn(String isbn, int quantity);

    /**
     * 주문 상품의 isbn을 통해 주문의 상품 상세정보를 반환합니다.
     *
     * @param isbnList 주문 상품의 Isbn 목록
     * @return 주문 상품의 상세 정보
     * @author 최예린
     * @since 1.0
     */
    List<ProductOrderSheetResponseDto> getByIsbnList(List<String> isbnList);

    /**
     * 주문 상품의 isbn을 통해 주문 상품의 엔티티를 반환합니다.
     *
     * @param isbnList 주문 상품의 isbn 목록
     * @return 주문 상품의 엔티티 목록
     * @author 최예린
     * @since 1.0
     */
    List<Product> findByIsbnList(List<String> isbnList);

    /**
     * 연관 상품 등록을 위한 상품 검색
     *
     * @param title    검색할 상품의 제목
     * @param pageable 페이징 정보
     * @return 조회된 상품의 정보
     * @author 김선홍
     * @since 1, 0
     */
    Page<Product> findProductRelationByTitle(Long id, String title, Pageable pageable);

    /**
     * 최신 상품 조회 메서드
     *
     * @param pageable 페이지 정보
     * @return 최신 상품 리스트
     * @author 김선홍
     * @since 1, 0
     */
    Page<Product> findRecentProductByPublishedDate(Pageable pageable);

    /**
     * 최근 본 상품 메소드
     *
     * @param totalIds 본 상품들의 id 리스트
     * @param pageIds  페이지에 보여줄 상품들
     * @param pageable 페이지 정보
     * @return 최근 본 상품 리스트
     * @author 김선홍
     * @since 1, 0
     */
    Page<Product> findRecentViewProductById(
            List<Long> totalIds,
            List<Long> pageIds,
            Pageable pageable
    );
}
