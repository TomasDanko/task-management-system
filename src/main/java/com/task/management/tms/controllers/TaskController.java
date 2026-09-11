package com.task.management.tms.controllers;

import com.task.management.tms.dto.TaskCreateDto;
import com.task.management.tms.dto.TaskResponseDto;
import com.task.management.tms.enumerator.TaskStatus;
import com.task.management.tms.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody TaskCreateDto dto) {
        TaskResponseDto createdTask = taskService.createTask(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponseDto>> getAllTask(@RequestParam(required = false) TaskStatus status,
                                                            @RequestParam(required = false) Long projectId,
                                                            @RequestParam(required = false) Long userId, Pageable pageable) {

        Page<TaskResponseDto> tasks = taskService.getAllTasks(status, projectId, userId, pageable);

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long id) {
        TaskResponseDto task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable Long id, @Valid @RequestBody TaskCreateDto dto) {
        TaskResponseDto updatedTask = taskService.updateTask(id, dto);
        return ResponseEntity.ok(updatedTask);
    }

    @PutMapping("/{taskId}/assign/{userId}")
    public ResponseEntity<TaskResponseDto> assignUser(@PathVariable Long taskId, @PathVariable Long userId) {
        TaskResponseDto task = taskService.assignUser(taskId, userId);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{taskId}/status")
    public ResponseEntity<TaskResponseDto> changeStatus(@PathVariable Long taskId, @RequestParam TaskStatus taskStatus) {
        TaskResponseDto task = taskService.changeStatus(taskId, taskStatus);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TaskResponseDto>> getTaskByProject(@PathVariable Long projectId) {
        List<TaskResponseDto> tasks = taskService.getTasksByProject(projectId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskResponseDto>> getTaskByUser(@PathVariable Long userId) {
        List<TaskResponseDto> tasks = taskService.getTaskByUser(userId);
        return ResponseEntity.ok(tasks);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskResponseDto> deleteTask(@Valid @PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }


}
