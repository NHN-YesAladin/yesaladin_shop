package shop.yesaladin.shop.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.category.service.inter.QueryProductCategoryService;
import shop.yesaladin.shop.product.domain.model.*;
import shop.yesaladin.shop.product.domain.repository.QueryProductRepository;
import shop.yesaladin.shop.product.persistence.ElasticCommandProductRepository;
import shop.yesaladin.shop.product.service.inter.ElasticCommandProductService;
import shop.yesaladin.shop.publish.dto.PublishResponseDto;
import shop.yesaladin.shop.publish.service.inter.QueryPublishService;
import shop.yesaladin.shop.tag.dto.ProductTagResponseDto;
import shop.yesaladin.shop.tag.service.inter.QueryProductTagService;
import shop.yesaladin.shop.writing.dto.WritingResponseDto;
import shop.yesaladin.shop.writing.service.inter.QueryWritingService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 엘라스틱서치에 상품을 수정, 삭제하는 서비스 구현체
 *
 * @author 김선홍
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class ElasticCommandProductServiceImpl implements ElasticCommandProductService {

    private final ElasticCommandProductRepository elasticCommandProductRepository;
    private final QueryProductRepository queryProductRepository;
    private final QueryWritingService queryWritingService;
    private final QueryPublishService queryPublishService;
    private final QueryProductTagService queryProductTagService;
    private final QueryProductCategoryService queryProductCategoryService;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Long update(Long id) {
        Product product = queryProductRepository.findProductById(id)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.PRODUCT_NOT_FOUND,
                        "Product not found with id : " + id
                ));
        elasticCommandProductRepository.save(create(product));
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Long changeIsSale(Long id) {
        SearchedProduct searchedProduct = elasticCommandProductRepository.findById(id).orElseThrow(() -> new ClientException(
                ErrorCode.PRODUCT_NOT_FOUND,
                "Product not found with id : " + id
        ));
        searchedProduct.changeIsSale();
        elasticCommandProductRepository.save(searchedProduct);
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Long changeIsForcedOutOfStock(Long id) {

        SearchedProduct searchedProduct = elasticCommandProductRepository.findById(id).orElseThrow(() -> new ClientException(
                ErrorCode.PRODUCT_NOT_FOUND,
                "Product not found with id : " + id
        ));
        searchedProduct.changeIsForcedOutOfStock();
        elasticCommandProductRepository.save(searchedProduct);
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public void delete(Long id) {
        elasticCommandProductRepository.deleteByIdEquals(id);
    }

    private SearchedProduct create(Product product) {

        return SearchedProduct.builder()
                .id(product.getId())
                .title(product.getTitle())
                .contents(product.getContents())
                .description(product.getDescription())
                .actualPrice(product.getActualPrice())
                .discountRate(product.getDiscountRate())
                .isSeparatelyDiscount(product.isSeparatelyDiscount())
                .isSale(product.isSale())
                .quantity(product.getQuantity())
                .isForcedOutOfStock(product.isForcedOutOfStock())
                .searchedTotalDiscountRate(new SearchedProductTotalDiscountRate(product.getTotalDiscountRate()
                        .getId(), product.getTotalDiscountRate().getDiscountRate()))
                .preferentialShowRanking(product.getPreferentialShowRanking())
                .isSubscriptionAvailable(product.isSubscriptionAvailable())
                .thumbnailFile(product.getThumbnailFile().getUrl())
                .isDeleted(product.isDeleted())
                .publishedDate(findPublishDate(product))
                .categories(findCategories(product))
                .tags(findTags(product))
                .publisher(findPublisher(product))
                .authors(findAuthors(product))
                .build();
    }

    private List<SearchedProductCategory> findCategories(Product product) {
        List<SearchedProductCategory> categories = new ArrayList<>();
        List<CategoryResponseDto> dtos = queryProductCategoryService.findCategoriesByProduct(product);
        dtos.forEach(dto -> categories.add(new SearchedProductCategory(
                dto.getId(),
                dto.getParentId(),
                dto.getName(),
                dto.getIsShown(),
                true
        )));
        return categories;
    }

    private List<SearchedProductTag> findTags(Product product) {
        List<SearchedProductTag> tags = new ArrayList<>();
        List<ProductTagResponseDto> dtos = queryProductTagService.findByProduct(product);
        dtos.forEach(dto -> tags.add(new SearchedProductTag(
                dto.getTag().getId(),
                dto.getTag().getName()
        )));
        return tags;
    }

    private SearchedProductPublisher findPublisher(Product product) {
        PublishResponseDto publishResponseDto = queryPublishService.findByProduct(product);
        return new SearchedProductPublisher(
                publishResponseDto.getPublisher().getId(),
                publishResponseDto.getPublisher().getName()
        );
    }

    private List<SearchedProductAuthor> findAuthors(Product product) {
        List<SearchedProductAuthor> authors = new ArrayList<>();
        List<WritingResponseDto> dtos = queryWritingService.findByProduct(product);
        dtos.forEach(dto -> authors.add(new SearchedProductAuthor(
                dto.getAuthor().getId(),
                dto.getAuthor().getName()
        )));
        return authors;
    }

    private LocalDate findPublishDate(Product product) {
        return queryPublishService.findByProduct(product).getPublishedDate();
    }
}
