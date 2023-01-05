package shop.yesaladin.shop.member.domain.model;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원과 권한의 관계 엔티티 클래스 입니다.
 *
 * @author : 송학현
 * @since : 1.0
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "member_roles")
@Entity
public class MemberRole {
    @EmbeddedId
    private Pk id;

    @MapsId("memberId")
    @ManyToOne
    @JoinColumn(name = "member_Id", nullable = false, unique = true)
    private Member member;

    @MapsId("authorityId")
    @ManyToOne
    @JoinColumn(name = "authority_Id", nullable = false, unique = true)
    private Role role;

    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @EqualsAndHashCode
    @Getter
    @Embeddable
    public static class Pk implements Serializable {
        private Long memberId;
        private Integer authorityId;
    }
}
