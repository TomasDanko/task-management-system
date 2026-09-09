package com.task.management.tms.controllers;

import com.task.management.tms.dto.AuditLogResponseDto;
import com.task.management.tms.service.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditController {

    private final AuditLogService auditLogService;

    public AuditController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public ResponseEntity<List<AuditLogResponseDto>> getAllAuditLogs(){
        List<AuditLogResponseDto> auditLogs = auditLogService.getAllAuditLogs();
        return ResponseEntity.ok(auditLogs);
    }

    @GetMapping("/{identityType}/{identityId}")
    public ResponseEntity<List<AuditLogResponseDto>> getAuditLogs(@PathVariable String identityType, @PathVariable Long identityId){
        List<AuditLogResponseDto> auditLogs = auditLogService.getAuditLogs(identityType, identityId);
        return ResponseEntity.ok(auditLogs);
    }

}
