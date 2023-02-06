package shop.yesaladin.shop.product.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.category.dto.CategoryResponseDto;
import shop.yesaladin.shop.category.service.inter.QueryProductCategoryService;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.repository.QueryProductRepository;
import shop.yesaladin.shop.product.dto.*;
import shop.yesaladin.shop.product.exception.ProductNotFoundException;
import shop.yesaladin.shop.product.service.inter.QueryProductService;
import shop.yesaladin.shop.publish.dto.PublishResponseDto;
import shop.yesaladin.shop.publish.dto.PublishersResponseDto;
import shop.yesaladin.shop.publish.service.inter.QueryPublishService;
import shop.yesaladin.shop.tag.dto.ProductTagResponseDto;
import shop.yesaladin.shop.tag.dto.TagsResponseDto;
import shop.yesaladin.shop.tag.service.inter.QueryProductTagService;
import shop.yesaladin.shop.writing.dto.AuthorsResponseDto;
import shop.yesaladin.shop.writing.dto.WritingResponseDto;
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

    private final float PERCENT_DENOMINATOR_VALUE = 100;
    private final long ROUND_OFF_VALUE = 10;

    private final QueryProductRepository queryProductRepository;

    private final QueryWritingService queryWritingService;
    private final QueryPublishService queryPublishService;
    private final QueryProductTagService queryProductTagService;
    private final QueryProductCategoryService queryProductCategoryService;

    private List<String> getIsbnList(List<ProductOrderRequestDto> products) {
        return products
                .stream()
                .map(ProductOrderRequestDto::getIsbn)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public ProductDetailResponseDto findById(long id) {
        Product product = queryProductRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        int rate = getRate(product);
        long pointPrice = product.isGivenPoint() && product.getGivenPointRate() != 0
                ? calcPointPrice(product) : 0;

        PublishResponseDto publish = queryPublishService.findByProduct(product);

        return new ProductDetailResponseDto(
                product.getId(),
                Objects.nonNull(product.getEbookFile()) && !product.getEbookFile().getUrl().isBlank(),
                product.getTitle(),
                findAuthorsByProduct(product),
                publish.getPublisher().getName(),
                product.getThumbnailFile().getUrl(),
                product.getActualPrice(),
                calcSellingPrice(product, rate),
                rate,
                pointPrice,
                product.getGivenPointRate(),
                publish.getPublishedDate().toString(),
                product.getIsbn(),
                product.isSubscriptionAvailable(),
                product.getSubscribeProduct().getISSN(),
                product.getContents(),
                product.getDescription(),
                product.getQuantity() > 0 && !product.isForcedOutOfStock() && product.isSale() && !product.isDeleted(),
                queryProductCategoryService.findCategoriesByProduct(product)
        );
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public ProductModifyDto findProductByIdForForm(long id) {
        Product product = queryProductRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        List<WritingResponseDto> writings = queryWritingService.findByProduct(product);
        List<AuthorsResponseDto> authors = new ArrayList<>();
        for (WritingResponseDto writing : writings) {
            authors.add(new AuthorsResponseDto(
                    writing.getAuthor().getId(),
                    writing.getAuthor().getName(),
                    Objects.isNull(writing.getAuthor().getMember()) ? null
                            : writing.getAuthor().getMember().getLoginId()
            ));
        }

        PublishResponseDto publish = queryPublishService.findByProduct(product);

        List<ProductTagResponseDto> productTags = queryProductTagService.findByProduct(product);
        List<TagsResponseDto> tags = new ArrayList<>();
        for (ProductTagResponseDto productTag : productTags) {
            tags.add(new TagsResponseDto(
                    productTag.getTag().getId(),
                    productTag.getTag().getName()
            ));
        }

        List<CategoryResponseDto> categories = queryProductCategoryService.findCategoriesByProduct(
                product);

        return new ProductModifyDto(
                product.getIsbn(),
                product.getThumbnailFile().getUrl(),
                product.getTitle(),
                product.getContents(),
                product.getDescription(),
                Objects.isNull(product.getEbookFile()) ? null : product.getEbookFile().getUrl(),
                authors,
                new PublishersResponseDto(
                        publish.getPublisher().getId(),
                        publish.getPublisher().getName()
                ),
                publish.getPublishedDate().toString(),
                product.getProductTypeCode().name(),
                tags,
                product.getActualPrice(),
                product.isSeparatelyDiscount(),
                product.getDiscountRate(),
                product.isGivenPoint(),
                product.getGivenPointRate(),
                product.getProductSavingMethodCode().name(),
                product.isSubscriptionAvailable(),
                product.getSubscribeProduct().getISSN(),
                product.getQuantity(),
                product.getPreferentialShowRanking(),
                categories
        );
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<ProductsResponseDto> findAllForManager(Pageable pageable, Integer typeId) {
        Page<Product> page = getPaginatedProductsForManager(
                pageable,
                typeId
        );

        return getProductResponses(pageable, page);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<ProductsResponseDto> findAll(Pageable pageable, Integer typeId) {
        Page<Product> page = getPaginatedProducts(
                pageable,
                typeId
        );

        return getProductResponses(pageable, page);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public List<ViewCartDto> getCartProduct(Map<String, String> cart) {
        List<ViewCartDto> viewCart = new ArrayList<>();

        cart.keySet().stream()
                .map(key -> queryProductRepository.findById(Long.valueOf(key))
                        .orElseThrow(() -> new ProductNotFoundException(Long.valueOf(key))))
                .forEach(product -> {
                    int rate = getRate(product);
                    long pointPrice = product.isGivenPoint() && product.getGivenPointRate() != 0
                            ? calcPointPrice(product) : 0;

                    viewCart.add(new ViewCartDto(
                            product.getId(),
                            Integer.parseInt(cart.get(product.getId().toString())),
                            product.getThumbnailFile().getUrl(),
                            product.getTitle(),
                            product.getActualPrice(),
                            calcSellingPrice(product, rate),
                            rate,
                            pointPrice,
                            product.isForcedOutOfStock() || product.getQuantity() <= 0,
                            product.isSale(),
                            product.isDeleted(),
                            !Objects.isNull(product.getEbookFile()),
                            product.isSubscriptionAvailable()
                    ));
                });

        return viewCart;
    }

    /**
     * 전체 조회된 page 객체를 바탕으로 전체 조회 화면에 내보낼 정보를 담은 dto page 객체를 반환합니다.
     *
     * @param pageable 페이징 처리를 위한 객체
     * @param page     페이징 전체 조회된 객체
     * @return dto page 객체
     */
    private Page<ProductsResponseDto> getProductResponses(Pageable pageable, Page<Product> page) {
        List<ProductsResponseDto> products = new ArrayList<>();
        for (Product product : page.getContent()) {
            int rate = getRate(product);
            PublishResponseDto publish = queryPublishService.findByProduct(product);

            products.add(new ProductsResponseDto(
                    product.getId(),
                    product.getTitle(),
                    findAuthorsByProduct(product),
                    publish.getPublisher().getName(),
                    publish.getPublishedDate().toString(),
                    calcSellingPrice(product, rate),
                    rate,
                    product.getQuantity(),
                    product.isSale(),
                    product.isForcedOutOfStock(),
                    product.isSale() && !product.isDeleted(),
                    product.isDeleted(),
                    product.getThumbnailFile().getUrl(),
                    findTagsByProduct(product),
                    product.getEbookFile() != null ? product.getEbookFile().getUrl() : null
            ));
        }

        return new PageImpl<>(products, pageable, page.getTotalElements());
    }

    /**
     * 상품의 할인율을 얻어 반환합니다.
     *
     * @param product 할인율을 구할 상품
     * @return 상품의 할인율
     * @author 이수정
     * @since 1.0
     */
    private int getRate(Product product) {
        return product.isSeparatelyDiscount()
                ? product.getDiscountRate() : product.getTotalDiscountRate().getDiscountRate();
    }

    /**
     * 상품의 포인트 금액을 계산하여 반환합니다.
     *
     * @param product 계산할 상품
     * @return 계산된 상품의 포인트 금액
     * @author 이수정
     * @since 1.0
     */
    private long calcPointPrice(Product product) {
        return Math.round((product.getActualPrice() * product.getGivenPointRate() / PERCENT_DENOMINATOR_VALUE) / ROUND_OFF_VALUE) * ROUND_OFF_VALUE;
    }

    /**
     * 상품 유형에 따라 알맞은 전체 사용자용 전체 조회를 진행하고 Paging된 조회 결과를 반환합니다.
     *
     * @param pageable page 와 size를 자동으로 parsing 하여줌
     * @param typeId   상품 유형 Id
     * @return 전체 조회된 Page 객체
     * @author 이수정
     * @since 1.0
     */
    private Page<Product> getPaginatedProducts(Pageable pageable, Integer typeId) {
        if (Objects.isNull(typeId)) {
            return queryProductRepository.findAll(pageable);
        }
        return queryProductRepository.findAllByTypeId(pageable, typeId);
    }

    /**
     * 상품 유형에 따라 알맞은 관리자용 전체 조회를 진행하고 Paging된 조회 결과를 반환합니다.
     *
     * @param pageable page 와 size를 자동으로 parsing 하여줌
     * @param typeId   상품 유형 Id
     * @return 전체 조회된 Page 객체
     * @author 이수정
     * @since 1.0
     */
    private Page<Product> getPaginatedProductsForManager(Pageable pageable, Integer typeId) {
        if (Objects.isNull(typeId)) {
            return queryProductRepository.findAllForManager(pageable);
        }
        return queryProductRepository.findAllByTypeIdForManager(pageable, typeId);
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
        if (rate == 0) {
            return product.getActualPrice();
        }
        return Math.round((product.getActualPrice()
                - product.getActualPrice() * rate / PERCENT_DENOMINATOR_VALUE) / ROUND_OFF_VALUE)
                * ROUND_OFF_VALUE;
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
     * 연관된 태그 조회을 요청하여 응답받고 응답받은 태그 Dto List에서 태그 이름을 추출하여 반환합니다.
     *
     * @param product 태그를 조회할 상품
     * @return 상품과 관련된 태그 이름 List
     * @author 이수정
     * @since 1.0
     */
    private List<String> findTagsByProduct(Product product) {
        return queryProductTagService.findByProduct(product).stream()
                .map(tag -> tag.getTag().getName())
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
                .peek(product -> product.setQuantity(orderProduct.get(product.getIsbn())))
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
