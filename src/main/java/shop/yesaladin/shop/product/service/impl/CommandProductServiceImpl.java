package shop.yesaladin.shop.product.service.impl;

import java.util.Objects;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.file.dto.FileResponseDto;
import shop.yesaladin.shop.file.service.inter.CommandFileService;
import shop.yesaladin.shop.file.service.inter.QueryFileService;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.service.inter.QueryMemberService;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.SubscribeProduct;
import shop.yesaladin.shop.product.domain.model.TotalDiscountRate;
import shop.yesaladin.shop.product.domain.repository.CommandProductRepository;
import shop.yesaladin.shop.product.domain.repository.QueryProductRepository;
import shop.yesaladin.shop.product.dto.ProductCreateDto;
import shop.yesaladin.shop.product.dto.ProductResponseDto;
import shop.yesaladin.shop.product.dto.SubscribeProductResponseDto;
import shop.yesaladin.shop.product.dto.TotalDiscountRateResponseDto;
import shop.yesaladin.shop.product.exception.AlreadyProductExistsException;
import shop.yesaladin.shop.product.service.inter.CommandProductService;
import shop.yesaladin.shop.product.service.inter.CommandSubscribeProductService;
import shop.yesaladin.shop.product.service.inter.QuerySubscribeProductService;
import shop.yesaladin.shop.product.service.inter.QueryTotalDiscountRateService;
import shop.yesaladin.shop.publish.domain.model.Publish;
import shop.yesaladin.shop.publish.domain.model.Publisher;
import shop.yesaladin.shop.publish.dto.PublisherResponseDto;
import shop.yesaladin.shop.publish.service.inter.CommandPublishService;
import shop.yesaladin.shop.publish.service.inter.CommandPublisherService;
import shop.yesaladin.shop.publish.service.inter.QueryPublisherService;
import shop.yesaladin.shop.tag.domain.model.ProductTag;
import shop.yesaladin.shop.tag.domain.model.Tag;
import shop.yesaladin.shop.tag.dto.TagResponseDto;
import shop.yesaladin.shop.tag.service.inter.CommandProductTagService;
import shop.yesaladin.shop.tag.service.inter.CommandTagService;
import shop.yesaladin.shop.tag.service.inter.QueryTagService;
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

    // Product
    private final CommandProductRepository commandProductRepository;
    private final QueryProductRepository queryProductRepository;

    // File
    private final CommandFileService commandFileService;
    private final QueryFileService queryFileService;

    // SubscribeProduct
    private final CommandSubscribeProductService commandSubscribeProductService;
    private final QuerySubscribeProductService querySubscribeProductService;

    // TotalDiscountRate
    private final QueryTotalDiscountRateService queryTotalDiscountRateService;

    // Writing
    private final CommandWritingService commandWritingService;

    // Member
    private final QueryMemberService queryMemberService;

    // Publisher
    private final CommandPublisherService commandPublisherService;
    private final QueryPublisherService queryPublisherService;
    private final CommandPublishService commandPublishService;

    // Tag
    private final CommandTagService commandTagService;
    private final QueryTagService queryTagService;
    private final CommandProductTagService commandProductTagService;


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
    public ProductResponseDto create(ProductCreateDto dto) {

        // File
        FileResponseDto thumbnailFile = queryFileService.findByName(dto.getThumbnailFileName());
        if (Objects.isNull(thumbnailFile)) {
            thumbnailFile = commandFileService.register(dto.toThumbnailFileEntity());
        }

        FileResponseDto ebookFile = queryFileService.findByName(dto.getEbookFileName());
        if (Objects.isNull(ebookFile)) {
            ebookFile = commandFileService.register(dto.toEbookFileEntity());
        }

        // SubscribeProduct
        SubscribeProductResponseDto subscribeProduct = querySubscribeProductService.findByISSN(dto.getISSN());
        if (Objects.isNull(subscribeProduct)) {
            subscribeProduct = commandSubscribeProductService.register(dto.toSubscribeProductEntity());
        }

        // TotalDiscountRate
        TotalDiscountRateResponseDto totalDiscountRate = queryTotalDiscountRateService.findById(1);

        // Product
        Product product = queryProductRepository.findByISBN(dto.getISBN()).orElse(null);
        if (!Objects.isNull(product)) {
            throw new AlreadyProductExistsException(dto.getISBN());
        }
        product = commandProductRepository.save(dto.toProductEntity(
                subscribeProduct.toEntity(),
                thumbnailFile.toEntity(),
                ebookFile.toEntity(),
                totalDiscountRate.toEntity()
        ));

        // Writing
        Member member = null;
        if (!dto.getLoginId().equals("")) {
            member = queryMemberService.findMemberByLoginId(dto.getLoginId()).toEntity();
        }
        commandWritingService.create(dto.getWriterName(), product, member);

        // Publish
        PublisherResponseDto publisher = queryPublisherService.findByName(dto.getPublisherName());
        if (Objects.isNull(publisher)) {
            publisher = commandPublisherService.register(Publisher.builder().name(dto.getPublisherName()).build());
        }
        commandPublishService.register(Publish.create(product, publisher.toEntity(), dto.getPublishedDate()));

        // Tag
        for (String name : dto.getTags()) {
            TagResponseDto tag = queryTagService.findByName(name);
            if (Objects.isNull(tag)) {
                tag = commandTagService.register(Tag.builder().name(name).build());
            }
            commandProductTagService.register(ProductTag.create(product, tag.toEntity()));
        }

        // TODO: add Category

        return new ProductResponseDto(product.getId());
    }
}
