package shop.yesaladin.shop.member.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.yesaladin.shop.member.domain.model.Member;
import shop.yesaladin.shop.member.domain.model.MemberAddress;
import shop.yesaladin.shop.member.dto.MemberAddressCreateRequestDto;
import shop.yesaladin.shop.member.dto.MemberAddressCreateResponseDto;
import shop.yesaladin.shop.member.dto.MemberAddressUpdateResponseDto;
import shop.yesaladin.shop.member.dummy.MemberDummy;
import shop.yesaladin.shop.member.exception.MemberAddressNotFoundException;
import shop.yesaladin.shop.member.exception.MemberNotFoundException;
import shop.yesaladin.shop.member.service.inter.CommandMemberAddressService;

@AutoConfigureRestDocs
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
    void createMemberAddress_failByValidationError_forParameterizedTest(Map<String, Object> request)
            throws Exception {
        //given
        Long memberId = 1L;

        Mockito.when(commandMemberAddressService.save(eq(memberId), any())).thenThrow(
                new MemberNotFoundException("MemberId: " + memberId));

        //when
        ResultActions result = mockMvc.perform(post("/v1/members/{memberId}/addresses", memberId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", startsWith("Validation failed")));
    }

    @Test
    void createMemberAddress_failByValidationError() throws Exception {
        //given
        Long memberId = 1L;

        Map<String, Object> request = Map.of("address", "", "isDefault", false);

        Mockito.when(commandMemberAddressService.save(eq(memberId), any())).thenThrow(
                new MemberNotFoundException("MemberId: " + memberId));

        //when
        ResultActions result = mockMvc.perform(post("/v1/members/{memberId}/addresses", memberId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", startsWith("Validation failed")));

        //docs
        result.andDo(document(
                "create-member-address-fail-validation-error",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("memberId").description("회원의 Pk")),
                requestFields(
                        fieldWithPath("address").type(JsonFieldType.STRING).description("등록할 주소"),
                        fieldWithPath("isDefault").type(JsonFieldType.BOOLEAN)
                                .description("대표 주소 여부")
                ),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                )
        ));
    }

    @Test
    void createMemberAddress() throws Exception {
        //given
        long memberId = 1L;
        long addressId = 1L;

        MemberAddress memberAddress = getMemberAddress(
                addressId,
                MemberDummy.dummyWithId(memberId)
        );

        Map<String, Object> request = Map.of("address", address, "isDefault", isDefault);

        Mockito.when(commandMemberAddressService.save(anyLong(), any())).thenReturn(
                MemberAddressCreateResponseDto.fromEntity(memberAddress));

        //when
        ResultActions result = mockMvc.perform(post("/v1/members/{memberId}/addresses", memberId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo((int) addressId)))
                .andExpect(jsonPath("$.address", equalTo(address)))
                .andExpect(jsonPath("$.isDefault", equalTo(isDefault)))
                .andExpect(jsonPath("$.member.id", equalTo((int) memberId)));

        ArgumentCaptor<MemberAddressCreateRequestDto> captor = ArgumentCaptor.forClass(
                MemberAddressCreateRequestDto.class);

        Mockito.verify(commandMemberAddressService, Mockito.times(1))
                .save(anyLong(), captor.capture());

        MemberAddressCreateRequestDto actual = captor.getValue();
        assertThat(actual.getAddress()).isEqualTo(address);
        assertThat(actual.getIsDefault()).isEqualTo(isDefault);

        //docs
        result.andDo(document(
                "create-member-address-fail-validation-error",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("memberId").description("회원의 Pk")),
                requestFields(
                        fieldWithPath("address").type(JsonFieldType.STRING).description("등록할 주소"),
                        fieldWithPath("isDefault").type(JsonFieldType.BOOLEAN)
                                .description("대표 주소 여부")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("등록된 배송지 Pk"),
                        fieldWithPath("address").type(JsonFieldType.STRING)
                                .description("등록된 배송지 주소"),
                        fieldWithPath("isDefault").type(JsonFieldType.BOOLEAN)
                                .description("등록된 배송지의 대표주소 여부"),
                        fieldWithPath("member.id").type(JsonFieldType.NUMBER).description("회원의 Pk"),
                        fieldWithPath("member.nickname").type(JsonFieldType.STRING)
                                .description("회원의 닉네임"),
                        fieldWithPath("member.name").type(JsonFieldType.STRING)
                                .description("회원의 이름"),
                        fieldWithPath("member.loginId").type(JsonFieldType.STRING)
                                .description("회원의 아이디"),
                        fieldWithPath("member.password").type(JsonFieldType.STRING)
                                .description("회원의 비밀번호"),
                        fieldWithPath("member.birthYear").type(JsonFieldType.NUMBER)
                                .description("회원의 생년"),
                        fieldWithPath("member.birthMonth").type(JsonFieldType.NUMBER)
                                .description("회원의 생월"),
                        fieldWithPath("member.birthDay").type(JsonFieldType.NUMBER)
                                .description("회원의 생일"),
                        fieldWithPath("member.email").type(JsonFieldType.STRING)
                                .description("회원의 이메일"),
                        fieldWithPath("member.phone").type(JsonFieldType.STRING)
                                .description("회원의 핸드폰번호"),
                        fieldWithPath("member.signUpDate").type(JsonFieldType.STRING)
                                .description("회원의 가입일"),
                        fieldWithPath("member.withdrawalDate").type(JsonFieldType.STRING)
                                .description("회원의 탈퇴일").optional(),
                        fieldWithPath("member.withdrawal").type(JsonFieldType.BOOLEAN)
                                .description("회원의 탈퇴여부"),
                        fieldWithPath("member.blocked").type(JsonFieldType.BOOLEAN)
                                .description("회원의 차단여부"),
                        fieldWithPath("member.memberGrade").type(JsonFieldType.STRING)
                                .description("회원의 등급"),
                        fieldWithPath("member.memberGenderCode").type(JsonFieldType.STRING)
                                .description("회원의 성별")
                )
        ));
    }

    @Test
    void markAsDefaultAddress_fail_memberAddressNotFoundException() throws Exception {
        //given
        long memberId = 1L;
        long addressId = 1L;

        Mockito.when(commandMemberAddressService.markAsDefault(memberId, addressId))
                .thenThrow(new MemberAddressNotFoundException(addressId));

        //when
        ResultActions result = mockMvc.perform(put(
                "/v1/members/{memberId}/addresses/{addressId}",
                memberId,
                addressId
        ));

        //then
        result.andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", startsWith("MemberAddress Not Found")));

        //docs
        result.andDo(document(
                "mark-as-default-address-fail-member-address-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("memberId").description("회원의 Pk"),
                        parameterWithName("addressId").description("배송지 Pk")
                ),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                )
        ));
    }

    @Test
    void markAsDefaultAddress_success() throws Exception {
        //given
        long memberId = 1L;
        long addressId = 1L;

        MemberAddress memberAddress = getMemberAddress(
                addressId,
                MemberDummy.dummyWithId(memberId)
        );

        Mockito.when(commandMemberAddressService.markAsDefault(memberId, addressId))
                .thenReturn(MemberAddressUpdateResponseDto.fromEntity(memberAddress));

        //when
        ResultActions result = mockMvc.perform(put(
                "/v1/members/{memberId}/addresses/{addressId}",
                memberId,
                addressId
        ));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.address", equalTo(memberAddress.getAddress())))
                .andExpect(jsonPath("$.member.id", equalTo((int) memberId)));

        //docs
        result.andDo(document(
                "mark-as-default-address-success",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("memberId").description("회원의 Pk"),
                        parameterWithName("addressId").description("배송지 Pk")
                ),
                responseFields(
                        fieldWithPath("address").type(JsonFieldType.STRING)
                                .description("대표 배송지 주소"),
                        fieldWithPath("member.id").type(JsonFieldType.NUMBER).description("회원의 Pk"),
                        fieldWithPath("member.nickname").type(JsonFieldType.STRING)
                                .description("회원의 닉네임"),
                        fieldWithPath("member.name").type(JsonFieldType.STRING)
                                .description("회원의 이름"),
                        fieldWithPath("member.loginId").type(JsonFieldType.STRING)
                                .description("회원의 아이디"),
                        fieldWithPath("member.password").type(JsonFieldType.STRING)
                                .description("회원의 비밀번호"),
                        fieldWithPath("member.birthYear").type(JsonFieldType.NUMBER)
                                .description("회원의 생년"),
                        fieldWithPath("member.birthMonth").type(JsonFieldType.NUMBER)
                                .description("회원의 생월"),
                        fieldWithPath("member.birthDay").type(JsonFieldType.NUMBER)
                                .description("회원의 생일"),
                        fieldWithPath("member.email").type(JsonFieldType.STRING)
                                .description("회원의 이메일"),
                        fieldWithPath("member.phone").type(JsonFieldType.STRING)
                                .description("회원의 핸드폰번호"),
                        fieldWithPath("member.signUpDate").type(JsonFieldType.STRING)
                                .description("회원의 가입일"),
                        fieldWithPath("member.withdrawalDate").type(JsonFieldType.STRING)
                                .description("회원의 탈퇴일").optional(),
                        fieldWithPath("member.withdrawal").type(JsonFieldType.BOOLEAN)
                                .description("회원의 탈퇴여부"),
                        fieldWithPath("member.blocked").type(JsonFieldType.BOOLEAN)
                                .description("회원의 차단여부"),
                        fieldWithPath("member.memberGrade").type(JsonFieldType.STRING)
                                .description("회원의 등급"),
                        fieldWithPath("member.memberGenderCode").type(JsonFieldType.STRING)
                                .description("회원의 성별")
                )
        ));
    }

    @Test
    void deleteMemberAddress_fail_MemberAddressNotFound() throws Exception {
        //given
        long memberId = 1L;
        long addressId = 1L;

        Mockito.when(commandMemberAddressService.delete(memberId, addressId))
                .thenThrow(new MemberAddressNotFoundException(addressId));
        //when
        ResultActions result = mockMvc.perform(delete(
                "/v1/members/{memberId}/addresses/{addressId}",
                memberId,
                addressId
        ));

        //then
        result.andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", startsWith("MemberAddress Not Found:")));

        //docs
        result.andDo(document(
                "delete-member-address-fail-member-address-not-found",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("memberId").description("회원의 Pk"),
                        parameterWithName("addressId").description("배송지 Pk")
                ),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                )
        ));
    }

    @Test
    void deleteMemberAddress() throws Exception {
        //given
        long memberId = 1L;
        long addressId = 1L;

        //when
        ResultActions result = mockMvc.perform(delete(
                "/v1/members/{memberId}/addresses/{addressId}",
                memberId,
                addressId
        ));
        //then
        result.andExpect(status().isOk());

        //docs
        result.andDo(document(
                "delete-member-address-success",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("memberId").description("회원의 Pk"),
                        parameterWithName("addressId").description("배송지 Pk")
                )
        ));
    }

    private MemberAddress getMemberAddress(long addressId, Member member) {
        return MemberAddress.builder()
                .id(addressId)
                .address(address)
                .isDefault(isDefault)
                .member(member)
                .build();
    }
}