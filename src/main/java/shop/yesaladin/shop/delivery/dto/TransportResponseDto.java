package shop.yesaladin.shop.delivery.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 배송 정보를 담은 DTO 클래스 입니다.
 *
 * @author 배수한
 * @since 1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransportResponseDto {

    private Long id;
    private LocalDate receptionDatetime;
    private LocalDate completionDatetime;
    private Long orderId;
    private String trackingNo;
    private String transportStatus;
}
