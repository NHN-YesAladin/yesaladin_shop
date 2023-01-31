package shop.yesaladin.shop.member.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.util.ReflectionUtils;
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
import shop.yesaladin.shop.member.dto.MemberAddressResponseDto;
import shop.yesaladin.shop.member.dto.MemberAddressCreateRequestDto;
import shop.yesaladin.shop.member.exception.AlreadyRegisteredUpToLimit;
import shop.yesaladin.shop.member.exception.AlreadyDeletedAddressException;
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
        String loginId = "user@1";

        //when
        ResultActions result = mockMvc.perform(post("/v1/members/{loginId}/addresses", loginId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", startsWith("Validation failed")));
    }

    @Test
    @DisplayName("회원 배송지 생성 실패-파라미터 오류")
    void createMemberAddress_failByValidationError() throws Exception {
        //given
        String loginId = "user@1";

        Map<String, Object> request = Map.of("address", "", "isDefault", false);

        //when
        ResultActions result = mockMvc.perform(post("/v1/members/{loginId}/addresses", loginId)
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
                pathParameters(parameterWithName("loginId").description("회원의 아이디")),
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
    @DisplayName("회원 배송지 생성 실패-존재하지 않는 회원")
    void createMemberAddress_failByNotFoundMember() throws Exception {
        //given
        String loginId = "user@1";

        Map<String, Object> request = Map.of("address", address, "isDefault", false);

        Mockito.when(commandMemberAddressService.save(eq(loginId), any())).thenThrow(
                new MemberNotFoundException("Member loginId: " + loginId));

        //when
        ResultActions result = mockMvc.perform(post("/v1/members/{loginId}/addresses", loginId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        //then
        result.andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", startsWith("Member not found")));

        ArgumentCaptor<MemberAddressCreateRequestDto> captor = ArgumentCaptor.forClass(
                MemberAddressCreateRequestDto.class);
        verify(commandMemberAddressService, times(1)).save(anyString(), captor.capture());

        assertThat(captor.getValue().getAddress()).isEqualTo(address);
        assertThat(captor.getValue().getIsDefault()).isEqualTo(isDefault);

        //docs
        result.andDo(document(
                "create-member-address-fail-not-found-member",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("loginId").description("회원의 아이디")),
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
    @DisplayName("회원 배송지 생성 실패-최대 배송지 등록 개수 초과")
    void createMemberAddress_failByAddressRegistrationRestriction() throws Exception {
        //given
        String loginId = "user@1";

        Map<String, Object> request = Map.of("address", address, "isDefault", false);

        Mockito.when(commandMemberAddressService.save(eq(loginId), any())).thenThrow(
                new AlreadyRegisteredUpToLimit(loginId));

        //when
        ResultActions result = mockMvc.perform(post("/v1/members/{loginId}/addresses", loginId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", startsWith("Already Registered Up To Limit")));

        ArgumentCaptor<MemberAddressCreateRequestDto> captor = ArgumentCaptor.forClass(
                MemberAddressCreateRequestDto.class);
        verify(commandMemberAddressService, times(1)).save(anyString(), captor.capture());

        assertThat(captor.getValue().getAddress()).isEqualTo(address);
        assertThat(captor.getValue().getIsDefault()).isEqualTo(isDefault);

        //docs
        result.andDo(document(
                "create-member-address-fail-registered-up-to-limit",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("loginId").description("회원의 아이디")),
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
    @DisplayName("회원 배송지 등록 성공")
    void createMemberAddress() throws Exception {
        //given
        String loginId = "user@1";
        long addressId = 1L;

        Map<String, Object> request = Map.of("address", address, "isDefault", isDefault);

        MemberAddressResponseDto response = ReflectionUtils.newInstance(
                MemberAddressResponseDto.class,
                addressId,
                address,
                isDefault,
                loginId
        );

        Mockito.when(commandMemberAddressService.save(eq(loginId), any())).thenReturn(response);

        //when
        ResultActions result = mockMvc.perform(post("/v1/members/{loginId}/addresses", loginId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo((int) addressId)))
                .andExpect(jsonPath("$.address", equalTo(address)))
                .andExpect(jsonPath("$.isDefault", equalTo(isDefault)))
                .andExpect(jsonPath("$.loginId", equalTo(loginId)));

        ArgumentCaptor<MemberAddressCreateRequestDto> captor = ArgumentCaptor.forClass(
                MemberAddressCreateRequestDto.class);

        verify(commandMemberAddressService, Mockito.times(1))
                .save(anyString(), captor.capture());

        MemberAddressCreateRequestDto actual = captor.getValue();
        assertThat(actual.getAddress()).isEqualTo(address);
        assertThat(actual.getIsDefault()).isEqualTo(isDefault);

        //docs
        result.andDo(document(
                "create-member-address-success",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(parameterWithName("loginId").description("회원의 아이디")),
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
                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("회원의 아이디")
                )
        ));
    }

    @Test
    @DisplayName("대표배송지 설정 실패-존재하지않는 배송지")
    void markAsDefaultAddress_fail_memberAddressNotFoundException() throws Exception {
        //given
        long addressId = 1L;
        String loginId = "user@1";

        Mockito.when(commandMemberAddressService.markAsDefault(loginId, addressId))
                .thenThrow(new MemberAddressNotFoundException(addressId));

        //when
        ResultActions result = mockMvc.perform(put(
                "/v1/members/{loginId}/addresses/{addressId}",
                loginId,
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
                        parameterWithName("loginId").description("회원의 아이디"),
                        parameterWithName("addressId").description("배송지 Pk")
                ),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                )
        ));
    }

    @Test
    @DisplayName("대표배송지 설정 성공")
    void markAsDefaultAddress_success() throws Exception {
        //given
        long addressId = 1L;
        String loginId = "user@1";

        MemberAddressResponseDto response = ReflectionUtils.newInstance(
                MemberAddressResponseDto.class,
                addressId,
                address,
                true,
                loginId
        );
        Mockito.when(commandMemberAddressService.markAsDefault(loginId, addressId))
                .thenReturn(response);

        //when
        ResultActions result = mockMvc.perform(put(
                "/v1/members/{loginId}/addresses/{addressId}",
                loginId,
                addressId
        ));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo((int) addressId)))
                .andExpect(jsonPath("$.address", equalTo(address)))
                .andExpect(jsonPath("$.isDefault", equalTo(true)))
                .andExpect(jsonPath("$.loginId", equalTo(loginId)));

        //docs
        result.andDo(document(
                "mark-as-default-address-success",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("loginId").description("회원의 아이디"),
                        parameterWithName("addressId").description("배송지 Pk")
                ),
                responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("등록된 배송지 Pk"),
                        fieldWithPath("address").type(JsonFieldType.STRING)
                                .description("등록된 배송지 주소"),
                        fieldWithPath("isDefault").type(JsonFieldType.BOOLEAN)
                                .description("등록된 배송지의 대표주소 여부"),
                        fieldWithPath("loginId").type(JsonFieldType.STRING).description("회원의 아이디")
                )
        ));
    }

    @Test
    @DisplayName("배송지 삭제 실패-존재하지않는 배송지")
    void deleteMemberAddress_fail_MemberAddressNotFound() throws Exception {
        //given
        long addressId = 1L;
        String loginId = "user@1";

        Mockito.when(commandMemberAddressService.delete(loginId, addressId))
                .thenThrow(new MemberAddressNotFoundException(addressId));
        //when
        ResultActions result = mockMvc.perform(delete(
                "/v1/members/{loginId}/addresses/{addressId}",
                loginId,
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
                        parameterWithName("loginId").description("회원의 아이디"),
                        parameterWithName("addressId").description("배송지 Pk")
                ),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                )
        ));
    }

    @Test
    @DisplayName("배송지 삭제 실패-이미 삭제된 배송지")
    void deleteMemberAddress_fail_AlreadyDeletedAddress() throws Exception {
        //given
        long addressId = 1L;
        String loginId = "user@1";

        Mockito.when(commandMemberAddressService.delete(loginId, addressId))
                .thenThrow(new AlreadyDeletedAddressException(addressId));
        //when
        ResultActions result = mockMvc.perform(delete(
                "/v1/members/{loginId}/addresses/{addressId}",
                loginId,
                addressId
        ));

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", startsWith("Already Deleted Address :")));

        //docs
        result.andDo(document(
                "delete-member-address-fail-already-deleted-address",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("loginId").description("회원의 아이디"),
                        parameterWithName("addressId").description("배송지 Pk")
                ),
                responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메세지")
                )
        ));
    }

    @Test
    @DisplayName("배송지 삭제 성공")
    void deleteMemberAddress() throws Exception {
        //given
        long addressId = 1L;
        String loginId = "user@1";

        //when
        ResultActions result = mockMvc.perform(delete(
                "/v1/members/{loginId}/addresses/{addressId}",
                loginId,
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
                        parameterWithName("loginId").description("회원의 아이디"),
                        parameterWithName("addressId").description("배송지 Pk")
                )
        ));
    }
}