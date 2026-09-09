package com.task.management.tms.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id")
    private Long id;

    @Column(name = "identityType", nullable = false, length = 250)
    private String identityType;

    @Column(name = "identityId", nullable = false)
    private Long identityId;

    @Column(name = "action", nullable = false, length = 250)
    private String action; //CREATE, UPDATE, DELETE

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    public AuditLog() {
    }

    public AuditLog(Long id, String identityType, Long identityId, String action, LocalDateTime timestamp, String username) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditLog auditLog = (AuditLog) o;
        return Objects.equals(id, auditLog.id) && Objects.equals(identityType, auditLog.identityType) && Objects.equals(identityId, auditLog.identityId) && Objects.equals(action, auditLog.action) && Objects.equals(timestamp, auditLog.timestamp) && Objects.equals(username, auditLog.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identityType, identityId, action, timestamp, username);
    }
}
