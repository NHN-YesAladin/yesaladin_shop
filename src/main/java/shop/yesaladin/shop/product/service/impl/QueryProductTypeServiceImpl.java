package shop.yesaladin.shop.product.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.product.domain.model.ProductTypeCode;
import shop.yesaladin.shop.product.dto.ProductTypeResponseDto;
import shop.yesaladin.shop.product.service.inter.QueryProductTypeService;

/**
 * 상품 유형 조회를 위한 Service 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class QueryProductTypeServiceImpl implements QueryProductTypeService {

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<ProductTypeResponseDto> findAll() {
        return List.of(ProductTypeCode.values()).stream()
                .map(type -> new ProductTypeResponseDto(
                        type.getId(),
                        type.name(),
                        type.getKoName()
                ))
                .collect(Collectors.toList());
    }
}
