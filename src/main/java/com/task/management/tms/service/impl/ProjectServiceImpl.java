    package com.task.management.tms.service.impl;

    import com.task.management.tms.dto.ProjectCreateDto;
    import com.task.management.tms.dto.ProjectResponseDto;
    import com.task.management.tms.dto.UserResponseDto;
    import com.task.management.tms.entity.Project;
    import com.task.management.tms.entity.User;
    import com.task.management.tms.exception.OwnerNotFoundException;
    import com.task.management.tms.exception.ProjectNotFoundException;
    import com.task.management.tms.mapper.ProjectMapper;
    import com.task.management.tms.repository.ProjectRepository;
    import com.task.management.tms.repository.UserRepository;
    import com.task.management.tms.service.ProjectService;
    import org.springframework.stereotype.Service;

    import java.util.List;

    @Service
    public class ProjectServiceImpl implements ProjectService {

        private final ProjectRepository projectRepository;
        private final ProjectMapper projectMapper;
        private final UserRepository userRepository;

        public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper, UserRepository userRepository) {
            this.projectRepository = projectRepository;
            this.projectMapper = projectMapper;
            this.userRepository = userRepository;
        }

        @Override
        public ProjectResponseDto createProject(ProjectCreateDto dto) {
            User owner = userRepository.findById(dto.getOwnerId()).orElseThrow(()-> new OwnerNotFoundException("Owner not found"));
            Project project = projectMapper.toEntity(dto);
            project.setOwner(owner);
            project.getMembers().add(owner);

            Project saved = projectRepository.save(project);

            return projectMapper.toDto(saved);
        }

        @Override
        public ProjectResponseDto getProjectById(Long id) {
            Project project = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException("Project not found"));

            return projectMapper.toDto(project);
        }

        @Override
        public List<ProjectResponseDto> getAllProjects() {
            return projectRepository.findAll()
                    .stream()
                    .map(projectMapper::toDto)
                    .toList();
        }

        @Override
        public ProjectResponseDto updateProject(Long id, ProjectCreateDto dto) {
            Project project = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException("Project not found"));

            User owner = userRepository.findById(dto.getOwnerId()).orElseThrow(() -> new OwnerNotFoundException("Owner not found"));

            project.setName(dto.getName());
            project.setDescription(dto.getDescription());
            project.setOwner(owner);

            Project updatedProject = projectRepository.save(project);

            return projectMapper.toDto(updatedProject);
        }

        @Override
        public void deleteProjectById(Long id) {
            Project project = projectRepository.findById(id).orElseThrow(() -> new ProjectNotFoundException("Project not found"));
            projectRepository.delete(project);
        }
    }
