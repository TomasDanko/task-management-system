package com.task.management.tms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.management.tms.controllers.UserController;
import com.task.management.tms.dto.UserResponseDto;
import com.task.management.tms.dto.UserUpdateDto;
import com.task.management.tms.enumerator.Role;
import com.task.management.tms.service.UserService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void getUserById_shouldReturnUser() throws Exception {

        UserResponseDto response = new UserResponseDto();
        response.setId(1L);
        response.setUsername("testuser");
        response.setEmail("test@test.com");
        response.setRole(Role.USER);

        when(userService.getUserById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.role").value("USER"));

        verify(userService).getUserById(1L);
    }

    @Test
    void getUserById_shouldReturnUserWithDifferentId() throws Exception {

        UserResponseDto response = new UserResponseDto();
        response.setId(25L);
        response.setUsername("anotheruser");
        response.setEmail("another@test.com");
        response.setRole(Role.MANAGER);

        when(userService.getUserById(25L))
                .thenReturn(response);

        mockMvc.perform(get("/api/users/25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(25))
                .andExpect(jsonPath("$.username").value("anotheruser"))
                .andExpect(jsonPath("$.role").value("MANAGER"));

        verify(userService).getUserById(25L);
    }

    @Test
    void getAllUsers_shouldReturnUsers() throws Exception {

        UserResponseDto user1 = new UserResponseDto();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setEmail("user1@test.com");
        user1.setRole(Role.USER);

        UserResponseDto user2 = new UserResponseDto();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setEmail("user2@test.com");
        user2.setRole(Role.MANAGER);

        when(userService.getAllUsers())
                .thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].username").value("user2"));

        verify(userService).getAllUsers();
    }

    @Test
    void getAllUsers_shouldReturnEmptyList() throws Exception {

        when(userService.getAllUsers())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(userService).getAllUsers();
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() throws Exception {

        UserUpdateDto dto = new UserUpdateDto();
        dto.setUsername("updateduser");
        dto.setEmail("updated@test.com");
        dto.setRole(Role.MANAGER);

        UserResponseDto response = new UserResponseDto();
        response.setId(1L);
        response.setUsername("updateduser");
        response.setEmail("updated@test.com");
        response.setRole(Role.MANAGER);

        when(userService.updateUser(eq(1L), any(UserUpdateDto.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("updateduser"))
                .andExpect(jsonPath("$.email").value("updated@test.com"))
                .andExpect(jsonPath("$.role").value("MANAGER"));

        verify(userService).updateUser(eq(1L), any(UserUpdateDto.class));
    }

    @Test
    void updateUser_shouldPassCorrectIdToService() throws Exception {

        UserUpdateDto dto = new UserUpdateDto();
        dto.setUsername("updateduser");
        dto.setEmail("updated@test.com");
        dto.setRole(Role.USER);

        UserResponseDto response = new UserResponseDto();
        response.setId(10L);
        response.setUsername("updateduser");
        response.setEmail("updated@test.com");
        response.setRole(Role.USER);

        when(userService.updateUser(eq(10L), any(UserUpdateDto.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/users/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));

        verify(userService).updateUser(eq(10L), any(UserUpdateDto.class));
    }

    @Test
    void deleteUser_shouldReturnOk() throws Exception {

        doNothing().when(userService).delete(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());

        verify(userService).delete(1L);
    }
}

