package com.task.management.tms.service;

import com.task.management.tms.dto.TaskCreateDto;
import com.task.management.tms.dto.TaskResponseDto;
import com.task.management.tms.enumerator.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface TaskService {

    TaskResponseDto createTask(TaskCreateDto dto);

    Page<TaskResponseDto> getAllTasks(TaskStatus status, Long projectId, Long userId, Pageable pageable);

    TaskResponseDto getTaskById(Long id);

    TaskResponseDto assignUser(Long taskId, Long userId);

    TaskResponseDto changeStatus(Long taskId, TaskStatus taskStatus);

    List<TaskResponseDto> getTasksByProject(Long projectId);

    List<TaskResponseDto> getTaskByUser(Long userId);

    TaskResponseDto updateTask(Long id, TaskCreateDto dto);

    void deleteTask(Long id);


}
