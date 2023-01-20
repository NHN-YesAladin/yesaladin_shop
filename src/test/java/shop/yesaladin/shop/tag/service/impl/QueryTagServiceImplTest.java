package shop.yesaladin.shop.tag.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.shop.tag.domain.model.Tag;
import shop.yesaladin.shop.tag.domain.repository.QueryTagRepository;
import shop.yesaladin.shop.tag.dto.TagResponseDto;
import shop.yesaladin.shop.tag.dto.TagsResponseDto;
import shop.yesaladin.shop.tag.exception.TagNotFoundException;
import shop.yesaladin.shop.tag.service.inter.QueryTagService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class QueryTagServiceImplTest {

    private QueryTagService service;
    private QueryTagRepository queryTagRepository;

    @BeforeEach
    void setUp() {
        queryTagRepository = mock(QueryTagRepository.class);

        service = new QueryTagServiceImpl(
                queryTagRepository
        );
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
        assertThatThrownBy(() -> service.findById(id)).isInstanceOf(TagNotFoundException.class);
    }

    @Test
    @DisplayName("태그 이름으로 태그 조회_존재하는 이름인 경우")
    void findByName_success() {
        // given
        String name = "행복한";
        Tag tag = Tag.builder().name(name).build();

        when(queryTagRepository.findByName(name)).thenReturn(Optional.of(tag));

        // when
        TagResponseDto foundTag = service.findByName(name);

        // then
        assertThat(foundTag).isNotNull();
        assertThat(foundTag.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("태그 이름으로 태그 조회_존재하지 않는 경우")
    void findByName_notExists() {
        // given
        String name = "행복한";
        when(queryTagRepository.findByName(name)).thenReturn(Optional.ofNullable(null));

        // when
        TagResponseDto foundTag = service.findByName(name);

        // then
        assertThat(foundTag).isNull();
    }

    @Test
    @DisplayName("태그 전체 조회 성공")
    void findAll() {
        // given
        String name1 = "행복한";
        String name2 = "슬픈";

        List<Tag> tags = Arrays.asList(
                Tag.builder().name(name1).build(),
                Tag.builder().name(name2).build()
        );

        Mockito.when(queryTagRepository.findAll()).thenReturn(tags);

        // when
        List<TagsResponseDto> response = service.findAll();

        // then
        assertThat(response.size()).isEqualTo(2);
        assertThat(response.get(0).getName()).isEqualTo(name1);
        assertThat(response.get(1).getName()).isEqualTo(name2);
    }
}
