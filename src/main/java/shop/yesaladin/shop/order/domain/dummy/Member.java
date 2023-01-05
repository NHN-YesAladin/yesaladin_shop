package shop.yesaladin.shop.order.domain.dummy;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 연관관계 매핑을 위한 dummy Member 엔티티입니다.
 *
 * @author 최예린
 */

@Entity
public class Member {
    @Id
    private Long id;
}
