package shop.yesaladin.shop.file.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.file.domain.repository.QueryFileRepository;
import shop.yesaladin.shop.file.dto.FileResponseDto;
import shop.yesaladin.shop.file.service.inter.QueryFileService;

class QueryFileServiceImplTest {

    private final String FILE_NAME = "UUID.png";

    private QueryFileService queryFileService;
    private QueryFileRepository queryFileRepository;

    @BeforeEach
    void setUp() {
        queryFileRepository = mock(QueryFileRepository.class);
        queryFileService = new QueryFileServiceImpl(queryFileRepository);
    }

    @Test
    void findByName() {
        // given
        LocalDateTime now = LocalDateTime.now();

        File file = File.builder()
                .name(FILE_NAME)
                .uploadDateTime(now)
                .build();

        when(queryFileRepository.findByName(FILE_NAME)).thenReturn(Optional.of(file));

        // when
        FileResponseDto foundFile = queryFileService.findByName(FILE_NAME);

        // then
        assertThat(foundFile).isNotNull();
        assertThat(foundFile.getName()).isEqualTo(FILE_NAME);
        assertThat(foundFile.getUploadDateTime()).isEqualTo(now);
    }
}
