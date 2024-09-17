package ru.neoflex.deal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.neoflex.deal.dto.StatementDtoFull;
import ru.neoflex.deal.service.AdminService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.neoflex.deal.enums.ChangeType.MANUAL;
import static ru.neoflex.deal.enums.Status.DOCUMENT_CREATED;

@WebMvcTest(controllers = AdminController.class)
class AdminControllerTest {

    @MockBean
    private AdminService adminService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    private String statementId = "6dd2ff79-5597-4c58-9a88-55ab84c9378d";

    @Test
    void saveStatementStatus() throws Exception {
        mvc.perform(put("/deal/admin/statement/{statementId}/status", statementId)
                        .param("status", String.valueOf(DOCUMENT_CREATED)))
                .andExpect(status().isOk());

        verify(adminService, times(1)).saveStatementStatus(statementId, DOCUMENT_CREATED, MANUAL);
    }

    @Test
    void findStatementById() throws Exception {
        when(adminService.findStatementById(statementId)).thenReturn(new StatementDtoFull());

        mvc.perform(get("/deal/admin/statement/{statementId}", statementId))
                .andExpect(status().isOk());

        verify(adminService, times(1)).findStatementById(statementId);
    }

    @Test
    void findAllStatements() throws Exception {
        when(adminService.findAllStatements()).thenReturn(List.of());

        mvc.perform(get("/deal/admin/statement")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(adminService, times(1)).findAllStatements();
    }
}
