package shop.yesaladin.shop.member.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.member.dto.MemberAddressCreateRequestDto;
import shop.yesaladin.shop.member.dto.MemberAddressCreateResponseDto;
import shop.yesaladin.shop.member.dto.MemberAddressUpdateResponseDto;
import shop.yesaladin.shop.member.dummy.MemberDummy;
import shop.yesaladin.shop.member.service.inter.CommandMemberAddressService;

@WebMvcTest(CommandMemberAddressController.class)
class CommandMemberAddressControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CommandMemberAddressService commandMemberAddressService;

    @Autowired
    ObjectMapper objectMapper;

    String address = "Gwang Ju";
    Boolean isDefault = false;

    private static Stream<Map<String, Object>> createMemberAddressData() {
        return Stream.of(
                Map.of("address", "", "isDefault", true),
                Map.of("address", "  "),
                Map.of("isDefault", true),
                Map.of(
                        "address",
                        "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",
                        "isDefault",
                        false
                )
        );
    }

    @ParameterizedTest
    @MethodSource(value = "createMemberAddressData")
    void createMemberAddress_failByValidationError(Map<String, Object> request) throws Exception {
        //given
        Long memberId = 1L;

        //when
        ResultActions result = mockMvc.perform(post("/v1/members/{memberId}/addresses", memberId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        //then
        result.andExpect(status().isBadRequest());
    }

    @Test
    void createMemberAddress() throws Exception {
        Long memberId = 1L;

        MemberAddress memberAddress = getMemberAddress(MemberDummy.dummyWithId(memberId));

        Map<String, Object> request = Map.of("address", address, "isDefault", isDefault);

        Mockito.when(commandMemberAddressService.save(anyLong(), any())).thenReturn(
                MemberAddressCreateResponseDto.fromEntity(memberAddress));

        mockMvc.perform(post("/v1/members/{memberId}/addresses", memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        ArgumentCaptor<MemberAddressCreateRequestDto> captor = ArgumentCaptor.forClass(
                MemberAddressCreateRequestDto.class);

        Mockito.verify(commandMemberAddressService, Mockito.times(1))
                .save(anyLong(), captor.capture());

        MemberAddressCreateRequestDto actual = captor.getValue();
        assertThat(actual.getAddress()).isEqualTo(address);
        assertThat(actual.getIsDefault()).isEqualTo(isDefault);
    }

    @Test
    void markAsDefaultAddress() throws Exception {
        //given
        Long memberId = 1L;
        Long addressId = 1L;

        MemberAddress memberAddress = getMemberAddress(MemberDummy.dummyWithId(memberId));

        Mockito.when(commandMemberAddressService.markAsDefault(memberId, addressId))
                .thenReturn(MemberAddressUpdateResponseDto.fromEntity(memberAddress));

        //when
        mockMvc.perform(put("/v1/members/{memberId}/addresses/{addressId}", memberId, addressId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.address", equalTo(memberAddress.getAddress())))
                .andExpect(jsonPath("$.member.id", equalTo(1)));
    }

    @Test
    void deleteMemberAddress() throws Exception {
        //given
        Long memberId = 1L;
        Long addressId = 1L;

        MemberAddress memberAddress = getMemberAddress(MemberDummy.dummyWithId(memberId));

        //when
        mockMvc.perform(delete("/v1/members/{memberId}/addresses/{addressId}", memberId, addressId))
                .andExpect(status().isOk());
    }

    private MemberAddress getMemberAddress(Member member) {
        return MemberAddress.builder()
                .address(address)
                .isDefault(isDefault)
                .member(member)
                .build();
    }
}