package shop.yesaladin.shop.point.persistence.converter;

import java.util.Arrays;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import shop.yesaladin.shop.point.domain.model.PointReasonCode;

/**
 * 포인트 사유 코드를 변환하는 컨버터입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Converter(autoApply = true)
public class PointReasonCodeConverter implements AttributeConverter<PointReasonCode, Integer> {

    /**
     * 포인트사유코드 엔티티를 Integer 타입으로 변환합니다.
     *
     * @param pointCode 포인트 사유 코드
     * @return Integer
     * @author 최예린
     * @since 1.0
     */
    @Override
    public Integer convertToDatabaseColumn(PointReasonCode pointCode) {
        return pointCode.getId();
    }

    /**
     * Integer를 포인트사유코드 엔티티로 변환합니다.
     *
     * @param integer 포인트 사유 코드 아이디
     * @return 포인트 사유 코드
     * @author 최예린
     * @since 1.0
     */
    @Override
    public PointReasonCode convertToEntityAttribute(Integer integer) {
        return Arrays.stream(PointReasonCode.values())
                .filter(id -> integer.equals(id.getId()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
