package shop.yesaladin.shop.file.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.file.domain.repository.CommandFileRepository;
import shop.yesaladin.shop.product.domain.model.Product;

/**
 * 파일 repository 구현체입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface JpaFileRepository extends Repository<Product, Long>,
        CommandFileRepository {

}
