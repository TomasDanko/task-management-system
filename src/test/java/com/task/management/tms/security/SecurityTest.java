package com.task.management.tms.security;

import com.task.management.tms.entity.User;
import com.task.management.tms.enumerator.Role;

import com.task.management.tms.model.UserModel;
import com.task.management.tms.repository.UserRepository;

import com.task.management.tms.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "jwt.secret=testSecretKeyForJwtTesting12345678901234567890",
        "jwt.token.expirationInMS=7200000"
})
@AutoConfigureMockMvc
public class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    private User user;
    private String userToken;
    private String adminToken;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@test.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setRole(Role.USER);

        UserModel userModel = new UserModel();
        userModel.setId(1L);
        userModel.setUsername("test@test.com");
        userModel.setEmail("test@test.com");
        userModel.setPassword(user.getPassword());
        userModel.setRole(Role.USER);

        userToken = jwtUtil.generateToken(userModel);

        UserModel adminModel = new UserModel();
        adminModel.setId(2L);
        adminModel.setUsername("admin@test.com");
        adminModel.setEmail("admin@test.com");
        adminModel.setPassword(
                passwordEncoder.encode("password123")
        );
        adminModel.setRole(Role.ADMIN);

        adminToken = jwtUtil.generateToken(adminModel);
    }

    @Test
    void register_shouldBeAllowedWithoutToken() throws Exception {

        User savedUser = new User();
        savedUser.setId(3L);
        savedUser.setUsername("newuser");
        savedUser.setEmail("new@test.com");
        savedUser.setPassword(
                passwordEncoder.encode("password123")
        );
        savedUser.setRole(Role.USER);

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        String requestBody = """
                {
                    "username": "newuser",
                    "email": "new@test.com",
                    "password": "password123",
                    "role": "USER"
                }
                """;

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    void login_shouldReturnTokenWithCorrectCredentials() throws Exception {

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));

        String requestBody = """
                {
                    "email": "test@test.com",
                    "password": "password123"
                }
                """;

        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void login_shouldReturnUnauthorizedWithWrongPassword() throws Exception {

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));

        String requestBody = """
                {
                    "email": "test@test.com",
                    "password": "wrongpassword"
                }
                """;

        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void currentUser_shouldReturnUnauthorizedWithoutToken() throws Exception {

        mockMvc.perform(get("/api/user/currentUser"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void currentUser_shouldBeAllowedWithValidToken() throws Exception {

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/user/currentUser")
                        .header(
                                "Authorization",
                                "Bearer " + userToken
                        ))
                .andExpect(status().isOk());
    }

    @Test
    void currentUser_shouldReturnUnauthorizedWithInvalidToken() throws Exception {

        mockMvc.perform(get("/api/user/currentUser")
                        .header(
                                "Authorization",
                                "Bearer invalid-token"
                        ))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void users_shouldReturnForbiddenForUserRole() throws Exception {

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/user/users")
                        .header(
                                "Authorization",
                                "Bearer " + userToken
                        ))
                .andExpect(status().isForbidden());
    }

    @Test
    void users_shouldBeAllowedForAdminRole() throws Exception {

        User admin = new User();
        admin.setId(2L);
        admin.setUsername("admin");
        admin.setEmail("admin@test.com");
        admin.setPassword(
                passwordEncoder.encode("password123")
        );
        admin.setRole(Role.ADMIN);

        when(userRepository.findByEmail("admin@test.com"))
                .thenReturn(Optional.of(admin));

        when(userRepository.findAll())
                .thenReturn(List.of(user, admin));

        mockMvc.perform(get("/api/user/users")
                        .header(
                                "Authorization",
                                "Bearer " + adminToken
                        ))
                .andExpect(status().isOk());
    }

    @Test
    void users_shouldReturnForbiddenForManagerRole() throws Exception {

        User manager = new User();
        manager.setId(3L);
        manager.setUsername("manager");
        manager.setEmail("manager@test.com");
        manager.setPassword(
                passwordEncoder.encode("password123")
        );
        manager.setRole(Role.MANAGER);

        UserModel managerModel = new UserModel();
        managerModel.setId(3L);
        managerModel.setUsername("manager@test.com");
        managerModel.setEmail("manager@test.com");
        managerModel.setPassword(manager.getPassword());
        managerModel.setRole(Role.MANAGER);

        String managerToken = jwtUtil.generateToken(managerModel);

        when(userRepository.findByEmail("manager@test.com"))
                .thenReturn(Optional.of(manager));

        mockMvc.perform(get("/api/user/users")
                        .header(
                                "Authorization",
                                "Bearer " + managerToken
                        ))
                .andExpect(status().isForbidden());
    }

    @Test
    void protectedEndpoint_shouldReturnUnauthorizedWithEmptyBearerToken()
            throws Exception {

        mockMvc.perform(get("/api/user/currentUser")
                        .header("Authorization", "Bearer "))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void protectedEndpoint_shouldReturnUnauthorizedWithMissingAuthorizationHeader()
            throws Exception {

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void projects_shouldReturnUnauthorizedWithoutToken()
            throws Exception {

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void tasks_shouldReturnUnauthorizedWithoutToken()
            throws Exception {

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void comments_shouldReturnUnauthorizedWithoutToken()
            throws Exception {

        mockMvc.perform(get("/api/comments"))
                .andExpect(status().isUnauthorized());
    }
}