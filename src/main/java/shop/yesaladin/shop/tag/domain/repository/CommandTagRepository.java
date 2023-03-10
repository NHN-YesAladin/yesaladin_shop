package shop.yesaladin.shop.tag.domain.repository;

import shop.yesaladin.shop.tag.domain.model.Tag;

/**
 * 상품 태그 등록 및 수정 관련 Repository Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandTagRepository {

    Tag save(Tag tag);
}
