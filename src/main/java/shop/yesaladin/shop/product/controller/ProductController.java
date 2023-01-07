package shop.yesaladin.shop.product.controller;

import java.io.Writer;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.product.domain.model.Product;
import shop.yesaladin.shop.product.dto.ProductCreateDto;
import shop.yesaladin.shop.product.service.inter.CommandProductService;
import shop.yesaladin.shop.product.service.inter.CommandWriterService;

/**
 * 상품 등록을 위한 RestController 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final CommandProductService commandProductService;
    private final CommandWriterService commandWriterService;

    /**
     * [POST /products] 요청을 받아 상품을 생성하고 생성된 상품 객체를 리턴합니다.
     *
     * @param productCreateDto 관리자에게서 입력받은 상품 생성정보
     * @return 생성된 상품을 담은 ResponseEntity
     * @author 이수정
     * @since 1.0
     */
    @PostMapping
    public ResponseEntity registerProduct(@Valid @RequestBody ProductCreateDto productCreateDto) {
        Writer writer = commandWriterService.create(productCreateDto.getWriter());

        Product product = commandProductService.create(
                productCreateDto,
                null,
                null,
                null,
                null,
                null
        );

        return new ResponseEntity(product, HttpStatus.CREATED);
    } // !!미완!!
}
