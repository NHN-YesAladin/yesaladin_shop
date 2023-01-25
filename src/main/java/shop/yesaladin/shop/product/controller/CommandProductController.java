package shop.yesaladin.shop.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.yesaladin.shop.product.dto.ProductCreateDto;
import shop.yesaladin.shop.product.dto.ProductOnlyIdDto;
import shop.yesaladin.shop.product.dto.ProductUpdateDto;
import shop.yesaladin.shop.product.service.inter.CommandProductService;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 상품 등록/수정/삭제를 위한 RestController 입니다.
 *
 * @author 이수정
 * @since 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/shop/v1/products")
public class CommandProductController {

    private final CommandProductService commandProductService;

    /**
     * [POST /products] 요청을 받아 상품을 생성하여 등록합니다.
     *
     * @param createDto 관리자에게서 입력받은 상품을 생성하기 위한 요청 파라미터
     * @return 생성된 상품 정보를 담은 ResponseEntity
     * @throws URISyntaxException URI 문법을 어긴 경우 던지는 예외
     * @author 이수정
     * @since 1.0
     */
    @PostMapping
    public ResponseEntity registerProduct(@Valid @RequestBody ProductCreateDto createDto)
            throws URISyntaxException {
        ProductOnlyIdDto product = commandProductService.create(createDto);

        return ResponseEntity.created(new URI(product.getId().toString())).body(product);
    }

    /**
     * [PUT /products/{productId}] 요청을 받아 상품 정보를 수정합니다.
     *
     * @param id        수정할 상품의 Id
     * @param updateDto 상품 정보 수정을 위한 Dto
     * @return 수정된 상품 정보를 담은 ResponseEntity
     * @author 이수정
     * @since 1.0
     */
    @PutMapping("/{productId}")
    public ResponseEntity updateProduct(
            @PathVariable("productId") long id,
            @Valid @RequestBody ProductUpdateDto updateDto
    ) {
        ProductOnlyIdDto product = commandProductService.update(id, updateDto);

        return ResponseEntity.ok().body(product);
    }


    /**
     * [POST /products/{productId}] 요청을 받아 상품을 삭제상태로 변경합니다.
     *
     * @param id 삭제할 상품의 Id
     * @author 이수정
     * @since 1.0
     */
    @PostMapping("/{productId}")
    public ResponseEntity deleteProduct(@PathVariable("productId") long id) {
        commandProductService.softDelete(id);

        return ResponseEntity.ok().build();
    }
}
