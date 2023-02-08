package shop.yesaladin.shop.point.domain.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Convert;
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
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.point.persistence.converter.PointCodeConverter;
import shop.yesaladin.shop.point.persistence.converter.PointReasonCodeConverter;

/**
 * 포인트 내역 엔티티 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "point_histories")
public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private long amount;

    @Column(name = "created_datetime", nullable = false)
    private LocalDateTime createDateTime;

    @Column(name = "point_code_id", nullable = false)
    @Convert(converter = PointCodeConverter.class)
    private PointCode pointCode;

    @Column(name = "point_reason_code_id", nullable = false)
    @Convert(converter = PointReasonCodeConverter.class)
    private PointReasonCode pointReasonCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
}
