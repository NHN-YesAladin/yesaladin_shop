package shop.yesaladin.shop.product.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import shop.yesaladin.shop.file.dto.TokenRequest;
import shop.yesaladin.shop.file.service.inter.StorageAuthService;
import shop.yesaladin.shop.product.domain.model.SearchedProduct;
import shop.yesaladin.shop.product.domain.model.SearchedProduct.SearchedAuthor;
import shop.yesaladin.shop.product.domain.model.SearchedProduct.SearchedCategories;
import shop.yesaladin.shop.product.domain.model.SearchedProduct.SearchedFile;
import shop.yesaladin.shop.product.domain.model.SearchedProduct.SearchedProductType;
import shop.yesaladin.shop.product.domain.model.SearchedProduct.SearchedTags;
import shop.yesaladin.shop.product.domain.model.SearchedProduct.SearchedTotalDiscountRate;
import shop.yesaladin.shop.product.domain.model.SearchedProduct.SubscribeProduct;
import shop.yesaladin.shop.product.domain.repository.SearchProductRepository;
import shop.yesaladin.shop.product.dto.SearchedProductResponseDto;

@SpringBootTest
class SearchProductServiceImplTest {

    SearchProductRepository searchProductRepository;
    SearchProductServiceImpl searchProductService;
    SearchedProductResponseDto searchedProductResponseDto;

    @BeforeEach
    void setUp() {
        searchProductRepository = Mockito.mock(SearchProductRepository.class);
        searchProductService = new SearchProductServiceImpl(searchProductRepository);
        searchedProductResponseDto = SearchedProductResponseDto.builder()
                .id(-1L)
                .title("title")
                .discountRate(10)
                .sellingPrice(1000L)
                .authors(List.of(new SearchedAuthor(1L, "author")))
                .isForcedOutOfStack(false)
                .thumbnailFileUrl(new SearchedFile(1L, "깃 허브.jpg", LocalDate.now()))
                .publishedDate(LocalDate.now().toString())
                .categories(List.of(new SearchedCategories(12L, null, "국내소설", true, false)))
                .tags(List.of(new SearchedTags(1L, "tag1")))
                .build();
    }


}