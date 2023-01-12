package shop.yesaladin.shop.point.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PointCode {
    USE(1), SAVE(2);

    private final int code;
}
