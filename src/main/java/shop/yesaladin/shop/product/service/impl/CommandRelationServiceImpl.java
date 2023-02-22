package shop.yesaladin.shop.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.domain.model.Relation;
import shop.yesaladin.shop.product.domain.model.Relation.Pk;
import shop.yesaladin.shop.product.domain.repository.CommandRelationRepository;
import shop.yesaladin.shop.product.domain.repository.QueryProductRepository;
import shop.yesaladin.shop.product.domain.repository.QueryRelationRepository;
import shop.yesaladin.shop.product.dto.ProductOnlyIdDto;
import shop.yesaladin.shop.product.service.inter.CommandRelationService;

/**
 * 상품 연관관계 등록/삭제를 위한 Service 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class CommandRelationServiceImpl implements CommandRelationService {

    private final CommandRelationRepository commandRelationRepository;
    private final QueryRelationRepository queryRelationRepository;
    private final QueryProductRepository queryProductRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public ProductOnlyIdDto create(Long productMainId, Long productSubId) {
        boolean isExistsRelationMain = isExistsRelation(productMainId, productSubId);
        boolean isExistsRelationSub = isExistsRelation(productSubId, productMainId);

        if (isExistsRelationMain && isExistsRelationSub) {
            throw new ClientException(
                    ErrorCode.PRODUCT_RELATION_ALREADY_EXIST,
                    "ProductMain id = " + productMainId + ", ProductSub id = " + productSubId
                            + " is already exists."
            );
        }

        Product productMain = queryProductRepository.findProductById(productMainId)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.PRODUCT_NOT_FOUND,
                        "ProductMain not found with id : " + productMainId
                ));

        Product productSub = queryProductRepository.findProductById(productSubId)
                .orElseThrow(() -> new ClientException(
                        ErrorCode.PRODUCT_NOT_FOUND,
                        "ProductSub not found with id : " + productSubId
                ));

        if (!isExistsRelationMain) {
            commandRelationRepository.save(Relation.create(productMain, productSub));
        }
        if (!isExistsRelationSub) {
            commandRelationRepository.save(Relation.create(productSub, productMain));
        }

        return new ProductOnlyIdDto(productMainId);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void delete(Long productMainId, Long productSubId) {
        boolean isExistsRelationMain = isExistsRelation(productMainId, productSubId);
        boolean isExistsRelationSub = isExistsRelation(productSubId, productMainId);

        if (!isExistsRelationMain && !isExistsRelationSub) {
            throw new ClientException(
                    ErrorCode.PRODUCT_RELATION_NOT_FOUND,
                    "ProductMain id = " + productMainId + ", ProductSub id = " + productSubId
                            + " is not found."
            );
        }

        if (isExistsRelationMain) {
            commandRelationRepository.deleteByPk(
                    Pk.builder().productMainId(productMainId).productSubId(productSubId).build());
        }
        if (isExistsRelationSub) {
            commandRelationRepository.deleteByPk(
                    Pk.builder().productMainId(productSubId).productSubId(productMainId).build());
        }
    }

    /**
     * 이미 존재하는 연관관계인지 확인합니다.
     *
     * @param productMainId 연관관계를 조회할 메인 상품 Id
     * @param productSubId  연관관계를 조회할 서브 상품 Id
     * @return 연관관계 존재하는지에 대한 여부
     * @author 이수정
     * @since 1.0
     */
    private boolean isExistsRelation(Long productMainId, Long productSubId) {
        return queryRelationRepository.existsByPk(
                Pk.builder().productMainId(productMainId).productSubId(productSubId).build());
    }
}
