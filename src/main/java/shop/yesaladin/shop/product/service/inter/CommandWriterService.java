package shop.yesaladin.shop.product.service.inter;

import java.io.Writer;

/**
 * 저자 생성을 위한 Service Interface 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
public interface CommandWriterService {

    Writer create(String writer);
}
