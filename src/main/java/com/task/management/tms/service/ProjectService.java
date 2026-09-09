package com.task.management.tms.service;

import com.task.management.tms.dto.ProjectCreateDto;
import com.task.management.tms.dto.ProjectResponseDto;

import java.util.List;

public interface ProjectService {

    ProjectResponseDto createProject(ProjectCreateDto dto);

    ProjectResponseDto getProjectById(Long id);

    List<ProjectResponseDto> getAllProjects();

    ProjectResponseDto updateProject(Long id, ProjectCreateDto dto);

    void deleteProjectById(Long id);
}
