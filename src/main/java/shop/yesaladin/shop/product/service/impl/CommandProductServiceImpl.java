package shop.yesaladin.shop.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.file.dto.FileResponseDto;
import shop.yesaladin.shop.file.service.inter.CommandFileService;
import shop.yesaladin.shop.file.service.inter.QueryFileService;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.domain.repository.*;
import shop.yesaladin.shop.product.dto.ProductCreateDto;
import shop.yesaladin.shop.product.dto.ProductOnlyIdDto;
import shop.yesaladin.shop.product.exception.AlreadyProductExistsException;
import shop.yesaladin.shop.product.exception.TotalDiscountRateNotExistsException;
import shop.yesaladin.shop.product.service.inter.CommandProductService;
import shop.yesaladin.shop.publish.domain.model.Publish;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.publish.service.inter.CommandPublishService;
import shop.yesaladin.shop.publish.service.inter.QueryPublishService;
import shop.yesaladin.shop.publish.service.inter.QueryPublisherService;
import shop.yesaladin.shop.tag.domain.model.ProductTag;
import shop.yesaladin.shop.tag.domain.model.Tag;
import shop.yesaladin.shop.tag.service.inter.CommandProductTagService;
import shop.yesaladin.shop.tag.service.inter.QueryTagService;
import shop.yesaladin.shop.writing.domain.model.Author;
import shop.yesaladin.shop.writing.domain.model.Writing;
import shop.yesaladin.shop.writing.service.inter.CommandWritingService;
import shop.yesaladin.shop.writing.service.inter.QueryAuthorService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 상품 생성을 위한 Service 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class CommandProductServiceImpl implements CommandProductService {

    private final int TOTAL_DISCOUNT_RATE_DEFAULT_ID = 1;

    // Product
    private final CommandProductRepository commandProductRepository;
    private final QueryProductRepository queryProductRepository;

    // SubscribeProduct
    private final CommandSubscribeProductRepository commandSubscribeProductRepository;
    private final QuerySubscribeProductRepository querySubscribeProductRepository;

    // TotalDiscountRate
    private final QueryTotalDiscountRateRepository queryTotalDiscountRateRepository;

    // File
    private final CommandFileService commandFileService;
    private final QueryFileService queryFileService;

    // Writing
    private final CommandWritingService commandWritingService;
    private final QueryAuthorService queryAuthorService;

    // Publish
    private final QueryPublishService queryPublishService;
    private final CommandPublishService commandPublishService;
    private final QueryPublisherService queryPublisherService;

    // Tag
    private final QueryTagService queryTagService;
    private final CommandProductTagService commandProductTagService;


    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public ProductOnlyIdDto create(ProductCreateDto dto) {

        // ThumbnailFile
        FileResponseDto thumbnailFile = commandFileService.register(dto.toThumbnailFileEntity());

        // EbookFile
        FileResponseDto ebookFile = null;
        if (Objects.nonNull(dto.toEbookFileEntity())) {
            ebookFile = commandFileService.register(dto.toEbookFileEntity());
        }

        // SubscribeProduct
        SubscribeProduct subscribeProduct = null;
        if (Objects.nonNull(dto.getISSN())) {
            subscribeProduct = querySubscribeProductRepository.findByISSN(dto.getISSN()).orElse(null);
            if (Objects.isNull(subscribeProduct)) {
                subscribeProduct = commandSubscribeProductRepository.save(dto.toSubscribeProductEntity());
            }
        }

        // TotalDiscountRate
        TotalDiscountRate totalDiscountRate = queryTotalDiscountRateRepository.findById(TOTAL_DISCOUNT_RATE_DEFAULT_ID)
                .orElseThrow(TotalDiscountRateNotExistsException::new);

        // Product
        Product product = queryProductRepository.findByISBN(dto.getISBN()).orElse(null);
        if (!Objects.isNull(product)) {
            throw new AlreadyProductExistsException(dto.getISBN());
        }
        product = commandProductRepository.save(dto.toProductEntity(
                subscribeProduct,
                thumbnailFile.toEntity(),
                Objects.isNull(ebookFile) ? null : ebookFile.toEntity(),
                totalDiscountRate
        ));

        // Writing
        List<Author> authors = dto.getAuthors().stream()
                .map(id -> queryAuthorService.findById(id).toEntity())
                .collect(Collectors.toList());

        for (Author author : authors) {
            commandWritingService.register(Writing.create(product, author));
        }

        // Publish
        PublisherResponseDto publisher = queryPublisherService.findById(dto.getPublisherId());
        commandPublishService.register(Publish.create(product, publisher.toEntity(), dto.getPublishedDate()));

        // Tag
        if (Objects.nonNull(dto.getTags())) {
            List<Tag> tags = dto.getTags().stream()
                    .map(id -> queryTagService.findById(id).toEntity())
                    .collect(Collectors.toList());

            for (Tag tag : tags) {
                commandProductTagService.register(ProductTag.create(
                        product,
                        Tag.builder().id(tag.getId()).name(tag.getName()).build()
                ));
            }
        }

        // TODO: add Category

        return new ProductOnlyIdDto(product.getId());
    }
}
