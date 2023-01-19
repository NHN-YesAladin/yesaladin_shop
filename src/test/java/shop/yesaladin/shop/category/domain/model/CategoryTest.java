package shop.yesaladin.shop.category.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shop.yesaladin.shop.category.dummy.CategoryDummy;


class CategoryTest {

    Category category;

    @BeforeEach
    void setUp() {
        category = CategoryDummy.dummyParent();
    }

    @Test
    void verifyChange() {
        // given
        String name = null;
        boolean isShown = false;
        Integer order = 1;

        // when
        category.verifyChange(name, isShown, order);

        // then
        assertThat(category.getName()).isEqualTo(category.getName());
        assertThat(category.isShown()).isEqualTo(isShown);
        assertThat(category.getOrder()).isEqualTo(order);

    }

    @Test
    void disableCategory() {
        // given
        String nameBeforeChanging = category.getName();
        String name = "바뀔 이름";

        // when
        category.verifyChange(name, null, null);
        category.disableCategory(nameBeforeChanging);

        // then
        assertThat(category.isDisable()).isEqualTo(true);
        assertThat(category.getName()).isEqualTo(nameBeforeChanging);
        assertThat(category.getName()).isNotEqualTo(name);
    }
}
