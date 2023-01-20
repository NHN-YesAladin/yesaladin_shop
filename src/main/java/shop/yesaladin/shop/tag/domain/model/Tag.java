package shop.yesaladin.shop.tag.domain.model;

import lombok.*;

import javax.persistence.*;

/**
 * 상품 태그의 엔터티 클래스입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "tags")
@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 15)
    private String name;
}
