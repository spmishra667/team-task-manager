package com.taskmanager.dto;

import com.taskmanager.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class UpdateStatusRequest {
    @NotNull
    private TaskStatus status;
}
