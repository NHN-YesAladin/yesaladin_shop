package shop.yesaladin.shop.writing.service.inter;

import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.writing.domain.model.Writing;
import shop.yesaladin.shop.writing.dto.WritingResponseDto;

/**
 * 집필 생성/수정/삭제를 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandWritingService {

    /**
     * 집필을 저장하고, 생성된 집필 dto를 반환합니다.
     *
     * @param writing 저장할 집필 엔터티
     * @return 생성된 집필 dto
     * @author 이수정
     * @since 1.0
     */
    WritingResponseDto register(Writing writing);

}
