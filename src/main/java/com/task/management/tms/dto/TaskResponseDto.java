package com.task.management.tms.dto;

import com.task.management.tms.enumerator.Priority;
import com.task.management.tms.enumerator.TaskStatus;

public class TaskResponseDto {

    private Long id;

    private String title;

    private String description;

    private TaskStatus status;

    private Priority priority;

    private String assignedTo;

    private Long projectId;

    public TaskResponseDto() {
    }

    public TaskResponseDto(Long id, String title, String description, TaskStatus status, Priority priority, String assignedTo, Long projectId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.assignedTo = assignedTo;
        this.projectId = projectId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
