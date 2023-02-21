package shop.yesaladin.shop.writing.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.shop.member.domain.model.Member;

/**
 * 저자의 엔터티 클래스입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "authors")
@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * 저자의 멤버 엔터티를 변경합니다.
     *
     * @param member 변경할 멤버 엔터티
     * @author 이수정
     * @since 1.0
     */
    public void changeMember(Member member) {
        this.member = member;
    }

    /**
     * 저자의 이름을 변경합니다.
     *
     * @param name 변경할 저자의 이름
     * @author 이수정
     * @since 1.0
     */
    public void changeName(String name) {
        this.name = name;
    }
}
