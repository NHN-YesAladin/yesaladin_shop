package shop.yesaladin.shop.point.domain.model;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;

/**
 * 포인트 코드 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor
public enum PointCode {
    USE(1), SAVE(2), SUM(3);

    private final int code;

    /**
     * 코드로 포인트 코드를 찾아냅니다.
     *
     * @param code 코드
     * @return 포인트 코드
     * @author 최예린
     * @since 1.0
     */
    public static PointCode findByCode(String code) {
        return Arrays.stream(PointCode.values())
                .filter(x -> x.name().equals(code.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new ClientException(
                        ErrorCode.POINT_INVALID_PARAMETER,
                        "Invalid code type with : " + code
                ));
    }
}
