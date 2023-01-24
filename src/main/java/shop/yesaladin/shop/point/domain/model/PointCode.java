package shop.yesaladin.shop.point.domain.model;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import shop.yesaladin.shop.point.exception.InvalidCodeParameterException;

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

    public static PointCode findByCode(String code) {
        return Arrays.stream(PointCode.values())
                .filter(x -> x.name().equals(code.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new InvalidCodeParameterException(code));
    }
}
