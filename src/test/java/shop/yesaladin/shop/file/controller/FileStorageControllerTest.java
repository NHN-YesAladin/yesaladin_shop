package shop.yesaladin.shop.file.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.json.Json;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.file.dto.FileUploadResponseDto;
import shop.yesaladin.shop.file.service.inter.FileStorageService;
import shop.yesaladin.shop.product.dummy.DummyFile;
import shop.yesaladin.shop.publish.controller.QueryPublisherController;

import java.io.FileInputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;
@AutoConfigureRestDocs
@WebMvcTest(FileStorageController.class)
class FileStorageControllerTest {

    private final String URL = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileStorageService service;

    @Test
    @DisplayName("파일 업로드")
    void fileUpload() throws Exception {
        // given
        MultipartFile multipartFile = new MockMultipartFile("image", new FileInputStream("src/test/resources/img/yesaladinnotfound.png"));

        File file = DummyFile.dummy(URL + "/image.jpg");
        Mockito.when(service.fileUpload(anyString(), anyString(), any()))
                .thenReturn(new FileUploadResponseDto(file.getUrl(), file.getUploadDateTime().toString()));

        // when
        ResultActions result = mockMvc.perform(post("/v1/files/file-upload/{domainName}/{typeName}", "domain", "type")
                .content(multipartFile.getBytes()));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("url", equalTo(URL + "/image.jpg")));

        // docs
        result.andDo(document(
                "file-upload",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("domainName").description("업로드할 파일의 도메인"),
                        parameterWithName("typeName").description("업로드할 파일의 유형")
                ),
                responseFields(
                        fieldWithPath("url").type(JsonFieldType.STRING).description("업로드한 파일의 URL"),
                        fieldWithPath("fileUploadDateTime").type(JsonFieldType.STRING).description("업로드한 파일의 업로드 시간")
                )
        ));
    }
}