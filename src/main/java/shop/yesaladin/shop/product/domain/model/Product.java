package shop.yesaladin.shop.product.domain.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.product.persistence.converter.ProductSavingMethodCodeConverter;
import shop.yesaladin.shop.product.persistence.converter.ProductTypeCodeConverter;

/**
 * 상품의 엔터티 클래스입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "products")
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String isbn;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String contents;
    @Column(nullable = false)
    private String description;

    @Column(name = "actual_price", nullable = false)
    private long actualPrice;
    @Column(name = "discount_rate")
    private int discountRate;
    @Column(name = "is_separately_discount", nullable = false)
    private boolean isSeparatelyDiscount;
    @Column(name = "given_point_rate", nullable = false)
    private int givenPointRate;
    @Column(name = "is_given_point", nullable = false)
    private boolean isGivenPoint;

    @Column(name = "is_subscription_available", nullable = false)
    private boolean isSubscriptionAvailable;
    @Column(name = "is_sale", nullable = false)
    private boolean isSale;
    @Column(name = "is_forced_out_of_stock", nullable = false)
    private boolean isForcedOutOfStock;

    @Column(nullable = false)
    private long quantity;
    @Column(name = "preferential_show_ranking", nullable = false)
    private int preferentialShowRanking;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscribe_product_id")
    private SubscribeProduct subscribeProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thumbnail_file_id")
    private File thumbnailFile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ebook_file_id")
    private File ebookFile;

    @Column(name = "product_type_code_id")
    @Convert(converter = ProductTypeCodeConverter.class)
    private ProductTypeCode productTypeCode;

    @OneToOne
    @JoinColumn(name = "discount_rate_id")
    private TotalDiscountRate totalDiscountRate;

    @Column(name = "product_saving_method_code_id")
    @Convert(converter = ProductSavingMethodCodeConverter.class)
    private ProductSavingMethodCode productSavingMethodCode;

    /**
     * 상품 soft delete를 위해 isDeleted를 true로, isSale을 false로 바꿉니다.
     *
     * @author 이수정
     * @since 1.0
     */
    public void deleteProduct() {
        if (this.isDeleted) {
            throw new ClientException(
                    ErrorCode.PRODUCT_ALREADY_DELETED,
                    "Product already deleted with id = " + id
            );
        }
        this.isDeleted = true;
        this.isSale = false;
    }

    /**
     * 상품의 재고수량을 바꿉니다.
     *
     * @param quantity 바꿀 재고수량
     * @author 이수정
     * @since 1.0
     */
    public void changeQuantity(long quantity) {
        if (quantity < 0) {
            throw new ClientException(
                    ErrorCode.PRODUCT_NEGATIVE_OR_ZERO_QUANTITY,
                    "Entered a Negative or Zero quantity (Input value: " + quantity + ")"
            );
        }
        this.quantity = quantity;
    }

    /**
     * 상품의 판매여부를 바꿉니다.
     *
     * @author 이수정
     * @since 1.0
     */
    public void changeIsSale() {
        this.isSale = !this.isSale;
    }

    /**
     * 상품의 강제품절여부를 바꿉니다.
     *
     * @author 이수정
     * @since 1.0
     */
    public void changeIsForcedOutOfStock() {
        this.isForcedOutOfStock = !this.isForcedOutOfStock;
    }
}
