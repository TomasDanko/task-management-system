package com.task.management.tms.service;

import com.task.management.tms.dto.AuditLogResponseDto;
import com.task.management.tms.entity.AuditLog;
import com.task.management.tms.mapper.AuditLogMapper;
import com.task.management.tms.repository.AuditLogRepository;
import com.task.management.tms.service.impl.AuditLogServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuditLogServiceTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private AuditLogMapper auditLogMapper;

    @InjectMocks
    private AuditLogServiceImpl auditLogService;


    @Test
    void getAuditLogs_shouldReturnAuditLogs() {

        String identityType = "TASK";
        Long identityId = 1L;

        AuditLog log1 = new AuditLog();
        AuditLog log2 = new AuditLog();

        AuditLogResponseDto responseDto1 =
                new AuditLogResponseDto();

        AuditLogResponseDto responseDto2 =
                new AuditLogResponseDto();

        when(auditLogRepository.findByIdentityTypeAndIdentityId(
                identityType, identityId))
                .thenReturn(List.of(log1, log2));

        when(auditLogMapper.toDto(log1))
                .thenReturn(responseDto1);

        when(auditLogMapper.toDto(log2))
                .thenReturn(responseDto2);

        List<AuditLogResponseDto> result =
                auditLogService.getAuditLogs(identityType, identityId);

        assertEquals(2, result.size());

        verify(auditLogRepository)
                .findByIdentityTypeAndIdentityId(identityType, identityId);
    }


    @Test
    void getAuditLogs_shouldReturnEmptyList_whenNoAuditLogsExist() {

        String identityType = "TASK";
        Long identityId = 1L;

        when(auditLogRepository.findByIdentityTypeAndIdentityId(
                identityType, identityId))
                .thenReturn(List.of());

        List<AuditLogResponseDto> result =
                auditLogService.getAuditLogs(identityType, identityId);

        assertEquals(0, result.size());

        verify(auditLogRepository)
                .findByIdentityTypeAndIdentityId(identityType, identityId);
    }


    @Test
    void getAllAuditLogs_shouldReturnAuditLogs() {

        AuditLog log1 = new AuditLog();
        AuditLog log2 = new AuditLog();

        AuditLogResponseDto responseDto1 =
                new AuditLogResponseDto();

        AuditLogResponseDto responseDto2 =
                new AuditLogResponseDto();

        when(auditLogRepository.findAll())
                .thenReturn(List.of(log1, log2));

        when(auditLogMapper.toDto(log1))
                .thenReturn(responseDto1);

        when(auditLogMapper.toDto(log2))
                .thenReturn(responseDto2);

        List<AuditLogResponseDto> result =
                auditLogService.getAllAuditLogs();

        assertEquals(2, result.size());

        verify(auditLogRepository).findAll();
    }


    @Test
    void getAllAuditLogs_shouldReturnEmptyList_whenNoAuditLogsExist() {

        when(auditLogRepository.findAll())
                .thenReturn(List.of());

        List<AuditLogResponseDto> result =
                auditLogService.getAllAuditLogs();

        assertEquals(0, result.size());

        verify(auditLogRepository).findAll();
    }
}
