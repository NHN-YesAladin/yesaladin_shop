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
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 쿠폰 코드 리스트와 성공 여부를 가지는 메시지 클래스입니다. 쿠폰 사용 완료 혹은 쿠폰 지급 완료 시 사용합니다.
 *
 * @author 김홍대
 * @since 1.0
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Document("CouponGivenResult")
public class CouponGivenResultMessage {

    @Id
    private ObjectId id;
    private List<String> couponCodes;
    private boolean success;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;
    @Default
    private ProcessingStatus processed = ProcessingStatus.NOT_PROCESSED;

}
