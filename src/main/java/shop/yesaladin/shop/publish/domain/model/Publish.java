package shop.yesaladin.shop.publish.domain.model;

import lombok.*;
import shop.yesaladin.shop.product.domain.model.Product;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    /**
     * 출판 엔터티를 생성해 반환합니다.
     *
     * @param product       출판된 상품 엔터티
     * @param publisher     출판한 출판사 엔터티
     * @param publishedDate 출판일
     * @return 출판 엔터티
     * @author 이수정
     * @since 1.0
     */
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
