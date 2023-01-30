package shop.yesaladin.shop.tag.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.tag.domain.repository.QueryProductTagRepository;
import shop.yesaladin.shop.tag.dto.ProductTagResponseDto;
import shop.yesaladin.shop.tag.service.inter.QueryProductTagService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 태그 관련 조회를 위한 Service 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class QueryProductTagServiceImpl implements QueryProductTagService {

    private final QueryProductTagRepository queryProductTagRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<ProductTagResponseDto> findByProduct(Product product) {
        return queryProductTagRepository.findByProduct(product).stream()
                .map(tag -> new ProductTagResponseDto(tag.getPk(), tag.getProduct(), tag.getTag()))
                .collect(Collectors.toList());
    }
}
