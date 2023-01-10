package shop.yesaladin.shop.member.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.member.dto.MemberAddressQueryDto;
import shop.yesaladin.shop.member.dummy.MemberDummy;
import shop.yesaladin.shop.member.service.inter.QueryMemberAddressService;

@WebMvcTest(QueryMemberAddressController.class)
class QueryMemberAddressControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    QueryMemberAddressService queryMemberAddressService;

    @Autowired
    ObjectMapper objectMapper;

    String address = "Gwang Ju";
    Boolean isDefault = false;
    Member member = MemberDummy.dummyWithId(1L);

    @Test
    void getMemberAddressByMemberId() throws Exception {
        Long memberId = 1L;

        Mockito.when(queryMemberAddressService.findByMemberId(memberId))
                .thenReturn(getMemberAddressList(10));

        mockMvc.perform(get("/v1/members/{memberId}/addresses", memberId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].member.id", equalTo(1)));
    }

    List<MemberAddressQueryDto> getMemberAddressList(int cnt) {
        List<MemberAddressQueryDto> memberAddressQueryList = new ArrayList<>();
        for (int i = 0; i < cnt; i++) {
            memberAddressQueryList.add(MemberAddressQueryDto.fromEntity(MemberAddress.builder()
                    .id((long) i + 1)
                    .address(address)
                    .isDefault(isDefault)
                    .member(member)
                    .build()));
        }
        return memberAddressQueryList;
    }
}