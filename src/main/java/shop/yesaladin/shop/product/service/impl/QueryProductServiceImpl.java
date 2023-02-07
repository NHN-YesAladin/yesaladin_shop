package shop.yesaladin.shop.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.category.service.inter.QueryProductCategoryService;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.repository.QueryProductRepository;
import shop.yesaladin.shop.product.dto.*;
import shop.yesaladin.shop.product.service.inter.QueryProductService;
import shop.yesaladin.shop.publish.dto.PublishResponseDto;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.publish.service.inter.QueryPublishService;
import shop.yesaladin.shop.tag.dto.TagResponseDto;
import shop.yesaladin.shop.tag.service.inter.QueryProductTagService;
import shop.yesaladin.shop.writing.dto.AuthorsResponseDto;
import shop.yesaladin.shop.writing.service.inter.QueryWritingService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 상품 조회를 위한 Service 구현체 입니다.
 *
 * @author 이수정
 * @author 최예린
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class QueryProductServiceImpl implements QueryProductService {

    private static final float PERCENT_DENOMINATOR_VALUE = 100;
    private static final long ROUND_OFF_VALUE = 10;

    private final QueryProductRepository queryProductRepository;

    private final QueryWritingService queryWritingService;
    private final QueryPublishService queryPublishService;
    private final QueryProductTagService queryProductTagService;
    private final QueryProductCategoryService queryProductCategoryService;

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public ProductOnlyTitleDto findTitleByIsbn(String isbn) {
        ProductOnlyTitleDto productOnlyTitleDto = queryProductRepository.findTitleByIsbn(isbn);
        if (Objects.isNull(productOnlyTitleDto)) {
            throw new ClientException(
                    ErrorCode.PRODUCT_NOT_FOUND,
                    "Product not found with isbn : " + isbn
            );
        }
        return productOnlyTitleDto;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public ProductDetailResponseDto findDetailProductById(long id) {
        Product product = queryProductRepository.findProductById(id)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.PRODUCT_NOT_FOUND,
                        "Product not found with id : " + id
                ));

        int rate = getRateByProduct(product);
        PublishResponseDto publish = queryPublishService.findByProduct(product);

        return ProductDetailResponseDto.builder()
                .id(product.getId())
                .isbn(product.getIsbn())
                .title(product.getTitle())
                .contents(product.getContents())
                .description(product.getDescription())
                .thumbnailFileUrl(product.getThumbnailFile().getUrl())
                .authors(findAuthorsByProduct(product))
                .publisher(PublisherResponseDto.getPublisherFromPublish(publish))
                .publishedDate(publish.getPublishedDate().toString())
                .tags(findTagsByProduct(product))
                .categories(queryProductCategoryService.findCategoriesByProduct(product))
                .actualPrice(product.getActualPrice())
                .sellingPrice(calcSellingPrice(product.getActualPrice(), rate))
                .discountRate(rate)
                .pointPrice(getPointPrice(product))
                .pointRate(product.getGivenPointRate())
                .isEbook(isEbook(product))
                .isSubscriptionAvailable(product.isSubscriptionAvailable())
                .issn(product.getSubscribeProduct().getISSN())
                .onSale(isOnSale(product))
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public ProductModifyDto findProductByIdForForm(long id) {
        Product product = queryProductRepository.findProductById(id)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.PRODUCT_NOT_FOUND,
                        "Product not found with id : " + id
                ));

        PublishResponseDto publish = queryPublishService.findByProduct(product);

        return ProductModifyDto.builder()
                .isbn(product.getIsbn())
                .thumbnailFile(product.getThumbnailFile().getUrl())
                .title(product.getTitle())
                .contents(product.getContents())
                .description(product.getDescription())
                .ebookFileUrl(isEbook(product) ? product.getEbookFile().getUrl() : null)
                .authors(findAuthorsByProduct(product))
                .publisher(PublisherResponseDto.getPublisherFromPublish(publish))
                .publishedDate(publish.getPublishedDate().toString())
                .tags(findTagsByProduct(product))
                .categories(queryProductCategoryService.findCategoriesByProduct(product))
                .actualPrice(product.getActualPrice())
                .isSeparatelyDiscount(product.isSeparatelyDiscount())
                .discountRate(product.getDiscountRate())
                .isGivenPoint(product.isGivenPoint())
                .givenPointRate(product.getGivenPointRate())
                .productTypeCode(product.getProductTypeCode().name())
                .productSavingMethodCode(product.getProductSavingMethodCode().name())
                .isSubscriptionAvailable(product.isSubscriptionAvailable())
                .issn(product.getSubscribeProduct().getISSN())
                .quantity(product.getQuantity())
                .preferentialShowRanking(product.getPreferentialShowRanking())
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<ViewCartDto> getCartProduct(Map<String, String> cart) {
        List<ViewCartDto> viewCart = new ArrayList<>();

        cart.keySet().stream()
                .map(key -> queryProductRepository.findProductById(Long.parseLong(key))
                        .orElseThrow(() -> new ClientException(
                                ErrorCode.PRODUCT_NOT_FOUND,
                                "Product not found with id : " + Long.parseLong(key)
                        )))
                .forEach(product -> {
                    int rate = getRateByProduct(product);

                    viewCart.add(ViewCartDto.builder()
                            .id(product.getId())
                            .quantity(Integer.parseInt(cart.get(product.getId().toString())))
                            .thumbnailFileUrl(product.getThumbnailFile().getUrl())
                            .isbn(product.getIsbn())
                            .title(product.getTitle())
                            .actualPrice(product.getActualPrice())
                            .sellingPrice(calcSellingPrice(product.getActualPrice(), rate))
                            .discountRate(rate)
                            .pointPrice(getPointPrice(product))
                            .isOutOfStack(product.isForcedOutOfStock() || product.getQuantity() <= 0)
                            .isSale(product.isSale())
                            .isDeleted(product.isDeleted())
                            .isEbook(isEbook(product))
                            .isSubscribeProduct(product.isSubscriptionAvailable())
                            .build());
                });

        return viewCart;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public PaginatedResponseDto<ProductsResponseDto> findAll(Pageable pageable, Integer typeId) {
        Page<Product> page;
        if (Objects.isNull(typeId)) {
            page = queryProductRepository.findAll(pageable);
        } else {
            page =  queryProductRepository.findAllByTypeId(pageable, typeId);
        }
        return getProductPaginatedResponses(page);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public PaginatedResponseDto<ProductsResponseDto> findAllForManager(Pageable pageable, Integer typeId) {
        Page<Product> page;
        if (Objects.isNull(typeId)) {
            page = queryProductRepository.findAllForManager(pageable);
        } else {
            page =  queryProductRepository.findAllByTypeIdForManager(pageable, typeId);
        }
        return getProductPaginatedResponses(page);
    }

    /**
     * 전체 조회된 page 객체를 바탕으로 전체 조회 화면에 내보낼 정보를 담은 dto page 객체를 반환합니다.
     *
     * @param page     페이징 전체 조회된 객체
     * @return PaginatedResponseDto
     * @author 이수정
     * @since 1.0
     */
    private PaginatedResponseDto<ProductsResponseDto> getProductPaginatedResponses(Page<Product> page) {
        List<ProductsResponseDto> products = new ArrayList<>();

        for (Product product : page.getContent()) {
            int rate = getRateByProduct(product);
            PublishResponseDto publish = queryPublishService.findByProduct(product);

            products.add(ProductsResponseDto.builder()
                    .id(product.getId())
                    .title(product.getTitle())
                    .authors(findAuthorsByProduct(product))
                    .publisher(PublisherResponseDto.getPublisherFromPublish(publish))
                    .publishedDate(publish.getPublishedDate().toString())
                    .sellingPrice(calcSellingPrice(product.getActualPrice(), rate))
                    .discountRate(rate)
                    .quantity(product.getQuantity())
                    .isSale(product.isSale())
                    .isForcedOutOfStock(product.isForcedOutOfStock())
                    .isShown(product.isSale() && !product.isDeleted())
                    .isDeleted(product.isDeleted())
                    .thumbnailFileUrl(product.getThumbnailFile().getUrl())
                    .tags(findTagsByProduct(product))
                    .ebookFileUrl(isEbook(product) ? product.getEbookFile().getUrl() : null)
                    .build());
        }

        return PaginatedResponseDto.<ProductsResponseDto>builder()
                .totalPage(page.getTotalPages())
                .currentPage(page.getNumber())
                .totalDataCount(page.getTotalElements())
                .dataList(products)
                .build();
    }

    /**
     * 상품의 판매여부를 반환합니다.
     *
     * @param product 판매여부를 얻을 상품
     * @return 상품의 판매여부
     * @author 이수정
     * @since 1.0
     */
    private boolean isOnSale(Product product) {
        return product.getQuantity() > 0 && !product.isForcedOutOfStock() && product.isSale() && !product.isDeleted();
    }

    /**
     * 상품이 eBook인지를 판단하여 반환합니다.
     *
     * @param product 판단할 상품
     * @return eBook 여부
     * @author 이수정
     * @since 1.0
     */
    private boolean isEbook(Product product) {
        return Objects.nonNull(product.getEbookFile()) && !product.getEbookFile().getUrl().isBlank();
    }

    /**
     * 상품의 정가, 할인율을 바탕으로 판매가를 계산해 반환합니다.
     *
     * @param actualPrice 상품의 정가
     * @param rate    상품의 할인율(전체 / 개별)
     * @return 계산된 상품의 판매가
     * @author 이수정
     * @since 1.0
     */
    private long calcSellingPrice(long actualPrice, int rate) {
        if (rate > 0) {
            return Math.round((actualPrice - actualPrice * rate / PERCENT_DENOMINATOR_VALUE)
                    / ROUND_OFF_VALUE) * ROUND_OFF_VALUE;
        }
        return actualPrice;
    }

    /**
     * 상품의 할인율을 얻어 반환합니다.
     *
     * @param product 할인율을 구할 상품
     * @return 상품의 할인율
     * @author 이수정
     * @since 1.0
     */
    private int getRateByProduct(Product product) {
        return product.isSeparatelyDiscount()
                ? product.getDiscountRate() : product.getTotalDiscountRate().getDiscountRate();
    }

    /**
     * 상품의 포인트를 반환합니다.
     *
     * @param product 포인트를 얻을 상품
     * @return 상품의 포인트
     * @author 이수정
     * @since 1.0
     */
    private long getPointPrice(Product product) {
        return product.isGivenPoint() && product.getGivenPointRate() != 0 ?
                Math.round((product.getActualPrice() * product.getGivenPointRate() / PERCENT_DENOMINATOR_VALUE)
                        / ROUND_OFF_VALUE) * ROUND_OFF_VALUE : 0;
    }

    /**
     * 상품의 저자 조회을 요청하여 응답받고 응답받은 저자 Dto List에서 저자의 이름을 추출하여 반환합니다.
     *
     * @param product 저자를 조회할 상품
     * @return 상품과 관련된 저자 이름 List
     * @author 이수정
     * @since 1.0
     */
    private List<AuthorsResponseDto> findAuthorsByProduct(Product product) {
        return queryWritingService.findByProduct(product).stream()
                .map(AuthorsResponseDto::getAuthorFromWriting)
                .collect(Collectors.toList());
    }

    /**
     * 연관된 태그 조회을 요청하여 응답받고 응답받은 태그 Dto List에서 태그 이름을 추출하여 반환합니다.
     *
     * @param product 태그를 조회할 상품
     * @return 상품과 관련된 태그 이름 List
     * @author 이수정
     * @since 1.0
     */
    private List<TagResponseDto> findTagsByProduct(Product product) {
        return queryProductTagService.findByProduct(product).stream()
                .map(TagResponseDto::getTagFromProductTag)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductOrderSheetResponseDto> getByOrderProducts(Map<String, Integer> orderProduct) {
        List<String> isbnList = new ArrayList<>(orderProduct.keySet());

        List<ProductOrderSheetResponseDto> result = queryProductRepository.getByIsbnList(isbnList);

        return result.stream()
                .map(product -> {
                    product.setQuantity(orderProduct.get(product.getIsbn()));
                    return product;
                })
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public SubscribeProductOrderResponseDto getIssnByOrderProduct(ProductOrderRequestDto orderProduct) {
        String isbn = orderProduct.getIsbn();
        int quantity = orderProduct.getQuantity();

        Product product = queryProductRepository.findOrderProductByIsbn(isbn, quantity)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.PRODUCT_NOT_FOUND,
                        "Product not found with isbn : " + isbn + "."
                ));
        checkValidSubscribeProducts(isbn, product);

        return new SubscribeProductOrderResponseDto(product);
    }

    private void checkValidSubscribeProducts(String isbn, Product product) {
        if (!product.isSubscriptionAvailable()) {
            throw new ClientException(
                    ErrorCode.PRODUCT_NOT_SUBSCRIBE_PRODUCT,
                    "Product with isbn(" + isbn + ") is not a subscribe product."
            );
        }
    }
}
