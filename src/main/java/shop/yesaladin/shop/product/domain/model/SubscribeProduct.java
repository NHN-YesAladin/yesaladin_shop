package shop.yesaladin.shop.product.domain.model;

import lombok.*;

import javax.persistence.*;

/**
 * 정기구독 상품의 엔터티 클래스입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "subscribe_products")
@Entity
public class SubscribeProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 9)
    private String ISSN;

}
