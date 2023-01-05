package shop.yesaladin.shop.review.domain.dummy;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 연관 관계 매핑을 위한 dummy File 엔터티 입니다.
 *
 * @author 서민지
 * @since 1.0
 */
@Entity
public class File {
    @Id
    private Long id;
}
