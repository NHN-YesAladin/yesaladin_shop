package shop.yesaladin.shop.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.Relation;
import shop.yesaladin.shop.product.domain.repository.QueryRelationRepository;
import shop.yesaladin.shop.product.dto.RelationsResponseDto;
import shop.yesaladin.shop.product.service.inter.QueryRelationService;
import shop.yesaladin.shop.publish.dto.PublishResponseDto;
import shop.yesaladin.shop.publish.service.inter.QueryPublishService;
import shop.yesaladin.shop.writing.service.inter.QueryWritingService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 상품 연관관계 조회를 위한 Service 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class QueryRelationServiceImpl implements QueryRelationService {

    private static final float PERCENT_DENOMINATOR_VALUE = 100;
    private static final long ROUND_OFF_VALUE = 10;

    private final QueryRelationRepository queryRelationRepository;
    private final QueryWritingService queryWritingService;
    private final QueryPublishService queryPublishService;

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public PaginatedResponseDto<RelationsResponseDto> findAllForManager(Long productId, Pageable pageable) {
        Page<Relation> page = queryRelationRepository.findAllForManager(productId, pageable);

        return getRelationsPaginatedResponses(page);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public PaginatedResponseDto<RelationsResponseDto> findAll(Long productId, Pageable pageable) {
        Page<Relation> page = queryRelationRepository.findAll(productId, pageable);

        return getRelationsPaginatedResponses(page);
    }

    /**
     * 페이징된 연관관계 Dto를 반환합니다.
     *
     * @param page 페이징 전체 조회된 객체
     * @return PaginatedResponseDto
     * @author 이수정
     * @since 1.0
     */
    private PaginatedResponseDto<RelationsResponseDto> getRelationsPaginatedResponses(Page<Relation> page) {
        List<RelationsResponseDto> relations = new ArrayList<>();
        for (Relation relation : page.getContent()) {
            Product product = relation.getProductSub();

            // 저자
            List<String> authors = findAuthorsByProduct(product);

            // 출판사
            PublishResponseDto publisher = queryPublishService.findByProduct(product);

            // 가격
            int rate = product.getTotalDiscountRate().getDiscountRate();
            if (product.isSeparatelyDiscount()) {
                rate = product.getDiscountRate();
            }
            long sellingPrice = calcSellingPrice(product, rate);

            relations.add(new RelationsResponseDto(
                    product.getId(),
                    product.getThumbnailFile().getUrl(),
                    product.getTitle(),
                    authors,
                    publisher.getPublisher().getName(),
                    publisher.getPublishedDate().toString(),
                    sellingPrice,
                    rate
            ));
        }

        return PaginatedResponseDto.<RelationsResponseDto>builder()
                .totalPage(page.getNumber())
                .currentPage(page.getNumber())
                .totalDataCount(page.getTotalElements())
                .dataList(relations)
                .build();
    }

    /**
     * 상품의 저자 조회을 요청하여 응답받고 응답받은 저자 Dto List에서 저자의 이름을 추출하여 반환합니다.
     *
     * @param product 저자를 조회할 상품
     * @return 상품과 관련된 저자 이름 List
     * @author 이수정
     * @since 1.0
     */
    private List<String> findAuthorsByProduct(Product product) {
        return queryWritingService.findByProduct(product).stream()
                .map(writing -> writing.getAuthor().getName())
                .collect(Collectors.toList());
    }

    /**
     * 상품의 정가, 할인율을 바탕으로 판매가를 계산해 반환합니다.
     *
     * @param product 정가를 계산할 상품
     * @param rate    상품의 할인율(전체 / 개별)
     * @return 계산된 상품의 판매가
     * @author 이수정
     * @since 1.0
     */
    private long calcSellingPrice(Product product, int rate) {
        return Math.round((product.getActualPrice()
                - product.getActualPrice() * rate / PERCENT_DENOMINATOR_VALUE) / ROUND_OFF_VALUE)
                * ROUND_OFF_VALUE;
    }
}
