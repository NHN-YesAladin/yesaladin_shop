package shop.yesaladin.shop.tag.domain.repository;

import java.util.Optional;
import shop.yesaladin.shop.tag.domain.model.Tag;

/**
 * 태그 조회 관련 repository 클래스입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QueryTagRepository {

    Optional<Tag> findByName(String name);
}
