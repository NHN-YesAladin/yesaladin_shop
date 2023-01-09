package shop.yesaladin.shop.writing.domain.repository;

import shop.yesaladin.shop.writing.domain.model.Writing;

/**
 * 집필 등록 및 수정 관련 repository 클래스입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandWritingRepository {

    Writing save(Writing writing);
}
