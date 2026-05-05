package com.taskmanager.dto;

import com.taskmanager.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data

public class TaskRequest {
    @NotBlank(message = "Title is required")
    private String title;
    private String description;
    private LocalDate dueDate;
    private Priority priority = Priority.MEDIUM;
    @NotNull(message = "Project ID required")
    private Long projectId;
    private Long assignedToUserId;
}
