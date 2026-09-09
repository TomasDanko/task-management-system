package com.task.management.tms.mapper;


import com.task.management.tms.dto.UserResponseDto;
import com.task.management.tms.dto.UserUpdateDto;
import com.task.management.tms.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserUpdateDto dto);

    UserResponseDto toDto(User user);
}
