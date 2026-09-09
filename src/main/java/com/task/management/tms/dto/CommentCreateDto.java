package com.task.management.tms.dto;

public class CommentCreateDto {

    private String content;

    private Long taskId;

    private Long authorId;

    public CommentCreateDto() {
    }

    public CommentCreateDto(String content, Long taskId, Long authorId) {
        this.content = content;
        this.taskId = taskId;
        this.authorId = authorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
}
