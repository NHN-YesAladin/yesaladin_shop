package shop.yesaladin.shop.tag.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.tag.domain.model.Tag;
import shop.yesaladin.shop.tag.domain.repository.CommandTagRepository;

/**
 * 상품 태그 Repository 구현체 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface JpaCommandTagRepository extends Repository<Tag, Long>,
        CommandTagRepository {

}
