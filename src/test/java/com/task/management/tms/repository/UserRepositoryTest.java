package com.task.management.tms.repository;

import com.task.management.tms.entity.User;
import com.task.management.tms.enumerator.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmail_shouldReturnUser() {

        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@test.com");
        user.setPassword("password");
        user.setRole(Role.USER);

        userRepository.save(user);

        Optional<User> result = userRepository.findByEmail("test@test.com");

        assertTrue(result.isPresent());
        assertEquals("test@test.com", result.get().getEmail());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    void findByEmail_shouldReturnEmpty_whenUserDoesNotExist() {

        Optional<User> result = userRepository.findByEmail("notfound@test.com");

        assertTrue(result.isEmpty());
    }
}
