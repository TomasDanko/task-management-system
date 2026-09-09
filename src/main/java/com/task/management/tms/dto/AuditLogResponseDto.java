package com.task.management.tms.dto;

import java.time.LocalDateTime;

public class AuditLogResponseDto {

    private Long id;

    private String identityType;

    private Long identityId;

    private String action;

    private LocalDateTime timestamp;

    private String username;

    public AuditLogResponseDto() {
    }

    public AuditLogResponseDto(Long id, String identityType, Long identityId,
                               String action, LocalDateTime timestamp,
                               String username) {
        this.id = id;
        this.identityType = identityType;
        this.identityId = identityId;
        this.action = action;
        this.timestamp = timestamp;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public Long getIdentityId() {
        return identityId;
    }

    public void setIdentityId(Long identityId) {
        this.identityId = identityId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}