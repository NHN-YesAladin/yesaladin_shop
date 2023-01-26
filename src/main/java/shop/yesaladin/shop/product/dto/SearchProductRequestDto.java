package shop.yesaladin.shop.product.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class SearchProductRequestDto {
    @NotBlank
    @NotNull
    String query;
    @Min(value = 1)
    int offset;
    @Min(value = 1)
    @Max(value = 20)
    int size;
}
