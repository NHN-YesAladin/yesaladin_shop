package shop.yesaladin.shop.category.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import shop.yesaladin.shop.category.exception.AlreadyDeletedCategoryException;

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

    public static final int DEPTH_PARENT = 0;
    public static final int DEPTH_CHILD = 1;
    public static final int DEPTH_DISABLE = -1;
    public static final long TERM_OF_PARENT_ID = 10000L;
    public static final long TERM_OF_CHILD_ID = 100L;

    @Id
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

    @Builder.Default
    @Column(name = "depth", nullable = false)
    private int depth = 0;

    @Builder.Default
    @Column(name = "disable", nullable = false)
    private boolean isDisable = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @ToString.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Category> children = new ArrayList<>();

    /**
     * entity의 변경감지를 위해 사용
     *
     * @param name
     * @param isShown
     * @param order
     */
    public void verifyChange(
            String name,
            Boolean isShown,
            Integer order
    ) {
        if (Objects.nonNull(name)) {
            this.name = name;
        }
        if (Objects.nonNull(isShown)) {
            this.isShown = isShown;
        }
        if (Objects.nonNull(order)) {
            this.order = order;
        }
    }

    /**
     * FK 제약조건으로 인해 삭제가 불가한 카테고리에 disable 이라는 기능을 추가 disable = true
     *
     * @param nameBeforeChanging 변경감지로 인해 이름이 변경 되는 경우, 기존 엔티티의 이름을 저장하기 위하여 사용
     */
    public void disableCategory(String nameBeforeChanging) {
        if (this.isDisable) {
            throw new AlreadyDeletedCategoryException(this.id);
        }
        this.isDisable = true;
        this.name = nameBeforeChanging;
    }


}
