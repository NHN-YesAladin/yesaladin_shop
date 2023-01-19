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
import shop.yesaladin.shop.product.dto.ProductOnlyIdDto;
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
}
