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

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Document("CouponCodes")
public class CouponCodesMessage {

    @Id
    private ObjectId id;
    private List<String> couponCodes;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;
    @Default
    private ProcessingStatus processed = ProcessingStatus.NOT_PROCESSED;

}
