package shop.yesaladin.shop.category.dummy;


import shop.yesaladin.shop.category.domain.model.Category;

public class CategoryDummy {

    public static Category dummyParent() {
        return Category.builder()
                .id(10000L)
                .name("국내도서")
                .isShown(true)
                .order(1)
                .parent(null)
                .build();
    }

    public static Category dummyParent2() {
        return Category.builder()
                .id(20000L)
                .name("잡지")
                .isShown(true)
                .order(1)
                .parent(null)
                .build();
    }

    public static Category dummyParent3() {
        return Category.builder()
                .id(30000L)
                .name("국외도서")
                .isShown(true)
                .order(1)
                .parent(null)
                .build();
    }

    public static Category dummyParent(Long id) {
        return Category.builder()
                .id(id)
                .name("국내도서")
                .isShown(true)
                .order(1)
                .parent(null)
                .build();
    }

    public static Category dummyChild(Category parent) {
        return Category.builder()
                .id(10100L)
                .name("소설")
                .isShown(true)
                .order(1)
                .depth(1)
                .parent(parent)
                .build();
    }

    public static Category dummyChild(Long id, Category parent) {
        return Category.builder()
                .id(id)
                .name("소설")
                .isShown(true)
                .order(1)
                .depth(1)
                .parent(parent)
                .build();
    }
}
