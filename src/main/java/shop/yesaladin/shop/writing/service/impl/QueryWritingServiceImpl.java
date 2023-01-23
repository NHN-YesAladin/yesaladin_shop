package shop.yesaladin.shop.writing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.writing.domain.repository.QueryWritingRepository;
import shop.yesaladin.shop.writing.dto.WritingResponseDto;
import shop.yesaladin.shop.writing.service.inter.QueryWritingService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 집필 조회를 위한 Service 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class QueryWritingServiceImpl implements QueryWritingService {

    private final QueryWritingRepository queryWritingRepository;

    /**
     * 해당 상품의 집필 관계를 조회하여 Dto List로 반환합니다.
     *
     * @param product 관계를 조회할 상품
     * @return 조회된 집필 관계 dto List
     * @author 이수정
     * @since 1.0
     */
    @Transactional(readOnly = true)
    @Override
    public List<WritingResponseDto> findByProduct(Product product) {
        return queryWritingRepository.findByProduct(product).stream()
                .map(writing -> new WritingResponseDto(writing.getProduct(), writing.getAuthor()))
                .collect(Collectors.toList());
    }
}
