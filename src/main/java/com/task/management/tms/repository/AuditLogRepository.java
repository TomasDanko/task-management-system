package com.task.management.tms.repository;

import com.task.management.tms.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByIdentityTypeAndIdentityId(String identityType, Long identityId);
}
