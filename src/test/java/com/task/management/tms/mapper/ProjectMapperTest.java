package com.task.management.tms.mapper;

import com.task.management.tms.dto.ProjectCreateDto;
import com.task.management.tms.dto.ProjectResponseDto;
import com.task.management.tms.entity.Project;
import com.task.management.tms.entity.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectMapperTest {

    private final ProjectMapper projectMapper = Mappers.getMapper(ProjectMapper.class);

    @Test
    void toEntity_shouldMapProjectCreateDtoToProject() {

        ProjectCreateDto dto = new ProjectCreateDto();
        dto.setName("Test project");
        dto.setDescription("Test description");
        dto.setOwnerId(1L);

        Project project = projectMapper.toEntity(dto);

        assertNotNull(project);
        assertEquals("Test project", project.getName());
        assertEquals("Test description", project.getDescription());
    }

    @Test
    void toDto_shouldMapProjectToProjectResponseDto() {

        User owner = new User();
        owner.setId(1L);
        owner.setUsername("testuser");

        Project project = new Project();
        project.setId(10L);
        project.setName("Test project");
        project.setDescription("Test description");
        project.setOwner(owner);

        ProjectResponseDto dto = projectMapper.toDto(project);

        assertNotNull(dto);
        assertEquals(10L, dto.getId());
        assertEquals("Test project", dto.getName());
        assertEquals("Test description", dto.getDescription());
        assertEquals("testuser", dto.getOwnerUsername());
    }

    @Test
    void toEntity_shouldReturnNull_whenDtoIsNull() {

        Project project = projectMapper.toEntity(null);

        assertNull(project);
    }

    @Test
    void toDto_shouldReturnNull_whenProjectIsNull() {

        ProjectResponseDto dto = projectMapper.toDto(null);

        assertNull(dto);
    }
}
