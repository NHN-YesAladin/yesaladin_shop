package shop.yesaladin.shop.tag.domain.repository;

import shop.yesaladin.shop.tag.domain.model.Tag;

import java.util.List;
import java.util.Optional;

/**
 * 태그 조회 관련 Repository Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface QueryTagRepository {

    Optional<Tag> findById(Long id);

    Optional<Tag> findByName(String name);
}
