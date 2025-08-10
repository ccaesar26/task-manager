package com.is.lab.taskmanager.dto;

import lombok.Data;

/**
 * A lightweight DTO for displaying projects in a list.
 * It avoids fetching heavy collections like tasks and collaborators.
 */
@Data
public class ProjectListDto {
    private Long id;
    private String name;
    private String description;
    private String ownerUsername;
}