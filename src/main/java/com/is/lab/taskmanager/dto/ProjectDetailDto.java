package com.is.lab.taskmanager.dto;

import lombok.Data;

import java.util.Set;

@Data
public class ProjectDetailDto {
    private Long id;
    private String name;
    private String description;
    private UserDto owner; // Folosim UserDto pentru a afișa detalii despre proprietar
    private Set<TaskDto> tasks;
    private Set<UserDto> collaborators;
}
