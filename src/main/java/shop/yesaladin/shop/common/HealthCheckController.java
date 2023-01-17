package shop.yesaladin.shop.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * l7 health 체크를 위한 rest controller 클래스 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@Slf4j
@RestController
public class HealthCheckController {

    private boolean checked = true;

    @GetMapping("/monitor/l7check")
    public ResponseEntity healthCheck() {
        return this.checked ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @PutMapping(value = "/deploy", params = {"status"})
    public void check(@RequestParam(value = "status") String status) {
        this.checked = status.equals("ok");
    }
}
