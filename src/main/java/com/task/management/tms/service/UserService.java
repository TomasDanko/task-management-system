package com.task.management.tms.service;

import com.task.management.tms.dto.UserResponseDto;
import com.task.management.tms.dto.UserUpdateDto;

import java.util.List;

public interface UserService {

    UserResponseDto getUserById(Long id);

    List<UserResponseDto> getAllUsers();

    UserResponseDto updateUser(Long id, UserUpdateDto dto);

    void delete(Long id);
}
