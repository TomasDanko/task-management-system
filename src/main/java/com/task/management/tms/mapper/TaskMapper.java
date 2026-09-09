package com.task.management.tms.mapper;

import com.task.management.tms.dto.TaskCreateDto;
import com.task.management.tms.dto.TaskResponseDto;
import com.task.management.tms.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task toEntity(TaskCreateDto dto);

    @Mapping(source = "assignedTo.username", target = "assignedTo")
    @Mapping(source = "project.id", target = "projectId")
    TaskResponseDto toDto(Task task);


}
