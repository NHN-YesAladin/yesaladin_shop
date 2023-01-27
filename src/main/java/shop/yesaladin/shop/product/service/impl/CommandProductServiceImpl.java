package shop.yesaladin.shop.product.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.file.dto.FileResponseDto;
import shop.yesaladin.shop.file.service.inter.CommandFileService;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.domain.repository.CommandProductRepository;
import shop.yesaladin.shop.product.domain.repository.CommandSubscribeProductRepository;
import shop.yesaladin.shop.product.domain.repository.QueryProductRepository;
import shop.yesaladin.shop.product.domain.repository.QuerySubscribeProductRepository;
import shop.yesaladin.shop.product.domain.repository.QueryTotalDiscountRateRepository;
import shop.yesaladin.shop.product.dto.ProductCreateDto;
import shop.yesaladin.shop.product.dto.ProductOnlyIdDto;
import shop.yesaladin.shop.product.dto.ProductTagCreateDto;
import shop.yesaladin.shop.product.dto.ProductUpdateDto;
import shop.yesaladin.shop.product.exception.AlreadyProductExistsException;
import shop.yesaladin.shop.product.exception.NegativeOrZeroQuantityException;
import shop.yesaladin.shop.product.exception.ProductNotFoundException;
import shop.yesaladin.shop.product.exception.RequestedQuantityLargerThanSellQuantityException;
import shop.yesaladin.shop.product.exception.TotalDiscountRateNotExistsException;
import shop.yesaladin.shop.product.service.inter.CommandProductService;
import shop.yesaladin.shop.publish.domain.model.Publish;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.publish.service.inter.CommandPublishService;
import shop.yesaladin.shop.publish.service.inter.QueryPublisherService;
import shop.yesaladin.shop.tag.service.inter.CommandProductTagService;
import shop.yesaladin.shop.writing.domain.model.Author;
import shop.yesaladin.shop.writing.domain.model.Writing;
import shop.yesaladin.shop.writing.service.inter.CommandWritingService;
import shop.yesaladin.shop.writing.service.inter.QueryAuthorService;

/**
 * 상품 생성을 위한 Service 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class CommandProductServiceImpl implements CommandProductService {

    private static final int TOTAL_DISCOUNT_RATE_DEFAULT_ID = 1;

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

    // Writing
    private final CommandWritingService commandWritingService;
    private final QueryAuthorService queryAuthorService;

    // Publish
    private final CommandPublishService commandPublishService;
    private final QueryPublisherService queryPublisherService;

    // Tag
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
            subscribeProduct = querySubscribeProductRepository.findByISSN(dto.getISSN())
                    .orElseGet(() -> commandSubscribeProductRepository.save(dto.toSubscribeProductEntity()));
        }

        // TotalDiscountRate
        TotalDiscountRate totalDiscountRate = queryTotalDiscountRateRepository.findById(
                        TOTAL_DISCOUNT_RATE_DEFAULT_ID)
                .orElseThrow(TotalDiscountRateNotExistsException::new);

        // Product
        queryProductRepository.findByISBN(dto.getISBN()).ifPresent(foundProduct -> {
            throw new AlreadyProductExistsException(dto.getISBN());
        });

        Product product = commandProductRepository.save(dto.toProductEntity(
                subscribeProduct,
                thumbnailFile.toEntity(),
                Objects.isNull(ebookFile) ? null : ebookFile.toEntity(),
                totalDiscountRate
        ));

        // Writing
        List<Author> authors = dto.getAuthors()
                .stream()
                .map(id -> queryAuthorService.findById(id).toEntity())
                .collect(Collectors.toList());

        for (Author author : authors) {
            commandWritingService.register(Writing.create(product, author));
        }

        // Publish
        PublisherResponseDto publisher = queryPublisherService.findById(dto.getPublisherId());
        commandPublishService.register(Publish.create(
                product,
                publisher.toEntity(),
                dto.getPublishedDate()
        ));

        // Tag
        createProductTags(dto.getTags(), product);

        // TODO: add Category

        return new ProductOnlyIdDto(product.getId());
    }


    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public ProductOnlyIdDto update(Long id, ProductUpdateDto dto) {
        Product product = queryProductRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        // SubscribeProduct
        SubscribeProduct subscribeProduct = product.getSubscribeProduct();
        if (Objects.nonNull(dto.getISSN())) {
            subscribeProduct = querySubscribeProductRepository.findByISSN(dto.getISSN())
                    .orElse(null);
            if (Objects.isNull(subscribeProduct)) {
                subscribeProduct = commandSubscribeProductRepository.save(dto.toSubscribeProductEntity());
            }
        }

        // ThumbnailFile
        File thumbnailFile = commandFileService.changeFile(
                product.getThumbnailFile().getId(),
                dto.getThumbnailFileUrl()
        ).toEntity();

        // EbookFile
        File ebookFile = null;
        if (Objects.nonNull(product.getEbookFile())) {
            ebookFile = commandFileService.changeFile(
                    product.getEbookFile().getId(),
                    dto.getEbookFileUrl()
            ).toEntity();
        }

        // Writing
        commandWritingService.deleteByProduct(product);

        List<Author> authors = dto.getAuthors()
                .stream()
                .map(authorId -> queryAuthorService.findById(authorId).toEntity())
                .collect(Collectors.toList());

        for (Author author : authors) {
            commandWritingService.register(Writing.create(product, author));
        }

        // Publish
        commandPublishService.deleteByProduct(product);

        PublisherResponseDto publisher = queryPublisherService.findById(dto.getPublisherId());
        commandPublishService.register(Publish.create(
                product,
                publisher.toEntity(),
                dto.getPublishedDate()
        ));

        // Tag
        commandProductTagService.deleteByProduct(product);

        createProductTags(dto.getTags(), product);

        // TODO: add Category

        // Product
        product = dto.changeProduct(
                product,
                subscribeProduct,
                thumbnailFile,
                ebookFile,
                product.getTotalDiscountRate()
        );
        commandProductRepository.save(product);

        return new ProductOnlyIdDto(product.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void softDelete(Long id) {
        Product product = queryProductRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.deleteProduct();

        commandProductRepository.save(product);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void deductQuantity(Long id, int requestedQuantity) {
        if (requestedQuantity <= 0) {
            throw new NegativeOrZeroQuantityException(requestedQuantity);
        }

        Product product = queryProductRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        long sellQuantity = product.getQuantity();
        long deductedQuantity = sellQuantity - requestedQuantity;
        if (deductedQuantity < 0) {
            throw new RequestedQuantityLargerThanSellQuantityException(
                    requestedQuantity,
                    sellQuantity
            );
        }
        product.changeQuantity(deductedQuantity);

        commandProductRepository.save(product);
    }


    private void createProductTags(List<Long> tagIds, Product product) {
        if (Objects.isNull(tagIds)) {
            return;
        }
        tagIds.stream()
                .map(tagId -> new ProductTagCreateDto(product, tagId))
                .forEach(commandProductTagService::register);
    }
}
