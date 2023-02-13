package shop.yesaladin.shop.product.persistence;


import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.ProductTypeCode;
import shop.yesaladin.shop.product.domain.model.querydsl.QProduct;
import shop.yesaladin.shop.product.domain.model.querydsl.QRelation;
import shop.yesaladin.shop.product.domain.repository.QueryProductRepository;
import shop.yesaladin.shop.product.dto.ProductOnlyTitleDto;
import shop.yesaladin.shop.product.dto.ProductOrderSheetResponseDto;
import shop.yesaladin.shop.publish.domain.model.querydsl.QPublish;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 상품 조회를 위한 Repository QueryDsl 구현체 입니다.
 *
 * @author 이수정
 * @author 최예린
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Repository
public class QueryDslProductRepository implements QueryProductRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductOnlyTitleDto findTitleByIsbn(String isbn) {
        QProduct product = QProduct.product;

        return queryFactory.select(Projections.constructor(
                        ProductOnlyTitleDto.class,
                        product.title
                ))
                .from(product)
                .where(product.isbn.eq(isbn))
                .fetchFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean existsByIsbn(String isbn) {
         QProduct product = QProduct.product;

         String foundIsbn = queryFactory.select(product.isbn)
                 .from(product)
                 .where(product.isbn.eq(isbn))
                 .fetchFirst();

         return foundIsbn != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long findQuantityById(Long id) {
        QProduct product = QProduct.product;

        return queryFactory.select(product.quantity)
                .from(product)
                .where(product.id.eq(id))
                .fetchFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Product> findProductById(long id) {
        QProduct product = QProduct.product;

        return Optional.ofNullable(queryFactory.select(product)
                .from(product)
                .where(product.id.eq(id))
                .fetchFirst());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Product> findByIsbn(String isbn) {
        QProduct product = QProduct.product;

        return Optional.ofNullable(
                queryFactory.select(product)
                        .from(product)
                        .where(product.isbn.eq(isbn))
                        .fetchFirst()
        );
    }

    /**
     * {@inheritDoc}
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

        JPAQuery<Long> countQuery = queryFactory.select(product.count())
                .from(product)
                .where(product.isDeleted.isFalse().and(product.isSale.isTrue()));

        return PageableExecutionUtils.getPage(products, pageable, countQuery::fetchFirst);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Product> findAllByTypeId(Pageable pageable, Integer typeId) {
        QProduct product = QProduct.product;

        Optional<ProductTypeCode> productTypeCode = Arrays.stream(ProductTypeCode.values())
                .filter(value -> typeId.equals(value.getId()))
                .findAny();

        if (productTypeCode.isEmpty()) {
            throw new ClientException(
                    ErrorCode.PRODUCT_TYPE_CODE_NOT_FOUND,
                    "ProductTypeCode is not found : " + typeId
            );
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

        JPAQuery<Long> countQuery = queryFactory.select(product.count())
                .from(product)
                .where(product.productTypeCode.eq(productTypeCode.get())
                        .and(product.isDeleted.isFalse().and(product.isSale.isTrue())));

        return PageableExecutionUtils.getPage(products, pageable, countQuery::fetchFirst);
    }

    /**
     * {@inheritDoc}
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

        JPAQuery<Long> countQuery = queryFactory.select(product.count()).from(product);

        return PageableExecutionUtils.getPage(products, pageable, countQuery::fetchFirst);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Product> findAllByTypeIdForManager(Pageable pageable, Integer typeId) {
        QProduct product = QProduct.product;

        Optional<ProductTypeCode> productTypeCode = Arrays.stream(ProductTypeCode.values())
                .filter(value -> typeId.equals(value.getId()))
                .findAny();

        if (productTypeCode.isEmpty()) {
            throw new ClientException(
                    ErrorCode.PRODUCT_TYPE_CODE_NOT_FOUND,
                    "ProductTypeCode is not found : " + typeId
            );
        }

        List<Product> products = queryFactory
                .select(product)
                .from(product)
                .where(product.productTypeCode.eq(productTypeCode.get()))
                .orderBy(product.preferentialShowRanking.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(product.count())
                .from(product)
                .where(product.productTypeCode.eq(productTypeCode.get()));

        return PageableExecutionUtils.getPage(products, pageable, countQuery::fetchFirst);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Product> findOrderProductByIsbn(String isbn, int quantity) {
        QProduct product = QProduct.product;

        return Optional.ofNullable(
                queryFactory.select(product)
                        .from(product)
                        .where(product.isbn.eq(isbn)
                                .and(product.isDeleted.isFalse())
                                .and(product.isForcedOutOfStock.isFalse())
                                .and(product.isSale.isTrue())
                                .and(product.quantity.goe(quantity)))
                        .fetchFirst()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProductOrderSheetResponseDto> getByIsbnList(List<String> isbnList) {
        QProduct product = QProduct.product;

        NumberExpression<Integer> discountRate = product.isSeparatelyDiscount.when(true)
                .then(product.discountRate)
                .otherwise(product.totalDiscountRate.discountRate);

        return queryFactory.select(Projections.constructor(
                        ProductOrderSheetResponseDto.class,
                        product.id,
                        product.isbn,
                        product.title,
                        product.actualPrice,
                        discountRate,
                        product.isGivenPoint,
                        product.givenPointRate,
                        product.quantity
                ))
                .from(product)
                .where(product.isbn.in(isbnList)
                        .and(product.isDeleted.isFalse())
                        .and(product.isForcedOutOfStock.isFalse())
                        .and(product.isSale.isTrue())
                )
                .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Product> findByIsbnList(List<String> isbnList) {
        QProduct product = QProduct.product;

        return queryFactory.select(product)
                .from(product)
                .where(product.isbn.in(isbnList)
                        .and(product.isDeleted.isFalse())
                        .and(product.isForcedOutOfStock.isFalse())
                        .and(product.isSale.isTrue()))
                .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Product> findProductRelationByTitle(Long id, String title, Pageable pageable) {
        QProduct product = QProduct.product;
        QRelation relation = QRelation.relation;

        List<Product> reId = queryFactory.select(relation.productSub)
                .from(relation)
                .where(relation.productMain.id.eq(id))
                .fetch();
        List<Product> products = queryFactory.selectFrom(product)
                .where(product.title.contains(title)
                        .and(product.isDeleted.isFalse())
                        .and(product.id.ne(id))
                        .and(product.notIn(reId)))
                .offset((long) pageable.getPageNumber() * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory.select(product.count())
                .from(product)
                .where(product.title.contains(title)
                        .and(product.isDeleted.isFalse())
                        .and(product.id.ne(id))
                        .and(product.notIn(reId)))
                .fetchFirst();

        return new PageImpl<>(products, pageable, count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Product> findRecentProductByPublishedDate(Pageable pageable) {
        QProduct product = QProduct.product;
        QPublish publish = QPublish.publish;

        List<Product> products = queryFactory.selectFrom(product)
                .leftJoin(publish).on(publish.product.eq(product))
                .where(product.isDeleted.isFalse())
                .offset((long) pageable.getPageNumber() * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .orderBy(publish.publishedDate.desc())
                .fetch();

        Long count = queryFactory.select(product.count())
                .where(product.isDeleted.isFalse())
                .from(product)
                .fetchFirst();

        return new PageImpl<>(products, pageable, count);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Product> findRecentViewProductById(List<Long> ids, Pageable pageable) {
        QProduct product = QProduct.product;

        List<Product> products = new ArrayList<>();
        for (Long id : ids) {
            products.add(queryFactory.selectFrom(product)
                    .where(product.id.eq(id).and(product.isDeleted.isFalse()))
                    .offset((long) pageable.getPageNumber() * pageable.getPageSize())
                    .limit(pageable.getPageSize())
                    .fetchFirst());
        }

        Long count = queryFactory.select(product.count())
                .where(product.id.in(ids).and(product.isDeleted.isFalse()))
                .from(product)
                .fetchFirst();

        return new PageImpl<>(products, pageable, count);
    }
}

