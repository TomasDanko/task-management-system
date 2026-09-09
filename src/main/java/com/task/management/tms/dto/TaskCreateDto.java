package com.task.management.tms.dto;

import com.task.management.tms.enumerator.Priority;
import com.task.management.tms.enumerator.TaskStatus;

public class TaskCreateDto {

    private String title;

    private String description;

    private TaskStatus status;

    private Priority priority;

    private Long projectId;

    private Long assignedUserId;

    public TaskCreateDto() {
    }

    public TaskCreateDto(String title, String description, TaskStatus status, Priority priority, Long projectId, Long assignedUserId) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.projectId = projectId;
        this.assignedUserId = assignedUserId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(Long assignedUserId) {
        this.assignedUserId = assignedUserId;
    }
}
