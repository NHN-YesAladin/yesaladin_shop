package shop.yesaladin.shop.publish.domain.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
 * 출판의 엔터티 클래스입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "publish")
@Entity
public class Publish {

    @EmbeddedId
    private Pk pk;

    @Column(name = "published_date", nullable = false)
    private LocalDate publishedDate;

    @MapsId(value = "productId")
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @MapsId(value = "publisherId")
    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    public static Publish create(Product product, Publisher publisher, String publishedDate) {
        Pk pk = new Pk(product.getId(), publisher.getId());
        return new Publish(
                pk,
                LocalDate.parse(publishedDate, DateTimeFormatter.ISO_DATE),
                product,
                publisher
        );
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

        @Column(name = "publisher_id", nullable = false)
        private Long publisherId;
    }
}
