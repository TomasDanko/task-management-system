package com.task.management.tms.repository;

import com.task.management.tms.entity.Comment;
import com.task.management.tms.entity.Task;
import com.task.management.tms.enumerator.Priority;
import com.task.management.tms.enumerator.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByTaskId_shouldReturnComments() {

        Task task = new Task();
        task.setTitle("Test task");
        task.setDescription("Test description");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(Priority.MEDIUM);
        task.setCreatedAt(LocalDateTime.now());
        task.setDueDate(LocalDateTime.now().plusDays(7));

        entityManager.persist(task);

        Comment comment1 = new Comment();
        comment1.setText("First comment");
        comment1.setTask(task);
        comment1.setCreatedAt(LocalDateTime.now());

        Comment comment2 = new Comment();
        comment2.setText("Second comment");
        comment2.setTask(task);
        comment2.setCreatedAt(LocalDateTime.now());

        commentRepository.save(comment1);
        commentRepository.save(comment2);

        List<Comment> result = commentRepository.findByTaskId(task.getId());

        assertEquals(2, result.size());
        assertEquals("First comment", result.get(0).getText());
        assertEquals("Second comment", result.get(1).getText());
    }

    @Test
    void findByTaskId_shouldReturnEmpty_whenTaskHasNoComments() {

        Task task = new Task();
        task.setTitle("Test task");
        task.setDescription("Test description");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(Priority.MEDIUM);
        task.setCreatedAt(LocalDateTime.now());
        task.setDueDate(LocalDateTime.now().plusDays(7));

        entityManager.persist(task);

        List<Comment> result = commentRepository.findByTaskId(task.getId());

        assertTrue(result.isEmpty());
    }
}
