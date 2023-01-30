package shop.yesaladin.shop.publish.domain.model;

import lombok.*;

import javax.persistence.*;

/**
 * 출판사의 엔터티 클래스입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "publishers")
@Entity
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    /**
     * 출판사의 이름을 변경합니다.
     *
     * @param name 변경할 출판사의 이름
     * @author 이수정
     * @since 1.0
     */
    public void changeName(String name) {
        this.name = name;
    }
}
