package shop.yesaladin.shop.product.domain.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 연관관계의 엔터티 클래스입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "relations")
@Entity
public class Relation {

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

    /**
     * 상품 연관관계 엔터티를 생성하여 반환합니다.
     *
     * @param productMain 연관관계를 생성할 메인 상품
     * @param productSub  연관관계를 생성할 서브 상품
     * @return 생성된 연관관계 엔터티
     * @author 이수정
     * @since 1.0
     */
    public static Relation create(Product productMain, Product productSub) {
        return Relation.builder()
                .pk(new Pk(productMain.getId(), productSub.getId()))
                .productMain(productMain)
                .productSub(productSub)
                .build();
    }

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
