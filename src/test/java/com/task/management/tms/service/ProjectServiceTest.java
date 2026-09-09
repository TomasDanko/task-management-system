package com.task.management.tms.service;

import com.task.management.tms.dto.ProjectCreateDto;
import com.task.management.tms.dto.ProjectResponseDto;
import com.task.management.tms.entity.Project;
import com.task.management.tms.entity.User;
import com.task.management.tms.exception.OwnerNotFoundException;
import com.task.management.tms.exception.ProjectNotFoundException;
import com.task.management.tms.mapper.ProjectMapper;
import com.task.management.tms.repository.ProjectRepository;
import com.task.management.tms.repository.UserRepository;
import com.task.management.tms.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMapper projectMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;


    @Test
    void createProject_shouldCreateProject() {

        ProjectCreateDto dto = new ProjectCreateDto();
        dto.setOwnerId(1L);

        User owner = new User();
        Project project = new Project();
        Project savedProject = new Project();
        ProjectResponseDto responseDto = new ProjectResponseDto();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(owner));

        when(projectMapper.toEntity(dto))
                .thenReturn(project);

        when(projectRepository.save(project))
                .thenReturn(savedProject);

        when(projectMapper.toDto(savedProject))
                .thenReturn(responseDto);

        ProjectResponseDto result =
                projectService.createProject(dto);

        assertEquals(responseDto, result);
        assertEquals(owner, project.getOwner());

        verify(userRepository).findById(1L);
        verify(projectMapper).toEntity(dto);
        verify(projectRepository).save(project);
        verify(projectMapper).toDto(savedProject);
    }


    @Test
    void createProject_shouldThrowException_whenOwnerDoesNotExist() {

        ProjectCreateDto dto = new ProjectCreateDto();
        dto.setOwnerId(1L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                OwnerNotFoundException.class,
                () -> projectService.createProject(dto)
        );

        verify(userRepository).findById(1L);
    }


    @Test
    void getProjectById_shouldReturnProject() {

        Long id = 1L;

        Project project = new Project();
        ProjectResponseDto responseDto = new ProjectResponseDto();

        when(projectRepository.findById(id))
                .thenReturn(Optional.of(project));

        when(projectMapper.toDto(project))
                .thenReturn(responseDto);

        ProjectResponseDto result =
                projectService.getProjectById(id);

        assertEquals(responseDto, result);

        verify(projectRepository).findById(id);
        verify(projectMapper).toDto(project);
    }


    @Test
    void getProjectById_shouldThrowException_whenProjectDoesNotExist() {

        Long id = 1L;

        when(projectRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                ProjectNotFoundException.class,
                () -> projectService.getProjectById(id)
        );

        verify(projectRepository).findById(id);
    }


    @Test
    void getAllProjects_shouldReturnProjects() {

        Project project1 = new Project();
        Project project2 = new Project();

        ProjectResponseDto responseDto1 =
                new ProjectResponseDto();

        ProjectResponseDto responseDto2 =
                new ProjectResponseDto();

        when(projectRepository.findAll())
                .thenReturn(List.of(project1, project2));

        when(projectMapper.toDto(project1))
                .thenReturn(responseDto1);

        when(projectMapper.toDto(project2))
                .thenReturn(responseDto2);

        List<ProjectResponseDto> result =
                projectService.getAllProjects();

        assertEquals(2, result.size());

        verify(projectRepository).findAll();
    }


    @Test
    void updateProject_shouldUpdateProject() {

        Long id = 1L;
        Long ownerId = 2L;

        ProjectCreateDto dto = new ProjectCreateDto();
        dto.setOwnerId(ownerId);
        dto.setName("New project");
        dto.setDescription("New description");

        Project project = new Project();
        User owner = new User();
        Project updatedProject = new Project();
        ProjectResponseDto responseDto = new ProjectResponseDto();

        when(projectRepository.findById(id))
                .thenReturn(Optional.of(project));

        when(userRepository.findById(ownerId))
                .thenReturn(Optional.of(owner));

        when(projectRepository.save(project))
                .thenReturn(updatedProject);

        when(projectMapper.toDto(updatedProject))
                .thenReturn(responseDto);

        ProjectResponseDto result =
                projectService.updateProject(id, dto);

        assertEquals(responseDto, result);

        assertEquals("New project", project.getName());
        assertEquals("New description", project.getDescription());
        assertEquals(owner, project.getOwner());

        verify(projectRepository).findById(id);
        verify(userRepository).findById(ownerId);
        verify(projectRepository).save(project);
        verify(projectMapper).toDto(updatedProject);
    }


    @Test
    void updateProject_shouldThrowException_whenProjectDoesNotExist() {

        Long id = 1L;

        ProjectCreateDto dto = new ProjectCreateDto();
        dto.setOwnerId(2L);

        when(projectRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                ProjectNotFoundException.class,
                () -> projectService.updateProject(id, dto)
        );

        verify(projectRepository).findById(id);
    }


    @Test
    void updateProject_shouldThrowException_whenOwnerDoesNotExist() {

        Long id = 1L;
        Long ownerId = 2L;

        ProjectCreateDto dto = new ProjectCreateDto();
        dto.setOwnerId(ownerId);

        Project project = new Project();

        when(projectRepository.findById(id))
                .thenReturn(Optional.of(project));

        when(userRepository.findById(ownerId))
                .thenReturn(Optional.empty());

        assertThrows(
                OwnerNotFoundException.class,
                () -> projectService.updateProject(id, dto)
        );

        verify(projectRepository).findById(id);
        verify(userRepository).findById(ownerId);
    }


    @Test
    void deleteProjectById_shouldDeleteProject() {

        Long id = 1L;

        Project project = new Project();

        when(projectRepository.findById(id))
                .thenReturn(Optional.of(project));

        projectService.deleteProjectById(id);

        verify(projectRepository).findById(id);
        verify(projectRepository).delete(project);
    }


    @Test
    void deleteProjectById_shouldThrowException_whenProjectDoesNotExist() {

        Long id = 1L;

        when(projectRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                ProjectNotFoundException.class,
                () -> projectService.deleteProjectById(id)
        );

        verify(projectRepository).findById(id);
    }
}
