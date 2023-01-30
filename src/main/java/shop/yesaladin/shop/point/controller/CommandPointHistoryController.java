package shop.yesaladin.shop.point.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.yesaladin.shop.point.domain.model.PointCode;
import shop.yesaladin.shop.point.dto.PointHistoryRequestDto;
import shop.yesaladin.shop.point.dto.PointHistoryResponseDto;
import shop.yesaladin.shop.point.service.inter.CommandPointHistoryService;

/**
 * 포인트 사용/적립 관련 api 를 위한 rest controller 클래스 입니다.
 *
 * @author 최예린
 * @since 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/points")
public class CommandPointHistoryController {

    private final CommandPointHistoryService pointCommandService;

    /**
     * 포인트를 사용/적립했을 때 포인트 내역에 등록합니다.
     *
     * @param request 사용/적립한 포인트 값
     * @return 등록된 포인트 내역
     * @author 최예린
     * @since 1.0
     */
    @PostMapping(params = "code")
    public PointHistoryResponseDto createPointHistory(
            @RequestParam("code") String code,
            @Valid @RequestBody PointHistoryRequestDto request
    ) {
        PointCode pointCode = PointCode.findByCode(code);

        if (pointCode.equals(PointCode.USE)) {
            return pointCommandService.use(request);
        }
        return pointCommandService.save(request);
    }
}