package shop.yesaladin.shop.file.service.impl;

import org.junit.jupiter.api.BeforeEach;
import shop.yesaladin.shop.file.domain.repository.QueryFileRepository;
import shop.yesaladin.shop.file.service.inter.QueryFileService;

import static org.mockito.Mockito.mock;

class QueryFileServiceImplTest {

    private final String FILE_NAME = "UUID.png";

    private QueryFileService queryFileService;
    private QueryFileRepository queryFileRepository;

    @BeforeEach
    void setUp() {
        queryFileRepository = mock(QueryFileRepository.class);
        queryFileService = new QueryFileServiceImpl(queryFileRepository);
    }

}
