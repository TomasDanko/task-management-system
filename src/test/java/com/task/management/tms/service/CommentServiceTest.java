package com.task.management.tms.service;

import com.task.management.tms.dto.CommentCreateDto;
import com.task.management.tms.dto.CommentResponseDto;
import com.task.management.tms.entity.Comment;
import com.task.management.tms.entity.Task;
import com.task.management.tms.entity.User;
import com.task.management.tms.exception.CommentNotFoundException;
import com.task.management.tms.exception.TaskNotFoundException;
import com.task.management.tms.exception.UserNotFoundException;
import com.task.management.tms.mapper.CommentMapper;
import com.task.management.tms.repository.AuditLogRepository;
import com.task.management.tms.repository.CommentRepository;
import com.task.management.tms.repository.TaskRepository;
import com.task.management.tms.repository.UserRepository;
import com.task.management.tms.service.impl.CommentServiceImpl;
import com.task.management.tms.entity.AuditLog;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private CommentServiceImpl commentService;


    @Test
    void createComment_shouldCreateComment() {

        CommentCreateDto dto = new CommentCreateDto();
        dto.setTaskId(1L);
        dto.setAuthorId(2L);
        dto.setContent("Test comment");

        Task task = new Task();
        User user = new User();
        Comment comment = new Comment();
        Comment savedComment = new Comment();
        CommentResponseDto responseDto = new CommentResponseDto();

        savedComment.setId(1L);

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(user));

        when(commentMapper.toEntity(dto))
                .thenReturn(comment);

        when(commentRepository.save(comment))
                .thenReturn(savedComment);

        when(commentMapper.toDto(savedComment))
                .thenReturn(responseDto);

        CommentResponseDto result =
                commentService.createComment(dto);

        assertEquals(responseDto, result);

        assertEquals(task, comment.getTask());
        assertEquals(user, comment.getAuthor());
        // createdAt sa nastavuje priamo v service

        verify(taskRepository).findById(1L);
        verify(userRepository).findById(2L);
        verify(commentMapper).toEntity(dto);
        verify(commentRepository).save(comment);
        verify(auditLogRepository).save(ArgumentMatchers.any());
        verify(commentMapper).toDto(savedComment);
    }


    @Test
    void createComment_shouldThrowException_whenTaskDoesNotExist() {

        CommentCreateDto dto = new CommentCreateDto();
        dto.setTaskId(1L);
        dto.setAuthorId(2L);

        when(taskRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                TaskNotFoundException.class,
                () -> commentService.createComment(dto)
        );

        verify(taskRepository).findById(1L);
    }


    @Test
    void createComment_shouldThrowException_whenAuthorDoesNotExist() {

        CommentCreateDto dto = new CommentCreateDto();
        dto.setTaskId(1L);
        dto.setAuthorId(2L);

        Task task = new Task();

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        when(userRepository.findById(2L))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> commentService.createComment(dto)
        );

        verify(taskRepository).findById(1L);
        verify(userRepository).findById(2L);
    }


    @Test
    void getCommentById_shouldReturnComment() {

        Long id = 1L;

        Comment comment = new Comment();
        CommentResponseDto responseDto = new CommentResponseDto();

        when(commentRepository.findById(id))
                .thenReturn(Optional.of(comment));

        when(commentMapper.toDto(comment))
                .thenReturn(responseDto);

        CommentResponseDto result =
                commentService.getCommentById(id);

        assertEquals(responseDto, result);

        verify(commentRepository).findById(id);
        verify(commentMapper).toDto(comment);
    }


    @Test
    void getCommentById_shouldThrowException_whenCommentDoesNotExist() {

        Long id = 1L;

        when(commentRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                CommentNotFoundException.class,
                () -> commentService.getCommentById(id)
        );

        verify(commentRepository).findById(id);
    }


    @Test
    void updateComment_shouldUpdateComment() {

        Long id = 1L;

        CommentCreateDto dto = new CommentCreateDto();
        dto.setContent("Updated comment");

        Comment comment = new Comment();
        Comment updatedComment = new Comment();
        CommentResponseDto responseDto = new CommentResponseDto();

        when(commentRepository.findById(id))
                .thenReturn(Optional.of(comment));

        when(commentRepository.save(comment))
                .thenReturn(updatedComment);

        when(commentMapper.toDto(updatedComment))
                .thenReturn(responseDto);

        CommentResponseDto result =
                commentService.updateComment(id, dto);

        assertEquals(responseDto, result);
        assertEquals("Updated comment", comment.getText());

        verify(commentRepository).findById(id);
        verify(commentRepository).save(comment);
        verify(commentMapper).toDto(updatedComment);
    }


    @Test
    void updateComment_shouldThrowException_whenCommentDoesNotExist() {

        Long id = 1L;

        CommentCreateDto dto = new CommentCreateDto();
        dto.setContent("Updated comment");

        when(commentRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                CommentNotFoundException.class,
                () -> commentService.updateComment(id, dto)
        );

        verify(commentRepository).findById(id);
    }


    @Test
    void getCommentsByTask_shouldReturnComments() {

        Long taskId = 1L;

        Comment comment1 = new Comment();
        Comment comment2 = new Comment();

        CommentResponseDto responseDto1 =
                new CommentResponseDto();

        CommentResponseDto responseDto2 =
                new CommentResponseDto();

        when(commentRepository.findByTaskId(taskId))
                .thenReturn(List.of(comment1, comment2));

        when(commentMapper.toDto(comment1))
                .thenReturn(responseDto1);

        when(commentMapper.toDto(comment2))
                .thenReturn(responseDto2);

        List<CommentResponseDto> result =
                commentService.getCommentsByTask(taskId);

        assertEquals(2, result.size());

        verify(commentRepository).findByTaskId(taskId);
    }

    @Test
    void createComment_shouldSaveAuditWithAuthenticatedUser() {

        CommentCreateDto dto = new CommentCreateDto();
        dto.setTaskId(1L);
        dto.setAuthorId(2L);
        dto.setContent("Test comment");

        Task task = new Task();
        User user = new User();
        Comment comment = new Comment();
        Comment savedComment = new Comment();

        savedComment.setId(10L);

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(user));

        when(commentMapper.toEntity(dto))
                .thenReturn(comment);

        when(commentRepository.save(comment))
                .thenReturn(savedComment);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        "test@example.com",
                        null,
                        List.of()
                );

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        commentService.createComment(dto);

        verify(auditLogRepository).save(
                argThat(auditLog ->
                        auditLog.getIdentityType().equals("COMMENT")
                                && auditLog.getIdentityId().equals(10L)
                                && auditLog.getAction().equals("CREATE")
                                && auditLog.getUsername().equals("test@example.com")
                )
        );

        SecurityContextHolder.clearContext();
    }




    @Test
    void deleteCommentById_shouldDeleteComment() {

        Long id = 1L;

        Comment comment = new Comment();

        when(commentRepository.findById(id))
                .thenReturn(Optional.of(comment));

        commentService.deleteCommentById(id);

        verify(commentRepository).findById(id);
        verify(commentRepository).delete(comment);
    }


    @Test
    void deleteCommentById_shouldThrowException_whenCommentDoesNotExist() {

        Long id = 1L;

        when(commentRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                CommentNotFoundException.class,
                () -> commentService.deleteCommentById(id)
        );

        verify(commentRepository).findById(id);
    }
}
