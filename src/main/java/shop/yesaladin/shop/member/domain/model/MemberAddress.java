package shop.yesaladin.shop.member.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;

/**
 * 회원 배송지 엔티티 클래스 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "member_addresses")
public class MemberAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String address;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    /**
     * 현재 배송지를 대표 배송지로 설정합니다.
     *
     * @author 최예린
     * @since 1.0
     */
    public void markAsDefault() {
        this.isDefault = true;
    }

    /**
     * 배송지를 삭제합니다.
     *
     * @author 최예린
     * @since 1.0
     */
    public void delete() {
        if (this.isDeleted) {
            throw new ClientException(
                    ErrorCode.ADDRESS_ALREADY_DELETED,
                    "MemberAddress is already deleted with id : " + this.id
            );
        }
        this.isDeleted = true;
    }
}
