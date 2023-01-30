package shop.yesaladin.shop.tag.domain.model;

import lombok.*;
import shop.yesaladin.shop.product.domain.model.Product;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 상품 태그 관계의 엔터티 클래스입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "product_tags")
@Entity
public class ProductTag {

    @EmbeddedId
    private Pk pk;

    @MapsId(value = "productId")
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @MapsId(value = "tagId")
    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    /**
     * 상품 태그 관계 엔터티를 생성해 반환합니다.
     *
     * @param product 연관된 상품 엔터티
     * @param tag     연관된 태그 엔터티
     * @return 상품 태그 엔터티
     * @author 이수정
     * @since 1.0
     */
    public static ProductTag create(Product product, Tag tag) {
        Pk pk = new Pk(product.getId(), tag.getId());
        return new ProductTag(pk, product, tag);
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @EqualsAndHashCode
    @Embeddable
    public static class Pk implements Serializable {

        @Column(name = "product_id", nullable = false)
        private Long productId;

        @Column(name = "tag_id", nullable = false)
        private Long tagId;

    }
}
