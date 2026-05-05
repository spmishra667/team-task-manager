package com.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private String createdByName;
    private Long createdById;
    private String myRole;
    private LocalDateTime createdAt;
}
