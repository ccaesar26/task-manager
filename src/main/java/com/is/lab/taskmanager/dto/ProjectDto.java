package com.is.lab.taskmanager.dto;

import lombok.Data;
import java.util.Set;

@Data
public class ProjectDto {
    private Long id;
    private String name;
    private String description;
    private String ownerUsername;
    private Set<Long> taskIds;
    private Set<String> collaboratorUsernames;
}

