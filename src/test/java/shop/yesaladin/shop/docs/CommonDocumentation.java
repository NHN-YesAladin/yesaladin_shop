package shop.yesaladin.shop.docs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentRequest;
import static shop.yesaladin.shop.docs.ApiDocumentUtils.getDocumentResponse;

@WebMvcTest(DocsController.class)
@AutoConfigureRestDocs
@SuppressWarnings("all")
class CommonDocumentation {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    public void commonResponseFormat() throws Exception {
        ResultActions result = mockMvc.perform(get("/docs"));

        // docs
        result.andDo(document(
                "common-response-format",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                .description("요청 성공 여부"),
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                                .description("HTTP 응답 코드"),
                        fieldWithPath("data").type(JsonFieldType.VARIES).description("응답 본문"),
                        fieldWithPath("errorMessages").type(JsonFieldType.NULL)
                                .description("(요청 실패시) 오류 메시지 배열. (요청 성공시) null")
                )
        ));
    }
}
