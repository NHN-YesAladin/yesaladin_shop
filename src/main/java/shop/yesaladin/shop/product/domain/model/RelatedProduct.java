package shop.yesaladin.shop.product.domain.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 연관상품의 엔터티 클래스입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "related_products")
@Entity
public class RelatedProduct {

    @EmbeddedId
    private Pk pk;

    @MapsId(value = "productMainId")
    @ManyToOne
    @JoinColumn(name = "product_main_id")
    private Product productMain;

    @MapsId(value = "productSubId")
    @ManyToOne
    @JoinColumn(name = "product_sub_id")
    private Product productSub;


    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @EqualsAndHashCode
    @Embeddable
    public static class Pk implements Serializable {

        @Column(name = "product_main_id", nullable = false)
        private Long productMainId;

        @Column(name = "product_sub_id", nullable = false)
        private Long productSubId;
    }
}
