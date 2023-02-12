package shop.yesaladin.shop.docs;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.common.dto.ResponseDto;

@RestController
@RequestMapping("/docs")
public class DocsController {

    @GetMapping
    public ResponseDto<Void> responseDto() {
        return ResponseDto.<Void>builder().success(true).status(HttpStatus.OK).data(null).build();
    }
}
