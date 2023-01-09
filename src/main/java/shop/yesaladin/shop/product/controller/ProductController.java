package shop.yesaladin.shop.product.controller;

import java.net.URI;
import java.net.URISyntaxException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.product.dto.ProductCreateDto;
import shop.yesaladin.shop.product.dto.ProductResponseDto;
import shop.yesaladin.shop.product.service.inter.CommandProductService;

/**
 * 상품 등록을 위한 RestController 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/products")
public class ProductController {

    private final CommandProductService commandProductService;

    /**
     * [POST /products] 요청을 받아 상품을 생성하고 생성된 상품 객체를 리턴합니다.
     *
     * @param productCreateDto 관리자에게서 입력받은 상품 생성정보
     * @return 생성된 상품을 담은 ResponseEntity
     * @author 이수정
     * @since 1.0
     */
    @PostMapping
    public ResponseEntity registerProduct(@Valid @RequestBody ProductCreateDto productCreateDto)
            throws URISyntaxException {
        ProductResponseDto productResponseDto = commandProductService.create(productCreateDto);

        return ResponseEntity.created(new URI(productResponseDto.getId().toString()))
                .body(productResponseDto);
    }
}
