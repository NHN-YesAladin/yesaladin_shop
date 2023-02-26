package shop.yesaladin.shop.coupon.domain.model;

import java.time.LocalDateTime;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import shop.yesaladin.coupon.code.TriggerTypeCode;

/**
 * 쿠폰 지급 요청 시 사용되는 메시지 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Document("CouponGiveRequest")
public class CouponGiveRequestMessage {

    @Id
    private String requestId;
    private TriggerTypeCode triggerTypeCode;
    private Long couponId;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;
    @Default
    private ProcessingStatus processed = ProcessingStatus.NOT_PROCESSED;

}
