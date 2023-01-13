package shop.yesaladin.shop.tag.persistence;

import org.springframework.data.repository.Repository;
import shop.yesaladin.shop.tag.domain.model.Tag;
import shop.yesaladin.shop.tag.domain.repository.CommandTagRepository;
import shop.yesaladin.shop.tag.domain.repository.QueryTagRepository;

/**
 * 상품 태그 repository 구현체입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface JpaTagRepository extends Repository<Tag, Long>,
        CommandTagRepository, QueryTagRepository {

}
