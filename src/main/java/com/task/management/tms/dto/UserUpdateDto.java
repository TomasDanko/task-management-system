package com.task.management.tms.dto;

import com.task.management.tms.enumerator.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UserUpdateDto {

    @Size(min = 3, max = 150)
    private String username;

    @Email(message = "Email is not valid")
    private String email;

    private Role role;

    public UserUpdateDto() {
    }

    public UserUpdateDto(String username, String email, Role role) {
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
