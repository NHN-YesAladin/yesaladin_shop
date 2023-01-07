package shop.yesaladin.shop.product.controller;

import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.product.dto.ProductCreateDto;

/**
 * 상품 등록을 위한 RestController 입니다.
 *
 * @author : 이수정
 * @since : 1.0
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    @PostMapping
    public void registerProduct(@Valid @RequestBody ProductCreateDto productCreateDto) {

    }
}
