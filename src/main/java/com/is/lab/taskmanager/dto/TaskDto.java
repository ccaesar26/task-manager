package com.is.lab.taskmanager.dto;

import com.is.lab.taskmanager.model.TaskStatus;
import lombok.Data;

@Data
public class TaskDto {
    private Long id;
    private String name;
    private String description;
    private TaskStatus status;
    private Long projectId;
    private String assigneeUsername;
}

