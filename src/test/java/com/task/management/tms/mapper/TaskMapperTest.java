package com.task.management.tms.mapper;

import com.task.management.tms.dto.TaskCreateDto;
import com.task.management.tms.dto.TaskResponseDto;
import com.task.management.tms.entity.Project;
import com.task.management.tms.entity.Task;
import com.task.management.tms.entity.User;
import com.task.management.tms.enumerator.Priority;
import com.task.management.tms.enumerator.TaskStatus;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TaskMapperTest {

    private final TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);

    @Test
    void toEntity_shouldMapTaskCreateDtoToTask() {

        TaskCreateDto dto = new TaskCreateDto();
        dto.setTitle("Test task");
        dto.setDescription("Test description");
        dto.setStatus(TaskStatus.TODO);
        dto.setPriority(Priority.HIGH);
        dto.setProjectId(1L);

        Task task = taskMapper.toEntity(dto);

        assertNotNull(task);
        assertEquals("Test task", task.getTitle());
        assertEquals("Test description", task.getDescription());
        assertEquals(TaskStatus.TODO, task.getStatus());
        assertEquals(Priority.HIGH, task.getPriority());
    }

    @Test
    void toDto_shouldMapTaskToTaskResponseDto() {

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        Project project = new Project();
        project.setId(10L);
        project.setName("Test project");

        Task task = new Task();
        task.setId(100L);
        task.setTitle("Test task");
        task.setDescription("Test description");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setPriority(Priority.HIGH);
        task.setCreatedAt(LocalDateTime.now());
        task.setDueDate(LocalDateTime.now().plusDays(7));
        task.setAssignedTo(user);
        task.setProject(project);

        TaskResponseDto dto = taskMapper.toDto(task);

        assertNotNull(dto);
        assertEquals(100L, dto.getId());
        assertEquals("Test task", dto.getTitle());
        assertEquals("Test description", dto.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, dto.getStatus());
        assertEquals(Priority.HIGH, dto.getPriority());
        assertEquals("testuser", dto.getAssignedTo());
        assertEquals(10L, dto.getProjectId());
    }

    @Test
    void toEntity_shouldReturnNull_whenDtoIsNull() {

        Task task = taskMapper.toEntity(null);

        assertNull(task);
    }

    @Test
    void toDto_shouldReturnNull_whenTaskIsNull() {

        TaskResponseDto dto = taskMapper.toDto(null);

        assertNull(dto);
    }
}
