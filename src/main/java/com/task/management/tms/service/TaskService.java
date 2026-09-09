package com.task.management.tms.service;

import com.task.management.tms.dto.TaskCreateDto;
import com.task.management.tms.dto.TaskResponseDto;
import com.task.management.tms.enumerator.TaskStatus;

import java.util.List;

public interface TaskService {

    TaskResponseDto createTask(TaskCreateDto dto);

    List<TaskResponseDto> getAllTasks();

    TaskResponseDto getTaskById(Long id);

    TaskResponseDto assignUser(Long taskId, Long userId);

    TaskResponseDto changeStatus(Long taskId, TaskStatus taskStatus);

    List<TaskResponseDto> getTasksByProject(Long projectId);

    List<TaskResponseDto> getTaskByUser(Long userId);

    TaskResponseDto updateTask(Long id, TaskCreateDto dto);

    void deleteTask(Long id);


}
