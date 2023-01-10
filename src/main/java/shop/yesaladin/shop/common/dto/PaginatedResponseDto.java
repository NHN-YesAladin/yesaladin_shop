package shop.yesaladin.shop.common.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginatedResponseDto<T> {

    private final long totalPage;
    private final long currentPage;
    private final long totalDataCount;
    private final List<T> dataList;

}
