package com.taskmanager.dto;

import com.taskmanager.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data

public class AddMemberRequest {
    @Email
    @NotBlank
    private String email;
    private Role role = Role.MEMBER;
}
