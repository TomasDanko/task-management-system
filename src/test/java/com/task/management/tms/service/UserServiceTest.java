package com.task.management.tms.service;


import com.task.management.tms.dto.UserResponseDto;
import com.task.management.tms.dto.UserUpdateDto;
import com.task.management.tms.entity.User;
import com.task.management.tms.exception.UserNotFoundException;
import com.task.management.tms.mapper.UserMapper;
import com.task.management.tms.repository.UserRepository;
import com.task.management.tms.service.impl.UserServiceImpl;
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
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void getUserById_shouldReturnUser() {

        Long id = 1L;
        User user = new User();
        UserResponseDto responseDto = new UserResponseDto();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(responseDto);

        UserResponseDto result = userService.getUserById(id);

        assertEquals(responseDto, result);

        verify(userRepository).findById(id);
        verify(userMapper).toDto(user);
    }


    @Test
    void getUserById_shouldThrowException_whenUserDoesNotExist() {

        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserById(id)
        );

        verify(userRepository).findById(id);
    }


    @Test
    void getAllUsers_shouldReturnUsers() {

        User user1 = new User();
        User user2 = new User();

        UserResponseDto responseDto1 =
                new UserResponseDto(1L, "user1", "user1@email.com", null);

        UserResponseDto responseDto2 =
                new UserResponseDto(2L, "user2", "user2@email.com", null);

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        when(userMapper.toDto(user1)).thenReturn(responseDto1);
        when(userMapper.toDto(user2)).thenReturn(responseDto2);

        List<UserResponseDto> result = userService.getAllUsers();

        assertEquals(2, result.size());

        verify(userRepository).findAll();
    }


    @Test
    void updateUser_shouldUpdateUser() {

        Long id = 1L;

        UserUpdateDto dto = new UserUpdateDto();
        dto.setUsername("newUsername");
        dto.setEmail("new@email.com");

        User user = new User();
        User updatedUser = new User();
        UserResponseDto responseDto = new UserResponseDto();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(updatedUser);
        when(userMapper.toDto(updatedUser)).thenReturn(responseDto);

        UserResponseDto result = userService.updateUser(id, dto);

        assertEquals(responseDto, result);

        assertEquals("newUsername", user.getUsername());
        assertEquals("new@email.com", user.getEmail());

        verify(userRepository).findById(id);
        verify(userRepository).save(user);
        verify(userMapper).toDto(updatedUser);
    }


    @Test
    void updateUser_shouldThrowException_whenUserDoesNotExist() {

        Long id = 1L;
        UserUpdateDto dto = new UserUpdateDto();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.updateUser(id, dto)
        );

        verify(userRepository).findById(id);
    }


    @Test
    void delete_shouldDeleteUser() {

        Long id = 1L;
        User user = new User();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        userService.delete(id);

        verify(userRepository).findById(id);
        verify(userRepository).delete(user);
    }


    @Test
    void delete_shouldThrowException_whenUserDoesNotExist() {

        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.delete(id)
        );

        verify(userRepository).findById(id);
    }
}
