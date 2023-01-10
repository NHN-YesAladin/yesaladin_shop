package shop.yesaladin.shop.member.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import shop.yesaladin.shop.member.domain.model.MemberGrade;
import shop.yesaladin.shop.member.domain.repository.QueryMemberGradeRepository;

class QueryMemberGradeServiceImplTest {

    private QueryMemberGradeServiceImpl service;
    private QueryMemberGradeRepository repository;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(QueryMemberGradeRepository.class);
        service = new QueryMemberGradeServiceImpl(repository);
    }

    @Test
    void findById() throws Exception {
        //given
        int id = 1;
        String name = "화이트";
        long baseGivenPoint = 1000L;
        long baseOrderAmount = 100000L;

        MemberGrade expectedMemberGrade = Mockito.mock(MemberGrade.class);

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(expectedMemberGrade));
        Mockito.when(expectedMemberGrade.getName()).thenReturn(name);
        Mockito.when(expectedMemberGrade.getBaseGivenPoint()).thenReturn(baseGivenPoint);
        Mockito.when(expectedMemberGrade.getBaseOrderAmount()).thenReturn(baseOrderAmount);

        //when
        MemberGrade actualMemberGrade = service.findById(id);

        //then
        assertThat(actualMemberGrade).isEqualTo(expectedMemberGrade);
        assertThat(actualMemberGrade.getName()).isEqualTo(name);
        assertThat(actualMemberGrade.getBaseGivenPoint()).isEqualTo(baseGivenPoint);
        assertThat(actualMemberGrade.getBaseOrderAmount()).isEqualTo(baseOrderAmount);
    }
}
