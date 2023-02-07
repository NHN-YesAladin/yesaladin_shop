package shop.yesaladin.shop.product.persistence;

import static com.querydsl.jpa.JPAExpressions.select;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
import shop.yesaladin.shop.product.dto.ProductOnlyTitleDto;
import shop.yesaladin.shop.product.dto.ProductOrderSheetResponseDto;
import shop.yesaladin.shop.product.dto.ProductRelationResponseDto;
import shop.yesaladin.shop.product.exception.ProductTypeCodeNotFoundException;
import shop.yesaladin.shop.publish.domain.model.querydsl.QPublish;
import shop.yesaladin.shop.publish.domain.model.querydsl.QPublisher;
import shop.yesaladin.shop.writing.domain.model.querydsl.QAuthor;
import shop.yesaladin.shop.writing.domain.model.querydsl.QWriting;


/**
 * 상품 조회를 위한 Repository QueryDsl 구현체 입니다.
 *
 * @author 이수정
 * @author 최예린
 * @since 1.0
 */
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

    // TODO: ResponseDto 수정

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
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
                .where(product.productTypeCode.eq(productTypeCode.get()))
                .fetchFirst();

        return new PageImpl<>(products, pageable, totalCount);
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

        Long totalCount = queryFactory.select(product.count())
                .from(product)
                .where(product.isDeleted.isFalse().and(product.isSale.isTrue()))
                .fetchFirst();

        return new PageImpl<>(products, pageable, totalCount);
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
                .where(product.productTypeCode.eq(productTypeCode.get())
                        .and(product.isDeleted.isFalse().and(product.isSale.isTrue())))
                .fetchFirst();

        return new PageImpl<>(products, pageable, totalCount);
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

        NumberExpression<Long> expectedEarnedPoint = product.actualPrice.multiply(product.isGivenPoint.when(
                        true)
                .then(product.givenPointRate.divide(100))
                .otherwise(product.totalDiscountRate.discountRate.divide(100)));

        return queryFactory.select(Projections.constructor(
                        ProductOrderSheetResponseDto.class,
                        product.id,
                        product.isbn,
                        product.title,
                        product.actualPrice,
                        product.discountRate,
                        expectedEarnedPoint
                ))
                .from(product)
                .where(product.isbn.in(isbnList))
                .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Product> findByIsbnList(List<String> isbnList, Map<String, Integer> quantities) {
        QProduct product = QProduct.product;

        return queryFactory.select(product)
                .from(product)
                .where(product.isbn.in(isbnList)
                        .and(product.isDeleted.isFalse())
                        .and(product.isForcedOutOfStock.isFalse())
                        .and(product.isSale.isTrue())
                        .and(product.quantity.goe(quantities.get(product.isbn.toString()))))
                .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProductRelationResponseDto> findProductRelationByTitle(
            String title,
            Pageable pageable
    ) {
        QProduct product = QProduct.product;
        QWriting writing = QWriting.writing;
        QAuthor author = QAuthor.author;
        QPublish publish = QPublish.publish;
        QPublisher publisher = QPublisher.publisher;

        return queryFactory.select(
                        Projections.bean(
                                ProductRelationResponseDto.class,
                                product.id,
                                product.isbn,
                                product.title,
                                product.actualPrice,
                                product.discountRate,
                                product.isForcedOutOfStock,
                                product.quantity,
                                product.preferentialShowRanking,
                                ExpressionUtils.as(select(author.name).from(author), "authors")
//                                publisher.name.as("publisher"),
//                                publish.publishedDate
                        )
                )
                .from(product, writing)
                .where(product.title.contains(title).and(product.isDeleted.isFalse()))
                .leftJoin(writing).on(product.eq(writing.product)).fetchJoin()
                .leftJoin(author).on(writing.author.eq(author)).fetchJoin()
//                .groupBy(author.name)
//                .leftJoin(publish).on(product.id.eq(publish.product.id))
//                .leftJoin(publish).on(publisher.id.eq(publish.publisher.id))
                .fetch();
    }
}

