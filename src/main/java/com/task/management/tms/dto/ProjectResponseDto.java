package com.task.management.tms.dto;

public class ProjectResponseDto {

    private Long id;

    private String name;

    private String description;

    private String ownerUsername;

    public ProjectResponseDto() {
    }

    public ProjectResponseDto(Long id, String name, String description, String ownerUsername) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ownerUsername = ownerUsername;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }
}
