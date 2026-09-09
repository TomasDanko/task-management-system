package com.task.management.tms.mapper;

import com.task.management.tms.dto.AuditLogResponseDto;
import com.task.management.tms.entity.AuditLog;
import com.task.management.tms.enumerator.AuditAction;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AuditLogMapperTest {

    private final AuditLogMapper auditLogMapper = Mappers.getMapper(AuditLogMapper.class);

    @Test
    void toDto_shouldMapAuditLogToAuditLogResponseDto() {

        LocalDateTime timestamp = LocalDateTime.now();

        AuditLog log = new AuditLog();
        log.setId(1L);
        log.setIdentityType("TASK");
        log.setIdentityId(10L);
        log.setAction(AuditAction.CREATE.name());
        log.setTimestamp(timestamp);
        log.setUsername("testuser");

        AuditLogResponseDto dto = auditLogMapper.toDto(log);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("TASK", dto.getIdentityType());
        assertEquals(10L, dto.getIdentityId());
        assertEquals(AuditAction.CREATE.name(), dto.getAction());
        assertEquals(timestamp, dto.getTimestamp());
        assertEquals("testuser", dto.getUsername());
    }

    @Test
    void toDto_shouldReturnNull_whenAuditLogIsNull() {

        AuditLogResponseDto dto = auditLogMapper.toDto(null);

        assertNull(dto);
    }
}