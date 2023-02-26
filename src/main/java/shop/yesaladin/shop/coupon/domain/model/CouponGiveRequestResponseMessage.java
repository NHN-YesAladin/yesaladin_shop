package shop.yesaladin.shop.coupon.domain.model;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import shop.yesaladin.coupon.dto.CouponGiveDto;

/**
 * 쿠폰 지급 요청에 응답 시 사용되는 메시지 클래스입니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Document("CouponGiveRequestResponse")
public class CouponGiveRequestResponseMessage {

    @Id
    private String requestId;
    private List<CouponGiveDto> coupons;
    private boolean success;
    private String errorMessage;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;
    @Default
    private ProcessingStatus processed = ProcessingStatus.NOT_PROCESSED;

}
