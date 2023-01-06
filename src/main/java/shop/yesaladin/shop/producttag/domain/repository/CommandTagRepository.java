package shop.yesaladin.shop.producttag.domain.repository;

import shop.yesaladin.shop.producttag.domain.model.Tag;

/**
 * 상품 태그 등록 및 수정 관련 repository 클래스입니다.
 *
 * @author : 이수정
 * @since : 1.0
 */
public interface CommandTagRepository {
    Tag save(Tag tag);
}
