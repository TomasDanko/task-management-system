package com.task.management.tms.mapper;

import com.task.management.tms.dto.CommentCreateDto;
import com.task.management.tms.dto.CommentResponseDto;
import com.task.management.tms.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {


    @Mapping(target = "text", source = "content")
    Comment toEntity(CommentCreateDto dto);

    @Mapping(source = "text", target = "content")
    @Mapping(source = "author.username", target = "authorUsername")
    CommentResponseDto toDto(Comment comment);

}