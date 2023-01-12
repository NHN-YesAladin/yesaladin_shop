package shop.yesaladin.shop.publish.domain.repository;

import shop.yesaladin.shop.publish.domain.model.Publish;

/**
 * 출판 등록 및 수정 관련 repository 클래스입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandPublishRepository {

    Publish save(Publish publish);
}
