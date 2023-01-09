package shop.yesaladin.shop.writing.service.inter;

import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.writing.domain.model.Writing;

/**
 * 집필 생성을 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandWritingService {

    Writing create(String authorName, Product product, Member member);
}
