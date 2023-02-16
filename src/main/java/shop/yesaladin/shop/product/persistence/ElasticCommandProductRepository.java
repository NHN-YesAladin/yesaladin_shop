package shop.yesaladin.shop.product.persistence;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import shop.yesaladin.shop.product.domain.model.SearchedProduct;

/**
 * 엘라스틱서치에 상품을 수정, 삭제하는 레포지터리
 *
 * @author 김선홍
 * @since 1.0
 */
public interface ElasticCommandProductRepository extends ElasticsearchRepository<SearchedProduct, Long> {
    void deleteByIdEquals(Long id);
}
