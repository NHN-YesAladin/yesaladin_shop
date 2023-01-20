package shop.yesaladin.shop.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import shop.yesaladin.shop.product.dto.ProductCreateDto;
import shop.yesaladin.shop.product.dto.ProductResponseDto;
import shop.yesaladin.shop.product.dummy.DummyProductCreateDto;
import shop.yesaladin.shop.product.service.inter.CommandProductService;

@WebMvcTest(controllers = CommandProductController.class)
class CommandProductControllerTest {

    private final Long ID = 1L;
    private final String ISBN = "00001-...";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommandProductService commandProductService;

    private ProductCreateDto productCreateDto;
    private ProductResponseDto productResponseDto;

    @BeforeEach
    void setUp() {
        productCreateDto = DummyProductCreateDto.dummy(ISBN);
        productResponseDto = new ProductResponseDto(ID);
    }

    @Disabled
    @Test
    void registerProduct() throws Exception {
        // given
//        given(commandProductService.create(any())).willReturn(productResponseDto);
//
//        // when
//        ResultActions perform = mockMvc.perform(post("/v1/products")
//                .content(objectMapper.writeValueAsString(productCreateDto))
//                .contentType(MediaType.APPLICATION_JSON));
//
//        // then
//        perform.andExpect(status().isCreated())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id", equalTo(productResponseDto.getId().intValue())))
//                .andDo(print());
    }
}
