package shop.yesaladin.shop.product.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRelationResponseDto {

    Long id;
    private String isbn;
    private String title;
    private long actualPrice;
    private int discountRate;
    private boolean isForcedOutOfStock;
    private long quantity;
    private int preferentialShowRanking;
    private List<String> authors;
    private String publisher;
    private LocalDate publishedDate;
}
