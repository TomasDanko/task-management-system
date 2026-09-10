package com.task.management.tms.repository;

import com.task.management.tms.entity.AuditLog;
import com.task.management.tms.enumerator.AuditAction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AuditLogRepositoryTest {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Test
    void findByIdentityTypeAndIdentityId_shouldReturnAuditLogs() {

        AuditLog log1 = new AuditLog();
        log1.setIdentityType("TASK");
        log1.setIdentityId(1L);
        log1.setAction(AuditAction.CREATE.name());
        log1.setCreatedAt(LocalDateTime.now());
        log1.setUsername("system");

        AuditLog log2 = new AuditLog();
        log2.setIdentityType("TASK");
        log2.setIdentityId(1L);
        log2.setAction(AuditAction.UPDATE.name());
        log2.setCreatedAt(LocalDateTime.now());
        log2.setUsername("system");

        auditLogRepository.save(log1);
        auditLogRepository.save(log2);

        List<AuditLog> result =
                auditLogRepository.findByIdentityTypeAndIdentityId("TASK", 1L);

        assertEquals(2, result.size());
        assertEquals("TASK", result.get(0).getIdentityType());
        assertEquals(1L, result.get(0).getIdentityId());
        assertEquals("CREATE", result.get(0).getAction());

        assertEquals("TASK", result.get(1).getIdentityType());
        assertEquals(1L, result.get(1).getIdentityId());
        assertEquals("UPDATE", result.get(1).getAction());
    }

    @Test
    void findByIdentityTypeAndIdentityId_shouldReturnEmpty_whenNoAuditLogExists() {

        List<AuditLog> result =
                auditLogRepository.findByIdentityTypeAndIdentityId("TASK", 999L);

        assertTrue(result.isEmpty());
    }
}