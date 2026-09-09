package com.task.management.tms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.management.tms.controllers.CommentController;
import com.task.management.tms.dto.CommentCreateDto;
import com.task.management.tms.dto.CommentResponseDto;
import com.task.management.tms.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;


    @Test
    void createComment_shouldReturnCreated() throws Exception {

        CommentCreateDto dto = new CommentCreateDto(
                "This is a test comment",
                1L,
                2L
        );

        CommentResponseDto response = new CommentResponseDto(
                10L,
                "This is a test comment",
                "testuser",
                LocalDateTime.now()
        );

        when(commentService.createComment(any(CommentCreateDto.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.content").value("This is a test comment"))
                .andExpect(jsonPath("$.authorUsername").value("testuser"));

        verify(commentService).createComment(any(CommentCreateDto.class));
    }


    @Test
    void createComment_shouldAcceptDifferentTaskAndAuthorId() throws Exception {

        CommentCreateDto dto = new CommentCreateDto(
                "Another test comment",
                99L,
                55L
        );

        CommentResponseDto response = new CommentResponseDto(
                20L,
                "Another test comment",
                "anotheruser",
                LocalDateTime.now()
        );

        when(commentService.createComment(any(CommentCreateDto.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(20))
                .andExpect(jsonPath("$.content").value("Another test comment"))
                .andExpect(jsonPath("$.authorUsername").value("anotheruser"));

        verify(commentService).createComment(any(CommentCreateDto.class));
    }


    @Test
    void getCommentById_shouldReturnComment() throws Exception {

        CommentResponseDto response = new CommentResponseDto(
                10L,
                "Test comment",
                "testuser",
                LocalDateTime.now()
        );

        when(commentService.getCommentById(10L))
                .thenReturn(response);

        mockMvc.perform(get("/api/comments/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.content").value("Test comment"))
                .andExpect(jsonPath("$.authorUsername").value("testuser"));

        verify(commentService).getCommentById(10L);
    }


    @Test
    void getCommentById_shouldReturnCorrectCommentForDifferentId() throws Exception {

        CommentResponseDto response = new CommentResponseDto(
                50L,
                "Different comment",
                "user5",
                LocalDateTime.now()
        );

        when(commentService.getCommentById(50L))
                .thenReturn(response);

        mockMvc.perform(get("/api/comments/50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(50))
                .andExpect(jsonPath("$.content").value("Different comment"));

        verify(commentService).getCommentById(50L);
    }


    @Test
    void getCommentByTask_shouldReturnComments() throws Exception {

        List<CommentResponseDto> comments = List.of(
                new CommentResponseDto(
                        1L,
                        "First comment",
                        "user1",
                        LocalDateTime.now()
                ),
                new CommentResponseDto(
                        2L,
                        "Second comment",
                        "user2",
                        LocalDateTime.now()
                )
        );

        when(commentService.getCommentsByTask(1L))
                .thenReturn(comments);

        mockMvc.perform(get("/api/comments/task/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].content").value("First comment"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].content").value("Second comment"));

        verify(commentService).getCommentsByTask(1L);
    }


    @Test
    void getCommentByTask_shouldReturnEmptyList() throws Exception {

        when(commentService.getCommentsByTask(10L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/comments/task/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(commentService).getCommentsByTask(10L);
    }


    @Test
    void getCommentByTask_shouldPassCorrectTaskIdToService() throws Exception {

        when(commentService.getCommentsByTask(25L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/comments/task/25"))
                .andExpect(status().isOk());

        verify(commentService).getCommentsByTask(25L);
    }


    @Test
    void updateComment_shouldReturnUpdatedComment() throws Exception {

        CommentCreateDto dto = new CommentCreateDto(
                "Updated comment",
                1L,
                2L
        );

        CommentResponseDto response = new CommentResponseDto(
                10L,
                "Updated comment",
                "testuser",
                LocalDateTime.now()
        );

        when(commentService.updateComment(eq(10L), any(CommentCreateDto.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/comments/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.content").value("Updated comment"))
                .andExpect(jsonPath("$.authorUsername").value("testuser"));

        verify(commentService).updateComment(eq(10L), any(CommentCreateDto.class));
    }


    @Test
    void updateComment_shouldPassCorrectIdToService() throws Exception {

        CommentCreateDto dto = new CommentCreateDto(
                "Updated comment",
                5L,
                3L
        );

        CommentResponseDto response = new CommentResponseDto(
                25L,
                "Updated comment",
                "user3",
                LocalDateTime.now()
        );

        when(commentService.updateComment(eq(25L), any(CommentCreateDto.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/comments/25")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(25));

        verify(commentService).updateComment(eq(25L), any(CommentCreateDto.class));
    }


    @Test
    void updateComment_shouldAcceptDifferentTaskAndAuthorId() throws Exception {

        CommentCreateDto dto = new CommentCreateDto(
                "Another updated comment",
                99L,
                77L
        );

        CommentResponseDto response = new CommentResponseDto(
                30L,
                "Another updated comment",
                "user77",
                LocalDateTime.now()
        );

        when(commentService.updateComment(eq(30L), any(CommentCreateDto.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/comments/30")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(30))
                .andExpect(jsonPath("$.content").value("Another updated comment"));

        verify(commentService).updateComment(eq(30L), any(CommentCreateDto.class));
    }


    @Test
    void deleteComment_shouldReturnNoContent() throws Exception {

        mockMvc.perform(delete("/api/comments/10"))
                .andExpect(status().isNoContent());

        verify(commentService).deleteCommentById(10L);
    }


    @Test
    void deleteComment_shouldPassCorrectIdToService() throws Exception {

        mockMvc.perform(delete("/api/comments/55"))
                .andExpect(status().isNoContent());

        verify(commentService).deleteCommentById(55L);
    }


    @Test
    void getCommentById_shouldPassCorrectIdToService() throws Exception {

        CommentResponseDto response = new CommentResponseDto(
                100L,
                "Comment",
                "user100",
                LocalDateTime.now()
        );

        when(commentService.getCommentById(100L))
                .thenReturn(response);

        mockMvc.perform(get("/api/comments/100"))
                .andExpect(status().isOk());

        verify(commentService).getCommentById(100L);
    }


    @Test
    void createComment_shouldPassDtoToService() throws Exception {

        CommentCreateDto dto = new CommentCreateDto(
                "Comment content",
                7L,
                8L
        );

        CommentResponseDto response = new CommentResponseDto(
                70L,
                "Comment content",
                "user8",
                LocalDateTime.now()
        );

        when(commentService.createComment(any(CommentCreateDto.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(commentService).createComment(any(CommentCreateDto.class));
    }
}


