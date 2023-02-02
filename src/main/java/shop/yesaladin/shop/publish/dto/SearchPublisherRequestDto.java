package shop.yesaladin.shop.publish.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchPublisherRequestDto {
    String name;

    @Min(value = 0)
    int offset;
    @Min(value = 1)
    @Max(value = 20)
    int size;
}
