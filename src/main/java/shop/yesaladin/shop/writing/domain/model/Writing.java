package shop.yesaladin.shop.writing.domain.model;

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
import shop.yesaladin.shop.product.domain.model.Product;

/**
 * 집필의 엔터티 클래스입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "writing")
@Entity
public class Writing {

    @EmbeddedId
    private Pk pk;

    @MapsId(value = "productId")
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @MapsId(value = "authorId")
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    /**
     * 집필 엔터티를 생성해 반환합니다.
     *
     * @param product 집필한 상품
     * @param author  집필한 저자
     * @return 생성한 집필 엔터티
     * @author 이수정
     * @since 1.0
     */
    public static Writing create(Product product, Author author) {
        return Writing.builder()
                .pk(new Pk(product.getId(), author.getId()))
                .product(product)
                .author(author)
                .build();
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

        @Column(name = "author_id", nullable = false)
        private Long authorId;

    }
}
