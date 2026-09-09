package com.task.management.tms.service;

import com.task.management.tms.dto.CommentCreateDto;
import com.task.management.tms.dto.CommentResponseDto;

import java.util.List;

public interface CommentService {

    CommentResponseDto createComment(CommentCreateDto dto);

    CommentResponseDto getCommentById(Long id);

    CommentResponseDto updateComment(Long id, CommentCreateDto dto);

    List<CommentResponseDto> getCommentsByTask(Long taskId);

    void deleteCommentById(Long id);
}
