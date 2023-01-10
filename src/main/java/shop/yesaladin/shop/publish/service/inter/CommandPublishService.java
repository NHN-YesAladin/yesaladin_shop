package shop.yesaladin.shop.publish.service.inter;

import shop.yesaladin.shop.publish.domain.model.Publish;
import shop.yesaladin.shop.publish.dto.PublishResponseDto;

/**
 * 출판 등록을 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandPublishService {

    PublishResponseDto register(Publish publish);
}
