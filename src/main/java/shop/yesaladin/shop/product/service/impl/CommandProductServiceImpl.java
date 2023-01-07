package shop.yesaladin.shop.product.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.file.service.inter.CommandFileService;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.ProductTypeCode;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.domain.repository.CommandProductRepository;
import shop.yesaladin.shop.product.domain.repository.CommandTotalDiscountRateRepository;
import shop.yesaladin.shop.product.dto.ProductCreateDto;
import shop.yesaladin.shop.product.service.inter.CommandProductService;
import shop.yesaladin.shop.product.service.inter.CommandSubscribeProductService;
import shop.yesaladin.shop.product.service.inter.CommandTotalDiscountRateService;
import shop.yesaladin.shop.publisher.domain.model.Publisher;
import shop.yesaladin.shop.publisher.service.inter.CommandPublisherService;
import shop.yesaladin.shop.writing.domain.model.Writing;
import shop.yesaladin.shop.writing.domain.repository.CommandWritingRepository;
import shop.yesaladin.shop.writing.service.inter.CommandWritingService;

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

    private final CommandPublisherService commandPublisherService;
    private final CommandSubscribeProductService commandSubscribeProductService;
    private final CommandFileService commandFileService;
    private final CommandTotalDiscountRateService commandTotalDiscountRateService;
    private final CommandWritingService commandWritingService;


    /**
     * 상품을 생성하고 상품 테이블에 저장합니다. 생성된 상품 객체를 리턴합니다.
     *
     * @param dto 관리자에게서 입력받은 상품 생성정보
     * @return 생성된 상품 객체
     * @author 이수정
     * @since 1.0
     */
    @Transactional
    @Override
    public Product create(ProductCreateDto dto) {

        // Publisher
        Publisher publisher = commandPublisherService.register(dto.toPublisherEntity());

        // SubscribeProduct
        SubscribeProduct subscribeProduct = commandSubscribeProductService.register(dto.toSubscribeProductEntity());

        // File
        File thumbnailFile = commandFileService.register(dto.toThumbnailFileEntity());
        File ebookFile = commandFileService.register(dto.toEbookFileEntity());

        // TotalDiscountRate
        TotalDiscountRate totalDiscountRate = commandTotalDiscountRateService.register(dto.toTotalDiscountRateEntity());

        // Product
        Product product = commandProductRepository.save(dto.toProductEntity(publisher, subscribeProduct, thumbnailFile, ebookFile, totalDiscountRate));

        // Writing
//        commandWritingService.create(dto.getWriterName(), product, member);

        return product;
    }
}
