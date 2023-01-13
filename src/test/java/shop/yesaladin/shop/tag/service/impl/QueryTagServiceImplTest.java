package shop.yesaladin.shop.tag.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shop.yesaladin.shop.tag.domain.model.Tag;
import shop.yesaladin.shop.tag.domain.repository.QueryTagRepository;
import shop.yesaladin.shop.tag.dto.TagResponseDto;
import shop.yesaladin.shop.tag.service.inter.QueryTagService;

class QueryTagServiceImplTest {

    private final String TAG_NAME = "슬픈";

    private QueryTagService queryTagService;
    private QueryTagRepository queryTagRepository;

    @BeforeEach
    void setUp() {
        queryTagRepository = mock(QueryTagRepository.class);
        queryTagService = new QueryTagServiceImpl(queryTagRepository);
    }

    @Test
    void findByName() {
        // given
        Tag tag = Tag.builder().name(TAG_NAME).build();

        when(queryTagRepository.findByName(TAG_NAME)).thenReturn(Optional.of(tag));

        // when
        TagResponseDto foundTag = queryTagService.findByName(TAG_NAME);

        // then
        assertThat(foundTag).isNotNull();
        assertThat(foundTag.getName()).isEqualTo(TAG_NAME);
    }
}
