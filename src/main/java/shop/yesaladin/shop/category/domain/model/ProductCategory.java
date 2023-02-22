package shop.yesaladin.shop.category.domain.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import shop.yesaladin.shop.product.domain.model.Product;

/**
 * 상품과 카테고리의 관계 엔티티 클래스
 *
 * @author 배수한
 * @since 1.0
 */

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "product_categories")
@Entity
public class ProductCategory {

    @ToString.Exclude
    @EmbeddedId
    private Pk pk;

    @MapsId("categoryId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @MapsId("productId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductCategory(Category category, Product product) {
        this.category = category;
        this.product = product;
        this.pk = new Pk(category.getId(), product.getId());

    }

    /**
     * 상품 카테고리를 편히 생성하기 위한 메서드
     *
     * @param product  카테고리와 연관 지을 상품
     * @param category 상품과 연관 지을 카테고리
     * @return 상품 카테고리 엔티티
     */
    public static ProductCategory create(Product product, Category category) {
        Pk pk = new Pk(category.getId(), product.getId());
        return new ProductCategory(pk, category, product);
    }

    /**
     * 상품 카테고리에 사용한 복합키
     *
     * @author : 배수한
     * @since : 1.0
     */
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @EqualsAndHashCode
    @Getter
    @Embeddable
    public static class Pk implements Serializable {

        @Column(name = "category_id", nullable = false)
        private Long categoryId;

        @Column(name = "product_id", nullable = false)
        private Long productId;
    }
}
