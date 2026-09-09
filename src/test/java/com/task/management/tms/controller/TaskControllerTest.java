package com.task.management.tms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.management.tms.controllers.TaskController;
import com.task.management.tms.dto.TaskCreateDto;
import com.task.management.tms.dto.TaskResponseDto;
import com.task.management.tms.enumerator.Priority;
import com.task.management.tms.enumerator.TaskStatus;
import com.task.management.tms.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;


    @Test
    void createTask_shouldReturnCreated() throws Exception {

        TaskCreateDto dto = new TaskCreateDto();
        dto.setTitle("Test task");
        dto.setDescription("Test task description");
        dto.setPriority(Priority.HIGH);
        dto.setProjectId(1L);

        TaskResponseDto response = new TaskResponseDto();
        response.setId(1L);
        response.setTitle("Test task");
        response.setDescription("Test task description");
        response.setPriority(Priority.HIGH);
        response.setStatus(TaskStatus.TODO);
        response.setAssignedTo("testuser");
        response.setProjectId(1L);

        when(taskService.createTask(any(TaskCreateDto.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test task"))
                .andExpect(jsonPath("$.description").value("Test task description"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.status").value("TODO"))
                .andExpect(jsonPath("$.assignedTo").value("testuser"))
                .andExpect(jsonPath("$.projectId").value(1));

        verify(taskService).createTask(any(TaskCreateDto.class));
    }


    @Test
    void createTask_shouldReturnCreated_whenAssignedUserIsNotSet() throws Exception {

        TaskCreateDto dto = new TaskCreateDto();
        dto.setTitle("Unassigned task");
        dto.setDescription("Task without assigned user");
        dto.setPriority(Priority.MEDIUM);
        dto.setProjectId(2L);

        TaskResponseDto response = new TaskResponseDto();
        response.setId(2L);
        response.setTitle("Unassigned task");
        response.setDescription("Task without assigned user");
        response.setPriority(Priority.MEDIUM);
        response.setStatus(TaskStatus.TODO);
        response.setProjectId(2L);

        when(taskService.createTask(any(TaskCreateDto.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.title").value("Unassigned task"))
                .andExpect(jsonPath("$.assignedTo").doesNotExist())
                .andExpect(jsonPath("$.projectId").value(2));

        verify(taskService).createTask(any(TaskCreateDto.class));
    }


    @Test
    void getTaskById_shouldReturnTask() throws Exception {

        TaskResponseDto response = new TaskResponseDto();
        response.setId(1L);
        response.setTitle("Test task");
        response.setDescription("Test task description");
        response.setStatus(TaskStatus.TODO);
        response.setPriority(Priority.HIGH);
        response.setAssignedTo("testuser");
        response.setProjectId(1L);

        when(taskService.getTaskById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test task"))
                .andExpect(jsonPath("$.description").value("Test task description"))
                .andExpect(jsonPath("$.status").value("TODO"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.assignedTo").value("testuser"))
                .andExpect(jsonPath("$.projectId").value(1));

        verify(taskService).getTaskById(1L);
    }


    @Test
    void getTaskById_shouldReturnCorrectTaskForDifferentId() throws Exception {

        TaskResponseDto response = new TaskResponseDto();
        response.setId(25L);
        response.setTitle("Another task");
        response.setDescription("Another task description");
        response.setStatus(TaskStatus.IN_PROGRESS);
        response.setPriority(Priority.LOW);
        response.setProjectId(5L);

        when(taskService.getTaskById(25L))
                .thenReturn(response);

        mockMvc.perform(get("/api/tasks/25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(25))
                .andExpect(jsonPath("$.title").value("Another task"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.priority").value("LOW"))
                .andExpect(jsonPath("$.projectId").value(5));

        verify(taskService).getTaskById(25L);
    }


    @Test
    void updateTask_shouldReturnUpdatedTask() throws Exception {

        TaskCreateDto dto = new TaskCreateDto();
        dto.setTitle("Updated task");
        dto.setDescription("Updated task description");
        dto.setStatus(TaskStatus.IN_PROGRESS);
        dto.setPriority(Priority.MEDIUM);
        dto.setProjectId(1L);
        dto.setAssignedUserId(5L);

        TaskResponseDto response = new TaskResponseDto();
        response.setId(1L);
        response.setTitle("Updated task");
        response.setDescription("Updated task description");
        response.setStatus(TaskStatus.IN_PROGRESS);
        response.setPriority(Priority.MEDIUM);
        response.setAssignedTo("testuser");
        response.setProjectId(1L);

        when(taskService.updateTask(eq(1L), any(TaskCreateDto.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated task"))
                .andExpect(jsonPath("$.description").value("Updated task description"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.priority").value("MEDIUM"))
                .andExpect(jsonPath("$.assignedTo").value("testuser"))
                .andExpect(jsonPath("$.projectId").value(1));

        verify(taskService).updateTask(eq(1L), any(TaskCreateDto.class));
    }


    @Test
    void updateTask_shouldPassCorrectIdToService() throws Exception {

        TaskCreateDto dto = new TaskCreateDto();
        dto.setTitle("Updated task");
        dto.setDescription("Updated task description");
        dto.setStatus(TaskStatus.DONE);
        dto.setPriority(Priority.HIGH);
        dto.setProjectId(10L);

        TaskResponseDto response = new TaskResponseDto();
        response.setId(50L);
        response.setTitle("Updated task");
        response.setDescription("Updated task description");
        response.setStatus(TaskStatus.DONE);
        response.setPriority(Priority.HIGH);
        response.setProjectId(10L);

        when(taskService.updateTask(eq(50L), any(TaskCreateDto.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/tasks/50")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(50));

        verify(taskService).updateTask(eq(50L), any(TaskCreateDto.class));
    }


    @Test
    void deleteTask_shouldReturnNoContent() throws Exception {

        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());

        verify(taskService).deleteTask(1L);
    }


    @Test
    void deleteTask_shouldPassCorrectIdToService() throws Exception {

        doNothing().when(taskService).deleteTask(100L);

        mockMvc.perform(delete("/api/tasks/100"))
                .andExpect(status().isNoContent());

        verify(taskService).deleteTask(100L);
    }


    @Test
    void changeStatus_shouldReturnUpdatedTask() throws Exception {

        TaskResponseDto response = new TaskResponseDto();
        response.setId(1L);
        response.setTitle("Test task");
        response.setDescription("Test task description");
        response.setStatus(TaskStatus.DONE);
        response.setPriority(Priority.HIGH);
        response.setProjectId(1L);

        when(taskService.changeStatus(1L, TaskStatus.DONE))
                .thenReturn(response);

        mockMvc.perform(put("/api/tasks/1/status")
                        .param("taskStatus", "DONE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test task"))
                .andExpect(jsonPath("$.status").value("DONE"))
                .andExpect(jsonPath("$.priority").value("HIGH"));

        verify(taskService).changeStatus(1L, TaskStatus.DONE);
    }


    @Test
    void changeStatus_shouldSupportInProgressStatus() throws Exception {

        TaskResponseDto response = new TaskResponseDto();
        response.setId(1L);
        response.setTitle("Test task");
        response.setDescription("Test task description");
        response.setStatus(TaskStatus.IN_PROGRESS);
        response.setPriority(Priority.MEDIUM);
        response.setProjectId(1L);

        when(taskService.changeStatus(1L, TaskStatus.IN_PROGRESS))
                .thenReturn(response);

        mockMvc.perform(put("/api/tasks/1/status")
                        .param("taskStatus", "IN_PROGRESS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

        verify(taskService).changeStatus(1L, TaskStatus.IN_PROGRESS);
    }


    @Test
    void changeStatus_shouldSupportTodoStatus() throws Exception {

        TaskResponseDto response = new TaskResponseDto();
        response.setId(1L);
        response.setTitle("Test task");
        response.setDescription("Test task description");
        response.setStatus(TaskStatus.TODO);
        response.setPriority(Priority.LOW);
        response.setProjectId(1L);

        when(taskService.changeStatus(1L, TaskStatus.TODO))
                .thenReturn(response);

        mockMvc.perform(put("/api/tasks/1/status")
                        .param("taskStatus", "TODO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("TODO"));

        verify(taskService).changeStatus(1L, TaskStatus.TODO);
    }


    @Test
    void changeStatus_shouldReturnBadRequest_whenStatusIsInvalid() throws Exception {

        mockMvc.perform(put("/api/tasks/1/status")
                        .param("taskStatus", "INVALID"))
                .andExpect(status().isBadRequest());

        verify(taskService, never())
                .changeStatus(anyLong(), any(TaskStatus.class));
    }


    @Test
    void changeStatus_shouldReturnBadRequest_whenStatusIsMissing() throws Exception {

        mockMvc.perform(put("/api/tasks/1/status"))
                .andExpect(status().isBadRequest());

        verify(taskService, never())
                .changeStatus(anyLong(), any(TaskStatus.class));
    }


    @Test
    void getTaskByProject_shouldReturnTasks() throws Exception {

        TaskResponseDto task1 = new TaskResponseDto();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setDescription("Task 1 description");
        task1.setStatus(TaskStatus.TODO);
        task1.setPriority(Priority.HIGH);
        task1.setProjectId(10L);

        TaskResponseDto task2 = new TaskResponseDto();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setDescription("Task 2 description");
        task2.setStatus(TaskStatus.IN_PROGRESS);
        task2.setPriority(Priority.MEDIUM);
        task2.setProjectId(10L);

        when(taskService.getTasksByProject(10L))
                .thenReturn(List.of(task1, task2));

        mockMvc.perform(get("/api/tasks/project/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[0].status").value("TODO"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Task 2"))
                .andExpect(jsonPath("$[1].status").value("IN_PROGRESS"));

        verify(taskService).getTasksByProject(10L);
    }


    @Test
    void getTaskByProject_shouldReturnEmptyList() throws Exception {

        when(taskService.getTasksByProject(10L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/tasks/project/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(taskService).getTasksByProject(10L);
    }


    @Test
    void getTaskByUser_shouldReturnTasks() throws Exception {

        TaskResponseDto task = new TaskResponseDto();
        task.setId(1L);
        task.setTitle("User task");
        task.setDescription("User task description");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setPriority(Priority.HIGH);
        task.setAssignedTo("testuser");
        task.setProjectId(10L);

        when(taskService.getTaskByUser(5L))
                .thenReturn(List.of(task));

        mockMvc.perform(get("/api/tasks/user/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("User task"))
                .andExpect(jsonPath("$[0].status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$[0].assignedTo").value("testuser"));

        verify(taskService).getTaskByUser(5L);
    }


    @Test
    void getTaskByUser_shouldReturnEmptyList() throws Exception {

        when(taskService.getTaskByUser(5L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/tasks/user/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(taskService).getTaskByUser(5L);
    }
}


