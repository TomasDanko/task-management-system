package com.task.management.tms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.management.tms.dto.TaskCreateDto;
import com.task.management.tms.entity.Project;
import com.task.management.tms.entity.User;
import com.task.management.tms.enumerator.Priority;
import com.task.management.tms.enumerator.Role;
import com.task.management.tms.model.UserModel;
import com.task.management.tms.repository.ProjectRepository;
import com.task.management.tms.repository.TaskRepository;
import com.task.management.tms.repository.UserRepository;
import com.task.management.tms.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "jwt.secret=testSecretKeyForJwtTesting12345678901234567890",
        "jwt.token.expirationInMS=7200000"
})
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void createTask_shouldCreateTask() throws Exception {

        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@test.com");
        user.setPassword("password");
        user.setRole(Role.USER);

        user = userRepository.save(user);

        UserModel userModel = new UserModel();
        userModel.setUsername(user.getEmail());
        userModel.setRole(user.getRole());

        String token = jwtUtil.generateToken(userModel);

        Project project = new Project();
        project.setName("Test project");
        project.setDescription("Test project description");
        project.setOwner(user);

        project = projectRepository.save(project);

        TaskCreateDto dto = new TaskCreateDto();
        dto.setTitle("Test task");
        dto.setDescription("Test task description");
        dto.setPriority(Priority.HIGH);
        dto.setProjectId(project.getId());

        mockMvc.perform(post("/api/tasks")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test task"))
                .andExpect(jsonPath("$.description").value("Test task description"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.status").value("TODO"));

        assertEquals(1, taskRepository.count());
    }
}