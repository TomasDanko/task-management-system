package com.task.management.tms.service.impl;

import com.task.management.tms.dto.CommentCreateDto;
import com.task.management.tms.dto.CommentResponseDto;
import com.task.management.tms.entity.AuditLog;
import com.task.management.tms.entity.Comment;
import com.task.management.tms.entity.Task;
import com.task.management.tms.entity.User;
import com.task.management.tms.enumerator.AuditAction;
import com.task.management.tms.exception.CommentNotFoundException;
import com.task.management.tms.exception.TaskNotFoundException;
import com.task.management.tms.exception.UserNotFoundException;
import com.task.management.tms.mapper.CommentMapper;
import com.task.management.tms.repository.AuditLogRepository;
import com.task.management.tms.repository.CommentRepository;
import com.task.management.tms.repository.TaskRepository;
import com.task.management.tms.repository.UserRepository;
import com.task.management.tms.service.CommentService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final AuditLogRepository auditLogRepository;

    public CommentServiceImpl(CommentRepository commentRepository, TaskRepository taskRepository, UserRepository userRepository, CommentMapper commentMapper, AuditLogRepository auditLogRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public CommentResponseDto createComment(CommentCreateDto dto) {

        Task task = taskRepository.findById(dto.getTaskId()).orElseThrow(() -> new TaskNotFoundException("Task not found"));

        User user = userRepository.findById(dto.getAuthorId()).orElseThrow(() -> new UserNotFoundException("User not found"));

        Comment comment = commentMapper.toEntity(dto);

        comment.setTask(task);
        comment.setAuthor(user);
        comment.setCreatedAt(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);

        saveAudit(AuditAction.CREATE, "COMMENT", savedComment.getId());
        return commentMapper.toDto(savedComment);
    }

    @Override
    public CommentResponseDto getCommentById(Long id) {
        Comment comment = getComment(id);
        return commentMapper.toDto(comment);
    }

    @Override
    public CommentResponseDto updateComment(Long id, CommentCreateDto dto) {
        Comment comment = getComment(id);

        comment.setText(dto.getContent());

        Comment updatedComment = commentRepository.save(comment);
        return commentMapper.toDto(updatedComment);
    }

    @Override
    public List<CommentResponseDto> getCommentsByTask(Long taskId) {
        return commentRepository.findByTaskId(taskId)
                .stream()
                .map(commentMapper::toDto)
                .toList();
    }

    @Override
    public void deleteCommentById(Long id) {
        Comment comment = getComment(id);
        commentRepository.delete(comment);

    }

    private void saveAudit(AuditAction action, String identityType, Long identityId) {

        AuditLog log = new AuditLog();

        log.setIdentityType(identityType);
        log.setIdentityId(identityId);
        log.setAction(action.name());
        log.setTimestamp(LocalDateTime.now());

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            log.setUsername(authentication.getName());
        } else {
            log.setUsername("system");
        }

        auditLogRepository.save(log);
    }

    private Comment getComment(Long id) {

        return commentRepository.findById(id)
                .orElseThrow(() ->
                        new CommentNotFoundException("Comment not found"));
    }

}
