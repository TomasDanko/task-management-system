package com.task.management.tms.mapper;

import com.task.management.tms.dto.UserResponseDto;
import com.task.management.tms.entity.User;
import com.task.management.tms.enumerator.Role;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void toDto_shouldMapUserToUserResponseDto() {

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@test.com");
        user.setPassword("password");
        user.setRole(Role.USER);

        UserResponseDto dto = userMapper.toDto(user);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("testuser", dto.getUsername());
        assertEquals("test@test.com", dto.getEmail());
        assertEquals(Role.USER, dto.getRole());
    }

    @Test
    void toEntity_shouldReturnNull_whenDtoIsNull() {

        User user = userMapper.toEntity(null);

        assertNull(user);
    }

    @Test
    void toDto_shouldReturnNull_whenUserIsNull() {

        UserResponseDto dto = userMapper.toDto(null);

        assertNull(dto);
    }
}
