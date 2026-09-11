package com.task.management.tms.dto;

import java.time.LocalDateTime;

public class AuditLogResponseDto {

    private Long id;

    private String identityType;

    private Long identityId;

    private String action;

    private LocalDateTime createdAt;

    private String username;

    public AuditLogResponseDto() {
    }

    public AuditLogResponseDto(Long id, String identityType, Long identityId,
                               String action, LocalDateTime createdAt,
                               String username) {
        this.id = id;
        this.identityType = identityType;
        this.identityId = identityId;
        this.action = action;
        this.createdAt = createdAt;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}