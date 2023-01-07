package shop.yesaladin.shop.product.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.ProductSavingMethodCode;
import shop.yesaladin.shop.product.domain.model.ProductTypeCode;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.repository.CommandProductRepository;
import shop.yesaladin.shop.product.dto.ProductCreateDto;
import shop.yesaladin.shop.product.service.inter.CommandProductService;
import shop.yesaladin.shop.publisher.domain.model.Publisher;

/**
 * 상품 생성을 위한 Service 구현체입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class CommandProductServiceImpl implements CommandProductService {

    private final CommandProductRepository commandProductRepository;

    /**
     * 상품을 생성하고 상품 테이블에 저장합니다. 생성된 상품 객체를 리턴합니다.
     *
     * @param dto 관리자에게서 입력받은 상품 생성정보
     * @return 생성된 상품
     * @author 이수정
     * @since 1.0
     */
    @Override
    public Product create(
            ProductCreateDto dto,
            Publisher publisher,
            SubscribeProduct subscribeProduct,
            File file,
            ProductTypeCode productTypeCode,
            ProductSavingMethodCode productSavingMethodCode
    ) {
        Product product = Product.builder()
                .ISBN(dto.getISBN())
                .title(dto.getTitle())
                .contents(dto.getContents())
                .description(dto.getDescription())
                .actualPrice(dto.getActualPrice())
                .discountRate(dto.getDiscountRate())
                .isSeparatelyDiscount(dto.getIsSeparatelyDiscount())
                .givenPointRate(dto.getGivenPointRate())
                .isGivenPoint(dto.getIsGivenPoint())
                .isSubscriptionAvailable(dto.getIsSubscriptionAvailable())
                .isSale(dto.getIsSale())
                .isForcedOutOfStock(dto.getIsForcedOutOfStock())
                .quantity(dto.getQuantity())
                .publishedDate(LocalDate.parse(dto.getPublishedDate(), DateTimeFormatter.ISO_DATE))
                .preferentialShowRanking(dto.getPreferentialShowRanking())
                .publisher(publisher)
                .subscribeProduct(subscribeProduct)
                .file(file)
                .productTypeCode(productTypeCode)
                .productSavingMethodCode(productSavingMethodCode)
                .build();

        return commandProductRepository.save(product);
    } // !!미완!!, Docs 수정
}
