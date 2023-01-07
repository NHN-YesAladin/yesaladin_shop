package shop.yesaladin.shop.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 카테고리 조회를 api를 통하여 동작하기 위한 rest controller
 *   id, name을 통한 단일 조회
 *   페이징, parent_id를 통한 복수 조회
 *
 * @author 배수한
 * @since 1.0
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/categories")
public class QueryCategoryController {

}
