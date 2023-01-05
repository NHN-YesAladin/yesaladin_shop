package shop.yesaladin.shop.order.domain.dummy;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 연관관계 매핑을 위한 dummy Product 엔티티입니다.
 *
 * @author 서민지
 */
@Entity
public class Product {
    @Id
    private Long id;
}
