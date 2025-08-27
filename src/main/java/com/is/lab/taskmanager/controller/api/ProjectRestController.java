package com.is.lab.taskmanager.controller.api;

import com.is.lab.taskmanager.dto.ProjectDetailDto;
import com.is.lab.taskmanager.dto.ProjectFormDto;
import com.is.lab.taskmanager.dto.ProjectListDto;
import com.is.lab.taskmanager.model.User;
import com.is.lab.taskmanager.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectRestController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectListDto>> getAllProjects() {
        List<ProjectListDto> projects = projectService.findAllProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDetailDto> getProjectById(@PathVariable Long id) {
        ProjectDetailDto project = projectService.findProjectById(id);
        return ResponseEntity.ok(project);
    }

    @PostMapping
    public ResponseEntity<ProjectDetailDto> createProject(@RequestBody ProjectFormDto formDto,
                                                          @AuthenticationPrincipal User currentUser) {
        // In a real API, ownerId would come from the authenticated user's security context
        Long ownerId = 1L; // Temporary hardcoded value
        ProjectDetailDto createdProject = projectService.createProject(formDto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDetailDto> updateProject(@PathVariable Long id, @RequestBody ProjectFormDto formDto) {
        ProjectDetailDto updatedProject = projectService.updateProject(id, formDto);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }
}
