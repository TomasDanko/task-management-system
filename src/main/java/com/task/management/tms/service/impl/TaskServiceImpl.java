package com.task.management.tms.service.impl;

import com.task.management.tms.dto.TaskCreateDto;
import com.task.management.tms.dto.TaskResponseDto;
import com.task.management.tms.entity.AuditLog;
import com.task.management.tms.entity.Project;
import com.task.management.tms.entity.Task;
import com.task.management.tms.entity.User;
import com.task.management.tms.enumerator.AuditAction;
import com.task.management.tms.enumerator.TaskStatus;
import com.task.management.tms.exception.ProjectNotFoundException;
import com.task.management.tms.exception.TaskNotFoundException;
import com.task.management.tms.exception.UserNotFoundException;
import com.task.management.tms.mapper.TaskMapper;
import com.task.management.tms.repository.AuditLogRepository;
import com.task.management.tms.repository.ProjectRepository;
import com.task.management.tms.repository.TaskRepository;
import com.task.management.tms.repository.UserRepository;
import com.task.management.tms.service.TaskService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskMapper taskMapper;
    private final AuditLogRepository auditLogRepository;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository, ProjectRepository projectRepository, TaskMapper taskMapper, AuditLogRepository auditLogRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.taskMapper = taskMapper;
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public TaskResponseDto createTask(TaskCreateDto dto) {

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        Task task = taskMapper.toEntity(dto);

        task.setProject(project);
        task.setStatus(TaskStatus.TODO);
        task.setCreatedAt(LocalDateTime.now());
        task.setDueDate(LocalDateTime.now().plusDays(7));

        Task saved = taskRepository.save(task);

        saveAudit(AuditAction.CREATE, "TASK", saved.getId());

        return taskMapper.toDto(saved);
    }

    @Override
    public List<TaskResponseDto> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Override
    public TaskResponseDto getTaskById(Long id) {

        Task task = getTask(id);
        return taskMapper.toDto(task);
    }

    @Override
    public TaskResponseDto assignUser(Long taskId, Long userId) {

        Task task = getTask(taskId);

        User user = getUser(userId);

        task.setAssignedTo(user);

        Task updatedTask = taskRepository.save(task);

        saveAudit(AuditAction.ASSIGN_USER, "TASK", updatedTask.getId());

        return taskMapper.toDto(updatedTask);
    }

    @Override
    public TaskResponseDto changeStatus(Long taskId, TaskStatus taskStatus) {

        Task task = getTask(taskId);

        task.setStatus(taskStatus);

        Task updatedTask = taskRepository.save(task);

        saveAudit(AuditAction.STATUS_CHANGED, "TASK", updatedTask.getId());

        return taskMapper.toDto(updatedTask);
    }

    @Override
    public List<TaskResponseDto> getTasksByProject(Long projectId) {
        return taskRepository.findAll()
                .stream()
                .filter(task -> task.getProject() != null)
                .filter(task -> task.getProject().getId().equals(projectId))
                .map(taskMapper::toDto)
                .toList();
    }

    @Override
    public List<TaskResponseDto> getTaskByUser(Long userId) {
        return taskRepository.findAll()
                .stream()
                .filter(task -> task.getAssignedTo() != null)
                .filter(task -> task.getAssignedTo().getId().equals(userId))
                .map(taskMapper::toDto)
                .toList();
    }

    @Override
    public TaskResponseDto updateTask(Long id, TaskCreateDto dto) {
        Task task = getTask(id);
        Project project = projectRepository.findById(dto.getProjectId()).orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        task.setStatus(dto.getStatus());
        task.setProject(project);
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setPriority(dto.getPriority());

        Task updatedTask = taskRepository.save(task);

        saveAudit(AuditAction.UPDATE, "TASK", updatedTask.getId());
        return taskMapper.toDto(updatedTask);
    }

    @Override
    public void deleteTask(Long id) {
        Task task = getTask(id);
        taskRepository.delete(task);
        saveAudit(AuditAction.DELETE, "TASK", id);

    }

    private void saveAudit(AuditAction action,
                           String identityType,
                           Long identityId) {

        AuditLog log = new AuditLog();

        log.setIdentityType(identityType);
        log.setIdentityId(identityId);
        log.setAction(action.name());
        log.setTimestamp(LocalDateTime.now());

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            log.setUsername(authentication.getName());
        } else {
            log.setUsername("system");
        }

        auditLogRepository.save(log);
    }
    private Task getTask(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found"));
    }

    private User getUser(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
