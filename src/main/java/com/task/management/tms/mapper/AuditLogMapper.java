package com.task.management.tms.mapper;

import com.task.management.tms.dto.AuditLogResponseDto;
import com.task.management.tms.entity.AuditLog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuditLogMapper {

    AuditLogResponseDto toDto(AuditLog log);
}
