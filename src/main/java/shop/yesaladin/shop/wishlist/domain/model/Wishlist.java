package shop.yesaladin.shop.wishlist.domain.model;

import java.io.Serializable;
import java.time.LocalDateTime;
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
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.product.domain.model.Product;

/**
 * 위시리스트 도메인 엔티티
 *
 * @author 김선홍
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "wishlists")
@Entity
public class Wishlist {

    @EmbeddedId
    Pk pk;
    @Column(name = "registered_datetime")
    LocalDateTime registeredDateTime;

    @MapsId(value = "memberId")
    @ManyToOne
    @JoinColumn(name = "member_id")
    Member member;
    @MapsId(value = "productId")
    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @EqualsAndHashCode
    @Embeddable
    public static class Pk implements Serializable {
        @Column(name = "product_id")
        Long productId;
        @Column(name = "member_id")
        Long memberId;
    }
}
