package shop.yesaladin.shop.point.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
}
