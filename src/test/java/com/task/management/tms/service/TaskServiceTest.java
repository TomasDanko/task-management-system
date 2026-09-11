package com.task.management.tms.service;

import com.task.management.tms.dto.TaskCreateDto;
import com.task.management.tms.dto.TaskResponseDto;
import com.task.management.tms.entity.AuditLog;
import com.task.management.tms.entity.Project;
import com.task.management.tms.entity.Task;
import com.task.management.tms.entity.User;
import com.task.management.tms.enumerator.TaskStatus;
import com.task.management.tms.exception.ProjectNotFoundException;
import com.task.management.tms.exception.TaskNotFoundException;
import com.task.management.tms.exception.UserNotFoundException;
import com.task.management.tms.mapper.TaskMapper;
import com.task.management.tms.repository.AuditLogRepository;
import com.task.management.tms.repository.ProjectRepository;
import com.task.management.tms.repository.TaskRepository;
import com.task.management.tms.repository.UserRepository;
import com.task.management.tms.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;


import static org.mockito.ArgumentMatchers.argThat;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private TaskServiceImpl taskService;


    @Test
    void createTask_shouldCreateTask() {

        TaskCreateDto dto = new TaskCreateDto();
        dto.setProjectId(1L);

        Project project = new Project();
        project.setId(1L);

        Task task = new Task();
        Task savedTask = new Task();
        savedTask.setId(10L);

        TaskResponseDto responseDto = new TaskResponseDto();

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        when(taskMapper.toEntity(dto))
                .thenReturn(task);

        when(taskRepository.save(task))
                .thenReturn(savedTask);

        when(taskMapper.toDto(savedTask))
                .thenReturn(responseDto);

        TaskResponseDto result = taskService.createTask(dto);

        assertEquals(responseDto, result);
        assertEquals(project, task.getProject());
        assertEquals(TaskStatus.TODO, task.getStatus());

        verify(projectRepository).findById(1L);
        verify(taskMapper).toEntity(dto);
        verify(taskRepository).save(task);
        verify(taskMapper).toDto(savedTask);
        verify(auditLogRepository).save(org.mockito.ArgumentMatchers.any(AuditLog.class));
    }


    @Test
    void createTask_shouldThrowException_whenProjectDoesNotExist() {

        TaskCreateDto dto = new TaskCreateDto();
        dto.setProjectId(1L);

        when(projectRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ProjectNotFoundException.class,
                () -> taskService.createTask(dto)
        );

        verify(projectRepository).findById(1L);
    }


    @Test
    void getTaskById_shouldReturnTask() {

        Long id = 1L;

        Task task = new Task();
        TaskResponseDto responseDto = new TaskResponseDto();

        when(taskRepository.findById(id))
                .thenReturn(Optional.of(task));

        when(taskMapper.toDto(task))
                .thenReturn(responseDto);

        TaskResponseDto result = taskService.getTaskById(id);

        assertEquals(responseDto, result);

        verify(taskRepository).findById(id);
        verify(taskMapper).toDto(task);
    }

    @Test
    void getAllTasks_shouldReturnFilteredAndPagedTasks() {

        Pageable pageable = PageRequest.of(0, 2);

        Task task1 = new Task();
        Task task2 = new Task();

        TaskResponseDto responseDto1 = new TaskResponseDto();
        TaskResponseDto responseDto2 = new TaskResponseDto();

        Page<Task> taskPage =
                new PageImpl<>(List.of(task1, task2));

        when(taskRepository.findAll(
                any(Specification.class),
                eq(pageable)
        )).thenReturn(taskPage);

        when(taskMapper.toDto(task1))
                .thenReturn(responseDto1);

        when(taskMapper.toDto(task2))
                .thenReturn(responseDto2);

        Page<TaskResponseDto> result =
                taskService.getAllTasks(
                        TaskStatus.IN_PROGRESS,
                        1L,
                        2L,
                        pageable
                );

        assertEquals(2, result.getContent().size());
        assertEquals(0, result.getNumber());
        assertEquals(2, result.getSize());

        verify(taskRepository).findAll(
                any(Specification.class),
                eq(pageable)
        );
    }


    @Test
    void getTaskById_shouldThrowException_whenTaskDoesNotExist() {

        Long id = 1L;

        when(taskRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.getTaskById(id)
        );

        verify(taskRepository).findById(id);
    }


    @Test
    void assignUser_shouldAssignUserToTask() {

        Long taskId = 1L;
        Long userId = 2L;

        Task task = new Task();
        task.setId(taskId);

        User user = new User();
        user.setId(userId);

        Task updatedTask = new Task();
        updatedTask.setId(taskId);

        TaskResponseDto responseDto = new TaskResponseDto();

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(taskRepository.save(task))
                .thenReturn(updatedTask);

        when(taskMapper.toDto(updatedTask))
                .thenReturn(responseDto);

        TaskResponseDto result =
                taskService.assignUser(taskId, userId);

        assertEquals(responseDto, result);
        assertEquals(user, task.getAssignedTo());

        verify(taskRepository).findById(taskId);
        verify(userRepository).findById(userId);
        verify(taskRepository).save(task);
        verify(taskMapper).toDto(updatedTask);
        verify(auditLogRepository).save(
                org.mockito.ArgumentMatchers.any(AuditLog.class)
        );
    }


    @Test
    void assignUser_shouldThrowException_whenTaskDoesNotExist() {

        Long taskId = 1L;
        Long userId = 2L;

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.empty());

        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.assignUser(taskId, userId)
        );

        verify(taskRepository).findById(taskId);
    }


    @Test
    void assignUser_shouldThrowException_whenUserDoesNotExist() {

        Long taskId = 1L;
        Long userId = 2L;

        Task task = new Task();

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> taskService.assignUser(taskId, userId)
        );

        verify(taskRepository).findById(taskId);
        verify(userRepository).findById(userId);
    }


    @Test
    void changeStatus_shouldChangeTaskStatus() {

        Long taskId = 1L;

        Task task = new Task();
        task.setId(taskId);

        Task updatedTask = new Task();
        updatedTask.setId(taskId);

        TaskResponseDto responseDto = new TaskResponseDto();

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        when(taskRepository.save(task))
                .thenReturn(updatedTask);

        when(taskMapper.toDto(updatedTask))
                .thenReturn(responseDto);

        TaskResponseDto result =
                taskService.changeStatus(taskId, TaskStatus.IN_PROGRESS);

        assertEquals(responseDto, result);
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());

        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(task);
        verify(taskMapper).toDto(updatedTask);
        verify(auditLogRepository).save(
                org.mockito.ArgumentMatchers.any(AuditLog.class)
        );
    }


    @Test
    void changeStatus_shouldThrowException_whenTaskDoesNotExist() {

        Long taskId = 1L;

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.empty());

        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.changeStatus(
                        taskId,
                        TaskStatus.IN_PROGRESS
                )
        );

        verify(taskRepository).findById(taskId);
    }


    @Test
    void getTasksByProject_shouldReturnTasksForProject() {

        Long projectId = 1L;

        Project project = new Project();
        project.setId(projectId);

        Project otherProject = new Project();
        otherProject.setId(2L);

        Task task1 = new Task();
        task1.setProject(project);

        Task task2 = new Task();
        task2.setProject(otherProject);

        Task task3 = new Task();

        TaskResponseDto responseDto = new TaskResponseDto();

        when(taskRepository.findAll())
                .thenReturn(List.of(task1, task2, task3));

        when(taskMapper.toDto(task1))
                .thenReturn(responseDto);

        List<TaskResponseDto> result =
                taskService.getTasksByProject(projectId);

        assertEquals(1, result.size());
        assertEquals(responseDto, result.get(0));

        verify(taskRepository).findAll();
        verify(taskMapper).toDto(task1);
    }


    @Test
    void getTaskByUser_shouldReturnTasksForUser() {

        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        User otherUser = new User();
        otherUser.setId(2L);

        Task task1 = new Task();
        task1.setAssignedTo(user);

        Task task2 = new Task();
        task2.setAssignedTo(otherUser);

        Task task3 = new Task();

        TaskResponseDto responseDto = new TaskResponseDto();

        when(taskRepository.findAll())
                .thenReturn(List.of(task1, task2, task3));

        when(taskMapper.toDto(task1))
                .thenReturn(responseDto);

        List<TaskResponseDto> result =
                taskService.getTaskByUser(userId);

        assertEquals(1, result.size());
        assertEquals(responseDto, result.get(0));

        verify(taskRepository).findAll();
        verify(taskMapper).toDto(task1);
    }


    @Test
    void updateTask_shouldUpdateTask() {

        Long taskId = 1L;
        Long projectId = 2L;

        TaskCreateDto dto = new TaskCreateDto();
        dto.setProjectId(projectId);
        dto.setTitle("New title");
        dto.setDescription("New description");
        dto.setStatus(TaskStatus.IN_PROGRESS);

        Project project = new Project();
        project.setId(projectId);

        Task task = new Task();
        task.setId(taskId);

        Task updatedTask = new Task();
        updatedTask.setId(taskId);

        TaskResponseDto responseDto = new TaskResponseDto();

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        when(projectRepository.findById(projectId))
                .thenReturn(Optional.of(project));

        when(taskRepository.save(task))
                .thenReturn(updatedTask);

        when(taskMapper.toDto(updatedTask))
                .thenReturn(responseDto);

        TaskResponseDto result =
                taskService.updateTask(taskId, dto);

        assertEquals(responseDto, result);

        assertEquals("New title", task.getTitle());
        assertEquals("New description", task.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
        assertEquals(project, task.getProject());

        verify(taskRepository).findById(taskId);
        verify(projectRepository).findById(projectId);
        verify(taskRepository).save(task);
        verify(taskMapper).toDto(updatedTask);
        verify(auditLogRepository).save(
                org.mockito.ArgumentMatchers.any(AuditLog.class)
        );
    }


    @Test
    void updateTask_shouldThrowException_whenTaskDoesNotExist() {

        Long taskId = 1L;

        TaskCreateDto dto = new TaskCreateDto();

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.empty());

        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.updateTask(taskId, dto)
        );

        verify(taskRepository).findById(taskId);
    }


    @Test
    void updateTask_shouldThrowException_whenProjectDoesNotExist() {

        Long taskId = 1L;
        Long projectId = 2L;

        TaskCreateDto dto = new TaskCreateDto();
        dto.setProjectId(projectId);

        Task task = new Task();

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        when(projectRepository.findById(projectId))
                .thenReturn(Optional.empty());

        assertThrows(
                ProjectNotFoundException.class,
                () -> taskService.updateTask(taskId, dto)
        );

        verify(taskRepository).findById(taskId);
        verify(projectRepository).findById(projectId);
    }

    @Test
    void createTask_shouldSaveAuditWithAuthenticatedUser() {

        TaskCreateDto dto = new TaskCreateDto();
        dto.setProjectId(1L);

        Project project = new Project();
        project.setId(1L);

        Task task = new Task();

        Task savedTask = new Task();
        savedTask.setId(10L);

        TaskResponseDto responseDto = new TaskResponseDto();

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        when(taskMapper.toEntity(dto))
                .thenReturn(task);

        when(taskRepository.save(task))
                .thenReturn(savedTask);

        when(taskMapper.toDto(savedTask))
                .thenReturn(responseDto);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        "test@example.com",
                        null,
                        List.of()
                );

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        taskService.createTask(dto);

        verify(auditLogRepository).save(
                argThat(auditLog ->
                        auditLog.getIdentityType().equals("TASK")
                                && auditLog.getIdentityId().equals(10L)
                                && auditLog.getAction().equals("CREATE")
                                && auditLog.getUsername().equals("test@example.com")
                )
        );

        SecurityContextHolder.clearContext();
    }

    @Test
    void deleteTask_shouldDeleteTask() {

        Long taskId = 1L;

        Task task = new Task();
        task.setId(taskId);

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        taskService.deleteTask(taskId);

        verify(taskRepository).findById(taskId);
        verify(taskRepository).delete(task);
        verify(auditLogRepository).save(
                org.mockito.ArgumentMatchers.any(AuditLog.class)
        );
    }


    @Test
    void deleteTask_shouldThrowException_whenTaskDoesNotExist() {

        Long taskId = 1L;

        when(taskRepository.findById(taskId))
                .thenReturn(Optional.empty());

        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.deleteTask(taskId)
        );

        verify(taskRepository).findById(taskId);
    }
}