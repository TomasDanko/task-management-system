package com.task.management.tms.service.impl;

import com.task.management.tms.dto.AuditLogResponseDto;
import com.task.management.tms.mapper.AuditLogMapper;
import com.task.management.tms.repository.AuditLogRepository;
import com.task.management.tms.service.AuditLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepository, AuditLogMapper auditLogMapper) {
        this.auditLogRepository = auditLogRepository;
        this.auditLogMapper = auditLogMapper;
    }

    @Override
    public List<AuditLogResponseDto> getAuditLogs(String identityType, Long identityId) {
        return auditLogRepository.findByIdentityTypeAndIdentityId(identityType, identityId)
                .stream()
                .map(auditLogMapper::toDto).toList();
    }

    @Override
    public List<AuditLogResponseDto> getAllAuditLogs() {
        return auditLogRepository.findAll()
                .stream()
                .map(auditLogMapper::toDto)
                .toList();
    }
}
