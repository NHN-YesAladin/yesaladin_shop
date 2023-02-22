package shop.yesaladin.shop.tag.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.common.dto.PaginatedResponseDto;
import shop.yesaladin.shop.tag.domain.model.Tag;
import shop.yesaladin.shop.tag.domain.repository.QueryTagRepository;
import shop.yesaladin.shop.tag.dto.TagResponseDto;
import shop.yesaladin.shop.tag.service.inter.QueryTagService;

class QueryTagServiceImplTest {

    private QueryTagService service;
    private QueryTagRepository queryTagRepository;

    @BeforeEach
    void setUp() {
        queryTagRepository = mock(QueryTagRepository.class);

        service = new QueryTagServiceImpl(queryTagRepository);
    }

    @Test
    @DisplayName("ID로 태그 조회 성공")
    void findById_success() {
        // given
        Long id = 1L;
        String name = "행복한";
        Tag tag = Tag.builder().name(name).build();

        Mockito.when(queryTagRepository.findById(id)).thenReturn(Optional.ofNullable(tag));

        // when
        TagResponseDto response = service.findById(id);

        // then
        assertThat(response.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("ID로 태그 조회 실패_존재하지 않는 ID로 태그를 조회하려 하는 경우 예외 발생")
    void findById_notFoundId_throwTagNotFoundException() {
        // given
        Long id = 1L;
        Mockito.when(queryTagRepository.findById(id)).thenReturn(Optional.ofNullable(null));

        // when then
        assertThatThrownBy(() -> service.findById(id)).isInstanceOf(ClientException.class);
    }

    @Test
    @DisplayName("태그 관리자용 전체 조회 성공")
    void findAllForManager() {
        // given
        List<Tag> tags = new ArrayList<>();
        for (long i = 1L; i <= 10L; i++) {
            tags.add(Tag.builder().id(i).name("태그" + i).build());
        }

        Page<Tag> page = new PageImpl<>(
                tags,
                PageRequest.of(0, 5),
                tags.size()
        );

        Mockito.when(queryTagRepository.findAllForManager(any())).thenReturn(page);

        // when
        PaginatedResponseDto<TagResponseDto> response = service.findAllForManager(PageRequest.of(
                0,
                5
        ));

        // then
        assertThat(response.getTotalDataCount()).isEqualTo(10);
        assertThat(response.getDataList().get(0).getId()).isEqualTo(1L);
        assertThat(response.getDataList().get(9).getId()).isEqualTo(10L);
    }

    @Test
    @DisplayName("관리자가 태그 이름으로 검색")
    void findByNameForManager() {
        Tag tag = Tag.builder().id(1L).name("tag").build();

        Mockito.when(queryTagRepository.findByNameForManager("name", PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(List.of(tag), PageRequest.of(0, 1), 1));

        PaginatedResponseDto<TagResponseDto> response = service.findByNameForManager("name", PageRequest.of(0, 10));

        assertThat(response.getTotalDataCount()).isEqualTo(1);
        assertThat(response.getDataList().get(0).getName()).contains("tag");
    }
}
