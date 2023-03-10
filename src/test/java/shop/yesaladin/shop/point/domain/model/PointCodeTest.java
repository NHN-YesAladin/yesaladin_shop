package shop.yesaladin.shop.point.domain.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import shop.yesaladin.common.exception.ClientException;

class PointCodeTest {

    @ParameterizedTest
    @ValueSource(strings = {"use", "Use", "USE"})
    void findByCode(String code) {
        PointCode pointCode = PointCode.findByCode(code);

        Assertions.assertThat(pointCode).isEqualTo(PointCode.USE);
    }

    @ParameterizedTest
    @ValueSource(strings = {"usse", "something", ""})
    void findByCode_fail_InvalidCodeParameter(String code) {
        Assertions.assertThatThrownBy(() -> PointCode.findByCode(code)).isInstanceOf(
                ClientException.class);
    }
}