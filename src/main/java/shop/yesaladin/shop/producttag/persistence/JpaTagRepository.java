package shop.yesaladin.shop.producttag.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.producttag.domain.model.Tag;
import shop.yesaladin.shop.producttag.domain.repository.CommandTagRepository;

/**
 * 상품 태그 repository 구현체입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface JpaTagRepository extends Repository<Tag, Long>,
        CommandTagRepository {

}
