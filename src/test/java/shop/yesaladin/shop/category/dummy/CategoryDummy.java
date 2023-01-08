package shop.yesaladin.shop.category.dummy;


import shop.yesaladin.shop.category.domain.model.Category;

public class CategoryDummy {

    public static Category dummyParent() {
        //TODO Category autoIncrement off 이후 주석 삭제 예정
        return Category.builder()
//                .id(10000L)
                .name("국내도서")
                .isShown(true)
                .order(null)
                .parent(null)
                .build();
    }

    //TODO Category autoIncrement off 이후 삭제 예정
    public static Category dummyParent(Long id) {
        return Category.builder()
                .id(id)
                .name("국내도서")
                .isShown(true)
                .order(null)
                .parent(null)
                .build();
    }

    public static Category dummyChild() {
        //TODO Category autoIncrement off 이후 주석 삭제 예정
        return Category.builder()
//                .id(10100L)
                .name("소설")
                .isShown(true)
                .order(null)
                .parent(dummyParent())
                .build();
    }

    //TODO Category autoIncrement off 이후 삭제 예정
    public static Category dummyChild(Long id) {
        return Category.builder()
                .id(id)
                .name("소설")
                .isShown(true)
                .order(null)
                .parent(dummyParent())
                .build();
    }
}
