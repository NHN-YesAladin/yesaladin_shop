package shop.yesaladin.shop.common.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginatedResponseDto<T> {

    private long totalPage;
    private long currentPage;
    private long totalDataCount;
    private List<T> dataList;

}
