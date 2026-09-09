package com.task.management.tms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.management.tms.controllers.AuditController;
import com.task.management.tms.dto.AuditLogResponseDto;
import com.task.management.tms.service.AuditLogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuditController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuditLogService auditLogService;


    @Test
    void getAllAuditLogs_shouldReturnAuditLogs() throws Exception {

        List<AuditLogResponseDto> auditLogs = List.of(
                new AuditLogResponseDto(
                        1L,
                        "TASK",
                        10L,
                        "CREATE",
                        LocalDateTime.now(),
                        "user1"
                ),
                new AuditLogResponseDto(
                        2L,
                        "TASK",
                        10L,
                        "STATUS_CHANGED",
                        LocalDateTime.now(),
                        "user2"
                )
        );

        when(auditLogService.getAllAuditLogs())
                .thenReturn(auditLogs);

        mockMvc.perform(get("/api/audit-logs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].identityType").value("TASK"))
                .andExpect(jsonPath("$[0].identityId").value(10))
                .andExpect(jsonPath("$[0].action").value("CREATE"))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].action").value("STATUS_CHANGED"));

        verify(auditLogService).getAllAuditLogs();
    }


    @Test
    void getAllAuditLogs_shouldReturnEmptyList() throws Exception {

        when(auditLogService.getAllAuditLogs())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/audit-logs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(auditLogService).getAllAuditLogs();
    }


    @Test
    void getAuditLogs_shouldReturnAuditLogsForTask() throws Exception {

        List<AuditLogResponseDto> auditLogs = List.of(
                new AuditLogResponseDto(
                        1L,
                        "TASK",
                        10L,
                        "CREATE",
                        LocalDateTime.now(),
                        "user1"
                ),
                new AuditLogResponseDto(
                        2L,
                        "TASK",
                        10L,
                        "UPDATE",
                        LocalDateTime.now(),
                        "user1"
                )
        );

        when(auditLogService.getAuditLogs("TASK", 10L))
                .thenReturn(auditLogs);

        mockMvc.perform(get("/api/audit-logs/TASK/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].identityType").value("TASK"))
                .andExpect(jsonPath("$[0].identityId").value(10))
                .andExpect(jsonPath("$[0].action").value("CREATE"))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].action").value("UPDATE"));

        verify(auditLogService).getAuditLogs("TASK", 10L);
    }


    @Test
    void getAuditLogs_shouldReturnEmptyList() throws Exception {

        when(auditLogService.getAuditLogs("TASK", 999L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/audit-logs/TASK/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(auditLogService).getAuditLogs("TASK", 999L);
    }


    @Test
    void getAuditLogs_shouldPassCorrectIdentityIdToService() throws Exception {

        when(auditLogService.getAuditLogs("TASK", 25L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/audit-logs/TASK/25"))
                .andExpect(status().isOk());

        verify(auditLogService).getAuditLogs("TASK", 25L);
    }


    @Test
    void getAuditLogs_shouldAcceptDifferentIdentityType() throws Exception {

        List<AuditLogResponseDto> auditLogs = List.of(
                new AuditLogResponseDto(
                        5L,
                        "COMMENT",
                        50L,
                        "CREATE",
                        LocalDateTime.now(),
                        "user5"
                )
        );

        when(auditLogService.getAuditLogs("COMMENT", 50L))
                .thenReturn(auditLogs);

        mockMvc.perform(get("/api/audit-logs/COMMENT/50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].identityType").value("COMMENT"))
                .andExpect(jsonPath("$[0].identityId").value(50))
                .andExpect(jsonPath("$[0].action").value("CREATE"));

        verify(auditLogService).getAuditLogs("COMMENT", 50L);
    }


    @Test
    void getAuditLogs_shouldAcceptProjectIdentityType() throws Exception {

        List<AuditLogResponseDto> auditLogs = List.of(
                new AuditLogResponseDto(
                        10L,
                        "PROJECT",
                        100L,
                        "UPDATE",
                        LocalDateTime.now(),
                        "manager"
                )
        );

        when(auditLogService.getAuditLogs("PROJECT", 100L))
                .thenReturn(auditLogs);

        mockMvc.perform(get("/api/audit-logs/PROJECT/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].identityType").value("PROJECT"))
                .andExpect(jsonPath("$[0].identityId").value(100))
                .andExpect(jsonPath("$[0].action").value("UPDATE"))
                .andExpect(jsonPath("$[0].username").value("manager"));

        verify(auditLogService).getAuditLogs("PROJECT", 100L);
    }
}


