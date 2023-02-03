package shop.yesaladin.shop.writing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@AllArgsConstructor
public class SearchAuthorRequestDto {
    String name;
    @Min(value = 0)
    int offset;
    @Min(value = 1)
    @Max(value = 20)
    int size;
}
