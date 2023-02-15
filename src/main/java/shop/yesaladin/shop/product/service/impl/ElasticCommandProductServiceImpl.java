package shop.yesaladin.shop.product.service.impl;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.category.service.inter.QueryCategoryService;
import shop.yesaladin.shop.product.domain.model.SearchedProduct;
import shop.yesaladin.shop.product.domain.model.SearchedProductAuthor;
import shop.yesaladin.shop.product.domain.model.SearchedProductCategory;
import shop.yesaladin.shop.product.domain.model.SearchedProductPublisher;
import shop.yesaladin.shop.product.domain.model.SearchedProductTag;
import shop.yesaladin.shop.product.domain.model.SearchedProductTotalDiscountRate;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.domain.repository.QueryTotalDiscountRateRepository;
import shop.yesaladin.shop.product.dto.SearchedProductUpdateDto;
import shop.yesaladin.shop.product.persistence.ElasticCommandProductRepository;
import shop.yesaladin.shop.product.service.inter.ElasticCommandProductService;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.publish.service.inter.QueryPublisherService;
import shop.yesaladin.shop.tag.dto.TagResponseDto;
import shop.yesaladin.shop.tag.service.inter.QueryTagService;
import shop.yesaladin.shop.writing.dto.AuthorResponseDto;
import shop.yesaladin.shop.writing.service.inter.QueryAuthorService;

/**
 * 엘라스틱서치에 상품을 수정, 삭제하는 서비스 구현체
 *
 * @author 김선홍
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class ElasticCommandProductServiceImpl implements ElasticCommandProductService {

    private static final int TOTAL_DISCOUNT_RATE_DEFAULT_ID = 1;
    private final ElasticCommandProductRepository elasticCommandProductRepository;
    private final QueryTotalDiscountRateRepository queryTotalDiscountRateRepository;
    private final QueryAuthorService queryAuthorService;
    private final QueryPublisherService queryPublisherService;
    private final QueryTagService queryTagService;
    private final QueryCategoryService queryCategoryService;

    /**
     * {@inheritDoc}
     */
    @Override
    public SearchedProduct update(Long id, SearchedProductUpdateDto updateDto) {
        if (!elasticCommandProductRepository.existsById(id)) {
            throw new ClientException(
                    ErrorCode.PRODUCT_NOT_FOUND,
                    "Original product not found with id : " + id
            );
        }
        return elasticCommandProductRepository.save(SearchedProductUpdateDto.toIndex(
                updateDto,
                id,
                findTotalDiscountRate(),
                findTags(updateDto.getTags()),
                findCategries(updateDto.getCategories()),
                findAuthors(updateDto.getAuthors()),
                findPublisher(updateDto.getPublisherId())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) {
        elasticCommandProductRepository.deleteByIdEquals(id);
    }

    private List<SearchedProductTag> findTags(List<Long> ids) {
        List<SearchedProductTag> tags = new ArrayList<>();
        for (Long id : ids) {
            TagResponseDto tag = queryTagService.findById(id);
            tags.add(new SearchedProductTag(tag.getId(), tag.getName()));
        }
        return tags;
    }

    private List<SearchedProductAuthor> findAuthors(List<Long> ids) {
        List<SearchedProductAuthor> authors = new ArrayList<>();
        for (Long id : ids) {
            AuthorResponseDto author = queryAuthorService.findById(id);
            authors.add(new SearchedProductAuthor(author.getId(), author.getName()));
        }
        return authors;
    }

    private List<SearchedProductCategory> findCategries(List<Long> ids) {
        List<SearchedProductCategory> categories = new ArrayList<>();
        for (Long id : ids) {
            CategoryResponseDto category = queryCategoryService.findCategoryById(id);
            categories.add(new SearchedProductCategory(category.getId(), category.getParentId(),
                    category.getName(), category.getIsShown(), false
            ));
        }
        return categories;
    }

    private SearchedProductPublisher findPublisher(Long id) {
        PublisherResponseDto publisher = queryPublisherService.findById(id);
        return new SearchedProductPublisher(publisher.getId(), publisher.getName());
    }

    private SearchedProductTotalDiscountRate findTotalDiscountRate() {
        TotalDiscountRate totalDiscountRate = queryTotalDiscountRateRepository.findById(
                        TOTAL_DISCOUNT_RATE_DEFAULT_ID)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.PRODUCT_TOTAL_DISCOUNT_RATE_NOT_EXIST,
                        "TotalDiscountRate not exists with id : " + TOTAL_DISCOUNT_RATE_DEFAULT_ID
                ));
        return new SearchedProductTotalDiscountRate(
                totalDiscountRate.getId(),
                totalDiscountRate.getDiscountRate()
        );
    }
}
