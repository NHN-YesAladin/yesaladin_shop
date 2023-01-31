package shop.yesaladin.shop.file.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;

import java.io.FileInputStream;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;
import shop.yesaladin.shop.file.domain.model.File;
import shop.yesaladin.shop.file.dto.FileUploadResponseDto;
import shop.yesaladin.shop.file.service.inter.FileStorageService;
import shop.yesaladin.shop.product.dummy.DummyFile;

@AutoConfigureRestDocs
@WebMvcTest(FileStorageController.class)
class FileStorageControllerTest {

    private final String URL = "https://api-storage.cloud.toast.com/v1/AUTH_/container/domain/type";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileStorageService service;

    @WithMockUser
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
                .with(csrf())
                .content(multipartFile.getBytes()));

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("url", equalTo(URL + "/image.jpg")));

        verify(service, times(1)).fileUpload(anyString(), anyString(), any());

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