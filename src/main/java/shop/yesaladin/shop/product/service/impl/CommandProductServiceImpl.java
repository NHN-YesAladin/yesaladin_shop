package shop.yesaladin.shop.product.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.category.domain.model.Category;
import shop.yesaladin.shop.category.domain.model.ProductCategory;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.category.service.inter.CommandProductCategoryService;
import shop.yesaladin.shop.category.service.inter.QueryCategoryService;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.file.dto.FileResponseDto;
import shop.yesaladin.shop.file.service.inter.CommandFileService;
import shop.yesaladin.shop.file.service.inter.QueryFileService;
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
import shop.yesaladin.shop.product.dto.ProductOrderRequestDto;
import shop.yesaladin.shop.product.dto.ProductUpdateDto;
import shop.yesaladin.shop.product.exception.NegativeOrZeroQuantityException;
import shop.yesaladin.shop.product.exception.ProductAlreadyExistsException;
import shop.yesaladin.shop.product.exception.ProductNotFoundException;
import shop.yesaladin.shop.product.exception.RequestedQuantityLargerThanSellQuantityException;
import shop.yesaladin.shop.product.exception.TotalDiscountRateNotExistsException;
import shop.yesaladin.shop.product.service.inter.CommandProductService;
import shop.yesaladin.shop.publish.domain.model.Publish;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.publish.service.inter.CommandPublishService;
import shop.yesaladin.shop.publish.service.inter.QueryPublisherService;
import shop.yesaladin.shop.tag.domain.model.ProductTag;
import shop.yesaladin.shop.tag.domain.model.Tag;
import shop.yesaladin.shop.tag.service.inter.CommandProductTagService;
import shop.yesaladin.shop.tag.service.inter.QueryTagService;
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
@Slf4j
@RequiredArgsConstructor
@Service
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
    private final CommandPublishService commandPublishService;
    private final QueryPublisherService queryPublisherService;

    // Tag
    private final QueryTagService queryTagService;
    private final CommandProductTagService commandProductTagService;

    // Category
    private final QueryCategoryService queryCategoryService;
    private final CommandProductCategoryService commandProductCategoryService;

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
        if (Objects.nonNull(dto.getIssn())) {
            subscribeProduct = querySubscribeProductRepository.findByISSN(dto.getIssn())
                    .orElse(null);
            if (Objects.isNull(subscribeProduct)) {
                subscribeProduct = commandSubscribeProductRepository.save(dto.toSubscribeProductEntity());
            }
        }

        // TotalDiscountRate
        TotalDiscountRate totalDiscountRate = queryTotalDiscountRateRepository.findById(
                        TOTAL_DISCOUNT_RATE_DEFAULT_ID)
                .orElseThrow(TotalDiscountRateNotExistsException::new);

        // Product
        Product product = queryProductRepository.findByIsbn(dto.getIsbn()).orElse(null);
        if (!Objects.isNull(product)) {
            throw new ProductAlreadyExistsException(dto.getIsbn());
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
        commandPublishService.register(Publish.create(
                product,
                publisher.toEntity(),
                dto.getPublishedDate()
        ));

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

        // Category
        List<Long> categoryIds = dto.getCategories();
        if (Objects.nonNull(categoryIds)) {
            List<Category> categories = dto.getCategories().stream().map(id -> {
                CategoryResponseDto response = queryCategoryService.findCategoryById(id);
                Category parentCategory = queryCategoryService.findCategoryById(response.getParentId())
                        .toEntity(null);
                return response.toEntity(parentCategory);
            }).collect(Collectors.toList());

            for (Category category : categories) {
                commandProductCategoryService.register(ProductCategory.create(product, category));
            }
        }

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
        if (Objects.nonNull(dto.getIssn())) {
            subscribeProduct = querySubscribeProductRepository.findByISSN(dto.getIssn())
                    .orElse(null);
            if (Objects.isNull(subscribeProduct)) {
                subscribeProduct = commandSubscribeProductRepository.save(dto.toSubscribeProductEntity());
            }
        }

        // ThumbnailFile
        File thumbnailFile = product.getThumbnailFile();
        if (Objects.nonNull(dto.getThumbnailFileUrl())) {
            thumbnailFile = commandFileService.register(dto.changeThumbnailFile(thumbnailFile)).toEntity();
        }

        // EbookFile
        File ebookFile = product.getEbookFile();
        if (Objects.nonNull(dto.getEbookFileUrl())) {
            ebookFile = commandFileService.register(dto.changeEbookFile(ebookFile)).toEntity();
        }

        // Writing
        commandWritingService.deleteByProduct(product);

        List<Author> authors = dto.getAuthors().stream()
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

        List<Tag> tags = dto.getTags().stream()
                .map(tagId -> queryTagService.findById(tagId).toEntity())
                .collect(Collectors.toList());

        for (Tag tag : tags) {
            commandProductTagService.register(ProductTag.create(
                    product,
                    Tag.builder().id(tag.getId()).name(tag.getName()).build()
            ));
        }

        // Category
        commandProductCategoryService.deleteByProduct(product);

        List<Category> categories = dto.getCategories().stream().map(catId -> {
            CategoryResponseDto response = queryCategoryService.findCategoryById(catId);
            Category parentCategory = queryCategoryService.findCategoryById(response.getParentId())
                    .toEntity(null);
            return response.toEntity(parentCategory);
        }).collect(Collectors.toList());

        for (Category category : categories) {
            commandProductCategoryService.register(ProductCategory.create(product, category));
        }

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

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void changeIsSale(long id) {
        Product product = queryProductRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.changeIsSale();

        commandProductRepository.save(product);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void changeIsForcedOutOfStock(long id) {
        Product product = queryProductRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.changeIsForcedOutOfStock();

        commandProductRepository.save(product);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Transactional
    @Override
    public Map<String, Product> orderProducts(List<ProductOrderRequestDto> products) {
        List<String> isbnList = getIsbnList(products);
        Map<String, Integer> quantities = products.stream()
                .collect(Collectors.toMap(
                        ProductOrderRequestDto::getIsbn,
                        ProductOrderRequestDto::getQuantity
                ));

        List<Product> productList = getAvailableProducts(
                products,
                isbnList,
                quantities
        );

        productList.forEach(product -> product.changeQuantity(quantities.get(product.getIsbn())));

        return productList
                .stream()
                .collect(Collectors.toMap(Product::getIsbn, product -> product));
    }

    private List<Product> getAvailableProducts(
            List<ProductOrderRequestDto> products,
            List<String> isbnList,
            Map<String, Integer> quantities
    ) {
        List<Product> productList = queryProductRepository.findByIsbnList(isbnList, quantities);

        if (productList.size() != products.size()) {
            throw new ClientException(
                    ErrorCode.BAD_REQUEST,
                    "Product is not available to order."
            );
        }
        return productList;
    }

    private static List<String> getIsbnList(List<ProductOrderRequestDto> products) {
        return products
                .stream()
                .map(ProductOrderRequestDto::getIsbn)
                .collect(Collectors.toList());
    }
}
