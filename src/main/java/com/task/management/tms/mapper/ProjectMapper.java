package com.task.management.tms.mapper;

import com.task.management.tms.dto.ProjectCreateDto;
import com.task.management.tms.dto.ProjectResponseDto;
import com.task.management.tms.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    Project toEntity(ProjectCreateDto dto);

    @Mapping(source = "owner.username", target = "ownerUsername")
    ProjectResponseDto toDto(Project project);
}
