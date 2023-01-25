package shop.yesaladin.shop.product.domain.model;

import lombok.*;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.product.exception.AlreadyDeletedProductException;
import shop.yesaladin.shop.product.persistence.converter.ProductSavingMethodCodeConverter;
import shop.yesaladin.shop.product.persistence.converter.ProductTypeCodeConverter;

import javax.persistence.*;

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
    private String ISBN;

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
     * 상품 soft delete를 위해 isDeleted를 true로 바꿉니다.
     *
     * @author 이수정
     * @since 1.0
     */
    public void deleteProduct() {
        if (this.isDeleted == true) {
            throw new AlreadyDeletedProductException(id);
        }
        this.isDeleted = true;
    }

    /**
     * 상품의 재고수량을 바꿉니다.
     *
     * @param quantity 바꿀 재고수량
     * @author 이수정
     * @since 1.0
     */
    public void changeQuantity(long quantity) {
        this.quantity = quantity;
    }
}
