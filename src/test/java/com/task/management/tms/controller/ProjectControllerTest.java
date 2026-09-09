package com.task.management.tms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.management.tms.controllers.ProjectController;
import com.task.management.tms.dto.ProjectCreateDto;
import com.task.management.tms.dto.ProjectResponseDto;
import com.task.management.tms.service.ProjectService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProjectService projectService;


    @Test
    void createProject_shouldReturnCreated() throws Exception {

        ProjectCreateDto dto = new ProjectCreateDto(
                "Test project",
                "Test project description",
                1L
        );

        ProjectResponseDto response = new ProjectResponseDto(
                1L,
                "Test project",
                "Test project description",
                "testuser"
        );

        when(projectService.createProject(any(ProjectCreateDto.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test project"))
                .andExpect(jsonPath("$.description").value("Test project description"))
                .andExpect(jsonPath("$.ownerUsername").value("testuser"));

        verify(projectService).createProject(any(ProjectCreateDto.class));
    }



    @Test
    void getAllProjects_shouldReturnProjects() throws Exception {

        ProjectResponseDto project1 = new ProjectResponseDto(
                1L,
                "Project 1",
                "Description 1",
                "user1"
        );

        ProjectResponseDto project2 = new ProjectResponseDto(
                2L,
                "Project 2",
                "Description 2",
                "user2"
        );

        when(projectService.getAllProjects())
                .thenReturn(List.of(project1, project2));

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Project 1"))
                .andExpect(jsonPath("$[0].ownerUsername").value("user1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Project 2"))
                .andExpect(jsonPath("$[1].ownerUsername").value("user2"));

        verify(projectService).getAllProjects();
    }


    @Test
    void getAllProjects_shouldReturnEmptyList() throws Exception {

        when(projectService.getAllProjects())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(projectService).getAllProjects();
    }


    @Test
    void getProjectById_shouldReturnProject() throws Exception {

        ProjectResponseDto response = new ProjectResponseDto(
                1L,
                "Test project",
                "Test project description",
                "testuser"
        );

        when(projectService.getProjectById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/projects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test project"))
                .andExpect(jsonPath("$.description").value("Test project description"))
                .andExpect(jsonPath("$.ownerUsername").value("testuser"));

        verify(projectService).getProjectById(1L);
    }


    @Test
    void getProjectById_shouldPassCorrectIdToService() throws Exception {

        ProjectResponseDto response = new ProjectResponseDto(
                5L,
                "Project 5",
                "Description",
                "user5"
        );

        when(projectService.getProjectById(5L))
                .thenReturn(response);

        mockMvc.perform(get("/api/projects/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("Project 5"));

        verify(projectService).getProjectById(5L);
    }

    @Test
    void updateProject_shouldReturnUpdatedProject() throws Exception {

        ProjectCreateDto dto = new ProjectCreateDto(
                "Updated project",
                "Updated description",
                2L
        );

        ProjectResponseDto response = new ProjectResponseDto(
                1L,
                "Updated project",
                "Updated description",
                "newowner"
        );

        when(projectService.updateProject(
                eq(1L),
                any(ProjectCreateDto.class)
        )).thenReturn(response);

        mockMvc.perform(put("/api/projects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated project"))
                .andExpect(jsonPath("$.description").value("Updated description"))
                .andExpect(jsonPath("$.ownerUsername").value("newowner"));

        verify(projectService).updateProject(
                eq(1L),
                any(ProjectCreateDto.class)
        );
    }


    @Test
    void updateProject_shouldPassCorrectIdToService() throws Exception {

        ProjectCreateDto dto = new ProjectCreateDto(
                "Updated project",
                "Updated description",
                3L
        );

        ProjectResponseDto response = new ProjectResponseDto(
                10L,
                "Updated project",
                "Updated description",
                "user3"
        );

        when(projectService.updateProject(eq(10L), any(ProjectCreateDto.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/projects/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));

        verify(projectService).updateProject(eq(10L), any(ProjectCreateDto.class));
    }


    @Test
    void deleteProject_shouldReturnNoContent() throws Exception {

        mockMvc.perform(delete("/api/projects/1"))
                .andExpect(status().isNoContent());

        verify(projectService).deleteProjectById(1L);
    }


    @Test
    void deleteProject_shouldPassCorrectIdToService() throws Exception {

        mockMvc.perform(delete("/api/projects/25"))
                .andExpect(status().isNoContent());

        verify(projectService).deleteProjectById(25L);
    }


    @Test
    void createProject_shouldAcceptDifferentOwnerId() throws Exception {

        ProjectCreateDto dto = new ProjectCreateDto(
                "Another project",
                "Another project description",
                99L
        );

        ProjectResponseDto response = new ProjectResponseDto(
                20L,
                "Another project",
                "Another project description",
                "anotheruser"
        );

        when(projectService.createProject(any(ProjectCreateDto.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(20))
                .andExpect(jsonPath("$.ownerUsername").value("anotheruser"));

        verify(projectService).createProject(any(ProjectCreateDto.class));
    }
}


