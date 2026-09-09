package com.task.management.tms.service;

import com.task.management.tms.dto.AuditLogResponseDto;

import java.util.List;

public interface AuditLogService {

    List<AuditLogResponseDto> getAuditLogs(String identityType, Long identityId);

    List<AuditLogResponseDto> getAllAuditLogs();
}
