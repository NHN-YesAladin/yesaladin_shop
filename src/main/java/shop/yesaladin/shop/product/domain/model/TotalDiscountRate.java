package shop.yesaladin.shop.product.domain.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 전체 할인율의 엔터티 클래스입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "total_discount_rates")
@Entity
public class TotalDiscountRate {

    @Id
    private int id;

    @Column(name = "discount_rate", nullable = false)
    private int discountRate;

}
