package shop.yesaladin.shop.category.domain.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 카테고리 엔티티
 *
 * @author 배수한
 * @since 1.0
 */

@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "categories")
@Entity
public class Category {

    @Id
    //TODO Category autoIncrement off 이후 삭제 예정
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    private String name;

    // mysql : tinyint -> java : boolean 으로 변환 가능
    // 0 -> false / 1 -> true
    @Builder.Default
    @Column(name = "is_shown", nullable = false)
    private boolean isShown = true;

    @Column(name = "`order`")
    private Integer order;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @ToString.Exclude
    @OneToMany(mappedBy = "parent")
    private List<Category> children;

}
