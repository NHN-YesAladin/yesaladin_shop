package shop.yesaladin.shop.product.persistence;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.ProductTypeCode;
import shop.yesaladin.shop.product.domain.model.querydsl.QProduct;
import shop.yesaladin.shop.product.domain.repository.QueryProductRepository;
import shop.yesaladin.shop.product.dto.ProductOrderResponseDto;
import shop.yesaladin.shop.product.exception.ProductTypeCodeNotFoundException;


/**
 * 상품 조회를 위한 Repository QueryDsl 구현체 입니다.
 *
 * @author 이수정
 * @author 최예
 * @since 1.0
 */
@RequiredArgsConstructor
@Repository
public class QueryDslProductRepository implements QueryProductRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * Id를 기준으로 상품을 조회합니다. 린
     *
     * @since 1.0
     */
    @Override
    public Optional<Product> findById(Long id) {
        QProduct product = QProduct.product;

        return Optional.ofNullable(
                queryFactory.select(product)
                        .from(product)
                        .where(product.id.eq(id))
                        .fetchFirst()
        );
    }

    /**
     * ISSN(Unique)기준으로 상품을 조회합니다.
     *
     * @param isbn 상품의 ISBN (Unique)
     * @return 조회된 상품 엔터티
     * @author 이수정
     * @since 1.0
     */
    @Override
    public Optional<Product> findByIsbn(String isbn) {
        QProduct product = QProduct.product;

        return Optional.ofNullable(
                queryFactory.select(product)
                        .from(product)
                        .where(product.ISBN.eq(isbn))
                        .fetchFirst()
        );
    }


    /**
     * 상품을 Paging하여 관리자용 전체 조회합니다.
     *
     * @param pageable page, size 정보를 담은 Pagination을 위한 객체
     * @return 조회된 상품 엔터티 Page
     * @author 이수정
     * @since 1.0
     */
    @Override
    public Page<Product> findAllForManager(Pageable pageable) {
        QProduct product = QProduct.product;

        List<Product> products = queryFactory
                .select(product)
                .from(product)
                .orderBy(product.preferentialShowRanking.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory.select(product.count())
                .from(product)
                .fetchFirst();

        return new PageImpl<>(products, pageable, totalCount);
    }

    /**
     * 상품을 상품 유형별로 Paging하여 관리자용 전체 조회합니다.
     *
     * @param pageable page, size 정보를 담은 Pagination을 위한 객체
     * @param typeId   조회할 상품 유형 Id
     * @return 조회된 상품 엔터티 Page
     * @author 이수정
     * @since 1.0
     */
    @Override
    public Page<Product> findAllByTypeIdForManager(Pageable pageable, Integer typeId) {
        QProduct product = QProduct.product;

        Optional<ProductTypeCode> productTypeCode = Arrays.stream(ProductTypeCode.values())
                .filter(value -> typeId.equals(value.getId()))
                .findAny();

        if (productTypeCode.isEmpty()) {
            throw new ProductTypeCodeNotFoundException(typeId);
        }

        List<Product> products = queryFactory
                .select(product)
                .from(product)
                .where(product.productTypeCode.eq(productTypeCode.get()))
                .orderBy(product.preferentialShowRanking.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory.select(product.count())
                .from(product)
                .fetchFirst();

        return new PageImpl<>(products, pageable, totalCount);
    }

    /**
     * 상품을 Paging하여 전체 사용자용 전체 조회합니다.
     *
     * @param pageable page, size 정보를 담은 Pagination을 위한 객체
     * @return 조회된 상품 엔터티 Page
     * @author 이수정
     * @since 1.0
     */
    @Override
    public Page<Product> findAll(Pageable pageable) {
        QProduct product = QProduct.product;

        List<Product> products = queryFactory
                .select(product)
                .from(product)
                .where(product.isDeleted.isFalse().and(product.isSale.isTrue()))
                .orderBy(product.preferentialShowRanking.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory.select(product.count())
                .from(product)
                .fetchFirst();

        return new PageImpl<>(products, pageable, totalCount);
    }

    /**
     * 상품을 상품 유형별로 Paging하여 전체 사용자용 전체 조회합니다.
     *
     * @param pageable page, size 정보를 담은 Pagination을 위한 객체
     * @param typeId   조회할 상품 유형 Id
     * @return 조회된 상품 엔터티 Page
     * @author 이수정
     * @since 1.0
     */
    @Override
    public Page<Product> findAllByTypeId(Pageable pageable, Integer typeId) {
        QProduct product = QProduct.product;

        Optional<ProductTypeCode> productTypeCode = Arrays.stream(ProductTypeCode.values())
                .filter(value -> typeId.equals(value.getId()))
                .findAny();

        if (productTypeCode.isEmpty()) {
            throw new ProductTypeCodeNotFoundException(typeId);
        }

        List<Product> products = queryFactory
                .select(product)
                .from(product)
                .where(product.productTypeCode.eq(productTypeCode.get())
                        .and(product.isDeleted.isFalse().and(product.isSale.isTrue())))
                .orderBy(product.preferentialShowRanking.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory.select(product.count())
                .from(product)
                .fetchFirst();

        return new PageImpl<>(products, pageable, totalCount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProductOrderResponseDto> getByIsbnList(List<String> isbnList) {
        QProduct product = QProduct.product;

        NumberExpression<Long> expectedEarnedPoint = product.actualPrice.multiply(product.isGivenPoint.when(
                        true)
                .then(product.givenPointRate.divide(100))
                .otherwise(product.totalDiscountRate.discountRate.divide(100)));

        return queryFactory.select(Projections.constructor(
                        ProductOrderResponseDto.class,
                        product.id,
                        product.ISBN,
                        product.title,
                        product.actualPrice,
                        product.discountRate,
                        expectedEarnedPoint
                ))
                .where(product.ISBN.in(isbnList))
                .fetch();
    }

    @Override
    public List<Product> findByIsbnList(List<String> isbnList) {
        QProduct product = QProduct.product;

        return queryFactory.select(product)
                .where(product.ISBN.in(isbnList))
                .fetch();
    }
}

