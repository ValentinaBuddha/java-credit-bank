package ru.neoflex.deal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.neoflex.deal.service.DocumentService;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DocumentController.class)
class DocumentControllerTest {

    @MockBean
    private DocumentService documentService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    private String statementId = "6dd2ff79-5597-4c58-9a88-55ab84c9378d";

    @Test
    void testSendDocuments() throws Exception {
        mvc.perform(post("/deal/document/{statementId}/send", statementId))
                .andExpect(status().isOk());

        verify(documentService, times(1)).sendDocuments(statementId);
    }

    @Test
    void testSignDocuments() throws Exception {
        mvc.perform(post("/deal/document/{statementId}/sign", statementId))
                .andExpect(status().isOk());

        verify(documentService, times(1)).signDocuments(statementId);
    }

    @Test
    void testVerifySesCode() throws Exception {
        mvc.perform(post("/deal/document/{statementId}/code", statementId)
                        .content(mapper.writeValueAsString(123456))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(documentService, times(1)).verifySesCode(statementId, "123456");
    }
}